package com.github.trace.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.BuriedPoint0;
import com.github.trace.service.CEPService;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wujing on 2016/3/11.
 */
@Controller
@RequestMapping("/cep")
public class CEPTraceController {

    @Autowired
    private CEPService cepService;

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
        model.addAttribute("navigation_id", caller.getNavigationId());
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
    public String compare(@RequestParam("BuriedPointList") String BuriedPointList,
                              @RequestParam("str1") String str1,
                              @RequestParam("str2") int str2,
                              Model model) {

        String source = BuriedPointList+"";
        String target =  cepService.getServerLog( str1,str2 ).toString();
//        String target = "[{'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':9,'M98':'2016-01-28 20:51:32','M99.M1':'activite','realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':9,'M98':1453985500196,'M99.M1':'unactivite','realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':2,'M98':1453985500196,'M99.avg':0,'M99.M3':0,'M99.M2':0,'M99.failed':0,'M99.M1':0,'realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':2,'M98':1453984760374,'M99.avg':0,'M99.M3':0,'M99.M2':0,'M99.failed':0,'M99.M1':0,'realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':9,'M98':1453984760374,'M99.M1':'unactivite','realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}]";
        JSONArray jsonArray  = JSON.parseArray(target);
        JSONArray ja1 = new JSONArray();
        LinkedHashMap<String, String> jsonMap1 = JSON.parseObject(source, new TypeReference<LinkedHashMap<String, String>>() {});

        for (Map.Entry<String, String> entry1 : jsonMap1.entrySet()) {
            JSONArray ja2 = new JSONArray();
            ja2.add(entry1.getKey()+"");
            ja2.add(entry1.getValue().split(",")[0]+"");
            int size = jsonArray.size();
            for (int i = 0; i < 5; i++) {
                if(i>=size){
                    JSONArray ja3 = new JSONArray();
                    ja3.add(true);
                    ja3.add("");
                    ja3.add("");
                    ja2.add(ja3);
                    continue;
                }
                LinkedHashMap<String, String> jsonMap2 = JSON.parseObject(jsonArray.get(i).toString(), new TypeReference<LinkedHashMap<String, String>>() {});

                if(jsonMap2.containsKey(entry1.getKey()) ){
                    if(entry1.getValue().split(",")[2].equals("1")){
                        String patternString = "";
                        String patternString2 = "";

                        if(entry1.getValue().split(",")[1].equals("文本")){
                            patternString = ".*";
                        }

                        if(entry1.getValue().split(",")[1].equals("数字")){
                            patternString = "^[0-9]*$";
                        }

                        String value = jsonMap2.get(entry1.getKey());
                        Matcher matcher;
                        if(entry1.getValue().split(",")[1].equals("日期")){
                            patternString = "(\\d{2}|\\d{4})(?:\\-)?([0]{1}\\d{1}|[1]{1}[0-2]{1})(?:\\-)?([0-2]{1}\\d{1}|[3]{1}[0-1]{1})(?:\\s)?([0-1]{1}\\d{1}|[2]{1}[0-3]{1})(?::)?([0-5]{1}\\d{1})(?::)?([0-5]{1}\\d{1})";
                            Pattern patt = Pattern.compile("^[0-9]*$");
                            matcher = patt.matcher(value);
                            if(matcher.matches()){
                                try {
                                    long dateTime = Long.valueOf(value);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date(dateTime);
                                    value = sdf.format(date);
                                }catch (Exception e){

                                }
                            }
                        }
                        Pattern pattern = Pattern.compile(patternString);
                        matcher = pattern.matcher(value);
                        boolean b= matcher.matches();


                        if(entry1.getValue().split(",").length>=4){

                            try {
                                patternString2 = URLDecoder.decode(entry1.getValue().split(",")[3].toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            Pattern pattern2 = null;
                            pattern2 = Pattern.compile( patternString2);

                            Matcher matcher2 = pattern2.matcher(jsonMap2.get(entry1.getKey()));
                            b= matcher2.matches();
                        }
                        JSONArray ja3 = new JSONArray();
                        Object err = "";
                        String val = jsonMap2.get(entry1.getKey());
                        if(val==null)
                            err = "null";
                        else if("".equals(val))
                            err="空值";
                        else{
                            if(entry1.getValue().split(",")[1].equals("日期")){
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date date = new Date(Long.valueOf(val));
                                    val =sdf.format(date);
                                }catch (NumberFormatException e){

                                }
                            }
                            err=val;
                        }
                        ja3.add(b);
                        ja3.add(val);
                        ja3.add(err);
                        ja2.add(ja3);
                    }else{
                        String val = jsonMap2.get(entry1.getKey());
                        JSONArray ja3 = new JSONArray();
                        ja3.add(true);
                        ja3.add(val);
                        ja3.add(val);
                        ja2.add(ja3);
                    }
                }else{
                    JSONArray ja3 = new JSONArray();
                    ja3.add(false);
                    ja3.add("");
                    ja3.add("缺失");
                    ja2.add(ja3);
                }
            }
            ja1.add(ja2);
        }
        // 左边导航条
        ControllerHelper.setLeftNavigationTree(model, cepService, "");
        System.out.println("BuriedPointList"+ja1.toJSONString());
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("source",source);
        jsonObj.put("target",target);
        jsonObj.put("tableData",ja1);

        return jsonObj.toJSONString();
    }

}

