package com.ksymmy.dao;

import com.ksymmy.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @author think 2018/1/4 18:55
 * @description MenuDao 接口
 */
public interface MenuDao {
    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    public Integer add(Menu menu);

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    public int update(Menu menu);

    /**
     * 根据Id查找
     *
     * @param id
     * @return
     */
    public Menu findById(Integer id);

    /**
     * 查询菜单集合
     *
     * @param map
     * @return
     */
    public List<Menu> list(Map<String, Object> map);

    /**
     * 获取总记录数
     *
     * @param map
     * @return
     */
    public Long getTotal(Map<String, Object> map);

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    public Integer delete(Integer id);
}
