package com.github.trace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.github.trace.entity.AnalyzeLogFields;

import com.github.trace.mapper.AnalyzeLogMapper;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.trace.utils.SendLogCheckMonitor;

/**
 * Created by wangjiezhao on 2016/4/13.
 */
@Service
public class AnalyzeLogService {
    private static final Logger LOG = LoggerFactory.getLogger(AnalyzeLogService.class);

    @Autowired
    private AnalyzeLogMapper analyzeLogMapperr;

    @Autowired
    private CEPService cepService;


    private final SimpleDateFormat timestampFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");


    private Map<String,Map<String,String>> buriedMap=new TreeMap<String,Map<String,String>>();


    private Map<String,Map<String,Map<String,String>>> buriedMapCache=new HashMap<String,Map<String,Map<String,String>>>();
    private Map<String,Long> buriedMapCacheTimestamp=new HashMap<>();

    private Map<String,Set<String>> tagMap=new TreeMap<String,Set<String>>();
    private Set<String> tagSet=new HashSet<String>();
    private Map<String,Map<String,Map<String,String>>>  buriedTwoMap=new HashMap<String,Map<String,Map<String,String>>>();

    private String MobileDevEnterPre;
    private String MobileDevEnter;
    private String MobileDevEnterKey;
    private String MobileDevEnterVal;
    private String NonMobileDevEnter;

    public void setBuriedTwoMap(){
        List<AnalyzeLogFields> fields=analyzeLogMapperr.getBuriedTwoMap();

        for(AnalyzeLogFields field:fields){
            String name=field.getTag();
            String fieldName=field.getField_name();

            Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();

            if(buriedTwoMap.containsKey(name)) {
                map=buriedTwoMap.get(name);
            }

            Map<String,String> map2=new HashMap<String, String>();

            map2.put("desc",field.getField_desc());
            map2.put("type",field.getField_type());
            map2.put("regex",field.getField_regex());
            map.put(fieldName,map2);

            buriedTwoMap.put(name,map);
        }
    }

    public Map<String,Map<String,String>> getBuriedInfoByBusi(String navName){

        if(!buriedMapCache.containsKey(navName)||(buriedMapCache.containsKey(navName)&&(System.currentTimeMillis()-buriedMapCacheTimestamp.getOrDefault(navName,System.currentTimeMillis()))>=3600*1000L)) {
            List<AnalyzeLogFields> fields = analyzeLogMapperr.getBuriedInfoByBusi(navName);

            System.out.println("get buriedMap from:"+navName);
            LOG.warn("get buriedMap from:"+navName);

            Map<String,Map<String,String>> buriedMap=new HashMap<>();
            buriedMap.clear();
            for (AnalyzeLogFields field : fields) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("desc", field.getBp_value_desc());
                map.put("type", field.getBp_value());
                map.put("ischeck", String.valueOf(field.getIs_checked()));
                map.put("regex", field.getRegex());

                buriedMap.put(field.getBp_name(), map);
            }

            buriedMapCache.put(navName,buriedMap);
            buriedMapCacheTimestamp.put(navName,System.currentTimeMillis());
        }

        return buriedMapCache.get(navName);
    }

    public void setTagGroupInfo(){
        List<AnalyzeLogFields> fields=analyzeLogMapperr.getTagGroupInfo();
        String tag;
        String groupInfo;
        for(AnalyzeLogFields field:fields){
            tag = field.getLevel_tag();
            groupInfo=field.getTag_group();

            tagMap.put(tag,new TreeSet<String>(Arrays.asList(groupInfo.split(","))));
        }
    }

    public void setTagSet(){
        List<AnalyzeLogFields> fields=analyzeLogMapperr.getTagSet();

        for(AnalyzeLogFields field:fields) {
            tagSet.add(field.getD_level_tag());
        }
    }

    @PostConstruct
    public void init(){
        configure();
        setTagGroupInfo();
        setTagSet();
        setBuriedTwoMap();

    }

    public void configure(){
        this.MobileDevEnterPre="M99";
        this.MobileDevEnter="M99.M1";
        this.MobileDevEnterKey="M97";
        this.MobileDevEnterVal="^7$";
        //this.MobileDevEnterPattern=Pattern.compile(MobileDevEnterVal);

        this.NonMobileDevEnter="actionId";
    }

    public String handleTag(String val){
        String err = "";
        if(!Strings.isNullOrEmpty(val)){
            if(!buriedTwoMap.containsKey(val)){
                err="入口取值不存在";
            }
        }else{
            err="字段取值为空ornull";
        }

        return err;
    }


    public List<String> handleField(String val,String type,String regex){
        String patternString = "";
        String patternString2 = "";
        boolean b;
        String err = "";

        List<String> rt=new ArrayList<>(2);
        rt.add(0,val);

        if (Strings.isNullOrEmpty(val)) {
            err="字段值为空ornull";
            rt.add(0,"");
            rt.add(1,err);
            return rt;
        }



        if (type.equals("文本")) {
            patternString = ".*";
        }

        if (type.equals("数字")) {
            patternString = "^[0-9]*$";
        }

        if (type.equals("日期")) {
            patternString = "^\\d{13}$";
        }

        if (!Strings.isNullOrEmpty(regex)) {
            try {
                patternString2 = URLDecoder.decode(regex,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Pattern pattern2 = Pattern.compile(patternString2);

            Matcher matcher2 = pattern2.matcher(val);
            b = matcher2.matches();


            if(type.equals("日期")&&b){
                long nowTimestamp=new Date().getTime();
                long logTimestamp=Long.parseLong(val);


                try {
                    Date date = new Date(Long.parseLong(val));
                    String value = timestampFormat.format(date);
                    //System.out.println(value);
                    rt.add(0,value);
                }catch(Exception e){
                    LOG.warn("timestamp field exception",e);
                    err="日期类型字段正则取值异常";
                }
            }else {
                if (!b) {
                    err = "正则不匹配";
                }
            }

        }else{
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(val);
            b = matcher.matches();

            if(type.equals("日期")&&b){
                long nowTimestamp=new Date().getTime();
                long logTimestamp=Long.parseLong(val);


                try {
                    Date date = new Date(Long.parseLong(val));
                    String value = timestampFormat.format(date);
                    //System.out.println(value);
                    rt.add(0,value);
                }catch(Exception e){
                    LOG.warn("timestamp field exception",e);
                    err="日期类型字段取值异常";
                }
            }else {
                if (!b) {
                    err = "类型不匹配";
                }
            }
        }

        rt.add(1,err);

        return rt;

    }

    public List<Map<String,List<String>>>  process(String Target,String navName) {
        JSONArray jsonArray = JSON.parseArray(Target);
        //JSONArray ja1 = new JSONArray();
        String key;
        Map<String,String> val;
        String desc,type,regex; //isChecked;
        String desc2,type2,regex2,key2,val2;
        String desc3,type3,regex3,key3,val3,isChecked3;

        List<String>  hfResult;

        //List<List<String>> output=new ArrayList<List<String>>();
//        List<Map<String,List<String>>> output=new ArrayList<Map<String, List<String>>>(jsonArray.size());
        List<Map<String,List<String>>> output=new ArrayList<Map<String, List<String>>>();

        Map<String,List<String>> extra=new HashMap<>();

        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                LinkedHashMap<String, String> jsonMap2 = JSON.parseObject(jsonArray.get(i).toString(), new TypeReference<LinkedHashMap<String, String>>() {
                });


                Map<String, List<String>> map = new HashMap<String, List<String>>();

                for (Map.Entry<String, Map<String, String>> entry1 : buriedMap.entrySet()) {
                    List<String> list = new ArrayList<String>();

                    key = entry1.getKey();
                    val = entry1.getValue();
                    desc = val.get("desc");
                    type = val.get("type");
                    //isChecked = val.get("ischeck");
                    regex = val.get("regex");

                    list.add(key);
                    list.add(desc);
                    list.add(type);
                    list.add(regex);

                    map.put(key, list);
                }

                //Sets.SetView<String> diff=Sets.difference(jsonMap2.keySet(),buriedMap.keySet());

                String enter = "";

                String err = "";

                Map<String, Map<String, String>> tagTwoMap;

                Set<String> keySets;
                Set<String> filterSets = new HashSet<String>();

                String enterKey;

                if (jsonMap2.containsKey(MobileDevEnterKey) && jsonMap2.get(MobileDevEnterKey).matches(MobileDevEnterVal)) {
                    map.get(MobileDevEnterKey).add(jsonMap2.get(MobileDevEnterKey));
                    map.get(MobileDevEnterKey).add("");

                    filterSets.add(MobileDevEnterKey);
                    filterSets.add(MobileDevEnterPre);

                    if (jsonMap2.containsKey(MobileDevEnter)) {

                        filterSets.add(MobileDevEnter);

                        enterKey = jsonMap2.get(MobileDevEnter);
                        if (enterKey.equals("")) {
                            err = "事件赋值为空";
                            map.get(MobileDevEnter).add("");
                            map.get(MobileDevEnter).add(err);
                        } else {
                            err = handleTag(enterKey);

                            if (!err.equals("")) {
                                map.get(MobileDevEnter).add(enterKey);
                                map.get(MobileDevEnter).add(err);
                            } else {
                                map.get(MobileDevEnter).add(enterKey);
                                map.get(MobileDevEnter).add("");
                            }

                            if (buriedTwoMap.containsKey(enterKey)) {

                                tagTwoMap = buriedTwoMap.get(enterKey);

                                keySets = tagTwoMap.keySet();

                                for (String k : keySets) {

                                    if (k == null) {
                                        LOG.warn("ks is null:" + enterKey);
                                        continue;
                                    }
                                    key2 = MobileDevEnterPre + "." + k;

                                    filterSets.add(key2);




                                    desc2 = tagTwoMap.get(k).get("desc");
                                    type2 = tagTwoMap.get(k).get("type");
                                    regex2 = tagTwoMap.get(k).get("regex");




                                    if(!map.containsKey(key2)){
                                        List<String> list = new ArrayList<String>();
                                        list.add(key2);
                                        list.add(desc2);
                                        list.add(type2);
                                        list.add(regex2);

                                        map.put(key2, list);


                                        if(!extra.containsKey(key2)){
                                            extra.put(key2,list);
                                        }

                                    }

                                    if (jsonMap2.containsKey(key2)) {
                                        val2 = jsonMap2.get(key2);



                                        hfResult = handleField(val2, type2, regex2);
                                        err = hfResult.get(1);
                                        val2 = hfResult.get(0);
                                        if (!err.equals("")) {
                                            map.get(key2).add(val2);
                                            map.get(key2).add(err);
                                        } else {
                                            map.get(key2).add(val2);
                                            map.get(key2).add("");
                                        }
                                    } else {
                                        try {
                                            map.get(key2).add("");
                                            map.get(key2).add("字段不存在");
                                        } catch (NullPointerException e) {
                                            List<String> list = new ArrayList<String>();
                                            list.add(key2);
                                            list.add(desc2);
                                            list.add(type2);
                                            list.add(regex2);
                                            list.add("");
                                            list.add("表中字段不存在");
                                            map.put(key2, list);


                                            if(!extra.containsKey(key2)){
                                                extra.put(key2,list);
                                            }

                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        err = "事件入口不存在";
                        map.get(MobileDevEnterKey).add("");
                        map.get(MobileDevEnterKey).add(err);
                    }


                } else if (jsonMap2.containsKey(NonMobileDevEnter)) {

                    map.get(NonMobileDevEnter).add(NonMobileDevEnter);
                    map.get(NonMobileDevEnter).add("");

                    filterSets.add(NonMobileDevEnter);

                    if (jsonMap2.containsKey(NonMobileDevEnter)) {
                        enterKey = jsonMap2.get(NonMobileDevEnter);

                        if (enterKey.equals("")) {
                            err = "事件赋值为空";
                            map.get(NonMobileDevEnter).add("");
                            map.get(NonMobileDevEnter).add(err);
                        } else {
                            err = handleTag(enterKey);
                            if (!err.equals("")) {
                                map.get(NonMobileDevEnter).add(enterKey);
                                map.get(NonMobileDevEnter).add(err);
                            } else {
                                map.get(NonMobileDevEnter).add(enterKey);
                                map.get(NonMobileDevEnter).add("");
                            }


                            tagTwoMap = buriedTwoMap.get(enterKey);


                            if (tagTwoMap == null) {
                                tagTwoMap = new HashMap<String, Map<String, String>>();
                            }

                            keySets = tagTwoMap.keySet();

                            for (String k : keySets) {
                                //if(k.equals(NonMobileDevEnter)){filterSets.add(NonMobileDevEnter);continue;}

                                key2 = k;
                                val2 = jsonMap2.get(key2);

                                filterSets.add(key2);

                                if (jsonMap2.containsKey(k)) {
                                    desc2 = tagTwoMap.get(k).get("desc");
                                    type2 = tagTwoMap.get(k).get("type");
                                    regex2 = tagTwoMap.get(k).get("regex");


                                    if(!map.containsKey(key2)){
                                        List<String> list = new ArrayList<String>();
                                        list.add(key2);
                                        list.add(desc2);
                                        list.add(type2);
                                        list.add(regex2);

                                        map.put(key2, list);
                                        if(!extra.containsKey(key2)){
                                            extra.put(key2,list);
                                        }

                                    }

                                    hfResult = handleField(val2, type2, regex2);
                                    err = hfResult.get(1);
                                    val2 = hfResult.get(0);
                                    if (!err.equals("")) {
                                        map.get(key2).add(val2);
                                        map.get(key2).add(err);
                                    } else {
                                        map.get(key2).add(val2);
                                        map.get(key2).add("");
                                    }
                                } else {
                                    map.get(key2).add("");
                                    map.get(key2).add("字段不存在");
                                }
                            }
                        }
                    } else {
                        err = "事件入口不存在";
                        map.get(MobileDevEnterKey).add("");
                        map.get(MobileDevEnterKey).add(err);
                    }

                }

                Set<String> bSets = new HashSet<String>(buriedMap.keySet());
                bSets.removeAll(filterSets);


                if (map.containsKey(MobileDevEnterPre)) {
                    map.get(MobileDevEnterPre).add("");
                    map.get(MobileDevEnterPre).add("");
                }
                for (String bKey : bSets) {
                    Map<String, String> bVal = buriedMap.get(bKey);

                    if(bVal==null){continue;}

                    val3 = jsonMap2.get(bKey);
                    desc3 = bVal.get("desc");
                    type3 = bVal.get("type");
                    isChecked3 = bVal.get("ischeck");
                    regex3 = bVal.get("regex");


                    if(!extra.containsKey(bKey)){
                        List<String> list = new ArrayList<String>();
                        list.add(bKey);
                        list.add(desc3);
                        list.add(type3);
                        list.add(regex3);
                        extra.put(bKey,list);
                    }


                    if (bKey.equals(MobileDevEnterPre)) {
                        val3 = "";
                        err = "";
                    } else if (Strings.isNullOrEmpty(val3)) {
                        val3 = "";
                        err = "字段不存在";
                    } else if (isChecked3.equals("1")) {
                        hfResult = handleField(val3, type3, regex3);
                        err = hfResult.get(1);
                        val3 = hfResult.get(0);
                    } else if (isChecked3.equals("0")) {
                        //err="";
                        hfResult = handleField(val3, type3, regex3);
                        err = hfResult.get(1);
                        val3 = hfResult.get(0);
                    } else {
                        err = "";
                    }

                    if (!err.equals("")) {
                        map.get(bKey).add(val3);
                        map.get(bKey).add(err);
                    } else {
                        map.get(bKey).add(val3);
                        map.get(bKey).add("");
                    }

                }
                output.add(map);
            }
        }catch(Exception e){
            LOG.warn("process log exception:",e);
            LOG.warn("navName:"+navName);
            LOG.warn(jsonArray.toJSONString());
        }


        int count=0;
        for(Map<String,List<String>> m:output) {
            for(Map.Entry<String,List<String>> m2:extra.entrySet()){
                String key5=m2.getKey();
                if(!m.containsKey(key5)){
                    List<String> list = new ArrayList<String>();


                    key = m2.getValue().get(0);
                    desc = m2.getValue().get(1);
                    //isChecked = val.get("ischeck");
                    type = m2.getValue().get(2);
                    regex=m2.getValue().get(3);

                    list.add(key);
                    list.add(desc);
                    list.add(type);
                    list.add(regex);
                    list.add("");
                    list.add("");

                    output.get(count).put(key, list);
                }
            }

            count++;
        }


        return output;
    }


    public  JSONArray  formatLog(String busiName,String Target){
        buriedMap=getBuriedInfoByBusi(busiName);

        List<Map<String,List<String>>> output=process(Target,busiName);


        JSONArray ja1 = new JSONArray();

        Map<String,JSONArray> rt=new HashMap<String, JSONArray>();

        for(Map<String,List<String>> m:output){

            for(Map.Entry<String,List<String>> m2:m.entrySet()){
                JSONArray ja2 = new JSONArray();
                JSONArray ja3 = new JSONArray();
                String key4=m2.getKey();
                JSONArray ja4=rt.get(key4);

                String  desc4=m2.getValue().get(1);
                String  val4=m2.getValue().get(4);
                try {
                    val4 = URLDecoder.decode(val4,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String err4=m2.getValue().get(5);

                if(ja4==null) {
                    ja2.add(key4);
                    ja2.add(desc4);

                    if(err4.equals("")){
                        ja3.add(true);
                    }else{
                        ja3.add(false);
                    }

                    ja3.add(val4);
                    ja3.add(err4);

                    ja2.add(ja3);

                    rt.put(key4,ja2);

                }else{
                    if(err4.equals("")){
                        ja3.add(true);
                    }else{
                        ja3.add(false);
                    }
                    ja3.add(val4);
                    ja3.add(err4);

                    rt.get(key4).add(ja3);
                }

            }
        }



        for(Map.Entry<String,JSONArray> m2:rt.entrySet()) {
            String key4 = m2.getKey();
            JSONArray ja4 = rt.get(key4);


            if(ja4.size()!=7){
                for(int i=ja4.size();i<7;i++) {
                    JSONArray ja33 = new JSONArray();
                    ja33.add(true);
                    ja33.add("");
                    ja33.add("");
                    ja4.add(ja33);
                }
            }

            ja1.add(ja4);
        }

        return ja1;
    }
    public Set<String> filterToES(String busiName,String Target,boolean isSentMonitor){
        buriedMap=getBuriedInfoByBusi(busiName);

        List<Map<String,List<String>>> result=process(Target,busiName);

        StringBuffer buffer=new StringBuffer();

        String message;

        String bpName,bpDesc,bpType,bpVal,err;
        boolean isSent=false;

        Set<String> esOutput=new HashSet<String>();

        for(Map<String,List<String>> ele:result){
            buffer.append("测试，请忽略\n[埋点异常   Busi:");
            buffer.append(busiName);
            buffer.append("]\n");

            Map<String,String> map=new HashMap<String, String>();


            for(Map.Entry<String,List<String>> m:ele.entrySet()){
                bpName=m.getKey();
                List<String> val=m.getValue();
                bpDesc=val.get(1);
                bpType=val.get(2);
                bpVal=val.get(4);
                err=val.get(5);

                if(!err.equals("")) {
                    isSent=true;
                    buffer.append(bpName);
                    buffer.append(":");
                    buffer.append(bpVal);
                    buffer.append(" Type:");
                    buffer.append(bpType);
                    buffer.append("  Error:");
                    buffer.append(err);
                    buffer.append("\n");
                }


                map.put(bpName,bpVal);


            }

            if(isSent) {
                message = buffer.toString();

                if(isSentMonitor) {
                    // SendLogCheckMonitor.sendPostRequest(busiName,message);
                    LOG.info("####################sent log check monitor##################");
                }
                esOutput.add(JSON.toJSONString(map));
            }
            buffer.setLength(0);

        }
        return esOutput;
    }


    public void sendMonitorByNavName(String navName){
        List<AnalyzeLogFields> fields=analyzeLogMapperr.getTopicByNavName(navName);
        String topic;
        if(fields.size()>0) {
            topic = fields.get(0).getTopic();
        }else{
            LOG.warn("sendMonitorByNavName can't get topic by navName:"+navName);
            return;
        }


        LOG.warn("sendMonitorByNavName topic:"+topic);

        int topicNo=5;

        String Target =  cepService.getServerLog(topic,topicNo).toString();

        buriedMap=getBuriedInfoByBusi(navName);

        List<Map<String,List<String>>> result=process(Target,navName);

        StringBuffer buffer=new StringBuffer();

        String message;

        String bpName,bpDesc,bpType,bpVal,err;
        boolean isSent=false;

        Set<String> esOutput=new HashSet<String>();

        for(Map<String,List<String>> ele:result){
            buffer.append("测试，请忽略\n[埋点异常   Busi:");
            buffer.append(navName);
            buffer.append("]\n");

            for(Map.Entry<String,List<String>> m:ele.entrySet()){
                bpName=m.getKey();
                List<String> val=m.getValue();
                //bpDesc=val.get(1);
                bpType=val.get(2);
                bpVal=val.get(4);
                err=val.get(5);

                if(!err.equals("")) {
                    isSent=true;
                    buffer.append(bpName);
                    buffer.append(":");
                    buffer.append(bpVal);
                    buffer.append(" Type:");
                    buffer.append(bpType);
                    buffer.append("  Error:");
                    buffer.append(err);
                    buffer.append("\n");
                }
            }

            if(isSent) {
                    message = buffer.toString();

                    SendLogCheckMonitor.sendPostRequest(navName,message);
                    LOG.info("monitor message:"+message);
                    LOG.info("####################sendMonitorByNavName  send monitor##################");
            }
            buffer.setLength(0);
        }
    }
}
