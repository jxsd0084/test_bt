package com.github.trace.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.service.CEPService;
import com.github.trace.utils.ControllerHelper;
import com.google.common.collect.ImmutableMap;
import org.apache.ibatis.annotations.Param;
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
    public String callerList(@RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id, @RequestParam(name = "parent_name") String parent_name, Model model) {

        List<BuriedPoint> caller = cepService.getBuriedPointList(parent_id, child_id);

        JSONArray ja1 = new JSONArray();

        // data, type, full, meta
        for (BuriedPoint br : caller) {
            JSONArray ja2 = new JSONArray();
            ja2.add(br.getId());            // 编号
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
        model.addAttribute("data", ja1);
        return "cep/bp_list";
    }

    @RequestMapping("/new")
    public String createConfig(@RequestParam(name = "id") int id, @RequestParam(name = "tag") String tag, Model model) {

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
    public String newConfig(@RequestParam(name = "tag") String tag, @RequestParam(name = "parent_id") int parent_id, @RequestParam(name = "child_id") int child_id,  @RequestParam(name = "parent_name") String parent_name,Model model) {

        ControllerHelper.setLeftNavigationTree(model, cepService); // 左边导航条

        model.addAttribute("parent_id", parent_id);
        model.addAttribute("child_id", child_id);
        model.addAttribute("parent_name", parent_name);
        model.addAttribute("tag", tag);
        return "func/conf_create";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modifyConfig(@Param("bp_name") String bp_name, @Param("bp_value") String bp_value, @Param("regex") String regex, @Param("bp_value_desc") String bp_value_desc, @Param("is_checked") boolean is_checked, @Param("id") int id) {
        String result = "";

        boolean flag = cepService.modifyBuriedPoint(bp_name, bp_value,regex, bp_value_desc, is_checked, id);
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

        return ControllerHelper.returnResponseVal(res);
    }

    @RequestMapping("/delete")
    public String deleteBuriedPoint(@Param("id") Integer id, Model model) {
        cepService.deleteBuriedPoint(id);
        return "func/bp_list";
    }

    @RequestMapping("/format")
    @ResponseBody
    public String format(@Param("BuriedPointList") String BuriedPointList, Model model) {

        System.out.println("BuriedPointList"+BuriedPointList);

        return BuriedPointList+"";
    }

    @RequestMapping("/serverLog")
    @ResponseBody
    public String serverLog(@Param("Path") String path, Model model) {

        System.out.println("BuriedPointList"+path);

        return path+"";
    }

    @RequestMapping("/compare")
    @ResponseBody
    public String compare(@Param("Source") String str1,@Param("Target") String str2, Model model) {

        JSONObject jsonObject1 = JSON.parseObject(str1);

        JSONObject jsonObject2 = JSON.parseObject(str2);

        List<BuriedPoint> caller = cepService.getBuriedPointList(1, 2);

        JSONArray ja1 = new JSONArray();

        LinkedHashMap<String, String> jsonMap1 = JSON.parseObject(str1, new TypeReference<LinkedHashMap<String, String>>() {
        });


        LinkedHashMap<String, String> jsonMap2 = JSON.parseObject(str2, new TypeReference<LinkedHashMap<String, String>>() {
        });

        for (Map.Entry<String, String> entry1 : jsonMap1.entrySet()) {

            if(jsonMap2.containsKey(entry1.getKey())){

                JSONArray ja2 = new JSONArray();
                ja2.add(entry1.getKey()+"");
                ja2.add(entry1.getValue()+"");

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

                ja2.add("");
                ja2.add("");

                ja1.add(ja2);

            }else{

                JSONArray ja2 = new JSONArray();
                ja2.add(entry1.getKey()+"");
                ja2.add(entry1.getValue()+"");
                ja2.add("");
                ja2.add("false");
                ja2.add("");
                ja2.add("");

                ja1.add(ja2);

            }

        }

        // 左边导航条
        ControllerHelper.setLeftNavigationTree(model, cepService);

        System.out.println("BuriedPointList"+ja1.toJSONString());

        return ja1.toJSONString();
    }

}

