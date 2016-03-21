package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataTypeService;
import com.github.trace.utils.ControllerHelper;
import com.google.common.collect.ImmutableMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/3/17.
 */
@Controller
@RequestMapping("/dataType")
public class DataTypeController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private DataTypeService dataTypeService;

    @RequestMapping("/listLevelOne")
    public String getLeveOneFieldsList(Model model) {
        List<LevelOneFields> list = dataTypeService.getLevelOneFieldList();

        JSONArray jsonArray1 = new JSONArray();
        for (LevelOneFields levelOneField : list) {
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(levelOneField.getId());
            jsonArray2.add(levelOneField.getLevel1FieldTag());
            jsonArray2.add(levelOneField.getLevel1FieldName());
            jsonArray2.add(levelOneField.getLevel1FieldDesc());

            jsonArray1.add(jsonArray2);
        }

        ControllerHelper.setLeftNavigationTree(model, cepService);
        model.addAttribute("data", jsonArray1);
        return "data/data_list";
    }

    @RequestMapping("/listLevelTwo")
    public String getLeveTwoFieldsList(@RequestParam(name = "id") int id, Model model) {
        List<LevelTwoFields> list = dataTypeService.getLevelTwoFieldList(id);

        JSONArray jsonArray1 = new JSONArray();
        for (LevelTwoFields fieldObj : list) {
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(fieldObj.getId());
            jsonArray2.add(fieldObj.getLevel1FieldName() + "_" + fieldObj.getId());
            jsonArray2.add(fieldObj.getLevel1FieldTag());
            jsonArray2.add(fieldObj.getLevel2FieldName());

            jsonArray1.add(jsonArray2);
        }

        ControllerHelper.setLeftNavigationTree(model, cepService);
        model.addAttribute("data", jsonArray1);
        return "data/data_list_2";
    }

    @RequestMapping("/editLevelOne")
    public String editLevelOne(@RequestParam(name = "id") int id, @RequestParam(name = "tag") String tag, Model model) {
        LevelOneFields fieldObj = dataTypeService.getLevelOneFieldById(id);
        model.addAttribute("obj", fieldObj);

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("tag", tag);
        return "data/data_edit";
    }

    @RequestMapping("/editLevelTwo")
    public String editLevelTwo(@RequestParam(name = "id") int id, @RequestParam(name = "tag") String tag, Model model) {
        LevelTwoFields fieldObj = dataTypeService.getLevelTwoFieldById(id);
        model.addAttribute("obj", fieldObj);

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("tag", tag);
        return "data/data_edit_2";
    }

    @RequestMapping("/newConifg")
    public String newConfig(@RequestParam(name = "tag") String tag, @RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id, Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条
        return "data/data_edit";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map modifyConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @Param("id") int id) {
        return null;
    }

    @RequestMapping("/addLevelOne")
    @ResponseBody
    public Map addLevelOne(@Param("tag") String level1FieldTag, @Param("name") String level1FieldName, @Param("desc") String level1FieldDesc) {

        String result = "";

        LevelOneFields levelOneFields = new LevelOneFields();
        levelOneFields.setLevel1FieldTag(level1FieldTag);
        levelOneFields.setLevel1FieldName(level1FieldName);
        levelOneFields.setLevel1FieldDesc(level1FieldDesc);

        int res = dataTypeService.addLevelOneFields(levelOneFields);
        if(res == 1){
            result = "数据插入成功!";
            return ImmutableMap.of("code", 200, "info", result);
        }else{
            result = "数据插入失败！";
            return ImmutableMap.of("code", -1, "info", result);
        }
    }

}
