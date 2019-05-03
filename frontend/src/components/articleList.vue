<template>
  <div id="articleList">
    <el-col :span="18" :offset="3">
      <el-card shadow="hover">
        <el-table :data="articleList" stripe @row-click="detail" style="height: 400px"
                  :default-sort="{prop:'createTime',order:'descending'}" v-loading="loading" element-loading-text="拼命加载中"
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="transparent"
                  empty-text="暂无文章"
        >
          <el-table-column prop="title" label="标题" sortable></el-table-column>
          <el-table-column prop="author" label="作者" align="center"></el-table-column>
          <el-table-column prop="createTime" label="创作时间" sortable align="center"></el-table-column>
          <el-table-column align="center">
            <template slot="header" slot-scope="scope">
              <el-select v-model="page.activeKind" value="" style="width: 115px" size="mini">
                <el-option v-for="kind in article_kinds" :label="kind" :value="kind" :key="kind.id"></el-option>
              </el-select>
            </template>
            <template slot-scope="scope">
              <el-tag :type="tag_class[article_kinds.indexOf(scope.row.kind)-1]" :hit="true" size="medium">
                {{scope.row.kind}}
              </el-tag>
            </template>
          </el-table-column>
          <!--<el-table-column prop="kind" label="类型" :filters="[{text:'全部',value:'全部'},{text:'科技',value:'科技'},{text:'环境',value:'环境'},{text:'社交',value:'社交'},{text:'故事',value:'故事'},{text:'散文',value:'散文'}]" :filter-method="filterHandler"></el-table-column>-->
        </el-table>
        <el-pagination :page-size="page.pagesize" :total="page.count" :current-page.sync="page.currentpage"
                       layout="total,prev,pager,next,jumper"></el-pagination>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    name: "articleList",
    data() {
      return {
        loading:false,
        token: "",
        article_kinds: ["全部", "科技", "环境", "故事", "社交", "散文"],
        articleList: [],
        page: {
          count: 0,
          pagesize: 6,
          currentpage: 1,
          activeKind: "全部"
        },
        tag_class:["primary","success","warning","danger","info"],
      }
    },
    mounted: function () {
      this.loading=true;
      this.token = JSON.parse(window.sessionStorage.getItem("Token"));
      if (this.token === null) {
        this.loading=false;
        this.$router.push({name: 'signin'})
      } else {
        this.token = this.token.Token;
        let page = JSON.parse(window.sessionStorage.getItem("page"));
        if (page === null) {
          this.page.activeKind = "全部";
          this.page.currentpage = 1;
        } else {
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
          } else {
            _self.$message("服务器宕机了")
          }
          _self.loading=false;
        }).catch(() => {
          _self.loading=false;
          _self.$router.push({name: 'signin'})
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
