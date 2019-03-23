<template>
  <div id="signin">
    <el-col :span="6" :offset="9" style="padding-top: 100px">
      <el-card>
        <!--<h1>美文网</h1>-->
        <el-form ref="form" v-model="info" size="small">
          <el-form-item label="账号">
            <el-input v-model="info.account" placeholder="Please input account"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input type="password" v-model="info.password" placeholder="Please input password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="signIn" type="primary" style="width: 100%">登录</el-button>
            <span style="float: left"><router-link to="signup" class="router-link">注册新账号</router-link></span>
            <span style="float:right;"><router-link to="forget" class="router-link">找回密码</router-link></span>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  let sha256 = require("js-sha256").sha256;
  export default {
    name: "signin",
    data() {
      return {
        info: {
          account: "",
          password: ""
        }
      };
    },
    methods: {
      signIn() {
        let _self = this;
        let password=sha256(_self.info.password);
        _self.axios.post('/sign/in', _self.qs.stringify({account: _self.info.account, password:password}))
          .then(function (res) {
            if (res.data.state === 1) {
              _self.$message({type: "success", message: "登录成功"});
              window.sessionStorage.setItem("Token", JSON.stringify({
                Token: res.data.token
              }));
              _self.$router.push({name: "home"});
            }
            else {
              _self.$message({type: "error", message: res.data.msg});
              _self.info.password = ""
            }
          })
          .catch(function () {
            _self.$message({type: "warning", message: "网络错误，请重试"})
          })
      }

    }
  };
</script>
<style>
  #signin {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }

  .router-link {
    color: #303133;
  }
</style>
