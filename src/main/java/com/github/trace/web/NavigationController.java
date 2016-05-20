package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.service.AnalyzeLogService;
import com.github.trace.service.CEPService;
import com.github.trace.service.Navigation0Service;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghl on 2016/3/31.
 */
@Controller
@RequestMapping("/nav")
public class NavigationController {

    @Autowired
    Navigation0Service navigation0Service;

    @Autowired
    CEPService cepService;
    @Autowired
    AnalyzeLogService analyzeLogService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<NavigationItem0> list = navigation0Service.queryAll();

        JSONArray jsonArray = ControllerHelper.convertToJSON(list);

        ControllerHelper.setLeftNavigationTree(model, cepService, "");

        model.addAttribute("data", jsonArray);
        return "nav/nav_list";
    }

    @RequestMapping("/create")
    @ResponseBody
    public Map create(@RequestParam(name = "parentId") int parentId,
                      @RequestParam(name = "name") String name,
                      @RequestParam(name = "manager") String manager,
                      @RequestParam(name = "itemType") int itemType,
                      @RequestParam(name = "topic") String topic){
        NavigationItem0 navigationItem0 = new NavigationItem0();
        navigationItem0.setParentId(parentId);
        navigationItem0.setItemType(itemType);
        navigationItem0.setName(name);
        navigationItem0.setTopic(topic);
        navigationItem0.setManager(manager);
        int res = navigation0Service.insert(navigationItem0);
        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(@RequestParam(name = "id") int id,@RequestParam(name = "parentId") int parentId,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "manager") String manager,
                         @RequestParam(name = "itemType") int itemType,
                         @RequestParam(name = "topic") String topic){
        NavigationItem0 navigationItem0 = new NavigationItem0();
        navigationItem0.setId(id);
        navigationItem0.setParentId(parentId);
        navigationItem0.setItemType(itemType);
        navigationItem0.setName(name);
        navigationItem0.setTopic(topic);
        navigationItem0.setManager(manager);
        int res=navigation0Service.modify(navigationItem0);
        return ControllerHelper.returnResponseVal(res,"修改");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestParam(name = "id") int id){
        List<NavigationItem0> list = navigation0Service.queryByParentId(id);
        if(list==null||list.isEmpty()){
            int res = navigation0Service.remove(id);
            return ControllerHelper.returnResponseVal(res, "删除");
        }else{
            return ControllerHelper.returnResponseValue(0, "该节点下挂有子节点，请先删除子节点");
        }
    }

    @RequestMapping("/new")
    public String newNav(@RequestParam(name = "parentId") int parentId,Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, "");

        model.addAttribute("parentId",parentId);
        return "nav/nav_create";
    }

    @RequestMapping("/edit")
    public String editNav(@RequestParam(name = "id") int id,Model model) {
        NavigationItem0 navigationItem0 = navigation0Service.queryById(id);
        ControllerHelper.setLeftNavigationTree(model, cepService, "");

        model.addAttribute("obj",navigationItem0);
        return "nav/nav_edit";
    }

    @RequestMapping("/getChildItem")
    public @ResponseBody List<NavigationItem0> queryChildItem(@RequestParam(name = "id") int id){
        List<NavigationItem0> list = navigation0Service.queryByParentId(id);
        return list;
    }

    @RequestMapping("/triggerWarn")
    public @ResponseBody Map triggerWarning(@RequestParam(name = "navigationName") String navigationName){
       analyzeLogService.sendMonitorByNavName(navigationName);
        return ControllerHelper.returnResponseVal(1, "报警");
    }
    @RequestMapping("/getUserInfo")
    public @ResponseBody String getUserInfo(@RequestParam(name = "username") String username){
        String userInfo = navigation0Service.getUserInfo(username);
        return userInfo;
    }
}
