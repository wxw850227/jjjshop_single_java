<template>
  <div class="product-add">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="基础设置" name="basic"></el-tab-pane>
      <el-tab-pane label="规格库存" name="spec"></el-tab-pane>
      <el-tab-pane label="商品详情" name="content"></el-tab-pane>
      <el-tab-pane label="高级设置" name="buyset"></el-tab-pane>
    </el-tabs>
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="180px">
      <!--基础信息-->
      <Basic v-show="activeName == 'basic'"></Basic>
      <!--规格设置-->
      <Spec v-show="activeName == 'spec'"></Spec>
      <!--商品详情-->
      <Content v-show="activeName == 'content'"></Content>
      <!--高级设置-->
      <Buyset v-show="activeName == 'buyset'"></Buyset>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">发布</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import PorductApi from '@/api/product.js';
import Basic from './part/Basic.vue';
import Spec from './part/Spec.vue';
import Content from './part/Content.vue';
import Buyset from './part/Buyset.vue';
export default {
  components: {
    /*基础信息*/
    Basic,
    /*规格信息*/
    Spec,
    /*商品详情*/
    Content,
    /*高级设置*/
    Buyset
  },
  data() {
    return {
      activeName: 'basic',
      /*是否正在加载*/
      loading: false,
      /*form表单数据*/
      form: {
        model: {
          /*商品名称*/
          productName:'',
          productNo: '',
          videoId: 0,
          posterId: 0,
          /*商品分类*/
          categoryId: null,
          /*商品图片*/
          image: [],
          /*是否纯图*/
          isPicture: 0,
          contentImage: [],
          video: {
            fileId: 0,
            filePath: ''
          },
          poster: {
            fileId: 0,
            filePath: ''
          },
          /*商品卖点*/
          sellingPoint:'',
          /*规格类别,默认10单规格，20多规格*/
          specType: 10,
          /*库存计算方式,默认20付款减库存，10下单减库存*/
          deductStockType: 20,
          /*检查用户等级*/
          isAloneGrade: 0,
          sku: {},
          /*多规格类别*/
          specMany: {
            /*多规格类别*/
            specAttr: [],
            /*多规格表格数据*/
            specList: []
          },
          /*商品详情内容*/
          content: '',
          /*运费模板ID*/
          deliveryId: '',
          /*商品状态*/
          productStatus: 10,
          /*初始销量*/
          salesInitial: 0,
          /*商品排序，默认100*/
          productSort: 100,
          /*是否开启积分赠送,默认1为开启，0为关闭*/
          isPointsGift: 1,
          /*是否允许使用积分抵扣,默认1为允许，0为不允许*/
          isPointsDiscount: 1,
          /*最大积分抵扣数量*/
          maxPointsDiscount: 0,
          /*会员折扣设置,默认1为单独设置折扣,0为默认折扣*/
          isEnableGrade: 1,
          /*等级金额类型,默认10为百分比，20为固定金额*/
          aloneGradeType: 10,
          /*限购会员*/
          gradeIds: [],
          /*是否虚拟商品*/
          isVirtual: 0,
          /*限购数量*/
          limitNum: 0,
          /*虚拟商品发货方式*/
          virtualAuto: 0,
          /*虚拟商品发货内容*/
          virtualContent: '',
          /*关联表单*/
          tableId: ''
        },
        /*商品分类*/
        category: [],
        /*运费模板*/
        delivery: [],
        /*会员等级*/
        gradeList: [],
        /*规格数据*/
        specData: null,
        /*是否锁住*/
        isSpecLocked: false,
        /*万能表单*/
        tableList: []
      }
    };
  },
  provide: function () {
    return {
      form: this.form
    }
  },
  created() {

    /*获取基础数据*/
    this.getBaseData();

  },
  methods: {

    /*获取基础数据*/
    getBaseData: function() {
      let self = this;
      PorductApi.getBaseData({}, true)
        .then(res => {
          self.loading = false;
          Object.assign(self.form, res.data);
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*转JSON字符串*/
    convertJson(list){
      let obj={};
      list.forEach(item=>{
        obj[item.gradeId] = item.productEquity;
      });
      return JSON.stringify(obj);
    },

    /*提交*/
    onSubmit: function() {
      let self = this;
      let params = self.form.model;
      self.$refs.form.validate(valid => {
        if (valid) {
          self.loading = true;
          params.aloneGradeEquity = self.convertJson(self.form.gradeList);
          if(params.gradeIds.length > 0){
            params.gradeIds = params.gradeIds.join(',');
          }else{
            params.gradeIds = '';
          }
          PorductApi.addProduct(params, true)
            .then(data => {
              self.loading = false;
              ElMessage ({
                message: '添加成功',
                type: 'success'
              });
              self.$router.push('/product/product/index');
            })
            .catch(error => {
              self.loading = false;
            });
        }
      });
    },

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    }

  }
};
</script>

<style lang="scss" scoped>
.basic-setting-content {
}

.product-add {
  padding-bottom: 100px;
}
</style>
