<template>
  <el-dialog
    title="添加插件"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-table
      size="small"
      :data="categoryList"
      border
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="name" label="操作" width="80">
        <template #default="scope">
          <el-button @click="addClick(scope.row)" size="small">添加</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import PlusApi from "@/api/plus.js";
export default {
  data() {
    return {
      form: {},
      categoryList: [],
      srot: "1",
      radio: "1",
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      /*是否正在加载*/
      loading: true,
    };
  },
  props: {
    open_add: Boolean,
    curModel: Object,
  },
  created() {
    this.dialogVisible = this.open_add;
    /*获取插件列表*/
    this.getCategoryList();
  },
  methods: {
    /*获取插件*/
    getCategoryList() {
      let self = this;
      PlusApi.getplus({}, true)
        .then((res) => {
          self.loading = false;
          self.categoryList = res.data;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*添加插件*/
    addClick(e) {
      let self = this;
      let params = {
        id: e.accessId,
        plusCategoryId: self.curModel.plusCategoryId,
      };
      PlusApi.addplus(params, true)
        .then((res) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，添加成功",
            type: "success",
          });
          self.dialogFormVisible(true);
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("closeDialog", {
          type: "success",
          openDialog: false,
        });
      } else {
        this.$emit("closeDialog", {
          type: "error",
          openDialog: false,
        });
      }
    },
  },
};
</script>

<style></style>
