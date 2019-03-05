package com.ksymmy.controller;


import com.ksymmy.entity.Blog;
import com.ksymmy.entity.PageBean;
import com.ksymmy.service.BlogService;
import com.ksymmy.service.BloggerService;
import com.ksymmy.service.LinkService;
import com.ksymmy.util.PageUtil;
import com.ksymmy.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页Controller
 *
 * @author ksymmy
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Resource
    private BlogService blogService;

    @Resource
    private BloggerService bloggerService;

    @Resource
    private LinkService linkService;

    @RequestMapping("/indexLogin")
    public String indexLogin(){
        return "login";
    }

    @RequestMapping("/toTopWindow")
    public void toTopWindow(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        if(session==null || session.getAttribute("currentUser")==null){
            response.setHeader("Content-type","text/html;UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write("<script language='javascript'>var r=confirm(\"未登录或登陆超时,请重新登陆!\");\n" +
                    "if(r==true){top.location.href = \"http://\"+window.location.host+\"/Blog/indexLogin.html\";}else{}</script>");
            pw.flush();
            pw.close();;
            pw = null;
        }
    }

    /**
     * 请求主页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "typeId", required = false) String typeId, @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        PageBean pageBean = new PageBean(Integer.parseInt(page), 10);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("typeId", typeId);
        map.put("releaseDateStr", releaseDateStr);
        List<Blog> blogList = blogService.list(map);
        for (Blog blog : blogList) {
            List<String> imagesList = blog.getImagesList();
            String blogInfo = blog.getContent();
            Document doc = Jsoup.parse(blogInfo);
            Elements jpgs = doc.select("img[src$=.jpg]"); //　查找扩展名是jpg的图片
            for (int i = 0; i < jpgs.size(); i++) {
                Element jpg = jpgs.get(i);
                imagesList.add(jpg.toString());
                if (i == 2) {
                    break;
                }
            }
        }
        mav.addObject("blogList", blogList);
        StringBuffer param = new StringBuffer(); // 查询参数
        if (StringUtil.isNotEmpty(typeId)) {
            param.append("typeId=" + typeId + "&");
        }
        if (StringUtil.isNotEmpty(releaseDateStr)) {
            param.append("releaseDateStr=" + releaseDateStr + "&");
        }
        mav.addObject("pageCode", PageUtil.genPagination(request.getContextPath() + "/index.html", blogService.getTotal(map), Integer.parseInt(page), 10, param.toString()));
        mav.addObject("mainPage", "foreground/blog/list.jsp");
        mav.addObject("pageTitle", "Java开源博客系统");
        mav.setViewName("mainTemp");
        return mav;
    }

    /**
     * 源码下载
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/download")
    public ModelAndView download() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "foreground/system/download.jsp");
        mav.addObject("pageTitle", "本站源码下载页面_Java开源博客系统");
        mav.setViewName("mainTemp");
        return mav;
    }
}
