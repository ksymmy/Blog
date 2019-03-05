package com.ksymmy.controller.admin;

import com.ksymmy.entity.Blogger;
import com.ksymmy.entity.Menu;
import com.ksymmy.entity.PageBean;
import com.ksymmy.service.MenuService;
import com.ksymmy.util.ResponseUtil;
import com.ksymmy.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author think 2018/1/4 18:29
 * @description 菜单管理Controller
 */

@Controller
@RequestMapping("/admin/menu")
public class MenuAdminController {

    @Resource
    private MenuService menuService;

    @RequestMapping("/index")
    public String index() {
        return "admin/menuManage";
    }

    @RequestMapping("/list")
    public String list(String rows, String page, HttpServletResponse response,String id) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("id",id);
        List<Menu> menuList = menuService.list(map);
        List<Map<String, Object>> mList;
        if(StringUtil.isEmpty(id)){
            mList = createTreeGridChildren(menuList, 0);
        }else{
            mList = createTreeGridChildren(menuList, Integer.parseInt(id));
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        ResponseUtil.write(response, JSONArray.fromObject(mList, jsonConfig));
        return null;
    }

    public static List<Map<String, Object>> createTreeGridChildren(List<Menu> list, Integer fid) {
        List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
        for (int j = 0; j < list.size(); j++) {
            Map<String, Object> map = null;
            Menu treeChild = list.get(j);
            if (fid.equals(treeChild.getFid())) {
                map = new HashMap<String, Object>();
                map.put("_parentId", list.get(j).getFid());
                map.put("id", list.get(j).getId());
                map.put("text", list.get(j).getName());
                map.put("iconCls", list.get(j).getIcons());
                map.put("url", list.get(j).getUrl());
                map.put("createDate", list.get(j).getCreateDate());
                map.put("xh",list.get(j).getXh());
                List<Map<String, Object>> children = createTreeGridChildren(list, treeChild.getId());
                if(children.size()>0){
                    map.put("state","closed");
                    map.put("children", children);
                }else{
                    map.put("state","open");
                }
            }

            if (map != null)
                childList.add(map);
        }
        return childList;
    }

    @RequestMapping("/addOrEditMenu")
    public String addOrEditMenu(String id, HttpServletRequest request) {
        return "admin/addOrEditMenu";
    }

    @RequestMapping("/menuTree")
    public String menuTree(HttpServletResponse response) throws Exception {
        List<Menu> menuList = menuService.list(null);
        List<Map<String, Object>> mList = createTreeGridChildren(menuList, 0);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        ResponseUtil.write(response, JSONArray.fromObject(mList, jsonConfig));
        return null;
    }

    @RequestMapping("/saveMenu")
    public String saveMenu(HttpServletRequest request, HttpServletResponse response, String id, String _parentId, String text, String url, String iconCls, String createDate, String xh) throws Exception {
        Integer resultTotal = 0;
        Menu menu;
        _parentId = StringUtil.isEmpty(_parentId)?"0":_parentId;
        if (StringUtil.isEmpty(id)) {
            menu = new Menu();
            menu.setFid(Integer.parseInt(_parentId));
            menu.setName(text);
            menu.setUrl(url);
            menu.setIcons(iconCls);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            menu.setCreateDate(sdf.parse(createDate));
            menu.setXh(Integer.parseInt(xh));
            menu.setBlogger((Blogger) request.getSession().getAttribute("currentUser"));
            resultTotal = menuService.add(menu);
        } else {
            menu = menuService.findById(Integer.parseInt(id));
            menu.setFid(Integer.parseInt(_parentId));
            menu.setName(text);
            menu.setUrl(url);
            menu.setIcons(iconCls);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            menu.setCreateDate(sdf.parse(createDate));
            menu.setXh(Integer.parseInt(xh));
//            menu.setBlogger((Blogger) request.getSession().getAttribute("currentUser"));
            resultTotal = menuService.update(menu);
        }
        JSONObject jsonObject = new JSONObject();
        if (resultTotal>0) {
            jsonObject.put("success", true);
            jsonObject.put("fid",menu.getFid());
        } else {
            jsonObject.put("success", false);
        }
        ResponseUtil.write(response, jsonObject);
        return null;
    }

    @RequestMapping("/delete")
    public String delete(String id,HttpServletResponse response) throws Exception{
        Integer resultTotal = 0;
        resultTotal = menuService.delete(Integer.parseInt(id));
        JSONObject jsonObject = new JSONObject();
        if (resultTotal>0) {
            jsonObject.put("success", true);
        } else {
            jsonObject.put("success", false);
        }
        ResponseUtil.write(response, jsonObject);
        return null;
    }
}
