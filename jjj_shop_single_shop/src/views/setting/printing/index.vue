<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--小票打印设置-->
      <div class="common-form">小票打印设置</div>

      <el-form-item label="是否开启小票打印">
        <div>
          <el-radio v-model="form.isOpen" :label="1">开启</el-radio>
          <el-radio v-model="form.isOpen" :label="0">关闭</el-radio>
        </div>
      </el-form-item>
      <el-form-item label="选择订单打印机">
        <el-select v-model="form.printerId" placeholder="请选择">
          <el-option v-for="(item,index) in printerList" :key="index" :label="item.printerName" :value="item.printerId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="订单打印方式">
     
          <el-checkbox v-model="checked" @change="handleCheckedCitiesChange">订单付款时</el-checkbox>
      
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
          isOpen: '',
          printerId: '',
          orderStatus: 0,
        },
        checked: false,
        printerList: [],
        loading: false,


      };
    },
    created() {
      this.getData()
    },

    methods: {
      getData() {
        let self = this;
        SettingApi.printingDetail({}, true)
          .then(res => {
            let vars = res.data;
            self.form.isOpen = vars.isOpen;
            self.form.printerId = vars.printerId;
            self.form.orderStatus = vars.orderStatus;
            self.printerList = vars.printerList;
            if (vars.orderStatus != null && vars.orderStatus == 20) {
              self.checked = true;
            }

          })
          .catch(error => {});
      },
      //提交表单
      onSubmit() {
        let self = this;
        let params = this.form;
        if (!self.form.printerId) {
          ElMessage .error('请选择订单打印机!');
          return;
        }
        self.loading = true;
        SettingApi.editPrinting(params, true)
          .then(data => {
            self.loading = false;
            ElMessage ({
              message: '恭喜你，打印设置成功',
              type: 'success'
            });
            // self.$router.push('/setting/Printing');

          })
          .catch(error => {
            self.loading = false;
          });
      },
      //监听复选框选中
      handleCheckedCitiesChange(e) {
        let self = this;
        if (e) {
          self.form.orderStatus = 20;
        } else {
          self.form.orderStatus = 0;
        }
      },

    }

  };
</script>

<style>
  .tips {
    color: #ccc;
  }
</style>
