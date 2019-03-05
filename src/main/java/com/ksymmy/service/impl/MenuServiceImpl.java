package com.ksymmy.service.impl;

import com.ksymmy.dao.MenuDao;
import com.ksymmy.entity.Menu;
import com.ksymmy.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author think 2018/1/4 18:35
 * @description
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;

    @Override
    public Integer add(Menu menu) {
        return menuDao.add(menu);
    }

    @Override
    public int update(Menu menu) {
        return menuDao.update(menu);
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public List<Menu> list(Map<String, Object> map) {
        return menuDao.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return menuDao.getTotal(map);
    }

    @Override
    public Integer delete(Integer id) {
        return menuDao.delete(id);
    }
}
