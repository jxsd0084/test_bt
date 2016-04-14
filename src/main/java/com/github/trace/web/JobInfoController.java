package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.JobInfo;
import com.github.trace.entity.JobTarget;
import com.github.trace.service.CEPService;
import com.github.trace.service.JobServer;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by weilei on 2016/4/1.
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {

    @Autowired
    private CEPService cepService;
    @Autowired
    private JobServer jobServer;

    @RequestMapping("/listJobInfo")
    public String listJobInfo(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "jobinfo/jobinfo_index");
    }

    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
        List<JobInfo> list = jobServer.getJobInfoListByBizId(bizId);
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
        return "jobinfo/jobinfo_edit";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map add(@RequestParam(name = "bizId") int bizId,
                   @RequestParam(name = "bizName") String bizName,
                   @RequestParam(name = "name") String name,
                   @RequestParam(name = "souId") int souId,
                   @RequestParam(name = "souName") String souName,
                   @RequestParam(name = "tarId") int tarId,
                   @RequestParam(name = "tarName") String tarName,
                   @RequestParam(name = "startTime") Date startTime,
                   @RequestParam(name = "endTime") Date endTime,
                   @RequestParam(name = "memo") String memo,
                   Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        JobInfo jobInfo = new JobInfo();
        jobInfo.setBizId(bizId);
        jobInfo.setBizName(bizName);
        jobInfo.setName(name);
        jobInfo.setSouId(souId);
        jobInfo.setSouName(souName);
        jobInfo.setTarId(tarId);
        jobInfo.setTarName(tarName);
        jobInfo.setCreateTime(new Date());
        jobInfo.setMemo(memo);
        int res = jobServer.addJobInfo(jobInfo);

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
        JobInfo jobInfo = jobServer.getJobInfoById(id);

        model.addAttribute("id", id);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        model.addAttribute("tag", tag);
        model.addAttribute("obj", jobInfo);
        return "jobinfo/jobinifo_edit";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(@RequestParam(name = "id") int id,
                      @RequestParam(name = "bizId") int bizId,
                      @RequestParam(name = "bizName") String bizName,
                      @RequestParam(name = "name") String name,
                      @RequestParam(name = "souId") int souId,
                      @RequestParam(name = "souName") String souName,
                      @RequestParam(name = "tarId") int tarId,
                      @RequestParam(name = "tarName") String tarName,
                      @RequestParam(name = "startTime") Date startTime,
                      @RequestParam(name = "endTime") Date endTime,
                      @RequestParam(name = "memo") String memo,
                      Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");

        JobInfo jobInfo = jobServer.getJobInfoById(id);
        jobInfo.setBizId(bizId);
        jobInfo.setBizName(bizName);
        jobInfo.setName(name);
        jobInfo.setSouId(souId);
        jobInfo.setSouName(souName);
        jobInfo.setTarId(tarId);
        jobInfo.setTarName(tarName);
        jobInfo.setCreateTime(new Date());
        jobInfo.setMemo(memo);
        int res = jobServer.updateJobInfo(jobInfo);

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
        jobServer.deleteJobInfoById(id);
        return setCommonParam(bizId, bizName, model, "jobinfo/jobinfo_index");
    }
}
