package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

	    JSONArray jsonArray = getSearchLogList(sLog);
	    model.addAttribute("data", jsonArray);
	    model.addAttribute("topic", topic);
	    model.addAttribute("keyWord", keyWord);
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
