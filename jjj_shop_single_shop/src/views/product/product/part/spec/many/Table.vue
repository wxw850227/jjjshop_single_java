<template>
    <div class="mt16">
      <el-form-item label="规格明细：" v-if="form.model.specMany && form.model.specMany.specList.length > 0">
        <div>
          批量设置
          <el-input size="small" v-model="batchData.productPrice" placeholder="销售价" style="width: 160px;padding-left: 4px;"></el-input>
          <el-input size="small" v-model="batchData.linePrice" placeholder="划线价" style="width: 160px;padding-left: 4px;"></el-input>
          <el-input size="small" v-model="batchData.stockNum" placeholder="库存" style="width: 160px;padding-left: 4px;"></el-input>
          <el-input size="small" v-model="batchData.productWeight" placeholder="重量" style="width: 160px;padding-left: 4px;"></el-input>
          <span></span>
          <el-button size="small" @click="onSubmitBatchData">应用</el-button>
        </div>
        <!--多规格表格-->
        <div >
          <el-table size="small" :data="form.model.specMany.specList" :span-method="objectSpanMethod" border style="width: 100%; margin-top: 20px">
            <el-table-column :label="item.groupName" :key="item.groupName" v-for="(item, index) in form.model.specMany.specAttr">
              <template #default="scope">
                {{ scope.row.rows[index].specValue }}
              </template>
            </el-table-column>
            <el-table-column label="规格图片">
              <template #default="scope">
                <img v-img-url="scope.row.specForm.imagePath" alt="" width="35" height="35" @click="chooseSpecImage(scope.$index)" />
              </template>
            </el-table-column>
            <el-table-column label="商品条码">
              <template #default="scope">
                <el-form-item label="" style="margin-bottom: 0;"><el-input size="small" prop="productNo" v-model="scope.row.specForm.productNo"></el-input></el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="销售价">
              <template #default="scope">
                <el-form-item
                  label=""
                  :rules="[{ required: true, message: ' ' }]"
                  :prop="'model.specMany.specList.' + scope.$index + '.specForm.productPrice'"
                  style="margin-bottom: 0;"
                >
                  <el-input size="small" prop="productPrice" v-model="scope.row.specForm.productPrice"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="划线价">
              <template #default="scope">
                <el-form-item label="" style="margin-bottom: 0;"><el-input size="small" prop="linePrice" v-model="scope.row.specForm.linePrice"></el-input></el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="库存">
              <template #default="scope">
                <el-form-item
                  label=""
                  :rules="[{ required: true, message: ' ' }]"
                  :prop="'model.specMany.specList.' + scope.$index + '.specForm.stockNum'"
                  style="margin-bottom: 0;"
                >
                  <el-input size="small" prop="stockNum" v-model="scope.row.specForm.stockNum"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="重量(kg)">
              <template #default="scope">
                <el-form-item
                  label=""
                  :rules="[{ required: true, message: ' ' }]"
                  :prop="'model.specMany.specList.' + scope.$index + '.specForm.productWeight'"
                  style="margin-bottom: 0;"
                >
                  <el-input size="small" prop="productWeight" v-model="scope.row.specForm.productWeight"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>
      <!--上传图片组件-->
      <Upload v-if="isupload" :isupload="isupload" @returnImgs="returnImgsFunc">上传图片</Upload>
    </div>
</template>

<script>
  import PorductApi from '@/api/product.js';
  import Upload from '@/components/file/Upload.vue';
  export default{
    components:{
      Upload
    },
    data() {
      return {
        /*批量设置sku属性*/
        batchData: {
          productPrice: '',
          linePrice: '',
          stockNum: '',
          productWeight: ''
        },
        /*图片是否打开*/
        isupload: false,
        //上传图片选择的下标
        specIndex: -1
      };
    },
    inject: ['form'],
    created() {

    },
    methods: {

      /*表格跨行*/
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        var specAttr = this.form.model.specMany.specAttr;
        // 规格组合总数 (table行数)
        var totalRow = 1;
        //比如2个规格,只有第一列有多行
        if (columnIndex < specAttr.length - 1) {
          let startRowIndex = columnIndex + 1;
          let endRowIndex = specAttr.length - 1;
          for (var i = startRowIndex; i <= endRowIndex; i++) {
            totalRow *= specAttr[i].specItems.length;
          }
          if (rowIndex % totalRow === 0) {
            return {
              rowspan: totalRow,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },

      /*批量设置sku属性*/
      onSubmitBatchData: function() {
        var self = this;
        self.form.model.specMany.specList.forEach(function(value) {
          for (var key in self.batchData) {
            if (self.batchData.hasOwnProperty(key) && self.batchData[key]) {
              value.specForm[key] = self.batchData[key];
            }
          }
        });
      },

      /*选择图片*/
      chooseSpecImage: function(specIndex) {
        this.isupload = true;
        this.specIndex = specIndex;
      },

      /*返回图片*/
      returnImgsFunc(e) {
        this.isupload = false;
        this.form.model.specMany.specList[this.specIndex].specForm.imageId = e[0]['fileId'];
        this.form.model.specMany.specList[this.specIndex].specForm.imagePath = e[0]['filePath'];
      }
    }
  }
</script>

<style>
</style>
