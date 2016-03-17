package com.github.trace.utils;

import com.github.trace.entity.NavigationItem;
import com.github.trace.service.CEPService;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Created by chenlong on 2016/3/17.
 */
public class ControllerHelper {

    // 此处为防止页面刷新之后, 左边导航条的数据丢失
    public static void setLeftNavigationTree(Model model, CEPService cepService){
        List<NavigationItem> navigationItemList = cepService.getConfiguration();
        model.addAttribute("navigationItemList", navigationItemList);
    }

}
