<template>
  <div class="buy-set-content pl16 pr16">
    <!--会员折扣设置-->
    <div class="common-form mt50">会员折扣设置</div>
    <el-form-item label="是否开启会员折扣：">
      <el-radio-group v-model="form.model.isEnableGrade">
        <el-radio :label="1">开启</el-radio>
        <el-radio :label="0">关闭</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="会员折扣设置：">
      <el-radio-group v-model="form.model.isAloneGrade">
        <el-radio :label="0">默认折扣</el-radio>
        <el-radio :label="1">单独设置折扣</el-radio>
      </el-radio-group>
      <div class="gray9" v-if="form.model.isAloneGrade==0">默认折扣：默认为用户所属会员等级的折扣率</div>
    </el-form-item>

    <el-form-item label="折扣佣金类型：" v-if="form.model.isAloneGrade==1">
      <el-radio-group v-model="form.model.aloneGradeType" @change="changeGradeType">
        <el-radio :label="10">百分比</el-radio>
        <el-radio :label="20">固定金额</el-radio>
      </el-radio-group>
    </el-form-item>

    <el-form-item label="" v-if="form.model.isAloneGrade==1">
      <div class="max-w460">
        <el-table :data="form.gradeList" border size="small" style="width: 100%">
          <el-table-column prop="name" label="会员等级">
          </el-table-column>
          <el-table-column prop="name" label="折扣">
            <template #default="scope">
              <div class="d-s-c">
                <el-input v-model="scope.row.productEquity" type="number" placeholder="请输入折扣"></el-input>
                <span class="ml10">{{ gradeUnit }}</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form-item>

  </div>
</template>

<script>
  export default {
    data() {
      return {
        unit: '%',
        gradeUnit: '%'
      };
    },
    created() {
      if(this.form.model.aloneGradeType == 20){
        this.gradeUnit = '元';
      }
      if(this.form.model.agentMoneyType == 20){
        this.unit = '元';
      }
    },
    inject: ['form'],
    methods: {
      /*换算单位*/
      changeMoneyType: function(val) {
        if (val == '10') {
          this.unit = '%';
        } else {
          this.unit = '元';
        }
      },
	  /*换算单位*/
	  changeGradeType: function(val) {
	    if (val == '10') {
	      this.gradeUnit = '%';
	    } else {
	      this.gradeUnit = '元';
	    }
	  }
    }
  };
</script>

<style></style>
