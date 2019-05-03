<template>
  <div id="chat">
    <el-col :span="18" :offset="3">
      <el-card shadow="hover">
      <el-container>
        <el-aside width="250px" style="border-style:none;border-color: gray">
            <el-input v-model="userAccount" size="mini" placeholder="请输入想联系的用户的账号">
              <i slot="suffix" class="el-input__icon el-icon-search" @click="add"></i>
            </el-input>
            <el-table :data="users" @row-click="selectUser" style="height: 500px;overflow-y: auto" highlight-current-row empty-text="暂无用户">
              <el-table-column prop="username"></el-table-column>
            </el-table>
        </el-aside>
        <el-main v-if="currentUser!==''">
          <el-card shadow="hover">
            <div slot="header">{{currentUser}}</div>
            <div style="height: 300px;overflow-y:auto;">
              <div v-for="(message,index) in messages[currentUser]" :key="index" style="width: 60%"
                   :class="{me:message.from===from.account,other:message.from!==from.account}">
                <img :src="'http://47.106.156.233:8088/sign/profile/get?account='+message.from"
                     style="height: 50px;width: 50px;border-radius: 50%"
                     :class="{myProfile:message.from===from.account,otherProfile:message.from!==from.account}" alt="暂无头像"/>
                <el-input type="textarea" v-model="message.content" autosize disabled></el-input>
              </div>
            </div>
          </el-card>
              <el-form>
                <el-form-item>
                  <el-input v-model="content" style="width: 100%" type="textarea" autosize></el-input>
                </el-form-item>
                <el-form-item size="mini">
                  <el-button @click="send" type="primary" style="float: right">发送</el-button>
                </el-form-item>
              </el-form>
        </el-main>
      </el-container>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    name: "chat",
    //     props:["from","to"],
    data() {
      return {
        users: [],
        accounts: [],
        userAccount: "",
        messages: {},
        currentUser: "",
        content: "",
        from: {account: "", username: ""},
        token: "",
        chatClient: null
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
          _self.from.account = res.data.data.account;
          _self.from.username = res.data.data.username;
          _self.chatClient = new WebSocket("ws://47.106.156.233:8088/websocket/" + _self.from.account);
          _self.chatClient.onmessage = function (msg) {
            let mess = JSON.parse(msg.data);
            if (_self.accounts.indexOf(mess.from) === -1) {
              _self.axios.get("/sign/userExist?account=" + mess.from).then(function (res) {
                if (res.data.state === 1) {
                  _self.users.unshift({account: mess.from, username: res.data.data.username});
                  _self.$set(_self.messages, mess.from, []);
                  _self.messages[mess.from].push(mess);
                }
              })
            } else {
              _self.messages[mess.from].push(mess);
            }
            _self.currentUser = mess.from;
          };
        }).catch(function () {
        })
      }
    },
    beforeDestroy() {
      this.chatClient.close();
    },
    methods: {
      selectUser(row) {
        this.currentUser = row.account;
      },
      add() {
        let _self = this;
        _self.axios.get("/sign/userExist?account=" + _self.userAccount).then(function (res) {
          if (res.data.state === 1) {
            _self.users.push({account: res.data.data.account, username: res.data.data.username});
            _self.accounts.push(_self.userAccount);
            _self.userAccount = ""
          } else {
            _self.$message({type: "info", message: "用户不存在"})
          }
        }).catch(function () {
          _self.$message({type: "warning", message: "网络出错"})
        });
      },
      send() {
        let _self = this;
        _self.chatClient.send(JSON.stringify({
          from: _self.from.account,
          to: _self.currentUser,
          content: _self.content
        }));
        if (Object.keys(_self.messages).indexOf(_self.currentUser) === -1) {
          _self.$set(_self.messages, _self.currentUser, []);
        }
        _self.messages[_self.currentUser].push({
          from: _self.from.account,
          to: _self.currentUser,
          content: _self.content
        });
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
    margin-top: 60px;
    color: #2c3e50;
  }
  el-input{
    border-radius: 2em;
  }

  .me {
    float: right;
    margin: 10px;
  }

  .other {
    float: left;
    margin: 10px;
  }

  .myProfile {
    float: right;
  }

  .otherProfile {
    float: left;
  }

  .myMessage {
    position: relative;
    right: 50px;
    text-align: right;
  }

  .otherMessage {
    position: relative;
    left: 50px;
    text-align: left;
  }
</style>
