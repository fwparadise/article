<template>
  <div id="collect">
    <el-col :span="18" :offset="3">
      <el-card shadow="hover" style="height: 500px;overflow-y: auto" v-loading="loading" element-loading-text="拼命加载中"
               element-loading-spinner="el-icon-loading"
               element-loading-background="transparent">
        <div slot="header">我的收藏</div>
        <ul>
          <li v-for="article in articles" :key="articles.id" style="text-align: left">
            <router-link target="_blank" :to="{path:'/articleContent/'+article.articleId}" style="color: black">
              {{article.title}}
            </router-link>
          </li>
        </ul>
      </el-card>
    </el-col>
  </div>
</template>
<script>
  export default {
    name: "collect",
    data: function () {
      return {
        loading:false,
        account: '',
        articles: [],
        token: ""
      }
    },
    mounted: function () {
      let _self = this;
      _self.loading=true;
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
          _self.account = res.data.data.account;
        }).catch(function () {
          _self.$router.push("/");
        })
      }
      _self.axios.get('/collect/list', {
        headers: {
          "Authorization": _self.token
        }
      }).then(function (res) {
        _self.articles = res.data.data;
        _self.loading=false;
      }).catch(function () {
        _self.$router.push("/");
        _self.loading=false;
      })
    }
  }
</script>
<style>
  #collect {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
