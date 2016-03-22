package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataTypeService;
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
    public String getLeveTwoFieldsList(@RequestParam(name = "id") int id,
                                       @RequestParam(name = "L1_tag") String l1_tag,
                                       @RequestParam(name = "L1_name") String l1_name,
                                       Model model) {
        List<LevelTwoFields> list = dataTypeService.getLevelTwoFieldList(id);

        JSONArray jsonArray1 = new JSONArray();
        for (LevelTwoFields fieldObj : list) {
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(fieldObj.getId());
            jsonArray2.add(fieldObj.getLevel1FieldName() + "_" + fieldObj.getId());
            jsonArray2.add(fieldObj.getLevel1FieldTag());
            jsonArray2.add(fieldObj.getLevel2FieldName());
            jsonArray2.add(fieldObj.getLevel2FieldDesc());

            jsonArray1.add(jsonArray2);
        }

        ControllerHelper.setLeftNavigationTree(model, cepService);
        model.addAttribute("data", jsonArray1);
        model.addAttribute("L1_id", id);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("L1_name", l1_name);
        return "data/data_list_2";
    }

    @RequestMapping("/editLevelOne")
    public String editLevelOne(@RequestParam(name = "id") int id,
                               @RequestParam(name = "tag") String tag,
                               Model model) {
        LevelOneFields fieldObj = dataTypeService.getLevelOneFieldById(id);
        model.addAttribute("obj", fieldObj);

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("tag", tag);
        return "data/data_edit";
    }

    @RequestMapping("/editLevelTwo")
    public String editLevelTwo(@RequestParam(name = "id") int id,
                               @RequestParam(name = "tag") String tag,
                               @RequestParam(name = "L1_tag") String l1_tag,
                               @RequestParam(name = "L1_name") String l1_name,
                               Model model) {
        LevelTwoFields fieldObj = dataTypeService.getLevelTwoFieldById(id);
        model.addAttribute("obj", fieldObj);

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("tag", tag);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("L1_name", l1_name);
        return "data/data_edit_2";
    }

    @RequestMapping("/new")
    public String newConfig(@RequestParam(name = "tag") String tag,
                            @RequestParam(name = "lev") int lev,
                            @RequestParam(name = "L1_id", required = false) String l1_id,
                            @RequestParam(name = "L1_tag", required = false) String l1_tag,
                            @RequestParam(name = "L1_name", required = false) String l1_name,
                            Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条
        model.addAttribute("tag", tag);

        if (lev == 1){
            return "data/data_edit";
        }else{
            model.addAttribute("L1_id", l1_id);
            model.addAttribute("L1_tag", l1_tag);
            model.addAttribute("L1_name", l1_name);
            return "data/data_edit_2";
        }
    }

    @RequestMapping("/modifyLevelOne")
    @ResponseBody
    public Map modifyLevelOne(@RequestParam("L1_tag") String l1_tag,
                              @RequestParam("L1_name") String l1_name,
                              @RequestParam("L1_desc") String l1_desc,
                              @RequestParam("id") int id) {
        LevelOneFields levelOneFields = new LevelOneFields();
        levelOneFields.setId(id);
        levelOneFields.setLevel1FieldTag(l1_tag);
        levelOneFields.setLevel1FieldName(l1_name);
        levelOneFields.setLevel1FieldDesc(l1_desc);

        int res = dataTypeService.updateLevelTwoByCascade(levelOneFields);

        return ControllerHelper.returnResponseVal(res, "更新");

    }

    @RequestMapping("/modifyLevelTwo")
    @ResponseBody
    public Map modifyLevelTwo(@RequestParam("L1_tag") String l1_tag,
                              @RequestParam("L1_name") String l1_name,
                              @RequestParam("L2_name") String l2_name,
                              @RequestParam("L2_desc") String l2_desc,
                              @RequestParam("id") int id) {
        LevelTwoFields levelTwoFields = new LevelTwoFields();
        levelTwoFields.setId(id);
        levelTwoFields.setLevel1FieldTag(l1_tag);
        levelTwoFields.setLevel1FieldName(l1_name);
        levelTwoFields.setLevel2FieldName(l2_name);
        levelTwoFields.setLevel2FieldDesc(l2_desc);

        int res = dataTypeService.updateLevelTwo(levelTwoFields);

        return ControllerHelper.returnResponseVal(res, "更新");

    }

    @RequestMapping("/addLevelOne")
    @ResponseBody
    public Map addLevelOne(@RequestParam("L1_tag") String l1_tag,
                           @RequestParam("L1_name") String l1_name,
                           @RequestParam("L1_desc") String l1_desc) {
        LevelOneFields levelOneFields = new LevelOneFields();
        levelOneFields.setLevel1FieldTag(l1_tag);
        levelOneFields.setLevel1FieldName(l1_name);
        levelOneFields.setLevel1FieldDesc(l1_desc);

        int res = dataTypeService.addLevelOneFields(levelOneFields);

        return ControllerHelper.returnResponseVal(res, "添加");

    }

    @RequestMapping("/addLevelTwo")
    @ResponseBody
    public Map addLevelTwo(@RequestParam("L1_id") int l1_id,
                           @RequestParam("L1_tag") String l1_tag,
                           @RequestParam("L1_name") String l1_name,
                           @RequestParam("L2_name") String l2_name,
                           @RequestParam("L2_desc") String l2_desc) {
        LevelTwoFields levelTwoFields = new LevelTwoFields();
        levelTwoFields.setLevel1FieldId(l1_id);
        levelTwoFields.setLevel1FieldTag(l1_tag);
        levelTwoFields.setLevel1FieldName(l1_name);
        levelTwoFields.setLevel2FieldName(l2_name);
        levelTwoFields.setLevel2FieldDesc(l2_desc);

        int res = dataTypeService.addLevelTwoFields(levelTwoFields);

        return ControllerHelper.returnResponseVal(res, "添加");

    }

}
