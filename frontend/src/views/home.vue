<template>
  <div id="home">
    <el-container>
      <el-header height="100px">
        <el-container style="width: 30%;float: right;margin-top: 20px">
          <el-aside width="100px" style="position: relative;">
            <el-link :href="'http://47.106.156.233:8088/sign/profile/get?account='+user.account" target="_blank">
              <img :src="'http://47.106.156.233:8088/sign/profile/get?account='+user.account" style="width: 60px;height: 60px;border-radius: 50%;">

              </el-link>
          </el-aside>
          <el-main style="margin: auto 0;text-align: left">
            <router-link to="/chat"><i class="el-icon-message" style="font-size: 18px"></i></router-link>
            <el-dropdown @command="handlerJump">
              <span class="el-dropdown-link">
    {{user.username}}<i class="el-icon-arrow-down el-icon--right"></i>
  </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="/info">个人信息</el-dropdown-item>
                <el-dropdown-item command="/created">我的创作</el-dropdown-item>
                <el-dropdown-item command="/collect">我的收藏</el-dropdown-item>
                <el-dropdown-item command="/compose">新建文章</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <span @click="signout"><el-link type="info" :underline="true">退出登录</el-link></span>
          </el-main>
        </el-container>
      </el-header>
      <el-main>
        <article-list></article-list>
      </el-main>
    </el-container>

  </div>
</template>

<style scoped>
  #home {
    width: 100%;
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
    font-size: 18px;
  }

  .el-icon-arrow-down {
    font-size: 18px;
  }
</style>

<script>

  import ArticleList from "../components/articleList";

  export default {
    name: "home",
    components: {ArticleList},
    data() {
      return {
        user: {
          username: "",
          account: ""
        },
        token: ""
      };
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
          _self.user = res.data.data;
        }).catch(function () {
        })
      }
    },
    methods: {
      signout() {
        window.sessionStorage.removeItem('page');
        window.sessionStorage.removeItem("Token");
        // this.axios.get('/user/out');
        this.$router.push('/')
      },
      handlerJump(command) {
        this.$router.push(command)
      }
    }
  }
</script>
