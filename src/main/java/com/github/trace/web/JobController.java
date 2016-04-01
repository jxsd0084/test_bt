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
 * Created by chenlong on 2016/4/1.
 */
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private DataSourceServer dataSourceServer;

    @RequestMapping("/list")
    public String index(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "job/job_index");
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
