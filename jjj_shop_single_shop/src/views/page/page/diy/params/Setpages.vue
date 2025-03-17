<template>
  <div>
    <div class="common-form">
      <span>{{ curItem.name }}</span>
    </div>
    <el-form size="small" :model="curItem" label-width="100px">
      <!--页面标题-->
      <el-form-item label="标题类型：">
        <el-radio-group v-model="curItem.params.titleType" @change="logechange">
           <el-radio :label="'text'">文本</el-radio>
          <el-radio :label="'image'">图片</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--图片-->
      <el-form-item label="顶部logo：" v-if="curItem.params.titleType=='image'">
        <div class="diy-setpages-cover">
          <img  v-if="curItem.params.toplogo" v-img-url="curItem.params.toplogo" alt="" @click="$parent.onEditorSelectImage(curItem.params, 'toplogo')" :style="'background-color:'+curItem.style.titleBackgroundColor+' ;'">
          <div>建议尺寸高度 X × 60</div>
        </div>
      </el-form-item>
      <el-form-item label="顶部logo：" v-if="curItem.params.titleType=='text'">
         <el-input v-model="curItem.params.title"></el-input>
      </el-form-item>
      <el-form-item label="文字颜色："  v-if="curItem.params.titleType=='text'">
        <div class="d-s-c">
          <el-color-picker v-model="curItem.style.titleTextColor"></el-color-picker>
          <el-button type="button" style="margin-left: 10px;" @click.stop="$parent.onEditorResetColor(curItem.style, 'titleTextColor', '#ffffff')">重置</el-button>
        </div>
      </el-form-item>
      <!--页面名称-->
      <el-form-item label="页面名称：">
        <el-input v-model="curItem.params.name"></el-input>
        <p class="gray">页面名称仅用于后台查找</p>
      </el-form-item>
      <!--分享标题-->
      <el-form-item label="分享标题：">
        <el-input v-model="curItem.params.shareTitle"></el-input>
        <p class="gray">小程序端转发时显示的标题</p>
      </el-form-item>
      <el-form-item label="分享logo：">
        <div class="diy-notice-icon">
          <img v-img-url="curItem.params.shareImg" alt="" @click="$parent.onEditorSelectImage(curItem.params, 'shareImg')">
        </div>
        <div>公众号分享logo，建议尺寸80×80</div>
      </el-form-item>

      <!-- 背景颜色 -->
      <el-form-item label="背景颜色：">
        <div class="d-s-c">
          <el-color-picker v-model="curItem.style.titleBackgroundColor"></el-color-picker>
          <el-button type="button" style="margin-left: 10px;" @click.stop="$parent.onEditorResetColor(curItem.style, 'titleBackgroundColor', '#ffffff')">重置</el-button>
        </div>
      </el-form-item>
      <!--页面标题-->
      <el-form-item label="商品分类：">
        <div class="gray9">仅首页有效</div>
        <el-radio-group v-model="curItem.category.open" @change="categorychange">
           <el-radio :label="1">开启</el-radio>
          <el-radio :label="0">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- 背景颜色 -->
      <el-form-item label="分类颜色：">
        <div class="d-s-c">
          <el-color-picker v-model="curItem.category.color"></el-color-picker>
          <el-button type="button" style="margin-left: 10px;" @click.stop="$parent.onEditorResetColor(curItem.category, 'color', '#ffffff')">重置</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        logo_type: 'image'
      };
    },
    props: ['curItem', 'selectedIndex', 'opts'],
    created() {

    },
    methods: {
      logechange(e){
         this.curItem.params.titleType = e;
      },
      categorychange(e){
         this.curItem.category.open=e;
      }
    }
  };
</script>

<style>
  .diy-setpages-cover>img{
    width: 60px;
    height: 60px;
  }
</style>
