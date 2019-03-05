package com.ksymmy.controller.admin;

import com.ksymmy.entity.BlogType;
import com.ksymmy.entity.Blogger;
import com.ksymmy.service.BlogService;
import com.ksymmy.service.BlogTypeService;
import com.ksymmy.service.BloggerService;
import com.ksymmy.util.CryptographyUtil;
import com.ksymmy.util.DateUtil;
import com.ksymmy.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * 管理员博主Controller层
 *
 * @author ksymmy
 */
@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {

    @Resource
    private BloggerService bloggerService;
    @Resource
    private BlogService blogService;
    @Resource
    private BlogTypeService blogTypeService;

    /**
     * 修改个人信息页面
     * @return
     */
    @RequestMapping("/modifyInfo")
    public String modifyInfo() {
        return "admin/modifyInfo";
    }

    /**
     * 写博客
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/writeBlog")
    public String writeBlog(HttpServletRequest request,HttpServletResponse response) throws Exception{
        List<BlogType> blogTypeCountList = blogTypeService.list(null);
        JSONArray jsonArray = JSONArray.fromObject(blogTypeCountList);
        request.setAttribute("blogTypeCountList",jsonArray);
        return "admin/writeBlog";
    }

    /**
     * 博客管理页面
     * @return
     */
    @RequestMapping("/blogManage")
    public String blogManage(){
        return "admin/blogManage";
    }
    /**
     * 修改博主信息
     *
     * @param imageFile
     * @param blogger
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(@RequestParam("imageFile") MultipartFile imageFile, Blogger blogger, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!imageFile.isEmpty()) {
            String filePath = request.getSession().getServletContext().getRealPath("/");
            String imageName = DateUtil.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];
            String userImagesPath = filePath + "/static/userImages/";
            File file = new File(userImagesPath);
            if(!file.exists()){
                file.mkdirs();
            }
            imageFile.transferTo(new File(userImagesPath + imageName));
            blogger.setImageName(imageName);
        }
        int resultTotal = bloggerService.update(blogger);
        StringBuffer result = new StringBuffer();
        if (resultTotal > 0) {
            result.append("<script language='javascript'>alert('修改成功！');</script>");
        } else {
            result.append("<script language='javascript'>alert('修改失败！');</script>");
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 查询博主信息
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletResponse response) throws Exception {
        Blogger blogger = bloggerService.find(1);
        JSONObject jsonObject = JSONObject.fromObject(blogger);
        ResponseUtil.write(response, jsonObject);
        return null;
    }

    /**
     * 修改博主密码
     *
     * @param newPassword
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyPassword")
    public String modifyPassword(String newPassword, HttpServletResponse response) throws Exception {
        Blogger blogger = new Blogger();
        blogger.setPassword(CryptographyUtil.md5(newPassword, "salt"));
        int resultTotal = bloggerService.update(blogger);
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 注销
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.getSession().invalidate();
        //SecurityUtils.getSubject().logout();
        return "login";
    }
}
