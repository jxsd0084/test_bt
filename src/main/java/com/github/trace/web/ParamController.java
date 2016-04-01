package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.BpJob;
import com.github.trace.entity.NavigationItem;
import com.github.trace.service.CEPService;
import com.github.trace.service.ParamService;
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
 * Created by chenlong on 2016/3/24.
 */
@Controller
@RequestMapping("/param")
public class ParamController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private ParamService paramService;

    @RequestMapping("/list")
    public String listParams(Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, "");
        List<BpJob> list = paramService.getBpJobList();
//        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
        JSONArray jsonArray1 = new JSONArray();
        for (BpJob job : list){
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(job.getId());
            jsonArray2.add(job.getBizName());
            jsonArray2.add(job.getConnection());
            jsonArray2.add(job.getTableName());
            jsonArray2.add(job.getTargetName());
            jsonArray2.add(job.getTargetAddress());
            jsonArray2.add(job.getTargetPath());
            jsonArray2.add(job.getExecuteTime());

            jsonArray1.add(jsonArray2);
        }
        model.addAttribute("data", jsonArray1);
        return "param/param_list";
    }

    @RequestMapping("/new")
    public String newParam(Model model) {
        List<NavigationItem> list = ControllerHelper.setLeftNavigationTree(model, cepService, "");
        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
        model.addAttribute("data", jsonArray);
        return "param/param_edit";
    }

    @RequestMapping("/edit")
    public String editParam(@RequestParam(name = "id") int id,
                            Model model) {
        NavigationItem navObj = cepService.getConfigById(id);

        model.addAttribute("id", id);
        model.addAttribute("navObj", navObj);
        return "param/param_edit";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map updateParam(@RequestParam(name = "id") int id,
                           @RequestParam(name = "topic") String topic) {
        NavigationItem navObj = cepService.getConfigById(id);
        navObj.setTopic(topic);
        int res = cepService.updateConfig(navObj);
        return ControllerHelper.returnResponseVal(res, "更新");
    }

}
