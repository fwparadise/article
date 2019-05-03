//package com.example.paper.listener;
//
//import com.example.paper.entity.User;
//import com.example.paper.service.ManageUser;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.util.Random;
//
//@Component
//@RabbitListener(queues = "mailQueue")
//public class mailListener {
//    private final ManageUser manageUser;
//    private final JavaMailSenderImpl mailSenderImpl;
//    @Value("${mail.fromMail.addr}")
//    private String from;
//
//    @Autowired
//    public mailListener(JavaMailSenderImpl mailSenderImpl, ManageUser manageUser) {
//        this.mailSenderImpl = mailSenderImpl;
//        this.manageUser = manageUser;
//    }
//
//    @RabbitHandler
//    public void send(String account){
//        System.out.println("开始发送邮件");
//        String str = "01234567890qwertyuiopasdfghjklzxcvbnm";
//        StringBuilder stringBuilder = new StringBuilder();
//        int index;
//        Random random = new Random();
//        for (int i = 0; i < 6; ++i) {
//            index = random.nextInt(36);
//            stringBuilder.append(str.charAt(index));
//        }
//        User user = manageUser.FindOne(account);
//        MimeMessage mimeMessage=mailSenderImpl.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper= null;
//        try {
//            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
//            mimeMessageHelper.setSubject("验证码");
//            mimeMessageHelper.setText(String.format("你的验证码为：%s",stringBuilder.toString()));
//            mimeMessageHelper.setTo(account);
//            mimeMessageHelper.setFrom(from);
//            mailSenderImpl.send(mimeMessage);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
