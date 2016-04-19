package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.entity.JobSource;
import com.github.trace.entity.JobTarget;
import com.github.trace.entity.TableField;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataSourceServer;
import com.github.trace.service.JobServer;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by weilei on 2016/4/1.
 */
@Controller
@RequestMapping("/jobtarget")
public class JobTargetController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private JobServer jobServer;

    @RequestMapping("/listJobTarget")
    public String listJobTarget(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "jobtarget/jobtarget_index");
    }

    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
        List<JobTarget> list = jobServer.getJobTarListByBizId(bizId);
        JSONArray jsonArray = ControllerHelper.convertToJSON(list);
        model.addAttribute("data", jsonArray);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return retPath;
    }

    @RequestMapping("/create")
    public String create(@RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         @RequestParam(name = "tag") String tag,
                         Model model) {
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        return "jobtarget/jobtarget_edit";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map add(@RequestParam(name = "bizId") int bizId,
                   @RequestParam(name = "bizName") String bizName,
                   @RequestParam(name = "name") String name,
                   @RequestParam(name = "type") String type,
                   @RequestParam(name = "ip") String ip,
                   @RequestParam(name = "port") int port,
                   @RequestParam(name = "path") String path,
                   @RequestParam(name = "memo") String memo,
                   Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobTarget jobTarget = new JobTarget();
        jobTarget.setBizId(bizId);
        jobTarget.setBizName(bizName);
        jobTarget.setName(name);
        jobTarget.setType(type);
        jobTarget.setIp(ip);
        jobTarget.setPort(port);
        jobTarget.setPath(path);
        jobTarget.setMemo(memo);
        int res = jobServer.addJobTar(jobTarget);

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "id", required = false) int id,
                         @RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         @RequestParam(name = "tag") String tag,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobTarget jobTarget = jobServer.getJobTarById(id);

        model.addAttribute("id", id);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        model.addAttribute("obj", jobTarget);
        return "jobtarget/jobtarget_edit";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(@RequestParam(name = "id") int id,
                      @RequestParam(name = "bizId") int bizId,
                      @RequestParam(name = "bizName") String bizName,
                      @RequestParam(name = "name") String name,
                      @RequestParam(name = "type") String type,
                      @RequestParam(name = "ip") String ip,
                      @RequestParam(name = "port") int port,
                      @RequestParam(name = "path") String path,
                      @RequestParam(name = "memo") String memo,
                      Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");

        JobTarget jobTarget = jobServer.getJobTarById(id);
        jobTarget.setBizId(bizId);
        jobTarget.setBizName(bizName);
        jobTarget.setName(name);
        jobTarget.setType(type);
        jobTarget.setIp(ip);
        jobTarget.setPort(port);
        jobTarget.setPath(path);
        jobTarget.setMemo(memo);
        int res = jobServer.updateJobTar(jobTarget);

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return ControllerHelper.returnResponseVal(res, "修改");
    }


    @RequestMapping("/delete")
    public String delete(@RequestParam(name = "id") int id,
                         @RequestParam(name = "bizId") int bizId,
                         @RequestParam(name = "bizName") String bizName,
                         Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        jobServer.deleteJobTarById(id);
        return setCommonParam(bizId, bizName, model, "jobtarget/jobtarget_index");
    }
}
