import Vue from 'vue'
import App from './App.vue'
import router from '@/router/index'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import Qs from 'qs'
import  VueQuillEditor from 'vue-quill-editor'
// require styles 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.use(VueQuillEditor);
// import sha256 from "js-sha256"
// Vue.use(sha256);
Vue.use(ElementUI);
Vue.prototype.axios = axios;
Vue.prototype.qs = Qs;
axios.defaults.baseURL = 'http://localhost:8080';
// axios.defaults.timeout = 15000;  //超时响应
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.withCredentials = true;
Vue.config.productionTip = false;

new Vue({
  render: h => h(App),
  router
}).$mount('#app');
