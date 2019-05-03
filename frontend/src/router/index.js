import Vue from 'vue'
import VueRouter from 'vue-router'
import signin from "@/views/signin"
import signup from "@/views/signup"
import articleContent from "@/views/articleContent"
import home from "@/views/home"
import forget from "@/views/forget"
import created from "../components/created"
import collect from "../components/collect"
import compose from "../components/compose"
import articleList from "../components/articleList"
import info from "../components/info"
import chat from "../views/chat"
import NotFound from "@/views/NotFound"

Vue.use(VueRouter);

export default new VueRouter({
  base:"/article/",
  mode: "history",
  routes: [
    {
      path: '/',
      name: 'signin',
      component: signin
    },
    {
      path: "/home",
      name: "home",
      component: home,
    },
    {
      path:"/created",
      name:'created',
      component:created
    },
    {
      path:"/collect",
      name:'collect',
      component:collect
    },
    {
      path:"/compose",
      name:'compose',
      component:compose
    },
    {
      path:"/articleList",
      name:'articleList',
      component:articleList
    },
    {
      path:"/info",
      name:"info",
      component:info
    },
    {
      path:"/chat",
      name:"chat",
      component:chat
    },
    {
      path: "/signup",
      name: "signup",
      component: signup,

    },
    {
      path: "/forget",
      name: "forget",
      component: forget
    },
    {
      path: "/articleContent/:id",
      name: "articleContent",
      component: articleContent
    },
    {
      path: '*',
      component: NotFound,
      name: 'notfound',
      meta: {
        title: '404-页面弄丢了'
      }
    }
  ]
})
