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

        System.out.println(caller);

        String temp;

        JSONArray ja1 =  new JSONArray();



        //data, type, full, meta
        for (BuriedPoint br:caller) {

//            JSONObject jo1 =  new JSONObject();
//            JSONObject jo2 =  new JSONObject();
//            JSONObject jo3 =  new JSONObject();
//            JSONObject jo4 =  new JSONObject();
//            jo1.put("data",br.getId());
//            jo2.put("type",br.getId());
//            jo3.put("full",br.getId());
//            jo4.put("meta",br.getId());
//            ja2.add(jo1);
//            ja2.add(jo2);
//            ja2.add(jo3);
//            ja2.add(jo4);
            JSONArray ja2 =  new JSONArray();
            ja2.add(br.getKey());
            ja2.add(br.getValue());
            ja2.add(br.getKeyType());
            ja2.add(br.getValueType());

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

