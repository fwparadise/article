package com.example.paper.controller;

import com.example.paper.bean.SignUpForm;
import com.example.paper.converter.SignUpFormToUser;
import com.example.paper.entity.User;
import com.example.paper.service.ManageUser;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value = "/sign")
public class sign {
    private final ManageUser manageUser;
    private final RedisTemplate<String,String> redisTemplate;
    private final RabbitTemplate rabbitTemplate;
 //   private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    public sign(ManageUser manageUser, RedisTemplate<String, String> redisTemplate, RabbitTemplate rabbitTemplate) {
        this.manageUser = manageUser;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
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
           //     logger.info(user.getUsername()+" success to log in");
            } else {
                resultVO.setState(0);
                resultVO.setMsg("密码错误");
                resultVO.setData(null);
            }
        }
        return resultVO;
    }

    @RequestMapping(value = "/up", method = RequestMethod.POST)
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
        if (redisTemplate.opsForValue().get(String.format("token::%s",user.getAccount()))!=null){
            token = redisTemplate.opsForValue().get(String.format("token::%s",user.getAccount()));
        }
        else {
            token = JwtUtil.generateToken(user);
            redisTemplate.opsForValue().set(String.format("token::%s",user.getAccount()),token);
            redisTemplate.expire(String.format("token::%s",user.getAccount()),3600, TimeUnit.SECONDS);
        }
        return token;
    }

    @RequestMapping(value = "/forget", method = RequestMethod.PUT)
    public ResultVO forget(@RequestParam("account") String account, @RequestParam("password") String password,@RequestParam("verification") String verification) {
        ResultVO resultVO = new ResultVO();
        User user = manageUser.FindOne(account);
        String ver=redisTemplate.opsForValue().get(String.format("verification::%s",account));
        if (user == null) {
            resultVO.setState(0);
            resultVO.setMsg("用户不存在");
            resultVO.setData(null);
        }
        else if(ver==null){
            resultVO.setState(0);
            resultVO.setMsg("你还没有验证码");
            resultVO.setData(null);
        }
        else if (!ver.equals(verification)){
            resultVO.setState(0);
            resultVO.setMsg("验证码错误");
            resultVO.setData(null);
        }
        else {
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
        if (manageUser.FindOne(account)==null){
            ResultVO resultVO=new ResultVO();
            resultVO.setMsg("账号不存在");
            resultVO.setState(0);
            resultVO.setData(null);
            return resultVO;
        }
        else {
            rabbitTemplate.convertAndSend("mailExchange", "/mail", account);
            ResultVO resultVO = new ResultVO();
            resultVO.setMsg("");
            resultVO.setState(1);
            resultVO.setData(null);
            return resultVO;
        }
    }

    @RequestMapping(value = "/profile/get", method = RequestMethod.GET)
    public void getProfile(@RequestParam("account") String account, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        try {
            OutputStream outputStream = response.getOutputStream();
            if (account.equals("")){
                outputStream.write(new byte[0]);
            }
            else{
                User user = manageUser.FindOne(account);
                outputStream.write(user.getProfile());
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/userExist")
    public ResultVO exist(@RequestParam("account") String account){
        User user=manageUser.FindOne(account);
        ResultVO resultVO=new ResultVO();
        if (user==null){
            resultVO.setState(0);
        }
        else{
            resultVO.setState(1);
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("account",account);
            hashMap.put("username",user.getUsername());
            resultVO.setData(hashMap);
        }
        return resultVO;
    }
}
