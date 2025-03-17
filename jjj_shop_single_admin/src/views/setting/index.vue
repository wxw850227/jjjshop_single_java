<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <div class="common-form">商城后台设置</div>
      <el-form-item
        label="商城系统名称"
        :rules="[{ required: true, message: ' ' }]"
        prop="shopName"
      >
        <el-input
          v-model="form.shopName"
          placeholder="商城名称"
          class="max-w460"
        ></el-input>
        <el-col class="tips"> shop端商城名称，显示在登录页 </el-col>
      </el-form-item>
      <el-form-item label="商城登录背景" prop="shopBgImg">
        <el-input
          v-model="form.shopBgImg"
          placeholder="商城登录背景"
          class="max-w460"
        ></el-input>
        <el-col class="tips">
          shop端商城登录背景，不填则为系统默认登录背景，填写网络地址
        </el-col>
      </el-form-item>
    </el-form>
  </div>
  <!--提交-->
  <div class="common-level-rail">
    <el-button type="primary" @click="onSubmit" :loading="loading"
      >提交</el-button
    >
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";
export default {
  data() {
    return {
      /*是否正在加载*/
      loading: false,
      /*form表单数据*/
      form: {},
    };
  },
  created() {
    this.getParams();
  },

  methods: {
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.serviceDetail({}, true)
        .then((res) => {
          self.form = res.data;
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    /*提交*/
    onSubmit() {
      let self = this;
      let params = this.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.editService(params, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，设置成功",
                type: "success",
              });
              self.$router.push("/setting/Index");
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },
  },
};
</script>
<style>
.tips {
  color: #ccc;
}
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}
input[type="number"] {
  -moz-appearance: textfield;
}
.button-wrapper {
  display: flex;
}
</style>
