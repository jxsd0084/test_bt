package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.NavigationItem;
import com.github.trace.service.CEPService;
import com.github.trace.service.InfluxRpcService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wujing on 2016/3/11.
 */
@Controller
@RequestMapping("/cep")
public class CEPTraceController {
    @Autowired
    private InfluxRpcService influxRpcService;

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

        // 此处为防止页面刷新之后, 左边导航条的数据丢失
        List<NavigationItem> navigationItemList = cepService.getConfiguration();
        model.addAttribute("navigationItemList", navigationItemList);

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("data", ja1);
        return "cep/bp_list";
    }

    @RequestMapping("/new")
    public String createConfig(@RequestParam(name = "id") int id,Model model) {


        BuriedPoint caller = cepService.getBuriedPoint(id);

        model.addAttribute("id", id );
        model.addAttribute("BpName", caller.getBpName() );
        model.addAttribute("BpValue", caller.getBpValue() );
        model.addAttribute("BpValueDesc", caller.getBpValueDesc() );
        model.addAttribute("IsChecked", caller.getIsChecked() );
        return "func/conf_create";
    }

    @RequestMapping("/modify")
    public boolean modifyConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @Param("id") int id, Model model) {

       return cepService.modifyBuriedPoint(bp_name,bp_value,bp_value_desc,is_checked,id);

     //   return "func/conf_create";
    }

}

