package com.github.trace.utils;

import com.github.trace.entity.NavigationItem;
import com.github.trace.service.CEPService;
import com.google.common.collect.ImmutableMap;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/3/17.
 */
public class ControllerHelper {

    // 此处为防止页面刷新之后, 左边导航条的数据丢失
    public static List setLeftNavigationTree(Model model, CEPService cepService){
        List<NavigationItem> navigationItemList = cepService.getConfiguration();
        model.addAttribute("navigationItemList", navigationItemList);
        model.addAttribute("navObj", navigationItemList.get(0));
        return navigationItemList;
    }

    /**
     * 处理响应信息
     * @param res
     * @return
     */
    public static Map returnResponseVal(int res, String msg){
        String result;
        if(res == 1){
            result = "数据" + msg + "成功!";
            return ImmutableMap.of("code", 200, "info", result);
        }else{
            result = "数据" + msg + "失败！";
            return ImmutableMap.of("code", -1, "info", result);
        }
    }

}
