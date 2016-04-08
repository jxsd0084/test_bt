package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.SearchLog;
import com.github.trace.service.SearchService;
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
	private SearchService searchService;

    @RequestMapping("/list")
    public String list(Model model) {
        return "search/search_list";
    }

    @RequestMapping("/searchLog")
    public String search(@RequestParam(name = "topic")   String topic,
                         @RequestParam(name = "keyWord") String keyWord,
		                 Model model) {

	    SearchLog sLog = new SearchLog();
		sLog.setTopic("dcx.MonitorRequest");
		sLog.setKeyWord("dd48eca1af5f46ec73e457bc92e856a3");
		sLog.setTag("stamp");                                           // 暂时写死
		sLog.setStartTime(System.currentTimeMillis() - 24*3600*1000L);  // 24h时间戳
		sLog.setEndTime(System.currentTimeMillis());                    // now时间戳

	    JSONArray jsonArray1 = getSearchLogList(sLog);
	    model.addAttribute("data", jsonArray1);

	    return "search/search_list";
    }

	private JSONArray getSearchLogList(SearchLog sLog) {
		List<Map<String, Object>> list = searchService.searchES(sLog);
		JSONArray jsonArray1 = new JSONArray();
		int cont = 0;
		for (Map<String, Object> logMap : list) {
		    JSONArray  jsonArray2 = new JSONArray();
			JSONObject jsonObject = new JSONObject();
		    jsonObject.putAll(logMap);
		    jsonArray2.add(++ cont);
		    jsonArray2.add(jsonObject.toString());
			jsonArray1.add(jsonArray2);
		}
		return jsonArray1;
	}

}
