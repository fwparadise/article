<template>
  <div id="chat">
    <el-container>
      <el-aside width="350px">
        <el-card>
          <el-form inline style="text-align: left">
            <el-form-item size="mini">
              <el-input v-model="user"></el-input>
            </el-form-item>
            <el-form-item size="mini">
              <el-button type="primary" @click="add">添加</el-button>
            </el-form-item>
            <el-form-item size="mini">
              <el-select v-model="currentUser">
                <el-option v-for="(user,index) in users" :key="index" :label="user" :value="user"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </el-aside>
      <el-main v-if="currentUser!==''">
        <el-card >
          <div slot="header">{{currentUser}}</div>
          <div style="max-height: 350px;overflow: scroll;overflow-x: hidden">
          <div v-for="(message,index) in messages[currentUser]" :key="index" style="width: 60%"
               :class="{me:message.from===from,other:message.from!==from}">
            <img :src="'http://localhost:8080/sign/profile/get?account='+message.from" style="height: 50px;width: 50px;border-radius: 50%" :class="{myProfile:message.from===from,otherProfile:message.from!==from}" alt="暂无头像"/>
            <div style="font-size: 20px;border-radius: 5px;background-color: #409EFF;" :class="{myMessage:message.from===from,otherMessage:message.from!==from}">{{message.content}}</div>
          </div>
          </div>
        </el-card>
        <el-card>
          <div>
            <el-form>
              <el-form-item>
                <el-input v-model="content" style="width: 100%"></el-input>
              </el-form-item>
              <el-form-item size="mini">
                <el-button @click="send" type="primary" style="float: right">发送</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: "chat",
    //     props:["from","to"],
    data() {
      return {
        users: [],
        user: "",
        messages: {},
        currentUser: "",
        content: "",
        from: "",
        token: "",
        chatClient:null
      }
    },
    mounted() {
      let _self = this;
      _self.token = JSON.parse(window.sessionStorage.getItem("Token"));
      if (_self.token === null) {
        _self.$router.push('/')
      } else {
        _self.token = _self.token.Token;
        _self.axios.get("/user/info", {
          headers: {
            "Authorization": _self.token
          }
        }).then(function (res) {
          _self.from = res.data.data.account;
          _self.chatClient = new WebSocket("ws://localhost:8080/websocket/" + _self.from);
          _self.chatClient.onmessage = function (msg) {
            let mess = JSON.parse(msg.data);
            if (_self.users.indexOf(mess.from) === -1) {
              _self.users.unshift(mess.from);
              _self.$set(_self.messages, mess.from, []);
            }
            _self.messages[mess.from].push(mess);
            _self.currentUser = mess.from;
          };
        }).catch(function () {
        })
      }
    },
    beforeDestroy(){
      this.chatClient.close();
    },
    methods: {
      add() {
        this.users.push(this.user);
        this.user = "";
      },
      send() {
        let _self = this;
        _self.chatClient.send(JSON.stringify({from: _self.from, to: _self.currentUser, content: _self.content}));
        if (Object.keys(_self.messages).indexOf(_self.currentUser) === -1) {
          _self.$set(_self.messages, _self.currentUser, []);
        }
        _self.messages[_self.currentUser].push({from: _self.from, to: _self.currentUser, content: _self.content});
        _self.content = ""
      }
    }
  }
</script>

<style scoped>
  #chat {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
  .me{
    float: right;
    margin: 10px;
  }
  .other{
    float: left;
    margin: 10px;
  }
  .myProfile {
    float: right;
  }
  .otherProfile {
    float: left;
  }
  .myMessage{
    position: relative;
    right: 50px;
    text-align: right;
  }
  .otherMessage{
    position: relative;
    left: 50px;
    text-align: left;
  }
</style>
