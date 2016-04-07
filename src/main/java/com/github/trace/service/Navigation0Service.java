package com.github.trace.service;

import com.github.trace.entity.NavigationItem0;
import com.github.trace.mapper.NavigationItem0Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wanghl on 2016/3/31.
 */
@Service
public class Navigation0Service {
    @Autowired
    private NavigationItem0Mapper navigationItem0Mapper;

    /**
     * 获取所有节点
     */
    public List<NavigationItem0> queryAll() {
       return navigationItem0Mapper.findAll();
    }

    /**
     * 创建导航条目
     * @param navigationItem0
     * @return
     */
    public int insert(NavigationItem0 navigationItem0) {
        return navigationItem0Mapper.insert(navigationItem0); }

    /**
     * 修改导航条目
     * @param navigationItem0
     * @return
     */
    public int modify(NavigationItem0 navigationItem0) {
        return navigationItem0Mapper.update(navigationItem0); }

    /**
     * 删除导航条目
     * @param id
     * @return
     */
    public int remove(int id) {
        return navigationItem0Mapper.deleteById(id);
    }

    /**
     * 查询单条导航栏项目
     * @param id
     * @return
     */
    public NavigationItem0 queryById(int id){
        return navigationItem0Mapper.findById(id);
    }

    /**
     * 查询单条导航栏项目
     * @param id
     * @return
     */
    public List<NavigationItem0> queryByParentId(int parentId){
        return navigationItem0Mapper.findByParentId(parentId);
    }
}
