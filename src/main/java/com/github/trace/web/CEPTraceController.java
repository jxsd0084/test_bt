package com.github.trace.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.github.trace.entity.BuriedPoint0;
import com.github.trace.service.AnalyzeLogService;
import com.github.trace.service.CEPService;

import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by wujing on 2016/3/11.
 */
@Controller
@RequestMapping("/cep")
public class CEPTraceController {

    @Autowired
    private CEPService cepService;


    @Autowired
    private AnalyzeLogService analyzeLogService;

 /*   @RequestMapping("/list")
    public String callerList(@RequestParam(name = "parent_id") int parent_id,
                             @RequestParam(name = "child_id") int child_id,
                             @RequestParam(name = "parent_name") String parent_name,
                             @RequestParam(name = "topic") String topic,
                             Model model) {

        List<BuriedPoint> caller = cepService.getBuriedPointList(parent_id, child_id);

        JSONArray ja1 = new JSONArray();

        // data, type, full, meta
        for (BuriedPoint br : caller) {
            JSONArray ja2 = new JSONArray();
            //  ja2.add(br.getId());            // 编号
            ja2.add(br.getBpName());        // 埋点字段
            ja2.add(br.getBpValue());       // 埋点数据类型
            ja2.add(br.getRegex());         // 自定义正则表达式
            ja2.add(br.getBpValueDesc());   // 埋点字段描述
            ja2.add(br.getIsChecked());     // 是否必填项
            ja2.add(br.getId());            // 操作

            ja1.add(ja2);
        }

        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("topic", topic);
        model.addAttribute("data", ja1);
        return "cep/bp_list";
    }*/
 @RequestMapping("/list")
 public String callerList(@RequestParam(name = "navigationId") int navigationId,
                          @RequestParam(name = "topic") String topic,
                          @RequestParam(name = "navigationName") String navigationName,
                          Model model) {

     List<BuriedPoint0> caller = cepService.getBuriedPoint0List(navigationId);

     JSONArray ja1 = new JSONArray();

     // data, type, full, meta
     for (BuriedPoint0 br : caller) {
         JSONArray ja2 = new JSONArray();
         //  ja2.add(br.getId());            // 编号
         ja2.add(br.getBpName());        // 埋点字段
         ja2.add(br.getBpValue());       // 埋点数据类型
         ja2.add(br.getRegex());         // 自定义正则表达式
         ja2.add(br.getBpValueDesc());   // 埋点字段描述
         ja2.add(br.getIsChecked());     // 是否必填项
         ja2.add(br.getId());            // 操作

         ja1.add(ja2);
     }

     ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条
     model.addAttribute("navigation_name", navigationName);
     model.addAttribute("navigation_id", navigationId);
     model.addAttribute("topic", topic);
     model.addAttribute("data", ja1);
     return "cep/bp_list";
 }
    @RequestMapping("/new")
    public String createConfig(@RequestParam(name = "id") int id,
                               @RequestParam(name = "tag") String tag,
                               @RequestParam(name = "navigationId") int navigationId,
                               @RequestParam(name = "navigationName") String navigationName,
                               @RequestParam(name = "topic") String topic,
                               Model model) {

        BuriedPoint0 caller = cepService.getBuriedPoint0(id);
        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("BpName", caller.getBpName() );
        model.addAttribute("BpValue", caller.getBpValue() );
        model.addAttribute("Regex", caller.getRegex() );
        model.addAttribute("BpValueDesc", caller.getBpValueDesc() );
        model.addAttribute("IsChecked", caller.getIsChecked() );

 //       model.addAttribute("parent_id", caller.getParentId());
 //       model.addAttribute("child_id", caller.getChildId());
 //       model.addAttribute("parent_name", caller.getParentName());
        model.addAttribute("navigation_id", navigationId);
        model.addAttribute("topic", topic);
        model.addAttribute("navigation_name", navigationName);
        model.addAttribute("tag", tag);
        return "func/conf_create";
    }

   /* @RequestMapping("/newConifg")
    public String newConfig(@RequestParam(name = "tag") String tag,
                            @RequestParam(name = "parent_id") int parent_id,
                            @RequestParam(name = "child_id") int child_id,
                            @RequestParam(name = "parent_name") String parent_name,
                            @RequestParam(name = "topic") String topic,
                            Model model) {

        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("topic", topic);
        model.addAttribute("tag", tag);
        return "func/conf_create";
    }*/

    @RequestMapping("/newConifg")
    public String newConfig(@RequestParam(name = "tag") String tag,
                            @RequestParam(name = "navigationId") int navigationId,
                            @RequestParam(name = "navigationName") String navigationName,
                            @RequestParam(name = "topic") String topic,
                            Model model) {

        ControllerHelper.setLeftNavigationTree(model, cepService, ""); // 左边导航条
        model.addAttribute("navigation_id", navigationId);
        model.addAttribute("topic", topic);
        model.addAttribute("navigation_name", navigationName);
        model.addAttribute("tag", tag);
        return "func/conf_create";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modifyConfig(@RequestParam("bp_name") String bp_name,
                            @RequestParam("bp_value") String bp_value,
                            @RequestParam("regex") String regex,
                            @RequestParam("bp_value_desc") String bp_value_desc,
                            @RequestParam("is_checked") boolean is_checked,
                            @RequestParam("id") int id) {

        BuriedPoint0 buriedPoint = new BuriedPoint0();
        buriedPoint.setId(id);
        buriedPoint.setBpName(bp_name);
        buriedPoint.setBpValue(bp_value);
        buriedPoint.setRegex(regex);
        buriedPoint.setBpValueDesc(bp_value_desc);
        buriedPoint.setIsChecked(is_checked==true?1:0);


        int res = cepService.modifyBuriedPoint0(buriedPoint);

        return ControllerHelper.returnResponseVal(res, "更新");
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map addConfig(@RequestParam("bp_name") String bp_name,
                         @RequestParam("bp_value") String bp_value,
                         @RequestParam("regex") String regex,
                         @RequestParam("bp_value_desc") String bp_value_desc,
                         @RequestParam("is_checked") boolean is_checked,
                         @RequestParam(name = "navigationId") int navigation_id
                        ) {

        BuriedPoint0 buriedPoint = new BuriedPoint0();
        buriedPoint.setBpName(bp_name);
        buriedPoint.setBpValue(bp_value);
        buriedPoint.setBpValueDesc(bp_value_desc);
        buriedPoint.setIsChecked(is_checked==true?1:0);
//        buriedPoint.setParentId(parent_id);
//        buriedPoint.setParentName("");
 //       buriedPoint.setChildId(child_id);
//        buriedPoint.setChildName("");
        buriedPoint.setRegex(regex);
        buriedPoint.setNavigationId(navigation_id);
        int res = cepService.addBuriedPoint0(buriedPoint);

        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/delete")
    public String deleteBuriedPoint(@RequestParam("id") Integer id) {
        cepService.deleteById(id);
        return "func/bp_list";
    }

    @RequestMapping("/compare")
    @ResponseBody
    public String compare(@RequestParam("navName") String navName,
                              @RequestParam("str1") String str1,
                              @RequestParam("str2") int str2,
                              Model model) {

        String target =  cepService.getServerLog( str1,str2 ).toString();

        //target="[{\"realip\":\"172.28.0.10\",\"M1\":\"0\",\"M99.M3\":\"com.facishare.fs.biz_function.webview.JsApiWebActivity\",\"M2\":\"1335\",\"M99.M2\":\"onstart\",\"M3\":\"352621066268480\",\"M99.M1\":\"lifecycle\",\"M4\":\"Android\",\"_ip\":\"172.31.103.120\",\"M5\":\"5.0\",\"M6\":\"5.2.0.26816\",\"M7\":\"26816\",\"M97\":\"7\",\"M8\":\"samsung/SM-G9006V/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M98\":\"1461053910127\",\"_time\":\"2016-04-19 16:18:59.208\"},{\"realip\":\"172.28.0.10\",\"M1\":\"0\",\"M99.M3\":\"com.facishare.fs.contacts_fs.SelectEmpActivity\",\"M2\":\"1335\",\"M99.M2\":\"oncreate\",\"M3\":\"352621066268480\",\"M99.M1\":\"lifecycle\",\"M4\":\"Android\",\"_ip\":\"172.31.103.120\",\"M5\":\"5.0\",\"M6\":\"5.2.0.26816\",\"M7\":\"26816\",\"M97\":\"7\",\"M8\":\"samsung/SM-G9006V/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M98\":\"1461053886958\",\"_time\":\"2016-04-19 16:18:59.207\"},{\"realip\":\"172.29.0.3\",\"M99.target\":\"application\",\"M1\":\"fsfte2a\",\"M2\":\"1089\",\"M3\":\"3c723c59f90a8bd83c7f6c42a2d01283\",\"M99.M1\":\"lifecycle\",\"M4\":\"iPhone OS\",\"_ip\":\"172.31.103.120\",\"M5\":\"9.3\",\"M6\":\"5.3\",\"M7\":\"100155\",\"M97\":9,\"M8\":\"Simulator\",\"M96\":\"WIFI\",\"M98\":1460982536871,\"M99.method\":\"onstop\",\"_time\":\"2016-04-19 16:18:59.558\"},{\"realip\":\"172.28.0.10\",\"M1\":\"0\",\"M99.M3\":\"com.facishare.fs.contacts_fs.SelectEmpActivity\",\"M2\":\"1335\",\"M99.M2\":\"onstart\",\"M3\":\"352621066268480\",\"M99.M1\":\"lifecycle\",\"M4\":\"Android\",\"_ip\":\"172.31.103.120\",\"M5\":\"5.0\",\"M6\":\"5.2.0.26816\",\"M7\":\"26816\",\"M97\":\"7\",\"M8\":\"samsung/SM-G9006V/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M98\":\"1461053886975\",\"_time\":\"2016-04-19 16:18:59.207\"},{\"realip\":\"172.28.0.10\",\"M1\":\"0\",\"M99.M3\":\"com.facishare.fs.biz_function.webview.JsApiWebActivity\",\"M2\":\"1335\",\"M99.M2\":\"onstop\",\"M3\":\"352621066268480\",\"M99.M1\":\"lifecycle\",\"M4\":\"Android\",\"_ip\":\"172.31.103.120\",\"M5\":\"5.0\",\"M6\":\"5.2.0.26816\",\"M7\":\"26816\",\"M97\":\"7\",\"M8\":\"samsung/SM-G9006V/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M98\":\"1461053887494\",\"_time\":\"2016-04-19 16:18:59.208\"}]";
        JSONArray rt=analyzeLogService.formatLog(navName,target);

        ControllerHelper.setLeftNavigationTree(model, cepService, "");

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("target",target.replace("},","},\n"));
        jsonObj.put("tableData",rt);

        System.out.println(jsonObj.toJSONString());
        return jsonObj.toJSONString();
    }

}

