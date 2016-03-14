package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.intern.InnerUtil;
import com.github.trace.intern.TimePickerUtil;
import com.github.trace.service.CEPService;
import com.github.trace.service.InfluxRpcService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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
    public String callerList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {
        //List<Map> caller = influxRpcService.recentCaller(start, end);
        //model.addAttribute("data", InnerUtil.toJsonService(caller));

        List<BuriedPoint> caller = cepService.getConfiguration();

        JSONArray ja1 =  new JSONArray();

        //data, type, full, meta
        for (BuriedPoint br:caller) {
            JSONArray ja2 =  new JSONArray();

            //埋点字段
            ja2.add(br.getBpName());

            //埋点数据类型
            ja2.add(br.getBpValue());

            //埋点字段描述
            ja2.add(br.getBpValueDesc());

            //是否必填项
            ja2.add(br.getIsChecked());

            //操作

            ja1.add(ja2);
        }


        model.addAttribute("data", ja1);
        return "cep/bp_list";
    }

    @RequestMapping("/new")
    public String createConfig(Model model) {
        return "func/conf_create";
    }



}

