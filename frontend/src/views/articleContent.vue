<template>
  <div id="articleContent">
    <el-col :span="18" :offset="3">
      <el-card shadow="hover">
        <div slot="header">
          <h6 style="text-align: left">
            <router-link to="/home">回到主页</router-link>
          </h6>
          <h1>{{article.title}}</h1>
          <h6 style="text-align: right">by {{article.author}} on {{article.time}}</h6>
        </div>
        <div>
          <p v-html="article.content" style="text-align: left;line-height: 30px"></p>
          <el-divider></el-divider>
          <el-row>
            <el-col :offset="3" :span="3">
              <el-rate v-model="article.star" :disabled="score" @change="mark" style="width: 140px;height: 28px;"
                       show-score></el-rate>
            </el-col>
            <el-col :offset="10" :span="1">
              <el-link :underline="false">
                <i v-if="!collect" class="el-icon-star-off" @click="like" style="font-size: 28px"></i>
                <i v-else class="el-icon-star-on" @click="cancel" style="color: darkred;font-size: 28px"></i>
              </el-link>
            </el-col>
            <el-col :offset="1" :span="1">
              <el-link :underline="false">
                <i class="el-icon-chat-dot-round" style="font-size: 28px" @click="$refs.comment.focus()"></i>
              </el-link>
            </el-col>
          </el-row>
          <p v-for="comment in article.comments" style="text-align: left;font-size: 12px">
            <b>{{comment.username}}</b>:{{comment.content}}
          </p>
          <el-form size="mini">
            <el-form-item>
              <el-input ref="comment" placeholder="请输入你的评论" v-model="comment" type="textarea"
                        autosize @focus="show=true" @blur="show=(comment!=='')"></el-input>
            </el-form-item>
            <el-form-item v-show="show">
              <el-button @click="make" type="primary" style="float: right" size="mini">发表</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-col>
  </div>
</template>
<script>
  export default {
    name: 'articleContent',
    data() {
      return {
        show: false,
        article: {
          articleid: 0, //文章id
          title: "",
          content: "",
          author: "",   //作者用户名
          time: "",
          star: 0,      //星数
          comments: [],   //评论列表
        },
        account: "",     //用户账号

        collect: false,    //是否被收藏
        comment: "",   //填写的评论内容
        score: false,
        token: ""
      }
    },
    computed: {
      minRows() {
        if (this.show){
          return 3;
        }
        else{
          return 1;
        }
      }
    },
    created() {
      let _self = this;
      _self.articleid = _self.$route.params.id;
      _self.token = JSON.parse(window.sessionStorage.getItem("Token"));
      if (_self.token === null) {
        _self.loading = false;
        _self.$router.push("/")
      } else {
        _self.token = _self.token.Token;
        _self.axios.get("/user/info", {
          headers: {
            "Authorization": _self.token
          }
        }).then(function (res) {
          _self.account = res.data.data.account;
        }).catch(function () {
          _self.$router.push("/")
        });
        _self.getscore(_self);
        _self.axios.get('/collect/exist?articleId=' + _self.articleid, {
          headers: {
            "Authorization": _self.token
          }
        }) //查询该文章是否被收藏
          .then(function (res) {
            _self.collect = res.data.data;
          })
          .catch(function () {
            _self.$router.push("/")
          });
      }
      _self.getstar(_self);    //获取评分
      _self.getcomment(_self);   //获取评论
      _self.axios.get('/article/detail?id=' + _self.articleid, {
        headers: {
          "Authorization": _self.token
        }
      })  //获取正文
        .then(function (res) {
          _self.article.title = res.data.data.article.title;
          _self.article.content = res.data.data.article.content;
          _self.article.author = res.data.data.author;
          _self.article.time = res.data.data.article.createTime;
        })
        .catch(function () {
          _self.$router.push("/")
        });
    },
    methods: {
      getscore: function (_self) {
        _self.axios.get('/star/exist?articleId=' + _self.articleid, {
          headers: {
            "Authorization": _self.token
          }
        }).then(function (res) {
          _self.score = res.data.data;
        }).catch(function () {
          _self.$router.push("/")
        });
      },
      getstar: function (_self) {
        _self.axios.get('/star/getstar?articleId=' + _self.articleid, {
          headers: {
            "Authorization": _self.token
          }
        })
          .then(function (res) {
            _self.article.star = res.data.data;
          })
          .catch(function () {
            _self.$router.push("/")
          });
      },
      getcomment: function (_self) {
        _self.axios.get('/comment/list?articleId=' + _self.articleid, {
          headers: {
            "Authorization": _self.token
          }
        })
          .then(function (res) {
            _self.article.comments = res.data.data;
          })
          .catch(function () {
            _self.$router.push("/")
          });
      },
      make: function () {
        let _self = this;
        _self.show = false;
        if (_self.comment === "") {
          _self.$alert("请输入评论内容")
        } else {
          this.axios.post('/comment/add', _self.qs.stringify({
            articleId: _self.articleid,
            content: _self.comment
          }), {
            headers: {
              "Authorization": _self.token
            }
          })
            .then(function (res) {
              if (res.data.state === 1) {
                _self.$message({type: "success", message: "评论成功"});
                _self.comment = "";
                _self.getcomment(_self);
              } else {
                _self.$message("评论失败")
              }
            })
            .catch(function () {
              _self.$router.push("/")
            })
        }
      },   //评论
      mark: function () {
        let _self = this;
        this.axios.post('/star/add', _self.qs.stringify({
          articleId: _self.articleid,
          score: _self.article.star
        }), {
          headers: {
            "Authorization": _self.token
          }
        })
          .then(function (res) {
            if (res.data.state === 1) {
              _self.$message({type: "success", message: "打分成功"});
              _self.score = true;
              _self.getstar(_self);
            } else {
              _self.$message("打分失败")
            }
          })
          .catch(function () {
            _self.$router.push("/")
          })
      },   //打分
      like: function () {
        let _self = this;
        _self.axios.post("/collect/add", _self.qs.stringify({
          articleId: _self.articleid,
        }), {
          headers: {
            "Authorization": _self.token
          }
        }).then(function (res) {
          if (res.data.state === 1) {
            _self.collect = true;
          } else {
            _self.$message({type: "warning", message: "收藏失败"})
          }
        }).catch(function () {
          _self.$router.push("/")
        })
      },   //收藏
      cancel: function () {
        let _self = this;
        _self.axios.delete("/collect/cancel", {
          params: {
            articleId: _self.articleid,
          }, headers: {
            "Authorization": _self.token
          }
        }).then(function (res) {
          if (res.data.state === 1) {
            _self.collect = false;
          } else {
            _self.$message({type: "warning", message: "取消失败"})
          }
        }).catch(function () {
          _self.$message(({type: "warning", message: "取消失败"}))
        })
      }   //取消收藏
    }
  }
</script>

<style>
  #articleContent {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }

  a:link {
    color: #666666;
    text-decoration: none;
  }

  a:hover {
    color: green;
    text-decoration: underline;
  }
</style>
