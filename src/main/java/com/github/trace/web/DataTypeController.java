package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.entity.M99Fields;
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

    @RequestMapping("/listM99")
    public String listM99(@RequestParam(name = "L1_id")   int    l1_id,
                          @RequestParam(name = "L2_id")   int    l2_id,
                          @RequestParam(name = "L1_tag")  String l1_tag,
                          @RequestParam(name = "L2_tag")  String l2_tag,
                          @RequestParam(name = "L1_name") String l1_name,
                          @RequestParam(name = "id")      int    id,
                          Model model) {
        JSONArray jsonArray = getM99FieldsList(l1_tag);
        ControllerHelper.setLeftNavigationTree(model, cepService, "");
        model.addAttribute("data", jsonArray);
        model.addAttribute("L1_id", l1_id);
        model.addAttribute("L2_id", l2_id);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("L2_tag", l2_tag);
        model.addAttribute("L1_name", l1_name);
        model.addAttribute("id", id);
        return "data/m99_list";
    }

    @RequestMapping("/newM99")
    public String newM99(@RequestParam(name = "L1_id")  int    l1_id,
                         @RequestParam(name = "L2_id")  int    l2_id,
                         @RequestParam(name = "L1_tag") String l1_tag,
                         @RequestParam(name = "L2_tag") String l2_tag,
                         @RequestParam(name = "tag")    String tag,
                         Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条
        model.addAttribute("tag", tag);
        model.addAttribute("L1_id", l1_id);
        model.addAttribute("L2_id", l2_id);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("L2_tag", l2_tag);

        return "data/m99_edit";
    }

    @RequestMapping("/editM99")
    public String editM99(@RequestParam(name = "id")     int    id,
                          @RequestParam(name = "L1_id")  int    l1_id,
                          @RequestParam(name = "L1_tag") String l1_tag,
                          @RequestParam(name = "tag")    String tag,
                          Model model) {
        M99Fields m99 = dataTypeService.getM99FieldsById(id);
        model.addAttribute("obj", m99);

        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条

        model.addAttribute("id", id);
        model.addAttribute("L1_id", l1_id);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("tag", tag);
        return "data/m99_edit";
    }

    @RequestMapping("/modifyM99")
    @ResponseBody
    public Map modifyM99(@RequestParam("L1_id")   int    l1_id,
                         @RequestParam("L1_tag")  String l1_tag,
                         @RequestParam("l1_name") String l1_name,
                         @RequestParam("l1_desc") String l1_desc,
                         @RequestParam("l1_type") String l1_type,
                         @RequestParam("l1_regx") String l1_regx,
                         @RequestParam("id") int id) {
        M99Fields m99Fields = new M99Fields();
        m99Fields.setId(id);
        m99Fields.setLevelOneId(l1_id);
        m99Fields.setM1Name(l1_tag);
        m99Fields.setFieldName(l1_name);
        m99Fields.setFieldDesc(l1_desc);
        m99Fields.setFieldType(l1_type);
        m99Fields.setFieldRegex(l1_regx);

        int res = dataTypeService.updateM99Fields(m99Fields);

        return ControllerHelper.returnResponseVal(res, "更新");
    }

    @RequestMapping("/addM99")
    @ResponseBody
    public Map addM99(@RequestParam("L1_id")   int    l1_id,
                      @RequestParam("L1_tag")  String l1_tag,
                      @RequestParam("l1_name") String l1_name,
                      @RequestParam("l1_desc") String l1_desc,
                      @RequestParam("l1_type") String l1_type,
                      @RequestParam("l1_regx") String l1_regx) {
        M99Fields m99Fields = new M99Fields();
        m99Fields.setLevelOneId(l1_id);
        m99Fields.setM1Name(l1_tag);
        m99Fields.setFieldName(l1_name);
        m99Fields.setFieldDesc(l1_desc);
        m99Fields.setFieldType(l1_type);
        m99Fields.setFieldRegex(l1_regx);

        int res = dataTypeService.addM99Fields(m99Fields);

        return ControllerHelper.returnResponseVal(res, "更新");

    }

    @RequestMapping("/listLevelOne")
    public String listLevelOneFields(Model model) {
        JSONArray jsonArray = getLevelOneFieldList();
        ControllerHelper.setLeftNavigationTree(model, cepService, "");
        model.addAttribute("data", jsonArray);
        return "data/data_list";
    }

    @RequestMapping("/listLevelTwo")
    public String listLevelTwoFields(@RequestParam(name = "L1_id")   int    l1_id,
                                     @RequestParam(name = "L1_tag")  String l1_tag,
                                     @RequestParam(name = "L1_name") String l1_name,
                                     Model model) {

        ControllerHelper.setLeftNavigationTree(model, cepService, "");

        JSONArray jsonArray = getLevelTwoFieldList(l1_id);

        model.addAttribute("data", jsonArray);
        model.addAttribute("L1_id", l1_id);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("L1_name", l1_name);
        return "data/data_list_2";
    }

    @RequestMapping("/editLevelOne")
    public String editLevelOne(@RequestParam(name = "L1_id") int l1_id,
                               @RequestParam(name = "tag") String tag,
                               Model model) {
        LevelOneFields fieldObj = dataTypeService.getLevelOneFieldById(l1_id);
        model.addAttribute("obj", fieldObj);

        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条

        model.addAttribute("id", l1_id);
        model.addAttribute("tag", tag);
        return "data/data_edit";
    }

    @RequestMapping("/editLevelTwo")
    public String editLevelTwo(@RequestParam(name = "id") int id,
                               @RequestParam(name = "tag") String tag,
                               @RequestParam(name = "L1_id") String l1_id,
                               @RequestParam(name = "L1_tag") String l1_tag,
                               @RequestParam(name = "L1_name") String l1_name,
                               Model model) {
        LevelTwoFields fieldObj = dataTypeService.getLevelTwoFieldById(id);
        model.addAttribute("obj", fieldObj);

        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("tag", tag);
        model.addAttribute("L1_id", l1_id);
        model.addAttribute("L1_tag", l1_tag);
        model.addAttribute("L1_name", l1_name);
        return "data/data_edit_2";
    }

    @RequestMapping("/new")
    public String newLevel(@RequestParam(name = "tag") String tag,
                            @RequestParam(name = "lev") int lev,
                            @RequestParam(name = "L1_id", required = false) String l1_id,
                            @RequestParam(name = "L1_tag", required = false) String l1_tag,
                            @RequestParam(name = "L1_name", required = false) String l1_name,
                            Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条
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
    public Map modifyLevelOne(@RequestParam("L1_tag")  String l1_tag,
                              @RequestParam("L1_name") String l1_name,
                              @RequestParam("L1_desc") String l1_desc,
                              @RequestParam("id") int id) {
        LevelOneFields levelOneFields = new LevelOneFields();
        levelOneFields.setId(id);
        levelOneFields.setLevel1FieldTag(l1_tag);
        levelOneFields.setLevel1FieldName(l1_name);
        levelOneFields.setLevel1FieldDesc(l1_desc);

        int res = dataTypeService.updateFieldsByCascade(levelOneFields);

        return ControllerHelper.returnResponseVal(res, "更新");

    }

    @RequestMapping("/modifyLevelTwo")
    @ResponseBody
    public Map modifyLevelTwo(@RequestParam("L1_tag")  String l1_tag,
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
    public Map addLevelOne(@RequestParam("L1_tag")  String l1_tag,
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
                           @RequestParam("L1_tag")  String l1_tag,
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

    /**
     * 一级字段列表
     * @return
     */
    public JSONArray getLevelOneFieldList() {
        List<LevelOneFields> list = dataTypeService.getLevelOneFieldList();
        JSONArray jsonArray1 = new JSONArray();
        for (LevelOneFields levelOneFields : list ) {
            JSONArray jsonArray2 = new JSONArray();
            String tagName = levelOneFields.getLevel1FieldTag();
            jsonArray2.add(levelOneFields.getId());
            jsonArray2.add(tagName);                                    // 标识 样例:AV
            jsonArray2.add(levelOneFields.getLevel1FieldName());        // 名称 样例:音视频
            jsonArray2.add(levelOneFields.getLevel1FieldDesc());        // 描述
          /*  int m99Count = dataTypeService.getM99FieldsCount(tagName);  // M99的扩展字段
            jsonArray2.add(m99Count);*/
            jsonArray1.add(jsonArray2);
        }
        return jsonArray1;
    }

    /**
     * 二级字段列表
     * @param l1_id
     * @return
     */
    private JSONArray getLevelTwoFieldList(int l1_id) {
        List<LevelTwoFields> list = dataTypeService.getLevelTwoFieldList(l1_id);
        JSONArray jsonArray1 = new JSONArray();
        for (LevelTwoFields levelTwoFields : list) {
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(levelTwoFields.getId());                     // 编号
            jsonArray2.add("");                                         // 占位
            jsonArray2.add(levelTwoFields.getLevel2FieldName());        // 二级组件名称 样例:音视频
            jsonArray2.add(levelTwoFields.getLevel2FieldDesc());        // 一级组件名称
            jsonArray2.add(levelTwoFields.getLevel1FieldId());          // 一级组件Id
            jsonArray2.add(levelTwoFields.getLevel1FieldTag());         // 一级组件标识 样例:AV

            jsonArray1.add(jsonArray2);
        }
        return jsonArray1;
    }

    /**
     * M99字段列表
     * @param l1_name
     * @return
     */
    private JSONArray getM99FieldsList(String l1_name) {
        List<M99Fields> list = dataTypeService.getM99Fields(l1_name);
        JSONArray jsonArray1 = new JSONArray();
        for (M99Fields m99 : list) {
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(m99.getId());
            jsonArray2.add(m99.getM1Name());                                        // M1           样例:AV
            jsonArray2.add(m99.getFieldName());                                     // 字段名称      样例:M2
            jsonArray2.add(m99.getFieldDesc());                                     // 字段描述      样例:音视频2
            jsonArray2.add(m99.getFieldType());                                     // 字段类型      样例:文本、日期、数字
            jsonArray2.add(m99.getFieldRegex());                                    // 正则表达式
            jsonArray2.add(m99.getLevelOneId());                                    // M1-Id        样例:1
            int m99Count = dataTypeService.getM99FieldsCount(m99.getFieldName());   // M99的扩展字段
            jsonArray2.add(m99Count);
            jsonArray1.add(jsonArray2);
        }
        return jsonArray1;
    }

}



