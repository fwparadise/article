<template>
  <div id="compose">
    <el-col :offset="2" :span="16">
      <el-card style="height: 550px">
        <el-form ref="form" inline style="text-align: left">
          <el-form-item label="标题" size="small">
            <el-input v-model="title" placeholder="请输入标题"></el-input>
          </el-form-item>
          <el-form-item label="类型" size="small">
            <el-select v-model="kind" value="" style="width: 90px">
              <el-option v-for="ikind in kinds" :label="ikind" :key="ikind.id" :value="ikind"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item size="small">
            <el-button type="primary" @click="submit">提交</el-button>
          </el-form-item>
          <el-form-item label="正文">
            <quill-editor id="editor"
              v-model="content"
              ref="myQuillEditor" style="height: 250px;" :options="editorOption">
            </quill-editor>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>

  </div>
</template>

<script>
  export default {
    name: "compose",
    data() {
      return {
        account: this.number,
        title: "",
        content: "",
        kinds: ["科技", "故事", "环境", "社交", "散文"],
        kind: "科技",
        token: "",
        articleId: "",
        editorOption: {
          modules: {
            toolbar: [
              ['bold', 'italic', 'underline', 'strike'],
              ['blockquote', 'code-block'],
              [{'header': 1}, {'header': 2}],
              [{'list': 'ordered'}, {'list': 'bullet'}],
              [{'script': 'sub'}, {'script': 'super'}],
              [{'indent': '-1'}, {'indent': '+1'}],
              [{'direction': 'rtl'}],
              [{'size': ['small', false, 'large', 'huge']}],
              [{'header': [1, 2, 3, 4, 5, 6, false]}],
              [{'font': []}],
              [{'color': []}, {'background': []}],
              [{'align': []}],
              ['clean'],
              ['link', 'image', 'video']]
            // ],
            // syntax: {
            //   highlight: text => hljs.highlightAuto(text).value
            // }
          }
        }
      }
    },
    mounted() {
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
          _self.account = res.data.data.account;
        }).catch(function () {
          _self.$router.push("/")
        })
      }
      if (_self.articleId!==""){
        _self.axios.get("/article/detail?id="+_self.articleId,{
          headers:{
            "Authorization":_self.token
          }
        }).then(function (res) {
          if (res.data.state===1){
            _self.title=res.data.data.article.title;
            _self.kind=res.data.data.article.kind;
            _self.content=res.data.data.article.content;
          }
          else{
            _self.$message({type:"warning",message:res.data.msg});
          }
        }).catch(function () {
          _self.$router.push('/')
        })
      }
    },
    methods: {
      submit: function () {
        let _self = this;
        if (_self.articleId==="") {
          this.axios.put('/article/add', _self.qs.stringify({
              title: _self.title,
              content: _self.content,
              kind: _self.kind
            }), {
              headers: {
                "Authorization": _self.token
              }
            }
          ).then(function (res) {
            if (res.data.state === 1) {
              _self.$confirm("是否要继续撰写？", "发表成功", {
                confirmButtonText: "继续撰写",
                cancelButtonText: "回到主页",
                type: "warning"
              }).then(() => {
                _self.title = "";
                _self.content = "";
                _self.kind = "科技"
              }).catch(() => {
                _self.$router.push({name: "home"})
              })
            }
            else {
              _self.$message("发表失败")
            }
          }).catch(function () {
            _self.$router.push("/")
          })
        }
        else{
          _self.axios.patch("/article/update",_self.qs.stringify({
            articleId:_self.articleId,
            title:_self.title,
            kind:_self.kind,
            content:_self.content
          }),{
            headers:{
              "Authorization":_self.token
            }
          }).then(function (res) {
            _self.$message({type:"success",message:"修改成功"})
          }).catch(function () {
            _self.$router.push("/")
          })
        }
      },
    },
  };
</script>
<style>
  #compose {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
