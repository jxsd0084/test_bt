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
                                                                    @RequestParam("buriedPoint") String buriedPoint){
        NavigationItem0 navigationItem0 = navigation0Service.queryById(navigationId);
        List<Map<String, Object>> result =elasticsearchService.aggregation(navigationItem0.getName(),buriedPoint, 0, System.currentTimeMillis());
        LOGGER.info("请求参数："+navigationItem0.getName()+buriedPoint+"es查询结果为：" + result.toString());
        return result;
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
        LOGGER.info("查询二级事件结果：" + list.toString());
        return list;
    }

    @RequestMapping("/searchBuriedPoint")
    public @ResponseBody List<BuriedPoint0> searchBuriedPoint(@RequestParam("navigationId") int navigationId) {
        List<BuriedPoint0> list = cepService.getBuriedPoint0List(navigationId);
        LOGGER.info("查询导航下埋点结果：" + list.toString());
        return list;
    }
}
