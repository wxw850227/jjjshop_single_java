<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--添加店员-->
      <div class="common-form">添加店员</div>

      <el-form-item label="选择用户">
        <el-row>
          <el-button @click="addClick" icon="Plus">选择会员</el-button>
          <div v-if="form.userId!=''" class="img">
            <img :src="userInfo.avatarurl" width="100" height="100" />
          </div>
          <div>
            <el-link :underline="false" disabled>选择后不可更改</el-link>
          </div>
        </el-row>
      </el-form-item>
      <el-form-item label="所属门店">
        <el-select v-model="form.storeId" placeholder="请选择">
          <el-option v-for="(item,index) in storeList" :key="index" :label="item.storeName" :value="item.storeId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店员姓名" prop="realName" :rules="[{required: true,message: '请输入店员姓名'}]">
        <el-input class="max-w460" v-model="form.realName" placeholder="请输入店员姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile" :rules="[{required: true,message: '请输入手机号'}]">
        <el-input class="max-w460" v-model="form.mobile" placeholder="请输入手机号"></el-input>
      </el-form-item>

      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>

    <!--添加-->
    <GetUser :is_open="open_add" @close="closeDialogFunc"></GetUser>
  </div>
</template>

<script>
  import StoreApi from '@/api/store.js';
  import GetUser from '@/components/user/GetUser.vue';
  export default {
    components: {
      /*编辑组件*/
      GetUser
    },
    data() {
      return {
        /*切换菜单*/
        // activeIndex: '1',
        /*form表单数据*/
        form: {
          userId: '',
          storeId: '',
          status: 1,
          realName: '',
          mobile: '',
        },
        user_info: {},
        /*是否打开添加弹窗*/
        open_add: false,
        storeList: [],
        /*是否正在加载*/
        loading:false
      };
    },
    created() {
      /*获取列表*/
      this.getTableList();
    },

    methods: {


      /*添加用户*/
      onSubmit() {
        let self = this;
        let form = self.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            StoreApi.addClerk(form, true)
              .then(data => {
                self.loading = false;
                if (data.code == 1) {
                  ElMessage ({
                    message: '恭喜你，店员添加成功',
                    type: 'success'
                  });
                  self.$router.push('/store/clerk/index');
                } else {
                  self.loading = false;
                }
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });


      },

      /*获取门店列表*/
      getTableList() {
        StoreApi.toAddClerk({}, true)
          .then(res => {
            this.storeList = res.data.storeList;
          })
          .catch(error => {

          });
      },

      /*打开弹出层*/
      addClick() {
        this.open_add = true;
      },

      /*关闭获取用户弹窗*/
      closeDialogFunc(e) {
        if (e && e.type != 'error') {
          this.userInfo = e.params[0];
          this.form.userId = e.params[0].userId;
        }
        this.open_add = false;
      },

      /*取消*/
      cancelFunc() {
        this.$router.back(-1);
      }


    }

  };
</script>

<style lang="scss" scoped>
  .basic-setting-content {}

  .product-add {
    padding-bottom: 50px;
  }

  .img {
    margin-top: 10px;
  }
</style>
