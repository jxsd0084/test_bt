package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataTypeService;
import com.github.trace.utils.ControllerHelper;
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

    @RequestMapping("/new")
    public String newConfig(@RequestParam(name = "tag") String tag, @RequestParam(name = "lev") int lev, Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条
        model.addAttribute("tag", tag);

        if (lev == 1){
            return "data/data_edit";
        }else{
            return "data/data_edit_2";
        }
    }

    @RequestMapping("/modifyLevelOne")
    @ResponseBody
    public Map modifyLevelOne(@Param("L1_tag") String l1_tag, @Param("L1_name") String l1_name, @Param("L1_desc") String l1_desc, @Param("id") int id) {

        LevelOneFields levelOneFields = new LevelOneFields();
        levelOneFields.setId(id);
        levelOneFields.setLevel1FieldTag(l1_tag);
        levelOneFields.setLevel1FieldName(l1_name);
        levelOneFields.setLevel1FieldDesc(l1_desc);

        int res = dataTypeService.updateLevelOne(levelOneFields);

        return ControllerHelper.returnResponseVal(res);

    }

    @RequestMapping("/modifyLevelTwo")
    @ResponseBody
    public Map modifyLevelTwo(@Param("L1_tag") String l1_tag, @Param("L1_name") String l1_name, @Param("L2_name") String l2_name, @Param("L2_desc") String l2_desc, @Param("id") int id) {

        LevelTwoFields levelTwoFields = new LevelTwoFields();
        levelTwoFields.setId(id);
        levelTwoFields.setLevel1FieldTag(l1_tag);
        levelTwoFields.setLevel1FieldName(l1_name);
        levelTwoFields.setLevel2FieldName(l2_name);
        levelTwoFields.setLevel2FieldDesc(l2_desc);

        int res = dataTypeService.updateLevelTwo(levelTwoFields);

        return ControllerHelper.returnResponseVal(res);

    }

    @RequestMapping("/addLevelOne")
    @ResponseBody
    public Map addLevelOne(@Param("L1_tag") String tag, @Param("L1_name") String name, @Param("L1_desc") String desc) {

        LevelOneFields levelOneFields = new LevelOneFields();
        levelOneFields.setLevel1FieldTag("a2");
        levelOneFields.setLevel1FieldName("a2");
        levelOneFields.setLevel1FieldDesc("a2");

        int res = dataTypeService.addLevelOneFields(levelOneFields);

        return ControllerHelper.returnResponseVal(res);

    }

    @RequestMapping("/addLevelTwo")
    @ResponseBody
    public Map addLevelTwo(@Param("L1_tag") String l1_tag, @Param("L1_name") String l1_name, @Param("L2_name") String l2_name, @Param("L2_desc") String l2_desc) {

        LevelTwoFields levelTwoFields = new LevelTwoFields();
        levelTwoFields.setLevel1FieldId(3);
        levelTwoFields.setLevel1FieldTag("a2");
        levelTwoFields.setLevel1FieldName("a2");
        levelTwoFields.setLevel2FieldName("a2");
        levelTwoFields.setLevel2FieldDesc("xxx");

        int res = dataTypeService.addLevelTwoFields(levelTwoFields);

        return ControllerHelper.returnResponseVal(res);

    }

}
