package com.ksymmy.controller.admin;

import com.ksymmy.entity.Blog;
import com.ksymmy.entity.BlogType;
import com.ksymmy.entity.Blogger;
import com.ksymmy.entity.Link;
import com.ksymmy.service.BlogService;
import com.ksymmy.service.BlogTypeService;
import com.ksymmy.service.BloggerService;
import com.ksymmy.service.LinkService;
import com.ksymmy.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 管理员系统Controller层
 *
 * @author ksymmy
 */
@Controller
@RequestMapping("/admin/system")
public class SystemAdminController {

    @Resource
    private BloggerService bloggerService;

    @Resource
    private BlogTypeService blogTypeService;

    @Resource
    private BlogService blogService;

    @Resource
    private LinkService linkService;

    /**
     * 刷新系统缓存
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/refreshSystem")
    public String refreshSystem(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
        Blogger blogger = bloggerService.find(1); // 查询博主信息
        blogger.setPassword(null);
        application.setAttribute("blogger", blogger);

        List<BlogType> blogTypeCountList = blogTypeService.countList(); // 查询博客类别以及博客的数量
        application.setAttribute("blogTypeCountList", blogTypeCountList);

        List<Blog> blogCountList = blogService.countList(); // 根据日期分组查询博客
        application.setAttribute("blogCountList", blogCountList);

        List<Link> linkList = linkService.list(null); // 获取所有友情链接
        application.setAttribute("linkList", linkList);

        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }
}
