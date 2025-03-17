<template>
  <div class="product-add pb50">
    <!--添加文章-->
    <el-form size="small" :model="form" ref="form" :rules="rules" label-width="100px">
      <div class="common-form">添加文章</div>
      <el-form-item label="文章标题" prop="article_title">
        <el-input v-model="form.articleTitle" placeholder="请输入文章标题" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="dec">
        <el-input v-model="form.description" placeholder="请输入文章描述" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="文章缩略图">
        <div>
          <el-button type="primary" @click="openUpload">上传图片</el-button>
          <img :src="path" class="mt10" :width="120" v-if="isImg" :isImg="isImg" />
          <div class="gray9">建议尺寸120*70</div>
          <!--上传图片组件-->
          <Upload v-if="isupload" :isupload="isupload" @returnImgs="returnImgsFunc">上传图片</Upload>
        </div>
      </el-form-item>
      <el-form-item label="文章分类" prop="categoryId">
        <el-select v-model="form.categoryId" placeholder="请选择">
          <el-option v-for="(item, index) in category" :key="index" :label="item.name" :value="item.categoryId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="虚拟阅读量">
        <el-input type="number" v-model="form.virtualViews" placeholder="请输入数字" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="文章状态">
        <el-radio-group v-model="form.articleStatus">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="文章内容">
        <div class="edit_container">
          <Uediter :text="form.articleContent" :config="ueditor.config" ref="ue" @contentChange="contentChangeFunc"></Uediter>
        </div>
      </el-form-item>
      <el-form-item label="排序">
        <el-input type="number" v-model="form.articleSort" placeholder="请输入数字" class="max-w460"></el-input>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
  import ArticleApi from '@/api/article.js';
  import Uediter from '@/components/UE.vue';
  import Upload from '@/components/file/Upload.vue';
  export default {
    components: {
      /*编辑器*/
      Uediter,
      /*图片上传*/
      Upload: Upload
    },
    data () {
      return {
        /*是否上传图片*/
        isupload: false,
        isImg: false,
        /*富文本配置*/
        ueditor: {
          text: '',
          config: {
            initialFrameWidth: 400,
            initialFrameHeight: 500
          }
        },
        path: '',
        /*form表单数据*/
        form: {
          articleTitle: '',
          categoryId: '',
          imageId: '',
          articleSort: 1,
          articleStatus: 1,
          virtualViews: 1,
          articleContent: '',
          description: ''
        },
        /*文章类别*/
        category: [],
        loading: false,
        /*验证规则*/
        rules: {
          articleTitle: [{
            required: true,
            message: '请输入文章标题',
            trigger: 'blur'
          }],
          description: [{
            required: true,
            message: '请输入文章描述',
            trigger: 'blur'
          }],
          categoryId: [{
            required: true,
            message: '选择文章分类',
            trigger: 'blur'
          }]
        }
      };
    },
    created () {
      /*获取列表*/
      this.getCategoryList();
    },
    methods: {
      contentChangeFunc(e){
        this.form.articleContent = e;
      },
      /*上传*/
      openUpload () {
        this.isupload = true;
      },

      /*获取图片*/
      returnImgsFunc (e) {
        let self = this;
        if (e != null) {
          this.form.imageId = e[0].fileId;
          this.path = e[0].filePath;
          this.isImg = true;
        }
        this.isupload = false;
      },

      /*获取文章类别*/
      getCategoryList () {
        let self = this;
        let Params = {};

        ArticleApi.toAddArticle(Params, true)
          .then(res => {
            self.category = res.data;
            console.log(self.category);
            if (self.catgory.length > 0) {
              self.form.categoryId = self.category[0].categoryId;
            }
          })
          .catch(error => {

          });
      },

      /*添加文章*/
      onSubmit () {
        let self = this;
        let form = self.form;
        self.$refs.form.validate(valid => {
          if (valid) {
            self.loading = true;
            ArticleApi.addArticle(form, true)
              .then(data => {
                self.loading = false;
                ElMessage ({
                  message: data.msg,
                  type: 'success'
                });
                self.$router.push('/plus/article/index');
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });
      },

      /*取消添加，返回文章列表*/
      cancelFunc () {
        this.$router.push({
          path: '/plus/article/index'
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
