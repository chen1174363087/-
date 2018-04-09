package com.chenxin.houseparent.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;

@Controller
@RequestMapping("/util")
public class Util {
    @RequestMapping(value = "/getIdentifyCode", method = RequestMethod.GET)
    public void getIdentifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedImage bi = new BufferedImage(68, 22, BufferedImage.TYPE_INT_BGR);
        Graphics g = bi.getGraphics();
        Color c = new Color(250, 150, 255);
        g.setColor(c);
        g.fillRect(0, 0, 68, 22);
        //验证码字符集合
        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            index = r.nextInt(len);
            //设置验证码字符随机颜色
            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
            //画出对应随机的验证码字符
            g.drawString(ch[index] + "", (i * 15) + 3, 18);
            sb.append(ch[index]);
        }
        //把验证码字符串放入Session
        request.getSession().setAttribute("picCode", sb.toString());
        Writer write = response.getWriter();
        write.write(sb.toString());
        write.flush();
        write.close();
        //String picDir = System.getProperty("user.home").toString() + System.getProperty("file.separator").toString()+"code.jpg";
        //File pic = new File(picDir);
        //在HttpServletResponse中写入验证码图片信息
        //ImageIO.write(bi, "JPG", pic);
        //Writer stream = response.getWriter();
        //stream.write(picDir);
    }

    /**
     * 验证验证码输入
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/identify", method = RequestMethod.POST)
    public void identify(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject ui) throws IOException {
        String piccode = (String) request.getSession().getAttribute("picCode");
        String checkcode = ui.getString("picCode");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (checkcode.equalsIgnoreCase(piccode)) {
            out.println("正确");
        } else {
            out.println("错误");
        }
        out.flush();
        out.close();
    }
}
