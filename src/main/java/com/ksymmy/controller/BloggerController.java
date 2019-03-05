package com.ksymmy.controller;

import com.ksymmy.controller.admin.MenuAdminController;
import com.ksymmy.entity.Blogger;
import com.ksymmy.entity.Menu;
import com.ksymmy.service.BloggerService;
import com.ksymmy.service.MenuService;
import com.ksymmy.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 博主 controller
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {

    @Resource
    private BloggerService bloggerService;
    @Resource
    private MenuService menuService;

    /**
     * 博主登录
     * @param blogger
     * @param request
     * @return String
     */
    @RequestMapping("/login.do")
    public String login(Blogger blogger, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(blogger.getUserName(), CryptographyUtil.md5(blogger.getPassword(), "salt"));
        try {
            subject.login(token);
            return "redirect:/blogger/adminMain.html";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("blogger", blogger);
            request.setAttribute("errorInfo", "用户名或密码错误!");
            //request.getRequestDispatcher("/login.jsp").forward(request,response);
            return "login";
        }
    }

    /**
     * 重定向到 博主管理页面
     * @return
     */
    @RequestMapping("/adminMain")
    public String adminMain(HttpServletRequest request,HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        if(session==null || session.getAttribute("currentUser")==null){
            response.setHeader("Content-type","text/html;UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write("<script language='javascript'>var r=confirm(\"未登录或登陆超时,请重新登陆!\");\n" +
                    "if(r==true){top.location.href = \"http://\"+window.location.host+\"/Blog/indexLogin.html\";}else{}</script>");
            pw.flush();
            pw.close();;
            pw = null;
            return null;
        }
        List<Menu> menus = menuService.list(null);
        List<Map<String, Object>> menuList = MenuAdminController.createTreeGridChildren(menus,0);
        request.setAttribute("menuList",menuList);
        return "admin/main";
    }

    public ModelAndView ablutMe() {
        ModelAndView mav = new ModelAndView("mainTemp");
        mav.addObject("blogger", bloggerService.find(1));
        mav.addObject("mainPage", "foreground/blogger/info.jsp");
        mav.addObject("pageTitle", "关于博主_开源博客");
        return mav;
    }

    /**
     * 查找博主信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/aboutMe")
    public ModelAndView aboutMe()throws Exception{
        ModelAndView mav=new ModelAndView();
        mav.addObject("blogger",bloggerService.find(1));
        mav.addObject("mainPage", "foreground/blogger/info.jsp");
        mav.addObject("pageTitle","关于博主_Java开源博客系统");
        mav.setViewName("mainTemp");
        return mav;
    }
}
