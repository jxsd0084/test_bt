package com.github.trace.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.github.trace.entity.BuriedPoint;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @RequestMapping("/list")
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

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("topic", topic);
        model.addAttribute("data", ja1);
        return "cep/bp_list";
    }

    @RequestMapping("/new")
    public String createConfig(@RequestParam(name = "id") int id,
                               @RequestParam(name = "tag") String tag,
                               Model model) {

        BuriedPoint caller = cepService.getBuriedPoint(id);
        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("id", id );
        model.addAttribute("BpName", caller.getBpName() );
        model.addAttribute("BpValue", caller.getBpValue() );
        model.addAttribute("Regex", caller.getRegex() );
        model.addAttribute("BpValueDesc", caller.getBpValueDesc() );
        model.addAttribute("IsChecked", caller.getIsChecked() );

        model.addAttribute("parent_id", caller.getParentId());
        model.addAttribute("child_id", caller.getChildId());
        model.addAttribute("parent_name", caller.getParentName());

        model.addAttribute("tag", tag);
        return "func/conf_create";
    }

    @RequestMapping("/newConifg")
    public String newConfig(@RequestParam(name = "tag") String tag,
                            @RequestParam(name = "parent_id") int parent_id,
                            @RequestParam(name = "child_id") int child_id,
                            @RequestParam(name = "parent_name") String parent_name,
                            @RequestParam(name = "topic") String topic,
                            Model model) {

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("topic", topic);
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

        BuriedPoint buriedPoint = new BuriedPoint();
        buriedPoint.setId(id);
        buriedPoint.setBpName(bp_name);
        buriedPoint.setBpValue(bp_value);
        buriedPoint.setRegex(regex);
        buriedPoint.setBpValueDesc(bp_value_desc);
        buriedPoint.setIsChecked(is_checked==true?1:0);

        int res = cepService.modifyBuriedPoint(buriedPoint);

        return ControllerHelper.returnResponseVal(res, "更新");
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map addConfig(@RequestParam("bp_name") String bp_name,
                         @RequestParam("bp_value") String bp_value,
                         @RequestParam("regex") String regex,
                         @RequestParam("bp_value_desc") String bp_value_desc,
                         @RequestParam("is_checked") boolean is_checked,
                         @RequestParam(name = "parent_id") int parent_id,
                         @RequestParam(name = "child_id") int child_id) {

        BuriedPoint buriedPoint = new BuriedPoint();
        buriedPoint.setBpName(bp_name);
        buriedPoint.setBpValue(bp_value);
        buriedPoint.setBpValueDesc(bp_value_desc);
        buriedPoint.setIsChecked(is_checked==true?1:0);
        buriedPoint.setParentId(parent_id);
//        buriedPoint.setParentName("");
        buriedPoint.setChildId(child_id);
//        buriedPoint.setChildName("");
        buriedPoint.setRegex(regex);

        int res = cepService.addBuriedPoint(buriedPoint);

        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/delete")
    public String deleteBuriedPoint(@RequestParam("id") Integer id) {
        cepService.deleteById(id);
        return "func/bp_list";
    }

    @RequestMapping("/format")
    @ResponseBody
    public String format(@RequestParam("BuriedPointList") String BuriedPointList,
                         Model model) {

        System.out.println("BuriedPointList"+BuriedPointList);

        return BuriedPointList+"";
    }

    @RequestMapping("/serverLog")
    @ResponseBody
    public String serverLog(@RequestParam("str1") String str1,
                            @RequestParam("str2") int str2,
                            Model model) {

        Set<String> serverLogSetList = cepService.getServerLog( str1,str2 );

//        String results = "";
//
//        for (String string: serverLogSetList ) {
//            results += string+"\n";
//        }

        return serverLogSetList.toString();
    }

    @RequestMapping("/compare")
    @ResponseBody
    public String compare(@RequestParam("Source") String str1,
                          @RequestParam("Target") String str2,
                          Model model) {

        JSONArray jsonArray  = JSON.parseArray(str2);


        JSONArray ja1 = new JSONArray();

        LinkedHashMap<String, String> jsonMap1 = JSON.parseObject(str1, new TypeReference<LinkedHashMap<String, String>>() {});


            for (Map.Entry<String, String> entry1 : jsonMap1.entrySet()) {
                JSONArray ja2 = new JSONArray();
                ja2.add(entry1.getKey()+"");
                ja2.add(entry1.getValue().split(",")[0]+"");
                for (int i = 0; i < jsonArray.size(); i++) {

                    LinkedHashMap<String, String> jsonMap2 = JSON.parseObject(jsonArray.get(i).toString(), new TypeReference<LinkedHashMap<String, String>>() {});

                    if(jsonMap2.containsKey(entry1.getKey())){

                        String patternString = "";
                        String patternString2 = "";

                        if(entry1.getValue().split(",")[1].equals("文本")){
                            patternString = ".*";
                        }

                        if(entry1.getValue().split(",")[1].equals("数字")){
                            patternString = "^[0-9]*$";
                        }

                        if(entry1.getValue().split(",")[1].equals("日期")){
                            patternString = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
                        }

                        Pattern pattern = Pattern.compile(patternString);
                        Matcher matcher = pattern.matcher(jsonMap2.get(entry1.getKey()));
                        boolean b= matcher.matches();


                        if(entry1.getValue().split(",").length>=3){

                            try {
                                patternString2 = URLDecoder.decode(entry1.getValue().split(",")[2].toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            Pattern pattern2 = null;
                            pattern2 = Pattern.compile( patternString2);

                            Matcher matcher2 = pattern2.matcher(jsonMap2.get(entry1.getKey()));
                            b= matcher2.matches();
                        }

                        ja2.add(jsonMap2.get(entry1.getKey()));
                        ja2.add(b);


                    }else{

                        ja2.add("");
                        ja2.add(false);

                    }

                }
                ja1.add(ja2);

        }

        // 左边导航条
        ControllerHelper.setLeftNavigationTree(model, cepService);

        System.out.println("BuriedPointList"+ja1.toJSONString());

        return ja1.toJSONString();
    }

    @RequestMapping("/compareByTopic")
    @ResponseBody
    public String compare(@RequestParam("topic") String topic,
                          @RequestParam("jsonArray") String jsonArray) {
        return cepService.compareByTopic(topic,jsonArray);

    }


}

