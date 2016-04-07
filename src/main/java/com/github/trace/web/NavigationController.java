package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.service.CEPService;
import com.github.trace.service.Navigation0Service;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
                      @RequestParam(name = "itemType") int itemType,
                      @RequestParam(name = "topic") String topic){
        System.out.println("创建开始");
        NavigationItem0 navigationItem0 = new NavigationItem0();
        navigationItem0.setParentId(parentId);
        navigationItem0.setItemType(itemType);
        navigationItem0.setName(name);
        navigationItem0.setTopic(topic);
        int res = navigation0Service.insert(navigationItem0);
        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/modify")
    public String modify(@RequestParam(name = "id") int id,@RequestParam(name = "parentId") int parentId,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "itemType") int itemType,
                         @RequestParam(name = "topic") String topic){
        NavigationItem0 navigationItem0 = new NavigationItem0();
        navigationItem0.setParentId(id);
        navigationItem0.setParentId(parentId);
        navigationItem0.setItemType(itemType);
        navigationItem0.setName(name);
        navigationItem0.setTopic(topic);
        navigation0Service.modify(navigationItem0);
        return "nav/list";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam(name = "id") int id,Model model){
        navigation0Service.remove(id);
        return list(model);
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
}
