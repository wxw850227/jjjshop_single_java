<template>
  <div>
    <el-form-item label="商品条码：" prop="model.sku.product_no">
      <el-input v-model="form.model.sku.productNo" class="max-w460"></el-input>
    </el-form-item>
    <el-form-item label="产品价格：" width="80" :rules="[{ required: true, message: '请填写产品价格' }]" prop="model.sku.productPrice">
      <el-input type="number" v-model="form.model.sku.productPrice" class="max-w460"></el-input>
    </el-form-item>
    <el-form-item label="产品划线价：" :rules="[{ required: true, message: '请填写产品划线价' }]" prop="model.sku.linePrice">
      <el-input type="number" v-model="form.model.sku.linePrice" class="max-w460"></el-input>
     </el-form-item>
    <el-form-item label="库存数量：" :rules="[{ required: true, message: '请填写库存数量' }]" prop="model.sku.stockNum">
      <el-input type="number" v-model="form.model.sku.stockNum" class="max-w460"></el-input>
    </el-form-item>
    <el-form-item label="商品重量(Kg)：" :rules="[{ required: true, message: '请填写商品重量' }]" prop="model.sku.productWeight">
      <el-input type="number" v-model="form.model.sku.productWeight" class="max-w460"></el-input>
    </el-form-item>
  </div>
</template>

<script>
  export default{
    inject: ['form']
  }
</script>

<style></style>
