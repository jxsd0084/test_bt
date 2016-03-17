package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.NavigationItem;
import com.github.trace.service.CEPService;
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
 * Created by wujing on 2016/3/11.
 */
@Controller
@RequestMapping("/cep")
public class CEPTraceController {

    @Autowired
    private CEPService cepService;

    @RequestMapping("/list")
    public String callerList(@RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id, @RequestParam(name = "parent_name") String parent_name, Model model) {

        List<BuriedPoint> caller = cepService.getBuriedPointList(parent_id, child_id);

        JSONArray ja1 = new JSONArray();

        // data, type, full, meta
        for (BuriedPoint br : caller) {
            JSONArray ja2 = new JSONArray();
            ja2.add(br.getId());            // 编号
            ja2.add(br.getBpName());        // 埋点字段
            ja2.add(br.getBpValue());       // 埋点数据类型
            ja2.add(br.getBpValueDesc());   // 埋点字段描述
            ja2.add(br.getIsChecked());     // 是否必填项
            ja2.add(br.getId());            // 操作

            ja1.add(ja2);
        }

        setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("data", ja1);
        return "cep/bp_list";
    }

    @RequestMapping("/new")
    public String createConfig(@RequestParam(name = "id") int id, @RequestParam(name = "tag") String tag, Model model) {

        BuriedPoint caller = cepService.getBuriedPoint(id);
        setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("BpName", caller.getBpName() );
        model.addAttribute("BpValue", caller.getBpValue() );
        model.addAttribute("BpValueDesc", caller.getBpValueDesc() );
        model.addAttribute("IsChecked", caller.getIsChecked() );
        model.addAttribute("tag", tag);
        return "func/conf_create";
    }

    @RequestMapping("/newConifg")
    public String newConfig(@RequestParam(name = "tag") String tag, @RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id, Model model) {

        setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("tag", tag);
        return "func/conf_create";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modifyConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @Param("id") int id) {
        String result = "";

        boolean flag = cepService.modifyBuriedPoint(bp_name, bp_value, bp_value_desc, is_checked, id);
        if(flag){
            result = "数据修改成功!";
            return ImmutableMap.of("code", 200, "info", result);
        }else{
            result = "数据修改失败！";
            return ImmutableMap.of("code", -1, "info", result);
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map addConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id) {
        String result = "";

        BuriedPoint buriedPoint = new BuriedPoint();
        buriedPoint.setBpName(bp_name);
        buriedPoint.setBpValue(bp_value);
        buriedPoint.setBpValueDesc(bp_value_desc);
        buriedPoint.setIsChecked(is_checked==true?1:0);
        buriedPoint.setParentId(parent_id);
        buriedPoint.setParentName("");
        buriedPoint.setChildId(child_id);
        buriedPoint.setChildName("");
        buriedPoint.setRegex("");

        int res = cepService.addBuriedPoint(buriedPoint);
        if(res == 1){
            result = "数据插入成功!";
            return ImmutableMap.of("code", 200, "info", result);
        }else{
            result = "数据插入失败！";
            return ImmutableMap.of("code", -1, "info", result);
        }
    }

    @RequestMapping("/delete")
    public String deleteBuriedPoint(@Param("id") Integer id, Model model) {
        cepService.deleteBuriedPoint(id);
        return "func/bp_list";
    }

    @RequestMapping("/format")
    @ResponseBody
    public String format(@Param("BuriedPointList") String BuriedPointList, Model model) {

        System.out.println("BP"+BuriedPointList);

        return BuriedPointList+"";
    }

    @RequestMapping("/compare")
    @ResponseBody
    public String compare(@Param("Source") String str1,@Param("Target") String str2, Model model) {

        System.out.println("BP"+str1);
        System.out.println("BP"+str2);

        return "compare";
    }

    // 此处为防止页面刷新之后, 左边导航条的数据丢失
    private void setLeftNavigationTree(Model model, CEPService cepService){
        List<NavigationItem> navigationItemList = cepService.getConfiguration();
        model.addAttribute("navigationItemList", navigationItemList);
    }

}

