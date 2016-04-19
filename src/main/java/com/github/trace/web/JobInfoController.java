package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.DbJob;
import com.github.trace.entity.JobInfo;
import com.github.trace.entity.JobSource;
import com.github.trace.entity.JobTarget;
import com.github.trace.service.CEPService;
import com.github.trace.service.JobSchedueService;
import com.github.trace.service.JobServer;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
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
    @Autowired
    private JobSchedueService jobSchedueService;

    @RequestMapping("/listJobInfo")
    public String listJobInfo(@RequestParam(name = "bizId") int bizId,
                        @RequestParam(name = "bizName") String bizName,
                        Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        return setCommonParam(bizId, bizName, model, "jobinfo/jobinfo_index");
    }

    private String setCommonParam(int bizId, String bizName, Model model, String retPath){
        List<JobInfo> list = jobServer.getJobInfoListByBizId(bizId);
        for(JobInfo ji:list){
            if("0".equals(ji.getExpType()))
                ji.setExpType("全量");
            else  if("1".equals(ji.getExpType()))
                ji.setExpType("增量");
        }
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
        model.addAttribute("souJson",getSouByBizId(bizId));
        model.addAttribute("tarJson",getTarByBizId(bizId));
        return "jobinfo/jobinfo_edit";
    }

    private JSONArray  getSouByBizId(int bizId){
        Map<Integer,String> map = new HashMap<Integer,String>();
        List<JobSource> list  = jobServer.getJobSouListByBizId(bizId);
        for(JobSource jobSou :list){
            map.put(jobSou.getId(),jobSou.getName());
        }
        return getJsonArr(map);
    }
    private JSONArray  getTarByBizId(int bizId){
        Map<Integer,String> map = new HashMap<Integer,String>();
        List<JobTarget> list  = jobServer.getJobTarListByBizId(bizId);
        for(JobTarget jobTar :list){
            map.put(jobTar.getId(),jobTar.getName());
        }
        return getJsonArr(map);
    }

    private JSONArray getJsonArr(Map<Integer,String> map){
        JSONArray tarArr = new JSONArray();
        for(Integer key :map.keySet()){
            Map tar = new HashMap();
            tar.put("key",String.valueOf(key));
            tar.put("value", map.get(key));
            tarArr.add(tar);
        }
        return tarArr;
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
                   @RequestParam(name = "expType") String expType,
                   @RequestParam(name = "startTime") String startTime,
                   @RequestParam(name = "endTime") String endTime,
                   @RequestParam(name = "exeTime") String exeTime,
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
        jobInfo.setExpType(expType);
        jobInfo.setStartTime(startTime);
        jobInfo.setEndTime(endTime);
        jobInfo.setExeTime(exeTime);
        jobInfo.setCreateTime(new Date());
        jobInfo.setMemo(memo);
        int res = jobServer.addJobInfo(jobInfo);

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizName", bizName);
        return ControllerHelper.returnResponseVal(res, "添加");
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "id") int id,
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
        model.addAttribute("souJson",getSouByBizId(bizId));
        model.addAttribute("tarJson",getTarByBizId(bizId));
        return "jobinfo/jobinfo_edit";
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
                      @RequestParam(name = "expType") String expType,
                      @RequestParam(name = "startTime") String startTime,
                      @RequestParam(name = "endTime") String endTime,
                      @RequestParam(name = "exeTime") String exeTime,
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
        jobInfo.setExpType(expType);
        jobInfo.setStartTime(startTime);
        jobInfo.setEndTime(endTime);
        jobInfo.setExeTime(exeTime);
        jobInfo.setUpdateTime(new Date());
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

    @RequestMapping("/importDB")
    public void importDB(@RequestParam(name = "id") int id,
                           @RequestParam(name = "bizId") int bizId,
                           @RequestParam(name = "bizName") String bizName,
                           Model model){
        ControllerHelper.setLeftNavigationTree(model, cepService, "ds");
        List<DbJob> jobList = jobSchedueService.getDbJobList(id);
        System.out.println("导入完毕");
    }
}
