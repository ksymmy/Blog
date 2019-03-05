package com.ksymmy.entity;

import java.util.Date;

/**
 * @author think 2018/1/4 18:20
 * @description 菜单域类
 */
public class Menu {
    private Integer id; //菜单id
    private Integer fid; //父菜单
    private String name; //菜单名称
    private String url; //url
    private String icons; //图标
    private Integer xh; //序号
    private Blogger blogger; //创建人
    private Date createDate; //创建时间
    private Integer state; //状态 0:禁用 1:启用

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }
}
