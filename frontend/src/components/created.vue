<template>
  <div id="created">
    <el-col :span="18" :offset="3">
      <el-card>
        <div slot="header">我的文章</div>
        <el-table :data="articles" height="250">
          <el-table-column prop="title" label="文章"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button @click="$router.push('/articleContent/'+scope.row.articleId)" type="primary" size="mini">查看</el-button>
              <el-button type="danger" size="mini" @click="cancel(scope)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    name: "created",
    data: function () {
      return {
        account: '',
        articles: [],
        token: ""
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
          if (res.data.state === 1) {
            _self.account = res.data.data.account;
          }
        }).catch(function () {
          _self.$router.push("/")
        });
        _self.axios.get('/user/created/', {
          headers: {
            'Authorization': _self.token
          }
        }).then(function (res) {
          if (res.data.state === 1) {
            _self.articles = res.data.data;
          }
          else {
            _self.articles = []
          }
        }).catch(function () {
          _self.$message("网络出错")
        })
      }
    },
    methods:{
      cancel(scope){
        let _self=this;
        _self.axios.delete("/article/delete",{
          params:{
            id:scope.row.articleId
          },headers:{
            "Authorization":_self.token
          }
        }).then(function () {
          _self.articles.splice(scope.$index,1);
          _self.$message({type:"success",message:"删除成功"})
        }).catch(function () {
          _self.$router.push("/")
        })
      }
    }
  }
</script>

<style>
  #created {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
