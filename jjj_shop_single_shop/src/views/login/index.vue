<template>
  <div class="login-bg" :style="'background-image:url(' + bgimg_url + ');'">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
      <h3 class="title" style="margin-bottom: 20px;">{{shop_name}}</h3>
      <!--用户名-->
      <el-form-item prop="account"><el-input type="text" v-model="ruleForm.account" auto-complete="off" placeholder="账号"></el-input></el-form-item>
      <!--密码-->
      <el-form-item prop="checkPass"><el-input type="password" v-model="ruleForm.checkPass" auto-complete="off" placeholder="密码"></el-input></el-form-item>
      <!--登录-->
      <el-form-item><el-button type="primary" style="width:100%;" @click.native.prevent="SubmitFunc" :loading="logining">登录</el-button></el-form-item>
    </el-form>
  </div>
</template>

<script>
import IndexApi from '@/api/index.js';
import bgimg from '@/assets/img/login_bg.jpg';
import UserApi from '@/api/user.js';
import { useUserStore } from '@/store';
const { afterLogin } = useUserStore();

export default {
  data() {
    return {
      /*是否正在加载*/
      loading:true,
      /*背景图片*/
      bgimg_url: '',
      /*是否正在提交*/
      logining: false,
      /*表单对象*/
      ruleForm: {
        /*用户名*/
        account: 'admin',
        /*密码*/
        checkPass: '123456'
      },
      /*验证规则*/
      rules: {
        /*用户名*/
        account: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        /*密码*/
        checkPass: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      /*基础配置*/
      baseData: {}
    };
  },
  created() {
    this.getData();
    if(this.$route.query.from&&this.$route.query.from=='admin'){
      this.saasLogin();
    }
  },
  methods: {
    /*获取基础配置*/
    getData() {
      let self = this;
      IndexApi.base(true)
        .then(res => {
          self.loading = false;
          const { data } = res;
          self.shop_name = data.shopName;
          if(data.shopBgImg != ''){
            self.bgimg_url = data.shopBgImg;
          }else{
            self.bgimg_url = bgimg;
          }
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*登录方法*/
    SubmitFunc(ev) {
      var _this = this;
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          this.logining = true;
          var Params = {
            username: this.ruleForm.account,
            password: this.ruleForm.checkPass
          };
          /*调用登录接口*/
          UserApi.login(Params, true)
            .then(async (res) => {
              this.logining = false;
              if (res.code == 1) {
                await afterLogin(res);
                this.logining = false; 
                this.$router.push({
                  path: '/home'
                })
              } else {
                ElMessage({
                  message: '登录失败',
                  type: 'error'
                });
              }
            })
            .catch(error => {
              this.logining = false;
            });
        }
      });
    },
    saasLogin(){
      /*调用登录接口*/
      UserApi.saasLogin({}, true)
        .then(res => {
          console.log(res);
          if (res.code == 1) {
            delCookie('baseInfo');
            /*保存个人信息*/
            setCookie('userinfo', res.data);
            /*设置一个登录状态*/
            setCookie('isLogin', true);
            /*跳转到首页*/
            this.$router.push({ path: '/' });
          } else {
            ElMessage({
              message: '登录失败',
              type: 'error'
            });
          }
        }).catch(error => {
          console.log(error);
        });
    },
  }
};
</script>

<style lang="scss" scoped>
.login-bg {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
}

.login-container {
  box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.1), 0 1px 0px 0 rgba(0, 0, 0, 0.04);
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  position: fixed;
  width: 350px;
  left: 50%;
  top: 50%;
  margin-left: -175px;
  margin-top: -175px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  .title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .remember {
    margin: 0px 0px 35px 0px;
  }
}
</style>
