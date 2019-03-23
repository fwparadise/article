<template>
  <div id="home">
    <el-container>
      <el-header height="30px" style="text-align: left">美文网</el-header>
      <el-main>
        <el-container>
          <el-aside width="200px">
            <el-card>
              <el-menu
                :default-active="$route.path" router>
                <el-menu-item index="articleList">
                  <i class="material-icons">list</i>
                  <span>文章列表</span>
                </el-menu-item>
                <el-menu-item index="info">
                  <i class="material-icons">person</i>
                  <span>个人信息</span>
                </el-menu-item>
                <el-menu-item index="compose">
                  <i class="material-icons">add_circle</i>
                  <span>发表文章</span>
                </el-menu-item>
                <el-menu-item index="created">
                  <i class="material-icons">create</i>
                  <span>我的创作</span>
                </el-menu-item>
                <el-menu-item index="collect">
                  <i class="material-icons">favorite</i>
                  <span>我的收藏</span>
                </el-menu-item>
                <el-menu-item index="chat">
                  <i class="material-icons">chat</i>
                  <span>聊天系统</span>
                </el-menu-item>
                <el-menu-item index="#" @click="signout">
                  <i class="material-icons">clear</i>
                  <span>退出登录</span>
                </el-menu-item>
              </el-menu>
            </el-card>
          </el-aside>
          <el-main>
            <keep-alive>
              <router-view></router-view>
            </keep-alive>
          </el-main>
        </el-container>
      </el-main>
    </el-container>

  </div>
</template>

<style scoped>
  #home {
    width: 100%;
  }

  .el-header {
    margin: 0;
  }

  .el-menu {
    padding: 0;
  }
</style>

<script>

  export default {
    name: "home",
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
      }
    }
  }
</script>
