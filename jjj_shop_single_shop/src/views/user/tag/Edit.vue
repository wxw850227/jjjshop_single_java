<template>
  <el-dialog title="编辑等级" v-model="dialogVisible" @close='dialogFormVisible' :close-on-click-modal="false"
    :close-on-press-escape="false" width="600px">
    <el-form size="small" :model="form" ref="form">
      <el-form-item label="标签名称" :label-width="formLabelWidth" prop="tagName" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.tagName" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" :disabled="submit_loading" @click="editGrade">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
  import TagApi from '@/api/tag.js';
  export default {
    data () {
      return {
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        dialogVisible: false,
        /*是否正在提交*/
        submit_loading: false,
      };
    },
    props: ['open_edit', 'form'],
    created () {
      this.dialogVisible = this.open_edit;
    },
    methods: {
      /*修改用户*/
      editGrade () {
        let self = this;
        let params = this.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.submit_loading = true;
            TagApi.editTag(params, true)
              .then(data => {
                self.submit_loading = false;
                ElMessage ({
                  message: '恭喜你，修改成功',
                  type: 'success'
                });
                self.dialogFormVisible(true);

              })
              .catch(error => {
                self.submit_loading = false;
              });

          }
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