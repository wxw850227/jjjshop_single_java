<template>
  <el-dialog title="修改地址" v-if="areaList != ''" v-model="dialogVisible" :close-on-click-modal="false" :close-on-press-escape="false" width="900px">
    <el-input class="mb16" size="small" v-model="addressData.name" placeholder="请输入收货人"></el-input>
    <el-input class="mb16" size="small" v-model="addressData.phone" placeholder="请输入收获电话"></el-input>
    <el-select class="mb16" v-model="addressData.provinceId" placeholder="省" @change="initCity">
      <el-option :label="item.name" :value="item.id" v-for="(item, index) in areaList" :key="index"></el-option>
    </el-select>
    <el-select v-if="addressData.provinceId != ''" class="mb16 ml10" v-model="addressData.cityId" placeholder="市" @change="initRegion">
      <el-option :label="item1.name" :value="item1.id" v-for="(item1, index1) in cityList" :key="index1"></el-option>
    </el-select>
    <el-select v-if="addressData.cityId != ''" class="mb16 ml10" v-model="addressData.regionId" placeholder="区">
      <el-option :label="item2.name" :value="item2.id" v-for="(item2, index2) in regionList" :key="index2"></el-option>
    </el-select>
    <el-input class="mb16" size="small" v-model="addressData.detail" placeholder="请输入详细地址"></el-input>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible(false)">取 消</el-button>
        <el-button size="small" type="primary" @click="dialogFormVisible(true)">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import OrderApi from '@/api/order.js';
import DataApi from '@/api/data.js';
export default {
  data() {
    return {
      /*是否显示*/
      dialogVisible: false,
      /*结果类别*/
      type: 'error',
      /*传出去的参数*/
      params: null,
      reverse: false,
      orderId: 0,
      activities: [],
      /*省市区*/
      areaList: [],
      cityList: [],
      regionList: [],
      address: {
        name: '',
        phone: '',
        region: {
          province: '',
          provinceId: '',
          city: '',
          cityId: '',
          region: '',
          regionId: '',
          detail: ''
        }
      }
    };
  },
  props: ['isChange', 'addressData'],
  watch: {
    isChange: function(n, o) {
      if (n != o) {
        this.dialogVisible = n;
        this.type = 'error';
      }
    },
    'addressData.provinceId': function(n, o) {
      if (n != o) {
        this.areaList.forEach((item, index) => {
          if (item.id == n) {
            this.cityList = item.children;
          }
        });
      }
    },
    'addressData.cityId': function(n, o) {
      if (n != o) {
        this.cityList.forEach((item, index) => {
          if (item.id == n) {
            console.log(item);
            this.regionList = item.children;
          }
        });
      }
    }
  },
  
  mounted() {
    this.getData();
  },

  methods: {
    getData() {
      let self = this;
      // DataApi.getRegion({}, true)
      //   .then(res => {
      //     self.areaList = res.data;
      //     self.areaList.forEach((item, index) => {
      //       if (item.id == self.addressData.provinceId) {
      //         self.cityList = item.children;
      //       }
      //     });
      //     self.cityList.forEach((item, index) => {
      //       if (item.id == self.addressData.cityId) {
      //         console.log(item);
      //         self.regionList = item.children;
      //       }
      //     });
      //   })
      //   .catch(error => {});
    },
    /*初始化城市id*/
    initCity() {
      this.addressData.cityId = '';
    },
    /*初始化区id*/
    initRegion() {
      this.addressData.regionId = '';
    },
    /*关闭弹窗*/
    dialogFormVisible(flag) {
      console.log(flag);
      if (flag) {
        this.$emit('closeDialog', {
          type: this.type,
          openDialog: false,
          params: this.addressData
        });
      } else {
        this.$emit('closeDialog', false);
      }
    }
  }
};
</script>

<style></style>
