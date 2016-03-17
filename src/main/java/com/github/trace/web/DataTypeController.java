package com.github.trace.web;

import com.github.trace.service.CEPService;
import com.github.trace.utils.ControllerHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by chenlong on 2016/3/17.
 */
@Controller
@RequestMapping("/dataType")
public class DataTypeController {

    @Autowired
    private CEPService cepService;

    @RequestMapping("/list")
    public String callerList(@RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id, @RequestParam(name = "parent_name") String parent_name, Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService);
        return "data/data_list";
    }

    @RequestMapping("/new")
    public String createConfig(@RequestParam(name = "id") int id, @RequestParam(name = "tag") String tag, Model model) {
        return "func/conf_create";
    }

    @RequestMapping("/newConifg")
    public String newConfig(@RequestParam(name = "tag") String tag, @RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id, Model model) {
        return "func/conf_create";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modifyConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @Param("id") int id) {
        return null;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map addConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id) {
        return null;
    }

    @RequestMapping("/delete")
    public String deleteBuriedPoint(@Param("id") Integer id, Model model) {
        return "func/bp_list";
    }

    @RequestMapping("/format")
    @ResponseBody
    public String format(@Param("BuriedPointList") String BuriedPointList, Model model) {
        return "";
    }

    @RequestMapping("/compare")
    @ResponseBody
    public String compare(@Param("Source") String str1,@Param("Target") String str2, Model model) {
        return "";
    }

}
