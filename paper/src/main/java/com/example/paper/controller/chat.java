/**
 * @author 2018年5月18日16:11:48
 */
package com.example.paper.controller;

import com.example.paper.entity.Message;
import com.example.paper.service.ManageMessage;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Component
@ServerEndpoint("/websocket/{account}")
public class chat {
   // private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static ManageMessage manageMessage;
    /**
     * 在线人数
     */
    private static int onlineNumber = 0;
    /**
     * 以用户的account为key，WebSocket为对象保存起来
     */
    private static Map<String, chat> clients = new ConcurrentHashMap<>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String account;


    /**
     * 建立连接
     *
     * @param session 会话
     */
    @OnOpen
    public void onOpen(@PathParam("account") String account, Session session)
    {
        try {
            onlineNumber++;
          //  logger.info("现在来连接的客户id：" + session.getId() + "账号：" + account);
            this.account = account;
            this.session = session;
           // logger.info("有新连接加入！ 当前在线人数" + onlineNumber);

            //把自己的信息加入到map当中
            clients.put(account, this);
            List<Message> messageList = manageMessage.getMessages(account);
            if (messageList.size() > 0) {
                for (Message m :
                        messageList) {
                    onMessage(m.getContent(), session);
                }
                manageMessage.deleteMessages(account);
            }
         //   logger.info("已发送离线消息");
        }
        catch (Exception e){
          //  logger.error("error"+e.getMessage());
        }

    }

    @OnError
    public void onError(Throwable error) {
     //   logger.info("服务端发生了错误"+error.getMessage());
        //error.printStackTrace();
    }
    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;
        //webSockets.remove(this);
        clients.remove(this.account);
      //  logger.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        try {
         //   logger.info("来自客户端消息：" + message+"客户端的id是："+session.getId());
            JSONObject jsonObject = new JSONObject(message);
            String to = jsonObject.getString("to");
            boolean flag=false;
            for (chat item : clients.values()) {
                if (item.account.equals(to) ) {
                    item.session.getAsyncRemote().sendText(message);
                 //   logger.info("成功发送到"+to);
                    flag=true;
                    break;
                }
            }
            if (!flag){
                Message message1=new Message();
                message1.setContent(message);
                message1.setReceiver(to);
                manageMessage.add(message1);
            }
        }
        catch (Exception e){
        //    logger.info("发生了错误了");
        }
    }
}

