<template>
  <div class="product-add">
    <!--form表单-->
    <el-row :gutter="20">
      <el-col :span="12" class="d-c-s">
        <div class="phone-theme">
          <img v-if="form.theme=='0'" :src="theme1_url">
          <img v-if="form.theme=='1'" :src="theme2_url">
          <img v-if="form.theme=='2'" :src="theme3_url">
          <img v-if="form.theme=='3'" :src="theme4_url">
          <img v-if="form.theme=='4'" :src="theme5_url">
          <img v-if="form.theme=='5'" :src="theme6_url">
          <img v-if="form.theme=='6'" :src="theme7_url">
        </div>
      </el-col>
      <el-col :span="12" class="d-c-s">
        <el-form size="small" ref="form" :model="form" label-:width="80">
          <div class="common-form">主题选择</div>
          <el-form-item label="">
            <el-radio-group v-model="form.theme" class="d-a-c f-w">
              <el-radio class="mb10 mr10" :label="0">主题一</el-radio>
              <el-radio class="mb10 mr10" :label="1">主题二</el-radio>
              <el-radio class="mb10 mr10" :label="2">主题三</el-radio>
              <el-radio class="mb10 mr10" :label="3">主题四</el-radio>
              <el-radio class="mb10 mr10" :label="4">主题五</el-radio>
              <el-radio class="mb10 mr10" :label="5">主题六</el-radio>
              <el-radio class="mb10 mr10" :label="6">主题七</el-radio>
            </el-radio-group>
          </el-form-item>
          <!--提交-->
          <div class="common-button-wrapper">
            <el-button type="primary" @click="onSubmit" :loading="loading">保存</el-button>
          </div>
        </el-form>
      </el-col>
    </el-row>

  </div>

</template>

<script>
  import PageApi from '@/api/page.js';
  import Upload from '@/components/file/Upload.vue';
  import {
    formatModel
  } from '@/utils/base.js';
  import theme1 from '@/assets/img/theme1.jpg';
  import theme2 from '@/assets/img/theme2.jpg';
  import theme3 from '@/assets/img/theme3.jpg';
  import theme4 from '@/assets/img/theme4.jpg';
  import theme5 from '@/assets/img/theme5.jpg';
  import theme6 from '@/assets/img/theme6.jpg';
  import theme7 from '@/assets/img/theme7.jpg';
  export default {
    data() {
      return {
        /*是否正在加载*/
        loading: false,
        /*form表单数据*/
        form: {
          theme: 'red',
        },
        all_type: [],
        type: [],
        /*是否打开图片选择*/
        isupload: false,
        theme7_url: theme7,
        theme6_url: theme6,
        theme5_url: theme5,
        theme4_url: theme4,
        theme3_url: theme3,
        theme2_url: theme2,
        theme1_url: theme1,
      };
    },
    created() {
      this.getParams()
    },

    methods: {

      /*获取配置数据*/
      getParams() {
        let self = this;
        PageApi.themeDetail({}, true).then(res => {
            self.form.theme = res.data.theme;
            self.loading = false;
          })
          .catch(error => {

          });
      },


      /*提交*/
      onSubmit() {
        let self = this;
        let params = this.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            PageApi.editTheme(params, true)
              .then(data => {
                self.loading = false;
                ElMessage ({
                  message: '恭喜你，设置成功',
                  type: 'success'
                });
                self.$router.push('/page/theme/index');
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });

      },
    }

  };
</script>
<style>
  .mb10 {
    margin-bottom: 10px;
  }

  .mr10 {
    margin-right: 10px;
  }

  .phone-theme>img {
    width: 250px;
  }

  .d-c-s {
    display: flex;
    justify-content: center;
    align-items: flex-start;
  }
</style>
