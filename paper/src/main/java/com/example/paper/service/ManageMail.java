package com.example.paper.service;

import com.example.paper.entity.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ManageMail {
    private final ManageUser manageUser;
    private final JavaMailSenderImpl mailSenderImpl;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    public ManageMail(JavaMailSenderImpl mailSenderImpl, ManageUser manageUser, RedisTemplate<String, String> redisTemplate) {
        this.mailSenderImpl = mailSenderImpl;
        this.manageUser = manageUser;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = "mailQueue")
    public void send(String account) {
        System.out.println("开始发送邮件");
        String str = "01234567890qwertyuiopasdfghjklzxcvbnm";
        StringBuilder stringBuilder = new StringBuilder();
        int index;
        Random random = new Random();
        for (int i = 0; i < 6; ++i) {
            index = random.nextInt(36);
            stringBuilder.append(str.charAt(index));
        }
        User user = manageUser.FindOne(account);
        MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject("验证码");
            mimeMessageHelper.setText(String.format("亲爱的%s:你的验证码为：%s(180s内有效)", user.getUsername(), stringBuilder.toString()));
            mimeMessageHelper.setTo(account);
            mimeMessageHelper.setFrom(from);
            mailSenderImpl.send(mimeMessage);
            redisTemplate.opsForValue().set(String.format("verification::%s", account), stringBuilder.toString());
            redisTemplate.expire(String.format("verification::%s", account), 180, TimeUnit.SECONDS);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
