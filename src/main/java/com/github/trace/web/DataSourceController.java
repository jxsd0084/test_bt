package com.github.trace.web;

import com.github.trace.service.CEPService;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by chenlong on 2016/3/25.
 */
@Controller
@RequestMapping("/ds")
public class DataSourceController {

    @Autowired
    private CEPService cepService;

    @RequestMapping("/index")
    public String index(Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return "ds/ds_index";
    }

    @RequestMapping("/edit")
    public String edit(Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return "ds/ds_edit";
    }


}
