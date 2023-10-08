<template>
  <div class="login-container">
    
    <div class="video-container">
      <div :style="fixStyle" class="filter">
        <div style="width: 400px; margin: 100px auto">
          
          <h3 class="title">基于SpringBoot的Minio文件系统</h3>
          <el-form
            autocomplete="on"
            :model="loginForm"
            :rules="loginRules"
            ref="loginForm"
            status-icon
            label-position="left"
            label-width="0px"
          >
            <el-form-item prop="email">
              <span class="svg-container svg-container_login">
                <icon-svg icon-class="name" />
              </span>
              <el-input
                v-model="loginForm.email"
                placeholder="请输入账户名或邮箱"
                @keyup.enter.native="handleLogin"
              ></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <span class="svg-container">
                <icon-svg icon-class="password" />
              </span>
              <el-input
                v-model="loginForm.password"
                show-password
                placeholder="请输入密码"
                @keyup.enter.native="handleLogin"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <div style="display: flex">
                <span class="svg-container">
                  <icon-svg icon-class="el-icon-key" ></icon-svg>
                </span>
                <el-input 
                  v-model="loginForm.validCode" 
                  style="width: 50%;margin-top: 5px" 
                  placeholder="请输入验证码"
                  @keyup.enter.native="handleLogin"
                >
               </el-input>
                <ValidCode @input="createValidCode"/>
              </div>            
            </el-form-item>

            <el-form-item>
              <el-button
                style="width: 100%"
                type="primary"
                @click.native.prevent="handleLogin"
                >登 录</el-button
              >
             
            </el-form-item>
           
          </el-form>
         
          
        </div>
      </div>
      <video
        :style="fixStyle"
        autoplay
        loop
        muted
        class="fillWidth"
        v-on:canplay="canplay"
      >
        <source src="../../assets/sea.mp4" type="video/mp4" />
        浏览器不支持 video 标签，建议升级浏览器。
      </video>
    </div>
  </div>
</template>

<script>
import { isValidateEmail } from "@/utils/validate";
import ValidCode from "@/components/ValidCode"

export default {
  name: "login",
  components: {
    ValidCode,
  },
  data() {
    const validateEmail = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error("账户名长度必须为3位或以上"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("密码长度必须为6位或以上"));
      } else {
        callback();
      }
    };
    return {
      vedioCanPlay: false,
      fixStyle: "",
      loading: false,
      validCode: '',
      loginForm: {
        email: "2049448867@qq.com",
        password: "123456",
      },
      loginRules: {
        email: [
          { required: true, trigger: "blur", validator: validateEmail },
        ],
        password: [
          { required: true, trigger: "blur", validator: validatePassword },
        ],
      },
      passwordType: "password",
      btnLoading: false,
    };
  },
  mounted() {
    window.onresize = () => {
      const windowWidth = document.body.clientWidth;
      const windowHeight = document.body.clientHeight;
      const windowAspectRatio = windowHeight / windowWidth;
      let videoWidth;
      let videoHeight;
      if (windowAspectRatio < 0.5625) {
        videoWidth = windowWidth;
        videoHeight = videoWidth * 0.5625;
        this.fixStyle = {
          height: windowWidth * 0.5625 + "px",
          width: windowWidth + "px",
          "margin-bottom": (windowHeight - videoHeight) / 2 + "px",
          "margin-left": "initial",
        };
      } else {
        videoHeight = windowHeight;
        videoWidth = videoHeight / 0.5625;
        this.fixStyle = {
          height: windowHeight + "px",
          width: windowHeight / 0.5625 + "px",
          "margin-left": (windowWidth - videoWidth) / 2 + "px",
          "margin-bottom": "initial",
        };
      }
    };
    window.onresize();
  },

  methods: {
    showPwd() {
      this.passwordType =
        this.passwordType === "password"
          ? ""
          : (this.passwordType = "password");
    },

    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          if (!this.loginForm.validCode) {
            this.$message.error("请输入验证码")
            return
          }
          if(this.loginForm.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          const account = {};
          if (isValidateEmail(this.loginForm.email)) {
            account.email = this.loginForm.email;
          } else {
            account.nickname = this.loginForm.email;
          }
          account.password = this.loginForm.password;
          this.loading = true;
          this.$store.dispatch("Login", account).then(() => {
            this.loading = false;
            this.$router.push({ path: "/dashboard" });
            
            this.$message({
                      type: 'success',
                      message: '登录成功!'
              })
          });
        }
      });
    },
    canplay() {
      this.vedioCanPlay = true;
    },
    send() {
      this.$router.push({ path: '/register' });
      // this.$router.push('/register')
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
@import "../../../src/styles/mixin.scss";

$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  @include relative;
  height: 100%;
  overflow-y: hidden;
  background-color: $bg;

  input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0 1000px #293444 inset !important;
    -webkit-text-fill-color: #fff !important;
  }

  input {
    background: transparent;
    border: 0;
    -webkit-appearance: none;
    border-radius: 0;
    padding: 12px 5px 12px 15px;
    color: $light_gray;
    height: 47px;
  }

  .el-input {
    display: inline-block;
    height: 50px;
    width: 85%;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;

    &_login {
      font-size: 20px;
    }
  }

  .title {
    font-size: 26px;
    color: $light_gray;
    margin: 0 auto 40px auto;
    text-align: center;
    font-weight: bold;
  }

  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 400px;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
  }
}

.video-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.video-container .poster img {
  z-index: 0;
  position: absolute;
}

.video-container .filter {
  z-index: 1;
  position: absolute;
  /*background: rgba(0, 0, 0, 0.4);*/
  width: 100%;
}

.fillWidth {
  width: 100%;
}
</style>
