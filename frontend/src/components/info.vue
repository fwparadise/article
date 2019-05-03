<template>
  <div id="info">
    <el-col :span="12" :offset="6">
      <el-card shadow="hover">
        <el-col :span="12" :offset="6">
        <el-form ref="form" size="small">
          <el-form-item label="头像">
            <el-upload
              class="upload-demo" :show-file-list="false"
              action="http://47.106.156.233:8088/user/profile/update"
            accept="image/jpeg,image/gif,image/png" :headers="header" name="profile" :on-success="fresh">
              <img :src="profile" style="height: 100px;width: 100px;display: inline-block" alt="暂无头像"/>
              <el-button size="text" type="primary">点击更改</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="账号">
            <el-input v-model="info.account" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="info.username"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="info.gender">
              <el-radio :label="0"><i class="el-icon-male"></i> </el-radio>
              <el-radio :label="1"><i class="el-icon-female"></i></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="个人说明">
            <el-input v-model="info.description"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="save" type="primary" style="width: 100%">保存</el-button>
            <span style="float: right"><router-link to="/forget">修改密码</router-link></span>
          </el-form-item>
        </el-form>
        </el-col>
      </el-card>
    </el-col>
  </div>
</template>

<script>

  export default {
    name: 'info',
    data() {
      return {
        info: {
          account: "",
          username: "",
          gender: 1,
          description: "",
        },
        uploadedProfile: "",
        token: ""
      }
    },
    computed:{
      header(){
        return{
          "Authorization":this.token,
          // 'Content-Type':'multipart/form-data'
        }
      },
      profile(){
        return "http://47.106.156.233:8088/sign/profile/get?account="+this.info.account;
      }
    },
    mounted: function () {
      let _self = this;
      _self.token = JSON.parse(window.sessionStorage.getItem("Token"));
      if (_self.token === null) {
        _self.$router.push('/')
      }
      else {
        _self.token = _self.token.Token;
        _self.axios.get("/user/info", {
          headers: {
            "Authorization": _self.token
          }
        }).then(function (res) {
          _self.info.account = res.data.data.account;
          _self.info.username = res.data.data.username;
          _self.info.gender = res.data.data.gender;
          _self.info.description = res.data.data.description;
        }).catch(function () {
          _self.$router.push("/")
        })
      }
    },
    methods: {
      save: function () {
        let _self = this;
        _self.axios.put('/user/update',_self.qs.stringify({
            gender: _self.info.genData,
            username: _self.info.username,
            description: _self.info.username
          }), {
            headers: {
              "Authorization": _self.token,
            }
          }
        )
          .then(function (res) {
            if (res.data.state === 1) {
              _self.$message({type: "success", message: "修改成功"});
            }
            else {
              _self.$message({type: "error", message: "修改失败"});
            }
          })
          .catch(function () {
            _self.$router.push("/")
          });
      },
      fresh:function () {
        window.location.reload()
      }
    }

  }
</script>

<style>
  #info {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
  a:link{
    color: green;
    text-decoration: none;
  }
  a:hover{
    color: red;
    text-decoration: underline;
  }
</style>
