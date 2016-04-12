package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.SearchLog;
import com.github.trace.service.CEPService;
import com.github.trace.service.SearchService;
import com.github.trace.utils.ControllerHelper;
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

	@Autowired
	private CEPService cepService;
	@Autowired
	private SearchService searchService;

    @RequestMapping("/list")
    public String list(Model model) {
	    ControllerHelper.setLeftNavigationTree(model, cepService, "");  // 左边导航条
        return "search/search_list";
    }

    @RequestMapping("/searchLog")
    public String search(@RequestParam(name = "topic")   String topic,
                         @RequestParam(name = "keyWord") String keyWord,
		                 Model model) {

	    ControllerHelper.setLeftNavigationTree(model, cepService, "");  // 左边导航条

	    SearchLog sLog = new SearchLog();
		sLog.setTopic(topic);                                           // 主题
		sLog.setKeyWord(keyWord);                                       // 搜索关键词
		sLog.setTag("stamp");                                           // 暂时写死
		sLog.setStartTime(System.currentTimeMillis() - 24*3600*1000L);  // 24h时间戳
		sLog.setEndTime(System.currentTimeMillis());                    // now时间戳
		sLog.setPageStart(0);
		sLog.setPageSize(1000);                                         // 默认1000条

	    JSONArray jsonArray = getSearchLogList(sLog);
	    model.addAttribute("data", jsonArray);
	    model.addAttribute("topic", topic);
	    model.addAttribute("keyWord", keyWord);
	    return "search/search_list";
    }

	private JSONArray getSearchLogList(SearchLog sLog) {
		List<Map<String, Object>> list = searchService.searchESWithSize(sLog);
		JSONArray jsonArray1 = new JSONArray();
		int cont = 0;
		for (Map<String, Object> logMap : list) {
			JSONArray jsonArray2 = new JSONArray();
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Object> entry : logMap.entrySet()) {
				highLight(sLog, sb, entry);
			}
			jsonArray2.add(++ cont);
			jsonArray2.add(sb.toString());
			jsonArray1.add(jsonArray2);
		}
		return jsonArray1;
	}

	private void highLight(SearchLog sLog, StringBuilder sb, Map.Entry<String, Object> entry) {
		String keyWord    = sLog.getKeyWord().trim();
		String entryKey   = entry.getKey();
		String entryValue = entry.getValue().toString();
		String temp       = entryKey + " : " + entryValue;
		String temp2      = entryKey + ":" + entryValue;
		if(temp.equals(keyWord) ||
		   temp2.equals(keyWord) ||
		   temp.contains(keyWord)) {
			sb.append("<br/>");
			sb.append("<font color=\"red\">");
			sb.append(entryKey + " : " + entryValue);
			sb.append("</font>");
			sb.append("<br/>");
		}else{
			sb.append(entryKey + " : " + entryValue);
			sb.append("&nbsp;&nbsp;");
		}
	}

}
