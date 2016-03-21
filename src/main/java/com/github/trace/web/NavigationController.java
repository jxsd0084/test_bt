package com.github.trace.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by chenlong on 2016/3/21.
 */
// 导航树
@Controller
public class NavigationController {

    @RequestMapping("/listPNode")
    public String navList(Model model){
        return "nav/nav_list";
    }

}
