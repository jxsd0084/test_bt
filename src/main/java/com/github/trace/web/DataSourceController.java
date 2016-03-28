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

import java.util.List;


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
    @Autowired

    @RequestMapping("/list")
    public String index(@RequestParam(name = "biz_id") int bizId,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        List<DatabaseInfo> list = dataSourceServer.getDataBaseInfoListById(bizId);
        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
        model.addAttribute("data", jsonArray);
        return "ds/ds_index";
    }

    @RequestMapping("/edit")
    public String edit(Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return "ds/ds_edit";
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

}
