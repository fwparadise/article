<template>
  <div id="forget">
    <el-col :span="6" :offset="9" style="padding-top: 100px">
      <el-card>
        <el-form size="mini">
          <el-form-item label="账号">
            <el-input v-model="account" placeholder="please input you account(it is you email)"/>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="sendtoken" style="width: 100%">发送</el-button>
          </el-form-item>
          <el-form-item label="验证码">
            <el-input v-model="token" placeholder="please input the token you have received"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="password" type="password" placeholder="please input new password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="update" type="primary" style="width: 100%">修改</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    name: "forget",
    data: function () {
      return {
        account: "",
        token: "",
        password: "",
        realtoken: "",
      }
    },
    methods: {
      sendtoken: function () {
        let _self = this;
        if (_self.account !== "") {
          _self.axios.get("/sign/verification?account=" + _self.account).then(function (res) {
            if (res.data.state === 1) {
              _self.realtoken = res.data.data;
            } else {
              _self.$message({type: "info", message: "发送失败，输入有效的邮箱"});
            }
          }).catch(function () {
            _self.$message({type: "warning", message: "网络出错"})
          })
        }
      },
      update: function () {
        let _self = this;
        _self.axios.patch("/sign/forget", _self.qs.stringify({
          account: _self.account,
          password: _self.password
        })).then(function (res) {
          if (res.data.state === 1) {
            _self.$message({type: "success", message: "修改成功"});
          } else {
            _self.$message({type: "error", message: "修改失败"});
          }
        }).catch(function () {
          _self.$message({type: "warning", message: "网络出错"});
        })
      }
    }
  }
</script>

<style>
  #forget {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
