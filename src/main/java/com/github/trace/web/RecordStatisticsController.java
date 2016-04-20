package com.github.trace.web;

import com.github.trace.service.CEPService;
import com.github.trace.utils.ControllerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wanghl on 2016/4/20.
 * description: buried point record statistics
 */
@Controller
@RequestMapping("/rsc")
public class RecordStatisticsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordStatisticsController.class);
    @Autowired
    private CEPService cepService;

    @RequestMapping("/list")
    public String list(Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, "");  // 左边导航条
        return "rsc/record_count_list";
    }

}
