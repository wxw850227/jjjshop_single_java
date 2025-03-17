<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <!--添加门店-->
      <div class="common-form">订单流程设置</div>
      <el-form-item label="未支付订单">
        <el-input v-model="form.closeDays" type="number" style="width: 200px;" class="mr10"></el-input>
        <el-select size="small" v-model="form.closeType" placeholder="天" type="number" style="width: 80px;">
          <el-option v-for="(item, index) in closeTypeList" :key="index" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
        <span>&nbsp;后自动关闭</span>
        <div class="tips">
          订单下单未付款，n天后自动关闭，设置0天不自动关闭
        </div>
      </el-form-item>
      <el-form-item label="已发货订单">
        <el-input v-model="form.receiveDays" type="number" style="width: 200px;"></el-input>
        天后自动确认收货
        <div class="tips">
          如果在期间未确认收货，系统自动完成收货，设置0天不自动收货
        </div>
      </el-form-item>
      <el-form-item label="已完成订单">
        <el-input v-model="form.refundDays" type="number" style="width: 200px;"></el-input>
        天内允许申请售后
        <div class="tips">
          订单完成后 ，用户在n天内可以发起售后申请，设置0天不允许申请售后
        </div>
      </el-form-item>


      <div class="common-form">运费设置</div>
      <el-form-item label="运费组合策略">
        <div>
          <el-radio v-model="form.freightRule" :label="10">叠加</el-radio>
          <div class="tips">
            订单中的商品有多个运费模板时，将每个商品的运费之和订为订单总运费
          </div>

        </div>
        <div style="margin-top: 20px">
          <el-radio v-model="form.freightRule" :label="20">以低运费结算</el-radio>
          <div class="tips">
            订单中的商品有多个运费模板时，取订单中运费最少的商品的运费计为订单总运费
          </div>
        </div>
        <div style="margin-top: 20px">
          <el-radio v-model="form.freightRule" :label="30">以高运费结算</el-radio>
          <div class="tips">
            订单中的商品有多个运费模板时，取订单中运费最多的商品的运费计为订单总运费
          </div>
        </div>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>


  </div>

</template>

<script>
  import SettingApi from '@/api/setting.js';

  export default {
    data() {
      return {
        /*切换菜单*/
        // activeIndex: '1',
        /*form表单数据*/
        form: {
          closeDays: '',
          closeType: 1,
          receiveDays: '',
          refundDays: '',
          freightRule: '',
        },
        closeTypeList: [
          {
            id:1,
            name:'天'
          },
          {
            id:2,
            name:'小时'
          },
          {
            id:3,
            name:'分钟'
          }
        ],
        loading: false,


      };
    },
    created() {
      this.getParams()
    },

    methods: {

      getParams() {
        let self = this;
        SettingApi.tradeDetail({}, true)
          .then(res => {
             if (res.code == 1) {
              let vars = res.data;
              self.form.closeDays = vars.closeDays;
              self.form.receiveDays = vars.receiveDays;
              self.form.refundDays = vars.refundDays;
              self.form.freightRule = vars.freightRule;
            }
          })
          .catch(error => {

          });

      },
      //监听复选框选中
      handleCheckedCitiesChange(val) {},
      onSubmit() {
        let self = this;
        self.loading = true;
        let form = self.form;
        SettingApi.editTrade(form, true)
          .then(data => {
            self.loading = false;
            ElMessage ({
              message: '恭喜你，交易设置成功',
              type: 'success'
            });
            self.$router.push('/setting/trade/index');
          })
          .catch(error => {
            self.loading = false;
          });
      },


    }

  };
</script>

<style>
  .tips {
    color: #ccc;
  }
</style>
