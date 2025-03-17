<template>
  <div class="product-add">
    <el-form size="small" ref="form" :model="form" label-width="100px">
      <div class="common-form">商品推荐</div>
      <el-form-item label="商品推荐">
        <el-radio-group v-model="form.isRecommend">
          <el-radio :label="0">关</el-radio>
          <el-radio :label="1">开</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="模块名称" :rules="[{ required: true, message: ' ' }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入模块名称" class="max-w460"
          :disabled="form.isRecommend == 0 ? true : false"></el-input>
      </el-form-item>
      <el-form-item label="展示位置">
        <el-checkbox-group v-model="form.location">
          <el-checkbox v-for="(item, index) in all_type" :label="item.value" :key="index"
            :disabled="form.isRecommend == 0 ? true : false">{{ item.name }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="推荐商品">
        <el-radio-group v-model="form.choice">
          <el-radio :label="0" :disabled="form.isRecommend == 0 ? true : false">按条件选择</el-radio>
          <el-radio :label="1" :disabled="form.isRecommend == 0 ? true : false">自定义选择</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="" v-if="form.choice == 0">
        <el-select v-model="type" placeholder="" :disabled="form.isRecommend == 0 ? true : false">
          <el-option v-for="(item, index) in showType" :label="item.name" :value="item.value" :key="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="" v-if="form.choice == 0">
        <el-select v-model="num" placeholder="" :disabled="form.isRecommend == 0 ? true : false">
          <el-option v-for="(item, index) in showNum" :label="item.name" :value="item.value" :key="index"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item size="small" label="" v-if="form.choice == 1">
          <el-button size="small" icon="Plus" @click="addClick">添加商品</el-button>
        </el-form-item>
        <el-form-item  label="" >
        <div class="flex-1">
          <el-table size="large" :data="tableData" border>
            <el-table-column label="图片" width="60">
              <template #default="scope">
                <div class="product-info">
                  <div class="pic"><img :width="30" :height="30" v-img-url="scope.row.productImage" /></div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="productName" label="商品"></el-table-column>
            <el-table-column label="排序" width="100">
              <template #default="scope">
                <el-input v-model="scope.row.sort" type="number" :disabled="form.isRecommend == 0 ? true : false">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="120">
              <template #default="scope">
                <el-button @click="deleteClick(scope.row)" type="text" size="small"
                  :disabled="form.isRecommend == 0 ? true : false">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" size="small" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>
    <!--选择商品-->
    <Product :isProduct="isProduct" @closeDialog="closeProductDialogFunc($event)"></Product>
  </div>
</template>

<script>
  import RecommendApi from '@/api/recommend.js';
  import Product from '@/components/product/Product.vue';
  export default {
    components: {
      /*商品选择*/
      Product
    },
    data () {
      return {
        form: {
          isRecommend: 0,
          choice: 0,
          location: [],
          name: ''
        },
        all_type: [
          {
            name: '购物车',
            value: 10
          },
          {
            name: '会员中心',
            value: 20
          },
          {
            name: '支付完成页',
            value: 30
          }
        ],
        showType: [
          {
            name: '根据销量降序展示',
            value: 10
          },
          {
            name: '根据添加商品时间降序展示',
            value: 20
          },
          {
            name: '根据人气降序展示',
            value: 30
          }
        ],
        showNum: [
          {
            name: '显示当前20个',
            value: 20
          },
          {
            name: '显示当前30个',
            value: 30
          },
          {
            name: '显示当前50个',
            value: 50
          }
        ],
        type: 10,
        num: 20,
        tableData: [],
        productArr: [],
        isProduct: false,
        Data: [],
        loading: false
      };
    },
    created () {
      /*获取数据*/
      this.getData();
    },
    methods: {
      /*获取当前数据*/
      getData () {
        let self = this;
        let Params = {};
        RecommendApi.getData(Params, true)
          .then(res => {
            self.loading = false;
            self.form = res.data;
            self.form.isRecommend = parseInt(res.data.isRecommend);
            self.form.choice = parseInt(res.data.choice);
            // 转成整数，兼容组件
            for (let i = 0; i < self.form.location.length; i++) {
              self.form.location[i] = parseInt(self.form.location[i]);
            }
            if (self.form.choice == 1) {
              res.data.product.forEach((item, index) => {
                self.productArr.push(item.productId);
              });
              self.tableData = res.data.product;
            } else {
              self.type = parseInt(self.form.type);
              self.num = parseInt(self.form.num);
            }
            self.form.isRecommend = parseInt(self.form.isRecommend);
            self.form.choice = parseInt(self.form.choice);
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /**
       * 保存数据
       */
      onSubmit () {
        let self = this;
        let params = self.form;

        if (params.choice == 1) {
          params.product = self.tableData;
        } else {
          params.type = self.type;
          params.num = self.num;
        }
        if (!self.checkData(params)) {
          return false;
        }
        self.loading = true;
        RecommendApi.saveData(params, true)
          .then(data => {
            self.loading = false;
            if (data.code == 1) {
              ElMessage({
                message: '恭喜你，保存成功',
                type: 'success'
              });
              self.getData();
            } else {
              self.loading = false;
            }
          })
          .catch(error => {
            self.loading = false;
          });
      },
      /*关闭弹窗*/
      closeProductDialogFunc (e) {
        this.isProduct = e.openDialog;
        if (e.type == 'success') {
          if (this.productArr.indexOf(e.params.productId) > -1) {
          ElMessage({
              message: '你已选中该产品',
              type: 'error'
            });
            return false;
          }
          let param = {};
          param.productId = e.params.productId;
          param.productName = e.params.productName;
          param.productImage = e.params.imagePath;
          param.sort = 1;
          this.tableData.push(param);
          this.productArr.push(e.params.productId);
        }
      },
      /*添加商品*/
      addClick () {
        this.isProduct = true;
      },
      /*删除商品*/
      deleteClick (e) {
        let index = this.productArr.indexOf(e.productId);
        console.log(index);
        this.productArr.splice(index, 1);
        this.tableData.splice(index, 1);
      },
      /*数据验证*/
      checkData (param) {
        if (!param.name) {
          return false;
        }
        if (param.location.length == 0) {
          ElMessage({
            message: '请选择展示位置',
            type: 'error'
          });
          return false;
        }
        if (param.choice == 1 && param.product.length == 0) {
          ElMessage({
            message: '请添加商品',
            type: 'error'
          });
          return false;
        }
        return true;
      }
    }
  };
</script>

<style></style>
