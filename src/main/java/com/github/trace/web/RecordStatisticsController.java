package com.github.trace.web;

import com.github.trace.entity.BuriedPoint0;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataTypeService;
import com.github.trace.service.ElasticsearchService;
import com.github.trace.service.Navigation0Service;
import com.github.trace.utils.ControllerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghl on 2016/4/20.
 * description: buried point record statistics
 */
@Controller
@RequestMapping("/rsc")
public class RecordStatisticsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordStatisticsController.class);
    @Autowired
    private CEPService cepService;
    @Autowired
    private DataTypeService dataTypeService;
    @Autowired
    private Navigation0Service navigation0Service;
    @Autowired
    private ElasticsearchService elasticsearchService;

    @RequestMapping("/list")
    public String list(Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, "");  // 左边导航条
        return "rsc/record_count_list";
    }

    @RequestMapping("/buriedPointCount")
    public @ResponseBody List<Map<String, Object>> buriedPointCount(@RequestParam("navigationId") int navigationId,
                                                                    @RequestParam("buriedPoint") String buriedPoint,
                                                                    @RequestParam("version") String version){
        NavigationItem0 navigationItem0 = navigation0Service.queryById(navigationId);
        List<LevelTwoFields> levelTwoFieldses = null;
        List<Map<String, Object>> result = null;
        long now = System.currentTimeMillis();
        long yesterday = now - 3*(24 * 3600 * 1000L);
        if("M99.M1".equals(buriedPoint)||"actionid".equals(buriedPoint.toLowerCase())) {
            if("Android".equals(navigationItem0.getName())){
                levelTwoFieldses = dataTypeService.getLevelTwoFieldByNavId(11);
            }else{
                levelTwoFieldses = dataTypeService.getLevelTwoFieldByNavId(navigationId);
            }
        }
//        if(StringUtils.isEmpty(version)) {
//            result = elasticsearchService.aggregation(navigationItem0.getName(), buriedPoint, yesterday, System.currentTimeMillis());
//        }else{
        String esSearchItem = buriedPoint.replaceAll("\\.","_");
        String appVersion = null;
        String innerVersion = null;
        if(version.contains(".")){
            appVersion = version;
        }else{
            innerVersion = version;
        }
        LOGGER.info("esSearchItem:" + esSearchItem);
        if("iOS".equals(navigationItem0.getName())){
            result = elasticsearchService.searchBySqlForMonitorRequest("iPhone OS", appVersion, innerVersion, esSearchItem, yesterday, now);
        }else if("Android".equals(navigationItem0.getName())){
            result = elasticsearchService.searchBySqlForMonitorRequest("Android", appVersion, innerVersion, esSearchItem, yesterday, now);
        }else{
            result = elasticsearchService.searchBySqlForOthers(navigationItem0.getTopic(),buriedPoint,yesterday,now);
        }
//       }
            List<Map<String, Object>> list = mergeData(levelTwoFieldses, result);
            return list;
    }

    @RequestMapping("/searchOneLevel")
    public @ResponseBody List<LevelOneFields> searchOneLevel(@RequestParam("navigationId") int navigationId) {
        List<LevelOneFields> list = dataTypeService.queryLevelOneByNavId(navigationId);
        LOGGER.info("查询一级事件结果：" + list.toString());
        return list;
    }

    @RequestMapping("/searchTwoLevel")
    public @ResponseBody List<LevelTwoFields> searchTwoLevel(@RequestParam("levelOneId") int levelOneId) {
        List<LevelTwoFields> list = dataTypeService.getLevelTwoFieldList(levelOneId);
        return list;
    }

    @RequestMapping("/searchBuriedPoint")
    public @ResponseBody List<BuriedPoint0> searchBuriedPoint(@RequestParam("navigationId") int navigationId) {
        List<BuriedPoint0> list = cepService.getBuriedPoint0List(navigationId);
        return list;
    }

    /**
     * 查询数据与本地数据进行合并
     * @param list1
     * @param list2
     * @return
     */
    private List<Map<String,Object>> mergeData(List<LevelTwoFields> list1,List<Map<String,Object>> list2){
        List<Map<String,Object>> list = new ArrayList<>();
        if(list1!=null) {
            LOGGER.info("二级事件数据：" + list1.toString());
            /**
             * 检查本地库漏掉埋点项
             */
            for (LevelTwoFields levelTwoFields : list1) {
                Map<String, Object> map = new HashMap<>();
                if (!list2.toString().contains(levelTwoFields.getLevel1FieldTag())) {
                    map.put("id",list.size()+1);
                    map.put("value", levelTwoFields.getLevel1FieldTag() + "(" + levelTwoFields.getLevel2FieldName() + ")");
                    map.put("totalCount", 0.0);
                    map.put("failCount", 0.0);
                    map.put("successCount", 0.0);
                    map.put("type", -1);
                    list.add(map);
                }
            }
            /**
             * 现查es中多出埋点项
             */
            for (Map<String, Object> map : list2) {
                if (!list1.toString().contains(map.get("value").toString())) {
                    map.put("id",list.size()+1);
                    map.put("type", 0);
                    list.add(map);
                }
            }
            /**
             * 正确项
             */
            for (Map<String, Object> map : list2) {
                for (LevelTwoFields levelTwoFields : list1) {
                    if (map.get("value").toString().equals(levelTwoFields.getLevel1FieldTag())) {
                        map.put("id",list.size()+1);
                        map.put("value", levelTwoFields.getLevel1FieldTag() + "(" + levelTwoFields.getLevel2FieldName() + ")");
                        map.put("type", 1);
                        list.add(map);
                    }
                }
            }
        }else{
            for (Map<String, Object> map : list2) {
                map.put("id",list.size()+1);
                map.put("type", 1);
                list.add(map);
            }
        }
        return list;
    }
}
