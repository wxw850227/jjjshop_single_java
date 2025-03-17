<template>
  <div class="pb50">
    <el-form size="small" ref="form" :model="form" label-width="100px">
      <el-form-item label="开启首页推送" prop="is_open">
        <el-switch v-model="form.isOpen"></el-switch>
      </el-form-item>
      <el-form-item label="活动名称" :rules="[{ required: true, message: ' ' }]" prop="name">
        <el-input class="max-w460" v-model="form.name" placeholder="" type="text"></el-input>
        <div class="gray">建议换活动，此名称更换，商城首页弹窗根据此名称不同来弹窗</div>
      </el-form-item>
      <div v-if="form.isOpen">
        <el-form-item label="类型">
          <el-radio v-model="form.type" label="1">图文</el-radio>
          <el-radio v-model="form.type" label="2">纯图</el-radio>
          <el-radio v-model="form.type" label="3">领取优惠券</el-radio>
        </el-form-item>

        <el-form-item v-if="form.type == 1" label="标题" :rules="[{ required: true, message: ' ' }]" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" class="max-w460"></el-input>
        </el-form-item>

        <el-form-item label="弹窗图片">
          <el-row>
            <el-button icon="Upload" @click="openUpload">选择图片</el-button>
            <div v-if="form.imageId > 0" class="img mt10"><img :src="form.filePath" width="100" height="100" /></div>
          </el-row>
          <div class="gray">图片为2M以内大小，格式限png，jpg格式
            <span v-if="form.type==1">建议尺寸：600x300；</span>
            <span v-if="form.type==2">建议尺寸：600x420；</span>
            <span v-if="form.type==3">建议尺寸：600x250；</span>
          </div>
        </el-form-item>
        <el-form-item v-if="form.type == 1" label="按钮文案" :rules="[{ required: true, message: ' ' }]" prop="des">
          <el-input class="max-w460" v-model="form.des" placeholder="" type="text"></el-input>
        </el-form-item>
        <el-form-item v-if="form.type == 1" label="简介说明" :rules="[{ required: true, message: ' ' }]" prop="remark">
          <el-input type="textarea" :rows="2" max="100" placeholder="请输入说明" v-model="form.remark" class="max-w460">
          </el-input>
        </el-form-item>
        <el-form-item label="跳转地址" v-if="form.type < 3" :rules="[{ required: true, message: ' ' }]" prop="link">
          <el-row class="max-w460">
            <div class="flex-1 mr10">
              <el-input placeholder="点击选择你要链接的位置" v-if="form.link"
              :model-value="'链接到' + form.link.type + ' ' + form.link.name" :disabled="true"></el-input>
              <el-input placeholder="点击选择你要链接的位置" v-if="!form.link" :disabled="true"></el-input>
            </div>
            <el-button icon="Link" @click="changeLink">选择链接</el-button>
          </el-row>
        </el-form-item>
        <!--选择优惠券-->
        <el-form-item label="选择优惠券" v-if="form.type == 3">
          <div class="btn-box">
            <el-button size="small" icon="Plus" @click="addCoupon()">添加优惠券</el-button>
            （最多可以添加三张）
          </div>
          <div class="mt10">
            <el-table size="small" :data="coupon" style="width: 100%">
              <el-table-column prop="name" label="优惠券">
                <template #default="scope">
                  <span class="fb orange">{{scope.row.name}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="类型">
                <template #default="scope">
                  <span class="fb orange" v-if="scope.row.type == 10">满减券</span>
                  <span class="fb orange" v-if="scope.row.type == 20">折扣券</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button @click="deleteCouponClick(scope.$index)" type="text" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
      </div>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>

    <!--上传图片组件-->
    <Upload v-if="isupload" :isupload="isupload" :type="type" @returnImgs="returnImgsFunc">上传图片</Upload>

    <!--选择连接-->
    <Setlink v-if="is_linkset" :is_linkset="is_linkset" @closeDialog="closeLinkset">选择链接</Setlink>

    <!--选择优惠券-->
    <GetCoupon v-if="open_add" :open_add="open_add" @closeDialog="closeProductDialogFunc($event)">选择优惠券弹出层</GetCoupon>
  </div>
</template>
<script>
  import HomepushApi from '@/api/homepush.js';
  import Upload from '@/components/file/Upload.vue';
  import Setlink from '@/components/setlink/Setlink.vue';
  import GetCoupon from '@/components/coupon/GetCoupon.vue';
  export default {
    components: {
      Upload,
      Setlink,
      GetCoupon
    },
    data () {
      return {
        form: {
          isOpen: false,
          name: '',
          des: '查看详情',
          title: '',
          remark: '',
          link: '',
          type: '1',
          imageId: 0,
          filePath: ''
        },
        coupon: [],
        /*是否加载完成*/
        loading: false,
        /*添加优惠券*/
        open_add: false,
        /*是否上传图片*/
        isupload: false,
        /*是否选择链接*/
        is_linkset: false
      };
    },
    created () {
      this.getData();
    },
    methods: {
      /*上传*/
      openUpload (e) {
        this.type = e;
        this.isupload = true;
      },

      /*获取图片*/
      returnImgsFunc (e) {
        if (e != null && e.length > 0) {
          this.form.filePath = e[0].filePath;
          this.form.imageId = e[0].fileId;
        }
        this.isupload = false;
      },

      /*获取数据*/
      getData () {
        let self = this;
        HomepushApi.getData()
          .then(res => {
            self.form = res.data;
            if (self.form.isOpen == 'true') {
              self.form.isOpen = true;
            }
            if (!self.form.hasOwnProperty('coupon')) {
              self.coupon = [];
            } else {
              self.coupon = self.form.coupon;
            }
          })
          .catch(error => { });
      },

      /*提交*/
      onSubmit () {
        let self = this;
        let params = self.form;
        if (self.form.type == 3) {
          params.coupon = self.coupon;
        }
        if (params.isOpen && !self.checkData(params)) {
          return false;
        }
        self.loading = true;
        HomepushApi.saveData(params, true)
          .then(data => {
            self.loading = false;
            if (data.code == 1) {
              ElMessage ({
                message: '恭喜你，保存成功',
                type: 'success'
              });
              self.getData();
            }
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*选择链接*/
      changeLink () {
        this.is_linkset = true;
      },

      /*获取链接并关闭弹窗*/
      closeLinkset (e) {
        this.is_linkset = false;
        if (e) {
          this.form.link = e;
        }
      },

      /*添加优惠券*/
      addCoupon () {
        this.open_add = true;
      },

      /*关闭弹窗*/
      closeProductDialogFunc (e) {
        this.open_add = e.openDialog;
        let len = this.coupon.length;
        if (len >= 3) {
          ElMessage({
            message: '最多添加3张优惠券',
            type: 'error'
          });
          return false;
        }
        if (e.type == 'success') {
          let obj = {};
          obj.couponId = e.params.couponId;
          obj.name = e.params.name;
          obj.type = e.params.couponType;
          this.coupon.push(obj);
        }
      },

      /*删除优惠券*/
      deleteCouponClick (e) {
        this.coupon.splice(e, 1);
      },

      /*数据验证*/
      checkData (param) {
        if (param.type == 1) {
          if (!param.title) {
            ElMessage({
              message: '标题不能为空',
              type: 'error'
            });
            return false;
          }
          if (!param.des || !param.remark) {
            return false;
          }
        }
        if (param.type < 3) {
          if (!param.link) {
            ElMessage({
              message: '请选择跳转链接',
              type: 'error'
            });
            return false;
          }
        } else {
          if (param.coupon.length == 0) {
            ElMessage({
              message: '请选择优惠卷',
              type: 'error'
            });
            return false;
          }
        }

        if (!(param.imageId > 0)) {
          ElMessage({
            message: '请上传图片',
            type: 'error'
          });
          return false;
        }
        return true;
      }
    }
  };
</script>

<style>
  .reward-list .reward-item {
    margin-bottom: 10px;
    padding: 10px 20px;
    border: 1px solid #ebeef5;
  }

  .reward-list .delete-reward {
    position: absolute;
    top: 10px;
    right: 20px;
    cursor: pointer;
    z-index: 10;
  }
</style>
