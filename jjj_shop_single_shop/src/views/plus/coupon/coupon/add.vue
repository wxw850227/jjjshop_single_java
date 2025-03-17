<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <el-tabs v-model="activeName" type="card">
        <el-tab-pane label="基本信息" name="1"></el-tab-pane>
        <el-tab-pane label="适用商品" name="2"></el-tab-pane>
      </el-tabs>
      <template v-if="activeName == '1'">
        <div class="common-form">添加优惠券</div>
        <el-form-item label="优惠券名称" prop="name" :rules="[{ required: true, message: ' ' }]">
          <el-input v-model="form.name" placeholder="请输入优惠券名称"></el-input>
          <div class="tips">例如：满100减10</div>
        </el-form-item>
        <el-form-item label="优惠券颜色">
          <el-radio-group v-model="form.color">
            <el-radio label="10">蓝色</el-radio>
            <el-radio label="20">红色</el-radio>
            <el-radio label="30">紫色</el-radio>
            <el-radio label="40">黄色</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优惠券类型">
          <el-radio-group v-model="form.couponType">
            <el-radio label="10">满减券</el-radio>
            <el-radio label="20">折扣券</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-if="form.couponType == 10">
          <el-form-item label="减免金额"><el-input v-model="form.reducePrice" placeholder="请输入减免金额" type="number"></el-input></el-form-item>
        </div>
        <div v-else>
          <el-form-item label="折扣率" :label-width="formLabelWidth" props="discount" :rules="[{ required: true, message: '请输入折扣率' }]">
            <el-input type="number" :precision="1" :step="1" :min="0" :max="100" placeholder="请输入折扣率" v-model="form.discount" class="max-w460" @input="validateDiscount">
              <template #append>
                %
              </template>
            </el-input>
           <div style="color: red;" v-if="showDiscountError">折扣率必须是1到100之间的整数</div>
            <div class="tips">折扣率范围1-100，95代表9.5折，0代表不折扣</div>
          </el-form-item>
          <!--          <el-form-item label="折扣率 ">
            <el-input v-model="form.discount" placeholder="请输入折扣率" type="number"></el-input>
            <div class="tips">折扣率范围0-10，9.5代表9.5折，0代表不折扣</div>
          </el-form-item> -->
        </div>

        <el-form-item label="最低消费金额"><el-input v-model="form.minPrice" placeholder="请输入最低消费金额" type="number"></el-input></el-form-item>

        <el-form-item label="到期类型">
          <el-radio-group v-model="form.expireType">
            <el-radio label="10">领取后生效</el-radio>
            <el-radio label="20">固定时间</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-if="form.expireType == 10">
          <el-form-item label="有效天数"><el-input v-model="form.expireDay" placeholder="请输入有效天数" type="number"></el-input></el-form-item>
        </div>
        <div v-else>
          <el-form-item label="有效时间">
            <el-date-picker
              v-model="form.activeTime"
              type="daterange"
              align="right"
              unlink-panels
              value-format="YYYY-MM-DD"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions0"
            ></el-date-picker>
          </el-form-item>
        </div>
        <el-form-item label="发放总数量 " prop="totalNum" :rules="[{ required: true, message: ' ' }]">
          <el-input v-model="form.totalNum" placeholder="请输入发放总数量" type="number"></el-input>
          <div class="tips">限制领取的优惠券数量，-1为不限制</div>
        </el-form-item>
        <el-form-item label="是否显示在领券中心">
          <el-radio-group v-model="form.showCenter">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">不显示</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="使用条件">
          <el-radio-group v-model="form.freeLimit">
            <el-radio :label="0">不限制</el-radio>
            <el-radio :label="1">不可与促销同时使用</el-radio>
            <el-radio :label="2">不可与等级优惠同时使用</el-radio>
            <el-radio :label="3">不可于促销和等级优惠同时使用</el-radio>
          </el-radio-group>
          <div class="tips">促销是指满减，等级优惠券值商品的会员等级折扣</div>
        </el-form-item>
        <el-form-item label="排序"><el-input type="number" v-model="form.sort" placeholder="请输入排序"></el-input></el-form-item>
      </template>
      <template v-if="activeName == '2'">
        <div class="common-form">适用商品</div>
        <el-form-item label="选择">
          <el-radio-group v-model="form.applyRange">
            <el-radio :label="10">全部商品</el-radio>
            <el-radio :label="20"><span>指定商品</span></el-radio>
            <el-radio :label="30"><span>指定分类</span></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.applyRange == 20">
          <el-button class="mb10" @click="isProduct = true" type="primary" plain>添加商品</el-button>
          <el-table v-if="productList.length > 0" :data="productList" max-height="400" border style="width: 100%">
            <el-table-column prop="productId" label="ID" width="180"></el-table-column>
            <el-table-column prop="productName" label="商品名称" width="180"></el-table-column>
            <el-table-column prop="image" label="图片">
              <template #default="scope">
                <img v-img-url="hasImages(scope.row.imagePath)" alt="" :width="80" />
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button @click="deleteClick(scope.$index)" type="text" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="" v-if="form.applyRange == 30">
          <el-button class="mb10" @click="isCategory = true" type="primary" plain>添加分类</el-button>
          <div>
            <el-tag size="large" class=" mr10 mb10" type="info" v-if="categoryList.first.length > 0" v-for="(item, index) in categoryList.first" :key="item.categoryId">
              {{ item.parent ? item.parent + '→' + item.name : item.name }}
            </el-tag>
            <el-tag size="large" class=" mr10 mb10" type="info" v-if="categoryList.second.length > 0" v-for="(item, index) in categoryList.second" :key="item.categoryId">
              {{ item.parent ? item.parent + '→' + item.name : item.name }}
            </el-tag>
          </div>
        </el-form-item>
      </template>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="info" size="small" @click="cancelFunc" :loading="loading">取消</el-button>
        <el-button type="primary" size="small" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>
    <!--选择商品-->
    <Product :isProduct="isProduct" :excludeIds="excludeIds" :islist="true" @closeDialog="closeProductFunc"></Product>
    <Category :isCategory="isCategory" :excludeIds="categoryIds" @close="closeCategoryFunc"></Category>
  </div>
</template>

<script>
import CouponApi from '@/api/coupon.js';
import Product from '@/components/product/Product.vue';
import Category from '@/components/category/category.vue';
export default {
  components: {
    Product,
    Category
  },
  data() {
    return {
      activeName: '1',
      /*切换菜单*/
      activeIndex: '1',
      /*form表单数据*/
      form: {
        color: '10',
        couponType: '10',
        expireType: '10',
        sort: 1,
        activeTime: [],
        showCenter: 1,
        /*优惠限制0不显示1不可与促销同时2不可与等级优惠同时3不可于促销和等级同*/
        freeLimit: 0,
        applyRange: 10
      },
      loading: false,
      pickerOptions0: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7; //如果没有后面的-8.64e7就是不可以选择今天的
        }
      },
      isCategory: false,
      isProduct: false,
      excludeIds: [],
      productList: [],
      categoryList: {
        first: [],
        second: []
      },
      categoryListFirst: [],
      categoryListSecond: []
    };
  },
  created() {},

  methods: {
    hasImages(e) {
      if (e) {
        return e;
      } else {
        return '';
      }
    },
   validateDiscount(value) {
      const discountPattern = /^[1-9]\d?$|^100$/; 
      if (!discountPattern.test(value)) {
       this.showDiscountError = true;
      } else {
         this.showDiscountError = false;
        this.form.discount = value;
      }
  },
    /*添加用户*/
    onSubmit() {
      let self = this;
      let form = self.form;
      form.productIds = self.excludeIds;
      form.categoryListFirst = self.categoryListFirst;
      form.categoryListSecond = self.categoryListSecond;
      self.$refs.form.validate(valid => {
        if (valid) {
          self.loading = true;
          CouponApi.addCoupon(form, true)
            .then(data => {
              self.loading = false;
              ElMessage ({
                message: '恭喜你，添加成功',
                type: 'success'
              });
              self.$router.push('/plus/coupon/index');
            })
            .catch(error => {
              self.loading = false;
            });
        }
      });
    },
    closeCategoryFunc(e) {
      let self = this;
      self.isCategory = false;
      if (e) {
        self.categoryList = e;
        self.categoryListFirst = [];
        e.first.forEach((item, index) => {
          self.categoryListFirst.push(item.categoryId);
        });
        self.categoryListSecond = [];
        e.second.forEach((item, index) => {
          self.categoryListSecond.push(item.categoryId);
        });
      }
    },
    /*关闭商品*/
    closeProductFunc(e) {
      let self = this;
      self.isProduct = e.openDialog;
      if (e.type == 'success') {
        e.params.forEach((item, index) => {
          let params = {
            productId: item.productId,
            productName: item.productName,
            imagePath: item.image
          };
          self.excludeIds.push(params.productId);
          self.productList.push(params);
        });
      }
    },
    /*取消*/
    cancelFunc() {
      this.$router.push({
        path: '/plus/coupon/index'
      });
    },
    deleteClick(e) {
      this.excludeIds.splice(e, 1);
      this.productList.splice(e, 1);
    }
  }
};
</script>

<style lang="scss" scoped>
.mb10 {
  margin-bottom: 10px;
}

.product-add {
  padding-bottom: 50px;
}

.tips {
  color: #ccc;
}
</style>
