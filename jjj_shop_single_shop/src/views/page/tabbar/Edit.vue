<template>
  <el-dialog title="修改导航" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form size="small" :model="form" ref="form">
      <el-form-item label="名称" prop="text" :label-width="formLabelWidth">
        <el-input v-model="form.text" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="链接地址：" prop="text" :label-width="formLabelWidth">
        <div class="d-s-c">
          <div class="url-box flex-1">
            <el-input placeholder="请选择链接地址" v-model="form.url"></el-input>
          </div>
          <div class="url-box ml10">
            <el-button type="primary" @click="changeLink(index)">选择链接</el-button>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="选中图标" prop="selectedIconPath" :label-width="formLabelWidth">
        <el-row>
          <el-button type="primary" @click="openUpload(1)">选择图片</el-button>
          <div v-if="form.selectedIconPath!=''" class="img">
            <img :src="form.selectedIconPath" width="36" height="36" />
          </div>
        </el-row>
      </el-form-item>
      <el-form-item label="未选中图标" prop="iconPath" :label-width="formLabelWidth">
        <el-row>
          <el-button type="primary" @click="openUpload(2)">选择图片</el-button>
          <div v-if="form.iconPath!=''" class="img">
            <img :src="form.iconPath" width="36" height="36" />
          </div>
        </el-row>
      </el-form-item>
    </el-form>
    <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible(false)">取 消</el-button>
          <el-button type="primary" @click="submit" :loading="loading">确 定</el-button>
        </div>
    </template>
    <!--上传图片组件-->
    <Upload v-if="isupload" :isupload="isupload" @returnImgs="returnImgsFunc">上传图片</Upload>
    <Setlink v-if="is_linkset" :is_linkset="is_linkset" @closeDialog="closeLinkset">选择链接</Setlink>
  </el-dialog>
</template>

<script>
  import PageApi from '@/api/page.js';
  import Upload from '@/components/file/Upload.vue';
  import Setlink from '@/components/setlink/SetTab.vue';
  export default {
    components: {
      Upload,
      Setlink
    },
    data() {
      return {
        form: {
          text: '',
          iconPath: '',
          selectedIconPath: '',
          url:''
        },
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        dialogVisible: false,
        loading: false,
        /*是否上传图片*/
        isupload: false,
        // 上传类型，1选中 2未选中
        upload_type: 1,
        is_linkset:false
      };
    },
    props: ['open_edit', 'editform'],
    created() {
      this.dialogVisible = this.open_edit;
      this.form.url = this.editform.url;
      this.form.text = this.editform.text;
      this.form.iconPath = this.editform.iconPath;
      this.form.selectedIconPath = this.editform.selectedIconPath;
    },
    methods: {
      /*选择链接*/
      changeLink(index) {
        this.is_linkset = true;
        this.index = index;
      },

      /*获取链接并关闭弹窗*/
      closeLinkset(e) {
        this.is_linkset = false;
        if (e) {
          this.form.url = '/'+e.url;
        }
      },
      /*修改用户*/
      submit() {
        let self = this;
        let params = self.form;
        params.index = self.editform.index;
        params.type = 'image';
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            PageApi.editTabbar(params, true).then(data => {
              self.loading = false;
              ElMessage ({
                message: '修改成功',
                type: 'success'
              });
              self.dialogFormVisible(true);
            }).catch(error => {
              self.loading = false;
            });
          }
        });
      },
      /*关闭弹窗*/
      dialogFormVisible(e) {
        if (e) {
          this.$emit('closeDialog', {
            type: 'success',
            openDialog: false
          })
        } else {
          this.$emit('closeDialog', {
            type: 'error',
            openDialog: false
          })
        }
      },
      /*上传*/
      openUpload(e) {
        this.upload_type = e;
        this.isupload = true;
      },
      /*获取图片*/
      returnImgsFunc(e) {
        if (e != null && e.length > 0) {
          if (this.upload_type == 1) {
            this.form.selectedIconPath = e[0].file_path;
          } else {
            this.form.iconPath = e[0].file_path;
          }
        }
        this.isupload = false;
      },

    }
  };
</script>

<style>
  .img {
    margin-top: 10px;
  }
</style>
