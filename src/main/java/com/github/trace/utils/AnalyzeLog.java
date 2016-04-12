package com.github.trace.utils;
//package com.fxiaoke.dataplatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.ExceptionSerializer;
import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjiezhao on 2016/4/6.
 */
public class AnalyzeLog {
    private static final Logger LOG = LoggerFactory.getLogger(AnalyzeLog.class);

    private Connection conn;

    //private Statement stmt;

    private final SimpleDateFormat timestampFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Map<String,Map<String,String>> buriedMap=new TreeMap<String,Map<String,String>>();
    private Map<String,Set<String>> tagMap=new TreeMap<String,Set<String>>();
    private Set<String> tagSet=new HashSet<String>();

    private Map<String,Map<String,Map<String,String>>>  buriedTwoMap=new HashMap<String,Map<String,Map<String,String>>>();

    private String MobileDevEnterPre;
    private String MobileDevEnter;
    private String MobileDevEnterKey;
    private String MobileDevEnterVal;
    private String NonMobileDevEnter;

    //private Pattern MobileDevEnterPattern;

    public void initDbConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            //jdbc:mysql://172.31.101.12:3306/oss?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true

            conn = DriverManager.getConnection("jdbc:mysql://172.31.101.12:3306/buried_point?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true", "fs", "fsxiaoke"); //链接本地MYSQL
            //      stmt = conn.createStatement();
        } catch (Exception e) {
            LOG.warn("mysql Exception:",e);
            conn=null;
            //    stmt=null;
        }
    }

    public Map<String,Set<String>> getTagGroupInfo(){
        return this.tagMap;
    }

    public Map<String,Map<String,String>> getBuriedInfo(){
        return this.buriedMap;
    }

    public Set<String> getTagSet(){return this.tagSet;}

    public Map<String,Map<String,Map<String,String>>> getBuriedTwoMap(){return this.buriedTwoMap;}

    public void setTagGroupInfo()  {
        try{
            Statement stmt;
            stmt = conn.createStatement();

            String q="SELECT level1_field_tag as level_tag,GROUP_CONCAT(id) as tag_group FROM level_two_fields group by level1_field_tag";
            ResultSet res = stmt.executeQuery(q);

            String tag;
            String groupInfo;


            while(res.next()) {
                tag = res.getString("level_tag");
                groupInfo=res.getString("tag_group");

                tagMap.put(tag,new TreeSet<String>(Arrays.asList(groupInfo.split(","))));
            }
        }catch (SQLException e){
            LOG.warn("sqlException:",e);
        }
    }

    public void setTagSet(){
        try{
            Statement stmt;
            stmt = conn.createStatement();

           // String q="SELECT distinct  level1_field_tag as level_tag FROM level_one_fields";

            String q="select  distinct  a.level1_field_tag as level_tag from level_two_fields a join m99_fields b on (a.level1_field_id=b.level_one_id and a.id=level_two_id)";

            ResultSet res = stmt.executeQuery(q);

            while(res.next()) {
                tagSet.add(res.getString("level_tag"));
            }
        }catch (SQLException e){
            LOG.warn("sqlException:",e);
        }
    }


    public void setBuriedInfoByBusi(String busi){
        try{
          //  PreparedStatement pstm=conn.prepareStatement("select a.bp_name,a.bp_value_desc,bp_value,is_checked,regex from buried_point a  join navigation_item b on(a.parent_id=b.parent_id and a.child_id=b.child_id and b.child_name=?) order by bp_name;");
            PreparedStatement pstm=conn.prepareStatement("select a.bp_name,a.bp_value_desc,bp_value,is_checked,regex from buried_point0 a  join navigation_item0 b on(a.navigation_id=b.id and  b.item_type=1 and b.name=?) order by bp_name;");
            pstm.setString(1,busi);

            ResultSet res = pstm.executeQuery();


            while(res.next()) {
                Map<String,String> map=new HashMap<String, String>();

                map.put("desc",res.getString("bp_value_desc"));
                map.put("type",res.getString("bp_value"));
                map.put("ischeck",String.valueOf(res.getInt("is_checked")));
                map.put("regex",res.getString("regex"));

                buriedMap.put(res.getString("bp_name"),map);
            }
        }catch (SQLException e){
            LOG.warn("sqlException:",e);
        }
    }



    public void setBuriedTwoMap(){
        try{
           Statement stmt=conn.createStatement();
           //String q="select m1_name,field_name,field_desc,field_type,field_regex from m99_fields;";

           String q="select  a.level1_field_tag as tag,b.field_name,b.field_desc,b.field_type,b.field_regex from level_two_fields a join m99_fields b on (a.level1_field_id=b.level_one_id and a.id=level_two_id);";
           ResultSet res = stmt.executeQuery(q);


            while(res.next()) {
                String name=res.getString("tag");
                String fieldName=res.getString("field_name");

                Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();

                if(buriedTwoMap.containsKey(name)) {
                    map=buriedTwoMap.get(name);
                }

                Map<String,String> map2=new HashMap<String, String>();

                map2.put("desc",res.getString("field_desc"));
                map2.put("type",res.getString("field_type"));
                map2.put("regex",res.getString("field_regex"));
                map.put(fieldName,map2);

                buriedTwoMap.put(name,map);
            }
        }catch (SQLException e){
            LOG.warn("sqlException:",e);
        }
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


    public String handleField(String val,String type,String regex){
        String patternString = "";
        String patternString2 = "";
        boolean b;
        String err = "";

        if (Strings.isNullOrEmpty(val)) {
            err="字段值为空ornull";
            return err;
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
                }catch(Exception e){
                    LOG.warn("timestamp field exception",e);
                    err="日期类型字段取值异常";
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
            if(!b){
                err="类型不匹配";
            }
        }

        return err;

    }
//    public List<List<String>>  process(String Target) {
public List<Map<String,List<String>>>  process(String Target) {
        JSONArray jsonArray = JSON.parseArray(Target);
        //JSONArray ja1 = new JSONArray();
        String key;
        Map<String,String> val;
        String desc,type,regex; //isChecked;
        String desc2,type2,regex2,key2,val2;
        String desc3,type3,regex3,key3,val3,isChecked3;

        //List<List<String>> output=new ArrayList<List<String>>();
        List<Map<String,List<String>>> output=new ArrayList<Map<String, List<String>>>(jsonArray.size());

            for (int i = 0; i < jsonArray.size(); i++) {
                LinkedHashMap<String, String> jsonMap2 = JSON.parseObject(jsonArray.get(i).toString(), new TypeReference<LinkedHashMap<String, String>>() {});

                Map<String,List<String>> map=new HashMap<String, List<String>>();

                for(Map.Entry<String, Map<String,String>> entry1:buriedMap.entrySet()) {
                    List<String> list=new ArrayList<String>();

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

                    map.put(key,list);
                }

                //Sets.SetView<String> diff=Sets.difference(jsonMap2.keySet(),buriedMap.keySet());

                String enter="";

                String err="";

                Map<String,Map<String,String>>  tagTwoMap;

                Set<String> keySets;
                Set<String> filterSets=new HashSet<String>();

                String enterKey;

                if(jsonMap2.containsKey(MobileDevEnterKey)&&jsonMap2.get(MobileDevEnterKey).matches(MobileDevEnterVal)){
                    map.get(MobileDevEnterKey).add(jsonMap2.get(MobileDevEnterKey));
                    map.get(MobileDevEnterKey).add("");

                    filterSets.add(MobileDevEnterKey);
                    filterSets.add(MobileDevEnterPre);

                    if(jsonMap2.containsKey(MobileDevEnter)){

                        filterSets.add(MobileDevEnter);

                        enterKey=jsonMap2.get(MobileDevEnter);
                        if(enterKey.equals("")){
                            err="事件赋值为空";
                            map.get(MobileDevEnter).add("");
                            map.get(MobileDevEnter).add(err);
                        }else {
                            err = handleTag(enterKey);

                            if (!err.equals("")) {
                                map.get(MobileDevEnter).add(enterKey);
                                map.get(MobileDevEnter).add(err);
                            } else {
                                map.get(MobileDevEnter).add(enterKey);
                                map.get(MobileDevEnter).add("");
                            }

                           if(buriedTwoMap.containsKey(enterKey)) {
                               tagTwoMap = buriedTwoMap.get(enterKey);

                               keySets = tagTwoMap.keySet();

                               for (String k : keySets) {

                                   key2 = MobileDevEnterPre + "." + k;

                                   filterSets.add(key2);

                                   desc2 = tagTwoMap.get(k).get("desc");
                                   type2 = tagTwoMap.get(k).get("type");
                                   regex2 = tagTwoMap.get(k).get("regex");

                                   if (jsonMap2.containsKey(key2)) {
                                       val2 = jsonMap2.get(key2);

                                       err = handleField(val2, type2, regex2);
                                       if (!err.equals("")) {
                                           map.get(key2).add(val2);
                                           map.get(key2).add(err);
                                       } else {
                                           map.get(key2).add("");
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

                                       }
                                   }
                               }

                           }
                            }
                        }else{
                        err="事件入口不存在";
                        map.get(MobileDevEnterKey).add("");
                        map.get(MobileDevEnterKey).add(err);
                    }


                }else if(jsonMap2.containsKey(NonMobileDevEnter)){

                    map.get(NonMobileDevEnter).add(NonMobileDevEnter);
                    map.get(NonMobileDevEnter).add("");

                    filterSets.add(NonMobileDevEnter);

                    if(jsonMap2.containsKey(NonMobileDevEnter)){
                        enterKey=jsonMap2.get(NonMobileDevEnter);

                        if(enterKey.equals("")){
                            err="事件赋值为空";
                            map.get(NonMobileDevEnter).add("");
                            map.get(NonMobileDevEnter).add(err);
                        }else {
                            err = handleTag(enterKey);
                            if (!err.equals("")) {
                                map.get(NonMobileDevEnter).add(enterKey);
                                map.get(NonMobileDevEnter).add(err);
                            } else {
                                map.get(NonMobileDevEnter).add(enterKey);
                                map.get(NonMobileDevEnter).add("");
                            }



                            tagTwoMap=buriedTwoMap.get(enterKey);
//                            System.out.println(enterKey);
//                            System.out.println(tagTwoMap.toString());
//                            System.out.println(tagTwoMap.keySet());

                            if(tagTwoMap==null){
                                tagTwoMap=new HashMap<String,Map<String,String>>();
                            }

                            keySets=tagTwoMap.keySet();

                            for(String k:keySets){
                                //if(k.equals(NonMobileDevEnter)){filterSets.add(NonMobileDevEnter);continue;}

                                key2=k;
                                val2=jsonMap2.get(key2);

                                filterSets.add(key2);

                                if(jsonMap2.containsKey(k)){
                                    desc2=tagTwoMap.get(k).get("desc");
                                    type2=tagTwoMap.get(k).get("type");
                                    regex2=tagTwoMap.get(k).get("regex");


                                    err=handleField(val2,type2,regex2);
                                    if(!err.equals("")){
                                        map.get(key2).add(val2);
                                        map.get(key2).add(err);
                                    }else{
                                        map.get(key2).add("");
                                        map.get(key2).add("");
                                    }
                                }else{
                                    map.get(key2).add("");
                                    map.get(key2).add("字段不存在");
                                }
                            }
                        }
                    }else{
                        err="事件入口不存在";
                        map.get(MobileDevEnterKey).add("");
                        map.get(MobileDevEnterKey).add(err);
                    }

                }

                Set<String>  bSets=new HashSet<String>(buriedMap.keySet());
                bSets.removeAll(filterSets);




                if(map.containsKey(MobileDevEnterPre)) {
                map.get(MobileDevEnterPre).add("");
                map.get(MobileDevEnterPre).add("");
                }
                for(String bKey:bSets){
                    Map<String,String> bVal=buriedMap.get(bKey);
                    val3=jsonMap2.get(bKey);
                    desc3 = bVal.get("desc");
                    type3 = bVal.get("type");
                    isChecked3 = bVal.get("ischeck");
                    regex3 = bVal.get("regex");

                    if(bKey.equals(MobileDevEnterPre)){
                        val3="";
                        err="";
                    }else if(Strings.isNullOrEmpty(val3)){
                        val3="";
                        err="字段不存在";
                    }else if(isChecked3.equals("1")){
                        err=handleField(val3,type3,regex3);
                    }else if(isChecked3.equals("0")){
                        err="";
                    }else{
                        err="";
                    }

                    if(!err.equals("")){
                        map.get(bKey).add(val3);
                        map.get(bKey).add(err);
                    }else{
                        map.get(bKey).add(val3);
                        map.get(bKey).add("");
                    }

                }
                output.add(map);
            }




        return output;
    }


    public  JSONArray  formatLog(String busiName,String Target){
        AnalyzeLog demo=new AnalyzeLog();
        demo.initDbConnection();
        demo.configure();
        demo.setBuriedInfoByBusi(busiName);
        demo.setTagGroupInfo();
        demo.setTagSet();
        demo.setBuriedTwoMap();

        List<Map<String,List<String>>> output=demo.process(Target);

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
        //return ja1.toJSONString();
        return ja1;
    }
    public Set<String> filterToES(String busiName,String Target,boolean isSentMonitor){
        AnalyzeLog demo=new AnalyzeLog();
//        demo.initDbConnection();
//        demo.configure();
//        demo.setBuriedInfoByBusi(busiName);
//        demo.setTagGroupInfo();
//        demo.setTagSet();
//        demo.setBuriedTwoMap();

        demo.initDbConnection();
        demo.configure();
        demo.setBuriedInfoByBusi(busiName);
        demo.setTagGroupInfo();
        demo.setTagSet();
        demo.setBuriedTwoMap();



        //Target="[{\"M1\":\"-1\",\"M3\":\"865863026397708\",\"M4\":\"Android\",\"M5\":\"4.4.4\",\"M6\":\"5.2.0.25877\",\"M7\":\"25877\",\"M8\":\"Meizu/MX4 Pro/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M97\":\"9\",\"M98\":\"1459493220916\",\"M99.M1\":1,\"realip\":\"172.28.0.147\",\"_ip\":\"172.31.103.120\",\"_time\":\"2016-04-01 14:59:26.331\"}, {\"M1\":\"-1\",\"M3\":\"865863026397708\",\"M4\":\"Android\",\"M5\":\"4.4.4\",\"M6\":\"5.2.0.25877\",\"M7\":\"25877\",\"M8\":\"Meizu/MX4 Pro/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M97\":\"7\",\"M98\":\"1459493220638\",\"M99.M2\":1856,\"M99.M1\":\"PlugLoad_200\",\"realip\":\"172.28.0.147\",\"_ip\":\"172.31.103.120\",\"_time\":\"2016-04-01 14:59:26.331\"}, {\"M1\":\"-1\",\"M3\":\"865863026397708\",\"M4\":\"Android\",\"M5\":\"4.4.4\",\"M6\":\"5.2.0.25877\",\"M7\":\"25877\",\"M8\":\"Meizu/MX4 Pro/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M97\":\"7\",\"M98\":\"1459493220637\",\"M99.M2\":2800,\"M99.M1\":\"PlugLoad_200\",\"realip\":\"172.28.0.147\",\"_ip\":\"172.31.103.120\",\"_time\":\"2016-04-01 14:59:26.331\"}, {\"M1\":\"-1\",\"M3\":\"865863026397708\",\"M4\":\"Android\",\"M5\":\"4.4.4\",\"M6\":\"5.2.0.25877\",\"M7\":\"25877\",\"M8\":\"Meizu/MX4 Pro/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M97\":\"7\",\"M98\":\"1459493220633\",\"M99.M2\":65,\"M99.M1\":\"PlugLoad_74\",\"realip\":\"172.28.0.147\",\"_ip\":\"172.31.103.120\",\"_time\":\"2016-04-01 14:59:26.331\"}, {\"M1\":\"-1\",\"M3\":\"865863026397708\",\"M4\":\"Android\",\"M5\":\"4.4.4\",\"M6\":\"5.2.0.25877\",\"M7\":\"25877\",\"M8\":\"Meizu/MX4 Pro/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M97\":\"7\",\"M98\":\"1459493264667\",\"M99.M1\":\"Login_2\",\"realip\":\"172.28.0.147\",\"_ip\":\"172.31.103.120\",\"_time\":\"2016-04-01 14:59:26.331\"}]";

       // Target="[{\"M1\":\"-1\",\"M3\":\"865863026397708\",\"M4\":\"Android\",\"M5\":\"4.4.4\",\"M6\":\"5.2.0.25877\",\"M7\":\"25877\",\"M8\":\"Meizu/MX4 Pro/armeabi-v7a/armeabi\",\"M96\":\"wifi\",\"M97\":\"7\",\"M98\":\"1459493264667\",\"M99.M1\":\"AV_174\",\"realip\":\"172.28.0.147\",\"_ip\":\"172.31.103.120\",\"_time\":\"2016-04-01 14:59:26.331\"}]";
        //List<List<String>> result=demo.process(Target);
        List<Map<String,List<String>>> result=demo.process(Target);


        //System.out.println(result.toString());
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
               // buffer.append("测试，请忽略\n");
                message = buffer.toString();
               //System.out.println(message);


                if(isSentMonitor) {
                    // SendLogCheckMonitor.sendPostRequest(busi,message);
                    LOG.info("####################sent log check monitor##################");
                }
                esOutput.add(JSON.toJSONString(map));
            }
            buffer.setLength(0);


        }
        return esOutput;
    }
}
