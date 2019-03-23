package com.example.paper.controller;

import com.example.paper.converter.SignUpFormToUser;
import com.example.paper.entity.User;
import com.example.paper.form.SignUpForm;
import com.example.paper.service.MailServiceImpl;
import com.example.paper.service.ManageUser;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value = "/sign")
public class sign {
    private final ManageUser manageUser;
    private final TemplateEngine templateEngine;
    private final MailServiceImpl mailService;
    private final StringRedisTemplate stringRedisTemplate;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    public sign(ManageUser manageUser, TemplateEngine templateEngine, MailServiceImpl mailService, StringRedisTemplate stringRedisTemplate) {
        this.manageUser = manageUser;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @RequestMapping(value = "/in", method = RequestMethod.POST)
    public ResultVO LogIn(@RequestParam("account") String account, @RequestParam("password") String password) {
        ResultVO resultVO = new ResultVO();
        if (account.equals("") || password.equals("")) {
            resultVO.setState(0);
            resultVO.setMsg("账号密码不能为空");
            resultVO.setData(null);
        } else {
            User user = manageUser.FindOne(account);
            if (user == null) {
                resultVO.setState(0);
                resultVO.setMsg("该用户不存在");
                resultVO.setData(null);
            } else if (user.getPassword().equals(password)) {
                resultVO.setState(1);
                String token=getToken(user);
                resultVO.setToken(token);
                resultVO.setMsg("成功");
                resultVO.setData(user);
                logger.info(user.getUsername()+" success to log in");
            } else {
                resultVO.setState(0);
                resultVO.setMsg("密码错误");
                resultVO.setData(null);
            }
        }
        return resultVO;
    }

    @RequestMapping(value = "/up", method = RequestMethod.PUT)
    public ResultVO LogUp(SignUpForm signUpForm) {
        ResultVO resultVO = new ResultVO();
        User user = SignUpFormToUser.convert(signUpForm);
        if (manageUser.FindOne(user.getAccount()) == null) {
            manageUser.AddUser(user);
            String token = getToken(user);
            resultVO.setState(1);
            resultVO.setToken(token);
            resultVO.setData(user);
            resultVO.setMsg("成功");
        } else {
            resultVO.setState(0);
            resultVO.setMsg("账号已存在");
            resultVO.setData(null);
        }
        return resultVO;
    }

    private String getToken(User user) {
        String token;
        if (stringRedisTemplate.hasKey(user.getAccount())){
            token = stringRedisTemplate.opsForValue().get(user.getAccount());
        }
        else {
            token = JwtUtil.generateToken(user);
            stringRedisTemplate.opsForValue().set(user.getAccount(),token);
            stringRedisTemplate.expire(user.getAccount(),3600, TimeUnit.SECONDS);
        }
        return token;
    }

    @RequestMapping(value = "/forget", method = RequestMethod.PATCH)
    public ResultVO forget(@RequestParam("account") String account, @RequestParam("password") String password) {
        ResultVO resultVO = new ResultVO();
        User user = manageUser.FindOne(account);
        if (user == null) {
            resultVO.setState(0);
            resultVO.setMsg("用户不存在");
            resultVO.setData(null);
        } else {
            user.setPassword(password);
            manageUser.UpdateUser(user);
            resultVO.setState(1);
            resultVO.setMsg("成功");
            resultVO.setData(null);
        }
        return resultVO;
    }

    @RequestMapping(value = "/verification", method = RequestMethod.GET)
    public ResultVO verification(@RequestParam("account") String account) {
        ResultVO resultVO = new ResultVO();
        String str = "01234567890qwertyuiopasdfghjklzxcvbnm";
        StringBuilder stringBuilder = new StringBuilder();
        int index;
        Random random = new Random();
        for (int i = 0; i < 6; ++i) {
            index = random.nextInt(36);
            stringBuilder.append(str.charAt(index));
        }
        User user = manageUser.FindOne(account);
        System.out.println(user);
        Context context = new Context();
        context.setVariable("token", stringBuilder.toString());
        context.setVariable("username", user.getUsername());
        String emailContent = templateEngine.process("email.html", context);
        if (mailService.sendSimpleMail(account, "主题：验证码", emailContent)) {
            resultVO.setState(1);
            resultVO.setMsg("成功");
            resultVO.setData(stringBuilder.toString());
        } else {
            resultVO.setState(0);
            resultVO.setMsg("失败");
            resultVO.setData(null);
        }
        return resultVO;
    }

    @RequestMapping(value = "/profile/get", method = RequestMethod.GET)
    public void getProfile(@RequestParam("account") String account, HttpServletResponse response) {
        User user = manageUser.FindOne(account);
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(user.getProfile());
            outputStream.flush();
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
    }
}
