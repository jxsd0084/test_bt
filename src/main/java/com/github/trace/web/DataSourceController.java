package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataSourceServer;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * Created by chenlong on 2016/3/25.
 */
@Controller
@RequestMapping("/ds")
public class DataSourceController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private DataSourceServer dataSourceServer;

    @RequestMapping("/list")
    public String index(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "ds/ds_index");
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "bizId") int bizId,
                       @RequestParam(name = "bizName") String bizName,
                       @RequestParam(name = "tag") String tag,
                       Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        return "ds/ds_edit";
    }

    @RequestMapping("/modify")
    public String modify(@RequestParam(name = "id") int id,
                         @RequestParam(name = "tag") String tag,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        DatabaseInfo dataBaseInfo = dataSourceServer.getDataBaseInfoById(id);
        model.addAttribute("tag", tag);
        model.addAttribute("obj", dataBaseInfo);
        return "ds/ds_edit";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam(name = "id") int id,
                         @RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        dataSourceServer.deleteDataBaseInfoById(id);
        return setCommonParam(bizId, bizName, model, "ds/ds_index");
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public Map add(@RequestParam(name = "bizId") int bizId,
                   @RequestParam(name = "bizName") String bizName,
                   @RequestParam(name = "dbType") String dbType,
                   @RequestParam(name = "name") String name,
                   @RequestParam(name = "dbDriver") String dbDriver,
                   @RequestParam(name = "dbUrl") String dbUrl,
                   @RequestParam(name = "dbPort") int dbPort,
                   @RequestParam(name = "dbName") String dbName,
                   @RequestParam(name = "dbUsername") String dbUsername,
                   @RequestParam(name = "dbPassword") String dbPassword,
                   Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");

        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setBizId(bizId);
        databaseInfo.setBizName(bizName);
        databaseInfo.setDbType(dbType);
        databaseInfo.setName(name);
        databaseInfo.setDbDriver(dbDriver);
        databaseInfo.setDbUrl(dbUrl);
        databaseInfo.setDbPort(dbPort);
        databaseInfo.setDbName(dbName);
        databaseInfo.setDbUsername(dbUsername);
        databaseInfo.setDbPassword(dbPassword);

        int res = dataSourceServer.addDatabaseInfo(databaseInfo);

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return ControllerHelper.returnResponseVal(res, "更新");
    }

    @RequestMapping("/tblsIndex")
    public String tablesIndex(Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return "ds/ds_index_2";
    }

    @RequestMapping("/fldsIndex")
    public String fieldsIndex(Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return "ds/ds_index_3";
    }

    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
        List<DatabaseInfo> list = dataSourceServer.getDataBaseInfoListById(bizId);
        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
        model.addAttribute("data", jsonArray);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return retPath;
    }

}
