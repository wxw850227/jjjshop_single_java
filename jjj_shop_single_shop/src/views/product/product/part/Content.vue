<template>
  <div class="basic-setting-content pl16 pr16">
    <!--商品详情-->
    <div class="common-form">商品详情</div>
    <el-form-item label="详情类型：">
      <el-radio-group v-model="form.model.isPicture">
        <el-radio :label="0">图文</el-radio>
        <el-radio :label="1">纯图</el-radio>
      </el-radio-group>
    </el-form-item>
    <!--内容-->
    <el-form-item label="详情内容：" v-show="form.model.isPicture == 0">
      <div class="edit_container">
        <Uediter :text="form.model.content" :config="ueditor.config" ref="ue" @contentChange="contentChangeFunc"></Uediter>
      </div>
    </el-form-item>
    <el-form-item label="详情图片：" v-if="form.model.isPicture == 1">
      <div class="draggable-list">
        <draggable class="wrapper" v-model="form.model.contentImage">
          <template #item="{ element, index }">
            <div class="item">
              <img v-img-url="element.filePath" />
              <a href="javascript:void(0);" class="delete-btn" @click.stop="deleteImg(index)">
                <el-icon><CloseBold /></el-icon>
              </a>
            </div>
          </template>
        </draggable>
        <div class="item img-select" @click="openUpload">
          <el-icon><Plus /></el-icon>
        </div>
      </div>
    </el-form-item>
    <!--商品图片组件-->
    <Upload v-if="is_upload" :config="config" :isupload="is_upload" @returnImgs="returnImgsFunc">上传图片</Upload>
  </div>
</template>

<script>
import Uediter from '@/components/UE.vue';
import Upload from '@/components/file/Upload.vue';
import draggable from 'vuedraggable';
export default {
  components: {
    /*编辑器*/
    Uediter,
    Upload,
    draggable
  },
  data() {
    return {
      /*富文本框配置*/
      ueditor: {
        text: '',
        config: {
          initialFrameWidth: 400,
          initialFrameHeight: 500
        },
      },
      is_upload: false,
      config: {
        total: 9,fileType: 'image',
      }
    };
  },
  created() {

  },
  inject: ['form'],
  methods:{
    /*获取富文本内容*/
    contentChangeFunc(e){
      this.form.model.content=e;
    },
    openUpload(){
      this.is_upload = true;
    },
    /*上传商品图片*/
    returnImgsFunc(e) {
      if (e != null) {
        let imgs = this.form.model.contentImage.concat(e);
          // 兼容后端
          for(let i=0;i<imgs.length;i++){
            if(typeof(imgs[i].imageId) == "undefined"){
              imgs[i].imageId = imgs[i].fileId;
            }
          }
          this.form.model.contentImage = imgs;
      }
      this.is_upload = false;
    },
    /*删除商品图片*/
    deleteImg(index) {
      this.form.model.contentImage.splice(index, 1);
    },
  }
};
</script>

<style></style>
