<template>
  <el-dialog title="会员标签" v-model="dialogVisible" @close='dialogFormVisible' :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form size="small">
      <el-form-item label="标签">
        <el-checkbox-group v-model="checkedTag">
          <el-checkbox v-for="(item,index) in allTag" :label="item.tagId" :key="index" border>{{item.tagName}}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" :loading="loading" @click="onSubmit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
  import UserApi from '@/api/user.js';
  export default {
    data () {
      return {
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        loading: false,
        dialogVisible: false,
        allTag: [],
        checkedTag: []
      };
    },
    props: ['open_tag', 'form'],
    created () {
      this.dialogVisible = this.open_tag;
      this.getData();
    },
    methods: {
      /*获取数据*/
      getData () {
        let self = this;
        UserApi.toEditTag({
          userId: self.form.userId
        }, true)
          .then(res => {
            self.allTag = res.data.allTag;
            self.checkedTag = res.data.userTag;
          })
          .catch(error => { });
      },
      /*添加用户*/
      onSubmit () {
        let self = this;
        self.loading = true;
        UserApi.editTag({
          userId: self.form.userId,
          checkedTag: self.checkedTag
        }, true)
          .then(data => {
            self.loading = false;
            ElMessage ({
              message: '恭喜你，修改成功',
              type: 'success'
            });
            self.dialogFormVisible(true);
          })
          .catch(error => {
            self.loading = false;
          });
      },
      /*关闭弹窗*/
      dialogFormVisible (e) {
        if (e) {
          this.$emit('closeDialog', {
            type: 'success',
            openDialog: false
          })
        } else {
          this.$emit('closeDialog', {
            type: 'error',
            openDialog: false
          })
        }
      }

    }
  };
</script>

<style></style>