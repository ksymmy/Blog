package com.ksymmy.service.impl;

import com.ksymmy.dao.LinkDao;
import com.ksymmy.entity.Link;
import com.ksymmy.service.LinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 友情链接Service实现类
 *
 * @author ksymmy
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkDao linkDao;

    public int add(Link link) {
        return linkDao.add(link);
    }

    public int update(Link link) {
        return linkDao.update(link);
    }

    public List<Link> list(Map<String, Object> map) {
        return linkDao.list(map);
    }

    public Long getTotal(Map<String, Object> map) {
        return linkDao.getTotal(map);
    }

    public Integer delete(Integer id) {
        return linkDao.delete(id);
    }

}