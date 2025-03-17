<template>
  <div class="product-add pb50" v-loading="loading">
    <!--编辑广告-->
    <el-form size="small" :model="form" ref="form" label-width="100px">
      <div class="common-form">编辑菜单</div>
      <el-form-item label="菜单名称" prop="name" :rules="[{ required: true, message: ' ' }]">
        <el-input v-model="form.name" placeholder="请输入菜单名称" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="图标" prop="icon" :rules="[{ required: true, message: ' ' }]">
        <div>
          <el-button type="primary" @click="openUpload">上传图片</el-button>
          <img class="mt10" v-img-url="form.icon" :width="48" alt="" />
          <div style="color: red;">图标大小为:48*48</div>
          <!--上传图片组件-->
          <Upload v-if="isupload" :isupload="isupload" @returnImgs="returnImgsFunc">上传图片</Upload>
        </div>
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.isShow">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sort" :rules="[{ required: true, message: ' ' }]">
        <el-input type="number" v-model="form.sort" placeholder="请输入排序" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="链接" prop="path" :rules="[{ required: true, message: ' ' }]">
        <el-row>
          <div class="url-box flex-1">
            <el-input :disabled="form.sysTag!=''" v-model="form.path" type="text" class="max-w460 mr10"
              style="width: 380px;"></el-input>
            <el-button :disabled="form.sysTag!=''" type="primary" @click="changeLink()">选择链接<span></span></el-button>
            <div class="tips" id="tips" style="color: #000;">{{form.pathName}}</div>
            <el-input v-model="form.pathName" type="hidden" class="max-w460"></el-input>
          </div>
          <div class="url-box ml10" style="float: right;"></div>
        </el-row>
      </el-form-item>
      <Setlink v-if="is_linkset" :is_linkset="is_linkset" @closeDialog="closeLinkset">选择链接</Setlink>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc" :loading="loading">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
  import PageApi from '@/api/page.js';
  import Upload from '@/components/file/Upload.vue';
  import Setlink from '@/components/setlink/Setlink.vue';
  import { formatModel } from '@/utils/base.js';
  export default {
    components: {
      /*图片上传*/
      Upload: Upload,
      Setlink
    },
    data() {
      return {
        /*是否加载完成*/
        loading: true,
        /*是否上传图片*/
        isupload: false,
        /*form表单数据*/
        is_linkset: false,
        menuId: 0,
        form: {
          menuId: 0,
          name: '',
          icon: '',
          sort: 1,
          isShow: 0,
          path: '',
          pathName: '',
          sysTag: ''
        },
      };
    },
    created() {
      this.menuId = this.$route.query.menuId;
      this.getDetail();
    },

    methods: {
      changeLink() {
        this.is_linkset = true;
      },

      /*获取链接并关闭弹窗*/
      closeLinkset(e) {
        this.is_linkset = false;
        if (e != null) {
          if(e.url.startsWith('/')){
            this.form.path = e.url;
          }else{
            this.form.path = '/' + e.url;
          }
          this.form.pathName = '链接到' + ' ' + e.type + ' ' + e.name;
        }
      },
      /*上传*/
      openUpload() {
        this.isupload = true;
      },

      /*获取图片*/
      returnImgsFunc(e) {
        let self = this;
        if (e != null) {
          this.form.icon = e[0].filePath;
        }
        this.isupload = false;
      },

      getDetail() {
        let self = this;
        PageApi.menuDetail({
            menuId: self.menuId
          }, true).then(res => {
            self.form = formatModel(self.form, res.data);
            self.loading = false;
          })
          .catch(error => {});
      },

      /*修改*/
      onSubmit() {
        let self = this;
        let params = self.form;
        // // 取到路由带过来的参数
        self.$refs.form.validate(valid => {
          if (valid) {
            PageApi.editMenu(params, true)
              .then(data => {
                ElMessage ({
                  message: data.msg,
                  type: 'success'
                });
                self.$router.push('/page/center/index');
              })
              .catch(error => {});
          }
        });
      },
      cancelFunc() {
        this.$router.push({
          path: '/page/center/index'
        });
      }
    }
  };
</script>

<style>
  .edit_container {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

  .ql-editor {
    height: 400px;
  }
</style>
