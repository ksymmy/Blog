package com.ksymmy.dao;

import com.ksymmy.entity.Blogger;

/**
 * 博主Dao接口
 *
 * @author ksymmy
 */
public interface BloggerDao {

    /**
     * 查询博主信息
     *
     * @return
     */
    public Blogger findById(Integer id);

    /**
     * 通过用户名查询用户
     *
     * @param userName
     * @return
     */
    public Blogger getByUserName(String userName);

    /**
     * 更新博主信息
     *
     * @param blogger
     * @return
     */
    public Integer update(Blogger blogger);
}
