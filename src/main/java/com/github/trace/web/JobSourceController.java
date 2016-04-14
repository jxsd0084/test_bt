package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.entity.JobSource;
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

import java.util.*;

/**
 * Created by chenlong on 2016/4/1.
 */
@Controller
@RequestMapping("/jobsource")
public class JobSourceController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private JobServer jobServer;
    @Autowired
    private DataSourceServer dataSourceServer;

    @RequestMapping("/listJobSource")
    public String listJobSource(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "jobsource/jobsource_index");
    }

    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
        List<JobSource> list = jobServer.getJobListByBizId(bizId);
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
        return "jobsource/jobsource_edit";
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
        JobSource jobSource = new JobSource();
        jobSource.setBizId(bizId);
        jobSource.setBizName(bizName);
        jobSource.setName(name);
        jobSource.setDbSourceId(dbSourceId);
        jobSource.setDbSourceName(dbSourceName);
        jobSource.setMemo(memo);
        jobSource.setCreateTime(new Date());
        int res = jobServer.addJob(jobSource);

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
        return "jobsource/ds_show";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "id", required = false) int id,
                         @RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         @RequestParam(name = "tag") String tag,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobSource jobSource = jobServer.getJobById(id);

        model.addAttribute("id", id);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        model.addAttribute("obj", jobSource);
        JSONArray jsonArr = getDataSouresByBizId(bizId);
        model.addAttribute("dss",jsonArr);
        return "jobsource/jobsource_edit";
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

        JobSource jobSource = new JobSource();
        jobSource.setId(id);
        jobSource.setBizId(bizId);
        jobSource.setBizName(bizName);
        jobSource.setName(name);
        jobSource.setDbSourceId(dbSourceId);
        jobSource.setDbSourceName(dbSourceName);
        jobSource.setMemo(memo);
        jobSource.setUpdateTime(new Date());

        int res = jobServer.updateJob(jobSource);

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
        return setCommonParam(bizId, bizName, model, "jobsource/jobsource_index");
    }

    @RequestMapping("/tblsIndex")
    public String tablesIndex(@RequestParam(name = "bizId") int bizId,
                              @RequestParam(name = "bizName") String bizName,
                              @RequestParam(name = "dbSourceId") int dbSourceId,
                              @RequestParam(name = "id") int id,
                              Model model){

        DatabaseInfo databaseInfo = dataSourceServer.getDataBaseInfoById(dbSourceId);
        List<String> list = dataSourceServer.getDatabaseTables(databaseInfo);

        JobSource jobSource = jobServer.getJobById(id);
        String tablesStr ="";
        if(jobSource!=null&&jobSource.getSelectTable()!=null)
            tablesStr = jobSource.getSelectTable();
        String[] tabs = tablesStr.split(",");
        Set tabSet = new HashSet();
        for(int i=0;i<tabs.length;i++){
            tabSet.add(tabs[i]);
        }
        JSONArray jsonArray1 = new JSONArray();
        int cont = 0;
        for (String tableName : list){
            cont ++;
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(cont);
            jsonArray2.add(databaseInfo.getDbName());
            jsonArray2.add(tableName);
            if(tabSet.contains(tableName))
                jsonArray2.add("<input type=\"checkbox\" checked=\"checked\"/>");
            else
                jsonArray2.add("<input type=\"checkbox\" />");
            jsonArray2.add("选择表字段");
            jsonArray1.add(jsonArray2);
        }
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        model.addAttribute("data", jsonArray1);
        model.addAttribute("obj", databaseInfo);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("jobId", id);
        return "ds/ds_index_2";
    }

    @RequestMapping("/fldsIndex")
    public String fieldsIndex(
                              @RequestParam(name = "tableName") String tableName,
                              @RequestParam(name = "bizId") int bizId,
                              @RequestParam(name = "bizName") String bizName,
                              @RequestParam(name = "dbSourceId") int dbSourceId,
                              @RequestParam(name = "id") int id,
                              Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobSource jobSource = jobServer.getJobById(id);
        String tablesStr = jobSource.getSelectData();
        JSONObject jsonObj = JSONObject.parseObject(tablesStr);
        String fieldArrJson ="[]";
        if(jsonObj!=null){
            Object obj = jsonObj.get(tableName);
            if(obj!=null){
                fieldArrJson = (JSONArray.toJSONString(obj));
            }
        }

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
        model.addAttribute("id", id);
        model.addAttribute("table",tableName);
        model.addAttribute("fieldArrJson",fieldArrJson);

        return "ds/ds_index_3";
    }

    @RequestMapping("/selectTable")
    public Map selectTable(@RequestParam(name = "jobId") int jobId,
                              @RequestParam(name = "tables") String tables,
                              Model model){

        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");

        JobSource jobSource = jobServer.getJobById(jobId);

        String selectData="";
        if(tables!=null&&!"".equals(tables)){
            String[] ts = tables.split(",");
            Set tableSet = new HashSet();
            for(String t : ts){
                tableSet.add(t);
            }
            String data = jobSource.getSelectData();
            JSONObject json = JSONObject.parseObject(data);
            if(json == null)
                json = new JSONObject();
            Set keySet = json.keySet();
            Iterator iter = keySet.iterator();
            while(iter.hasNext()){
                String key = (String)iter.next();
                if(!tableSet.contains(key))
                    json.remove(key);
            }
            selectData = json.toJSONString();
        }
        jobSource.setSelectData(selectData);
        jobSource.setSelectTable(tables);
        int res = jobServer.updateJob(jobSource);
        Map map = ControllerHelper.returnResponseVal(res, "保存");
        return map;
    }

    @RequestMapping("/selectField")
    public Map selectField(@RequestParam(name = "jobId") int jobId,
                           @RequestParam(name = "table") String table,
                           @RequestParam(name = "fields") String fields,
                           Model model){

        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobSource jobSource = jobServer.getJobById(jobId);
        String selectTable = jobSource.getSelectTable();
        if(selectTable==null||"".equals(selectTable)){
            selectTable = table;
        }else{
            boolean flag = false;
            String[] tables = selectTable.split(",");
            for(String t : tables){
                if(table.trim().equals(t.trim()))
                    flag = true;
            }
            if(!flag)
                selectTable = selectTable+","+table;
        }
        jobSource.setSelectTable(selectTable);

        String selectData = jobSource.getSelectData();
        JSONObject json = JSONObject.parseObject(selectData);
        if(json == null)
            json = new JSONObject();
        JSONArray fieldArr = JSONArray.parseArray(fields);
        json.put(table,fieldArr);
        jobSource.setSelectData(json.toJSONString());
        int res = jobServer.updateJob(jobSource);
        Map map = ControllerHelper.returnResponseVal(res, "保存");
        return map;
    }
}
