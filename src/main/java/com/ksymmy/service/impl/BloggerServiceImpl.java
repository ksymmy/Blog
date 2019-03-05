package com.ksymmy.service.impl;

import com.ksymmy.dao.BloggerDao;
import com.ksymmy.entity.Blogger;
import com.ksymmy.service.BloggerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 博主Service实现类
 *
 * @author ksymmy
 */
@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService {

    @Resource
    private BloggerDao bloggerDao;

    public Blogger find(Integer id) {
        return bloggerDao.findById(id);
    }

    public Blogger getByUserName(String userName) {
        return bloggerDao.getByUserName(userName);
    }

    public Integer update(Blogger blogger) {
        return bloggerDao.update(blogger);
    }


}
