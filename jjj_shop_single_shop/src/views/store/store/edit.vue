<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="80px">
      <!--编辑门店-->
      <div class="common-form">编辑门店</div>
      <el-form-item label="门店名称" prop="storeName" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.storeName" placeholder="请输入门店名称" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="门店logo">
        <el-row>
          <el-button icon="Picture" @click="openUpload">选择图片</el-button>
          <div v-if="form.logoImageId!=''" class="img">
            <img :src="filePath" width="100" height="100" />
          </div>
        </el-row>
      </el-form-item>
      <el-form-item label="联系人" prop="linkman" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.linkman" placeholder="请输入门店联系人" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="联系电话" prop="phone" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.phone" placeholder="请输入门店联系电话" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="营业时间" prop="shopHours" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.shopHours" placeholder="请输入营业时间,例如:8:30-17:30" class="max-w460"></el-input>
      </el-form-item>

      <el-form-item label="门店区域">
        <el-select v-model="form.provinceId" placeholder="省" @change="initCity" class="mr10">
          <el-option :label="item.name" :value="item.id" v-for="(item,index) in areaList" :key='index'></el-option>
        </el-select>
        <el-select v-if="form.provinceId!=''" v-model="form.cityId" placeholder="市" @change="initRegion" class="mr10">
          <el-option :label="item1.name" :value="item1.id" v-for="(item1,index1) in cityList"
            :key='index1'></el-option>
        </el-select>
        <el-select v-if="form.cityId!=''" v-model="form.regionId" placeholder="区">
          <el-option :label="item2.name" :value="item2.id"
            v-for="(item2,index2) in regionList" :key='index2'>
          </el-option>
        </el-select>

      </el-form-item>
      <el-form-item label="详细地址" prop="address" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.address" placeholder="请输入详细地址" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="门店坐标">
        <el-row class="mb16">
          <el-col :span="24">
            <el-input v-model="form.coordinate" class="max-w460"></el-input>
          </el-col>
        </el-row>
        <el-row class="mb16">
          <el-col :span="24">
            <Getpoint :form="form" @getMapdata="getMapdataFunc" @chose="choseFunc"></Getpoint>
          </el-col>
        </el-row>

      </el-form-item>
      <el-form-item label="门店简介">
        <el-input type="textarea" v-model="form.summary" placeholder="请输入门店简介" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="门店排序">
        <el-input type="number" v-model="form.sort" placeholder="请输入门店排序" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="自提核销">
        <el-radio-group v-model="form.isCheck">
          <el-radio :label="1">支持</el-radio>
          <el-radio :label="0">不支持</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="门店状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>
    <!--上传图片组件-->
    <Upload v-if="isupload" :isupload="isupload" :type="type" @returnImgs="returnImgsFunc">上传图片</Upload>
  </div>
</template>

<script>
  import Getpoint from '@/components/map/Getpoint.vue';
  import StoreApi from '@/api/store.js';
  import Upload from '@/components/file/Upload.vue';
  import { formatModel } from '@/utils/base.js'
  export default {
    components: {
      /*腾讯地图拾取器*/
      Getpoint,
      Upload
    },
    data () {
      return {
        /*form表单数据*/
        form: {
          storeId: null,
          storeName: 0,
          storeName: '',
          linkman: '',
          phone: '',
          shopHours: '',
          provinceId: '',
          cityId: '',
          regionId: '',
          address: '',
          coordinate: '',
          summary: '',
          sort: '',
          isCheck: '',
          status: '',
          logoImageId: 0
        },
        /*省市区*/
        areaList: [],
        cityList: [],
        regionList: [],
        /*查询信息*/
        list: [],
        /*是否上传图片*/
        isupload: false,
        loading: false,
        filePath: ''
      };
    },
    created () {
      this.form.storeId = this.$route.query.storeId;
      this.getData();
    },

    watch:{
      'form.provinceId':function(n,o){
        if(n!=o){
          this.areaList.forEach((item,index)=>{
            if(item.id == n){
              this.cityList = item.children;
            }
          })
        }
      },

      'form.cityId':function(n,o){
        if(n!=o){
          this.$nextTick(()=>{
            this.cityList.forEach((item,index)=>{
              if(item.id == n){
                console.log(item);
                this.regionList = item.children;
              }
            })
          })
        }
      }
    },
    methods: {
      /*修改*/
      onSubmit () {
        let self = this;
        let form = self.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            if(form.provinceId == 0 || form.provinceId == ''
              || form.cityId == 0 || form.cityId == ''
              || form.regionId == 0 || form.regionId == ''){
              ElMessage ({
                message: '请选择完整的省市区',
                type: 'success'
              });
              return;
            }
            self.loading = true;
            StoreApi.editShop(form, true)
              .then(data => {
                self.loading = false;
                ElMessage ({
                  message: '恭喜你，门店修改成功',
                  type: 'success'
                });
                self.$router.push('/store/store/index');
              }).catch(error => {
                self.loading = false;
              });
          }
        });
      },
      /*初始化城市id*/
      initCity () {
        this.form.cityId = ''
      },
      /*初始化区id*/
      initRegion () {
        this.form.regionId = ''
      },

      /*获取经纬度*/
      getMapdataFunc (e) {
        this.form.coordinate = e.data[0].toFixed(6) + ',' + e.data[1].toFixed(6);
      },

      /*获取参数*/
      getData () {
        let self = this;
        StoreApi.shopDetail({
          storeId: self.form.storeId
        }, true)
          .then(res => {
            self.form = formatModel(self.form, res.data.store);
            self.filePath = res.data.store.logoImagePath;
            self.form.isCheck = res.data.store.isCheck;
            self.form.status = res.data.store.status;
            self.areaList = res.data.regionList;
          })
          .catch(error => {

          });

      },

      /*上传*/
      openUpload (e) {
        this.type = e;
        this.isupload = true;
      },

      /*获取图片*/
      returnImgsFunc (e) {
        if (e != null) {
          this.filePath = e[0].filePath;
          this.form.logoImageId = e[0].fileId;
        }
        this.isupload = false;
      },

      /*选择的地址*/
      choseFunc (e) {
        this.form.coordinate = e.location.lat + ',' + e.location.lng;
        this.form.address = e.address;
      },

      /*取消*/
      cancelFunc () {
        this.$router.back(-1);
      }

    }

  };
</script>

<style lang="scss" scoped>
  .basic-setting-content {}

  .product-add {
    padding-bottom: 50px;
  }

  .img {
    margin-top: 10px;
  }
</style>
