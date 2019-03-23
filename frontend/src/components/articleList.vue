<template>
  <div id="articleList">
    <el-col :span="18" :offset="3">
      <el-card>
        <el-tabs v-model="page.activeKind" stretch>
          <el-tab-pane v-for="kind in article_kinds" :label="kind" :name="kind" :key="kind.id">
          </el-tab-pane>
        </el-tabs>
        <el-table :data="articleList" stripe @row-click="detail" style="height: 400px">
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="author" label="作者"></el-table-column>
          <el-table-column prop="createTime" label="创作时间"></el-table-column>
        </el-table>
        <el-pagination :page-size="page.pagesize" :total="page.count" :current-page.sync="page.currentpage"
                       layout="total,prev,pager,next"></el-pagination>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    name: "articleList",
    data() {
      return {
        token: "",
        article_kinds: ["全部", "科技", "环境", "故事", "社交", "散文"],
        articleList: [],
        page: {
          count: 0,
          pagesize: 7,
          currentpage: 1,
          activeKind: "全部"
        }
      }
    },
    mounted: function () {
      this.token = JSON.parse(window.sessionStorage.getItem("Token"));
      if (this.token === null) {
        this.$router.push({name: 'signin'})
      }
      else {
        this.token = this.token.Token;
        let page = JSON.parse(window.sessionStorage.getItem("page"));
        if (page === null) {
          this.page.activeKind = "全部";
          this.page.currentpage = 1;
        }
        else {
          this.page.activeKind = page.activeKind;
          this.page.currentpage = page.currentpage
        }
        this.getList()
      }
    },
    watch: {
      page: {
        handler: function () {
          this.getList()
        },
        deep: true,
      }
    },
    methods: {
      detail: function (row) {
        this.jump();
        this.$router.push({path: "/articleContent/" + row.articleId})
      },
      jump: function () {
        window.sessionStorage.setItem("page", JSON.stringify(this.page))
      },
      getList() {
        let _self = this;
        _self.axios.get("/article/list", {
          params: {
            kind: _self.page.activeKind,
            current: _self.page.currentpage,
            size: _self.page.pagesize
          },
          headers: {
            "Authorization": _self.token
          }
        }).then((res) => {
          if (res.data.state === 1) {
            _self.articleList = res.data.data.list;
            _self.page.count = res.data.data.total;
          }
          else {
            _self.$message("服务器宕机了")
          }
        }).catch(() => {
          this.$router.push({name: 'signin'})
        })
      }
    },
  };
</script>
<style>
  #articleList {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
