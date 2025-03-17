<template>
  <div class="product-add pb50" v-loading="loading">
    <!--编辑文章-->
    <el-form size="small" :model="form" ref="form" :rules="rules" label-width="100px">
      <div class="common-form">编辑文章</div>
      <el-form-item label="文章标题" prop="article_title">
        <el-input v-model="form.articleTitle" placeholder="请输入文章标题" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="dec">
        <el-input v-model="form.description" placeholder="请输入文章描述" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="文章缩略图">
        <div>
          <el-button type="primary" @click="openUpload">上传图片</el-button>
          <img class="mt10" v-img-url="form.imageUrl" :width="120" alt="" />
          <div class="gray9">建议尺寸 120*70</div>
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
          <Uediter v-if="!loading" :text="ueditor.text" :config="ueditor.config" ref="ue" @contentChange="contentChangeFunc"></Uediter>
        </div>
      </el-form-item>
      <el-form-item label="排序">
        <el-input type="number" v-model="form.articleSort" placeholder="请输入数字" class="max-w460"></el-input>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc" :loading="loading">取消</el-button>
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
        /*是否加载完成*/
        loading: true,
        /*是否上传图片*/
        isupload: false,
        /*富文本配置*/
        ueditor: {
          text: '',
          config: {
            initialFrameWidth: 400,
            initialFrameHeight: 500
          }
        },
        /*form表单数据*/
        form: {
          articleTitle: '',
          categoryId: '',
          imageId: '',
          image: {},
          articleSort: 1,
          articleStatus: '0',
          virtualViews: 1,
          articleContent: ''
        },
        /*新闻类别*/
        category: [],
        /*验证规则*/
        rules: {
          article_title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
          dec: [{ required: true, message: '请输入文章描述', trigger: 'blur' }],
          categoryId: [{
            required: true,
            message: '选择文章分类',
            trigger: 'blur'
          }]
        }
      };
    },
    created () {

      this.getDetail();

    },

  methods: {
    contentChangeFunc(e){
      this.ueditor.text = e;
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
        this.form.imageUrl = e[0].filePath;
        this.isImg = true;
      }
      this.isupload = false;
    },

    getDetail () {
      let self = this;
      // 取到路由带过来的参数
      const params = self.$route.query.articleId;
      ArticleApi.toEditArticle({ articleId: params }, true).then(res => {
        self.form = res.data.article;
        if (!self.form.image) {
          self.form.image = {};
        }
        self.category = res.data.categoryList;
        self.loading = false;
        self.ueditor.text = res.data.article.articleContent;
      })
          .catch(error => { });
    },

    /*修改文章*/
    onSubmit () {
      let self = this;
      let params = this.form;
      params.articleContent = this.ueditor.text;
      // 取到路由带过来的参数
      ArticleApi.editArticle(params, true)
          .then(data => {
            ElMessage ({
              message: data.msg,
              type: 'success'
            });
            self.$router.push('/plus/article/index');
          })
          .catch(error => { });
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
