<template>
  <div id="signup">
    <el-col :span="5" :offset="9" style="padding-top: 100px">
      <el-card shadow="hover">
        <el-form ref="form" v-model="info" size="mini" element-loading-text="拼命登录中"
                 element-loading-spinner="el-icon-loading"
                 element-loading-background="transparent" v-loading="loading">
          <el-form-item label="账号">
            <el-input v-model="info.account" placeholder="Please input your email as your account" required></el-input>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="info.username" placeholder="Please input your username" required></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="info.password" placeholder="Please input password" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="signUp" type="primary" style="width: 100%">注册</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
    <p>{{message}}</p>
  </div>
</template>

<script>
  let sha256=require("js-sha256").sha256;
  export default {
    name: "signup",
    data() {
      return {
        info: {
          account: "",
          password: "",
          username: "",
        },
        message: "",
        loading:false
      };
    },
    methods: {
      signUp() {
        let _self = this;
        _self.loading=true;
        let reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        if (!reg.test(_self.info.account)) {
          _self.loading=false
          _self.$message({type: 'error', message: "请输入有效邮箱"});
          _self.info.account = "";
        }
        else {
          let password=sha256(_self.info.password);
          _self.axios.post('/sign/up', _self.qs.stringify({
            account: _self.info.account,
            password: password,
            username: _self.info.username
          })).then(function (res) {
            if (res.data.state === 1) {
              _self.$message({type: "success", message: "注册成功"});
              _self.$router.push({name: "home"});
              window.sessionStorage.setItem("Token", JSON.stringify({
                Token: res.data.token
              }))
            }
            else {
              _self.$message({type: "warning", message: res.data.msg});
            }
            _self.loading=false
          })
            .catch(function () {
              _self.$message({type: "error", message: "网络错误，请重试"});
              _self.loading=false
            })
        }
      }
    }
  };
</script>
<style>
  #signup {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
