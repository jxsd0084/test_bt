package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.entity.SearchLog;
import com.github.trace.intern.DateUtil;
import com.github.trace.service.CEPService;
import com.github.trace.service.Navigation0Service;
import com.github.trace.service.SearchService;
import com.github.trace.utils.ControllerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/4/8.
 */
@Controller
@RequestMapping("/search")
public class SearchController {

	private static final Logger LOGGER = LoggerFactory.getLogger( SearchController.class );

	@Autowired
	private CEPService cepService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private Navigation0Service navigation0Service;
    @RequestMapping("/list")
    public String list(Model model) {
	    ControllerHelper.setLeftNavigationTree(model, cepService, "");  // 左边导航条
        return "search/search_list";
    }

    @RequestMapping("/searchLog")
    public String search(@RequestParam(name = "id")   int id,
                         @RequestParam(name = "keyWord") String keyWord,
		                 Model model) {

	    ControllerHelper.setLeftNavigationTree(model, cepService, "");  // 左边导航条
		NavigationItem0 navigationItem0 = (navigation0Service.queryById(id));
		String topic = "";
		if(navigationItem0!=null){
			topic = navigationItem0.getTopic();
		}
	    SearchLog sLog = new SearchLog();
		sLog.setTopic(topic);                                           // 主题
		sLog.setKeyWord(formateStr(keyWord,navigationItem0.getName())); // 搜索关键词
		sLog.setTag("stamp");                                           // 暂时写死
		sLog.setStartTime(System.currentTimeMillis() - 24*3600*1000L);  // 24h时间戳
		sLog.setEndTime(System.currentTimeMillis());                    // now时间戳
		sLog.setPageStart(0);
		sLog.setPageSize(1000);                                         // 默认1000条

	    JSONArray jsonArray = getSearchLogList(sLog);
	    model.addAttribute("data", jsonArray);
	    model.addAttribute("id", id);
	    model.addAttribute("keyWord", keyWord);
	    return "search/search_list";
    }

	private String formateStr(String key,String name){
		String value ="";
		if(key==null||"".equals(key.trim()))
			return "";
		String[] strs = key.split(" ");
		boolean flag = false;
		for(String str :strs){
			if(str!=null&&!"".equals(str)){
				if(str.equalsIgnoreCase("and")) {
					value += "AND ";
					flag = true;
				}
				else if(str.equalsIgnoreCase("or")) {
					value += "OR ";
					flag = true;
				}
				else
					value+=str+" ";
			}
		}
		if(!flag) {
			strs = value.split(" ");
			value = "";
			for (String str : strs) {
				if (str != null && !"".equals(str)) {
					value += str+" AND ";
				}
			}
			value = value.substring(0,value.lastIndexOf(" AND"));
		}

		if("IOS".equalsIgnoreCase(name)){
			if(value.length()>0)
				value +=" AND iPhone*";
			else
				value = "iPhone*";

		}else if("Android".equals(name)){
			if(value.length()>0)
				value +=" AND Android";
			else
				value = "Android";
		}

		return value;
	}

	private JSONArray getSearchLogList(SearchLog sLog) {
		List<Map<String, Object>> list = searchService.searchESWithSize(sLog);
		JSONArray jsonArray1 = new JSONArray();
		int cont = 0;
		if (list != null) {
			for (Map<String, Object> logMap : list) {
				JSONArray jsonArray2 = new JSONArray();
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, Object> entry : logMap.entrySet()) {
					highLight( sLog, sb, entry );
				}
				jsonArray2.add(++ cont);
				jsonArray2.add( autoIndent(sb).toString() );
				jsonArray1.add(jsonArray2);
			}
		}
		return jsonArray1;
	}

	// 自动缩进
	private StringBuilder autoIndent(StringBuilder sb) {
		String  value = sb.toString();
		value = value.replaceAll("\n","<br/>");
		String[] vals = value.split("<br/>");
		StringBuilder valueSb = new StringBuilder();
		for(String s : vals){
			s = getStr(s);
			if(s.endsWith("<br/>"))
				valueSb.append(s);
			else
			    valueSb.append(s + "<br/>");
		}
		return valueSb;
	}

	private String getStr(String str){
		if(str.length() > 225){
            String newStr = str.substring(225);
			return str.substring(0, 225) + "<br/>" + getStr(newStr);
		}else{
			return str+ "<br/>";
		}
	}

	// 高亮
	private void highLight(SearchLog sLog, StringBuilder sb, Map.Entry<String, Object> entry) {
		String keyWord    = sLog.getKeyWord().trim();
		String entryKey   = entry.getKey();
		String entryValue = entry.getValue().toString();
		String temp       = entryKey + " : " + entryValue;
		String temp2      = entryKey + ":" + entryValue;

		if("stamp".equals(entryKey) ||
		    "Time".equals(entryKey) ||
			 "M98".equals(entryKey)) {
			try{ // 时间戳格式化
				entryValue = DateUtil.formatYmdHis( Long.parseLong(entryValue) );
			}catch (NumberFormatException e){
				LOGGER.error("cast EntryString to long type failed !", e);
			}
		}

		String[] strs = keyWord.split(" ");
		boolean flag = false;
		for(String str:strs){
			if(str!=null&&!"".equals(str)&&!"AND".equals(str)&&!"OR".equals(str)&&
					!"iPhone*".equals(str)&&!"Android".equals(str)&&
					(temp.equals(str) || temp2.equals(str) ||	temp.contains(str)))
				flag = true;
		}

		if(flag) {
			sb.append("<br/>");
			sb.append("<font color=\"red\">");
			sb.append(entryKey + " : " + entryValue);
			sb.append("</font>");
			sb.append(",");
			sb.append("<br/>");
		}else{
			sb.append(entryKey + " : " + entryValue);
			sb.append(",  ");
		}
	}

}
