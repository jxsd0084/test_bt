package com.github.trace.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by chenlong on 2016/4/1.
 */
@Controller
@RequestMapping("/job")
public class JobController {

//    @Autowired
//    private CEPService cepService;
//    @Autowired
//    private DataSourceServer dataSourceServer;

//    @RequestMapping("/listJob")
//    public String index(@RequestParam(name = "bizId") int bizId,
//                        @RequestParam(name = "bizName") String bizName,
//                        Model model){
//        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
//        return setCommonParam(bizId, bizName, model, "job/job_index");
//    }

//    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
////        List<DatabaseInfo> list = dataSourceServer.getDataBaseInfoListById(bizId);
////        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
////        model.addAttribute("data", jsonArray);
//        model.addAttribute("bizId", bizId);
//        model.addAttribute("bizName", bizName);
//        return retPath;
//    }

}
