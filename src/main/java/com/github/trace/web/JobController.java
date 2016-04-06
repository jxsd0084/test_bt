package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.entity.JobConfig;
import com.github.trace.entity.TableField;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataSourceServer;
import com.github.trace.service.JobServer;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/4/1.
 */
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private JobServer jobServer;
    @Autowired
    private DataSourceServer dataSourceServer;

    @RequestMapping("/listJob")
    public String index(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "job/job_index");
    }

    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
        List<JobConfig> list = jobServer.getJobListByBizId(bizId);
        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
        model.addAttribute("data", jsonArray);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return retPath;
    }

    @RequestMapping("/create")
    public String create(@RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         @RequestParam(name = "tag") String tag,
                         Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        JSONArray jsonArr = getDataSouresByBizId(bizId);
        model.addAttribute("dss",jsonArr);
        return "job/job_edit";
    }

    private JSONArray  getDataSouresByBizId(int bizId){
        Map<Integer,String> map = new HashMap<Integer,String>();
        List<DatabaseInfo> list  = dataSourceServer.getDataBaseInfoListById(bizId);
        for(DatabaseInfo dbInfo :list){
            map.put(dbInfo.getId(),dbInfo.getName());
        }
        JSONArray jsonArr = new JSONArray();
        for(Integer key :map.keySet()){
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("key",String.valueOf(key));
            jsonObj.put("value", map.get(key));
            jsonArr.add(jsonObj);
        }
        return jsonArr;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map add(@RequestParam(name = "bizId") int bizId,
                   @RequestParam(name = "bizName") String bizName,
                   @RequestParam(name = "name") String name,
                   @RequestParam(name = "dbSourceId") String dbSourceId,
                   @RequestParam(name = "dbSourceName") String dbSourceName,
                   @RequestParam(name = "memo") String memo,
                   Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobConfig jobConfig = new JobConfig();
        jobConfig.setBizId(bizId);
        jobConfig.setBizName(bizName);
        jobConfig.setName(name);
        jobConfig.setDbSourceId(dbSourceId);
        jobConfig.setDbSourceName(dbSourceName);
        jobConfig.setMemo(memo);
        jobConfig.setCreateTime(new Date());
        int res = jobServer.addJob(jobConfig);

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/ds_show")
    public String dsShow(@RequestParam(name = "id", required = false) int id,
                       @RequestParam(name = "bizId") int bizId,
                       @RequestParam(name = "bizName") String bizName,
                       @RequestParam(name = "tag") String tag,
                       Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        DatabaseInfo dataBaseInfo = dataSourceServer.getDataBaseInfoById(id);

        model.addAttribute("id", id);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        model.addAttribute("obj", dataBaseInfo);
        return "job/ds_show";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "id", required = false) int id,
                         @RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         @RequestParam(name = "tag") String tag,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobConfig jobConfig = jobServer.getJobById(id);

        model.addAttribute("id", id);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        model.addAttribute("obj", jobConfig);
        JSONArray jsonArr = getDataSouresByBizId(bizId);
        model.addAttribute("dss",jsonArr);
        return "job/job_edit";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(@RequestParam(name = "id") int id,
                      @RequestParam(name = "bizId") int bizId,
                      @RequestParam(name = "bizName") String bizName,
                      @RequestParam(name = "name") String name,
                      @RequestParam(name = "dbSourceId") String dbSourceId,
                      @RequestParam(name = "dbSourceName") String dbSourceName,
                      @RequestParam(name = "memo") String memo,
                      Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");

        JobConfig jobConfig = new JobConfig();
        jobConfig.setId(id);
        jobConfig.setBizId(bizId);
        jobConfig.setBizName(bizName);
        jobConfig.setName(name);
        jobConfig.setDbSourceId(dbSourceId);
        jobConfig.setDbSourceName(dbSourceName);
        jobConfig.setMemo(memo);

        int res = jobServer.updateJob(jobConfig);

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return ControllerHelper.returnResponseVal(res, "修改");
    }


    @RequestMapping("/delete")
    public String delete(@RequestParam(name = "id") int id,
                         @RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        jobServer.deleteJobById(id);
        return setCommonParam(bizId, bizName, model, "job/job_index");
    }

    @RequestMapping("/tblsIndex")
    public String tablesIndex(@RequestParam(name = "bizId") int bizId,
                              @RequestParam(name = "bizName") String bizName,
                              @RequestParam(name = "dbSourceId") int dbSourceId,
                              Model model){

        DatabaseInfo databaseInfo = dataSourceServer.getDataBaseInfoById(dbSourceId);
        List<String> list = dataSourceServer.getDatabaseTables(databaseInfo);
        JSONArray jsonArray1 = new JSONArray();
        int cont = 0;
        for (String tableName : list){
            cont ++;
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(cont);
            jsonArray2.add(databaseInfo.getDbName());
            jsonArray2.add(tableName);
            jsonArray1.add(jsonArray2);
        }
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        model.addAttribute("data", jsonArray1);
        model.addAttribute("obj", databaseInfo);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);

        return "ds/ds_index_2";
    }

    @RequestMapping("/fldsIndex")
    public String fieldsIndex(
                              @RequestParam(name = "tableName") String tableName,
                              @RequestParam(name = "bizId") int bizId,
                              @RequestParam(name = "bizName") String bizName,
                              @RequestParam(name = "dbSourceId") int dbSourceId,
                              Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        DatabaseInfo databaseInfo = dataSourceServer.getDataBaseInfoById(dbSourceId);
        List<TableField> list = dataSourceServer.getTableFields(databaseInfo, tableName);
        JSONArray jsonArray1 = new JSONArray();
        int cont = 0;
        for (TableField field : list){
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(++ cont);
            jsonArray2.add(field.getColumnName());
            jsonArray2.add(field.getColumnType());
            jsonArray1.add(jsonArray2);
        }

        model.addAttribute("data", jsonArray1);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("dbSourceId",dbSourceId);
        return "ds/ds_index_3";
    }
}
