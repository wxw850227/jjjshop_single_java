<template>
  <el-dialog
    title="添加菜单&权限"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form size="small" :model="formData" :rules="formRules" ref="form">
      <!--菜单名称-->
      <el-form-item label="菜单名称" prop="name" :label-width="formLabelWidth">
        <el-input
          v-model="formData.name"
          autocomplete="off"
          placeholder="请输入菜单名称"
        ></el-input>
      </el-form-item>
      <!--类型-->
      <el-form-item label="类型" prop="name" :label-width="formLabelWidth">
        <el-radio-group v-model="formData.isRoute">
          <el-radio :label="1">页面</el-radio>
          <el-radio :label="0">按钮</el-radio>
          <el-radio :label="2">独立单页面</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--上级菜单-->
      <el-form-item
        label="上级菜单"
        prop="parentId"
        :label-width="formLabelWidth"
      >
        <el-cascader
          size="small"
          v-model="parentsVal"
          :options="accessList"
          :props="propsParam"
          @change="handleChange"
        ></el-cascader>
      </el-form-item>
      <!--路径-->
      <el-form-item label="路径" prop="path" :label-width="formLabelWidth">
        <el-input
          v-model="formData.path"
          autocomplete="off"
          placeholder="请输入组件文件路径"
        ></el-input>
        <p>提示：对应前端给的文件路径，例如：/index/index</p>
      </el-form-item>
      <!--图标-->
      <el-form-item label="图标" :label-width="formLabelWidth">
        <el-input
          v-model="formData.icon"
          autocomplete="off"
          placeholder="请输入icon"
        ></el-input>
        <p>提示：请选择系统提供的图标</p>
      </el-form-item>
      <!--是否是菜单-->
      <el-form-item
        label="是否是菜单"
        :label-width="formLabelWidth"
        v-if="formData.isRoute == 1"
      >
        <el-radio-group v-model="formData.isMenu">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--是否显示-->
      <el-form-item label="是否显示" :label-width="formLabelWidth">
        <el-radio-group v-model="formData.isShow">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--重定向-->
      <el-form-item
        label="重定向"
        :label-width="formLabelWidth"
        v-if="formData.isRoute == 1"
      >
        <el-input
          v-model="formData.redirectName"
          autocomplete="off"
          placeholder="请输入重定向地址"
        ></el-input>
      </el-form-item>
      <!--备注-->
      <el-form-item label="备注" prop="sort" :label-width="formLabelWidth">
        <el-input
          v-model="formData.remark"
          placeholder="请输入备注"
          type="textarea"
        ></el-input>
      </el-form-item>
      <!--排序-->
      <el-form-item label="排序" prop="sort" :label-width="formLabelWidth">
        <el-input
          v-model="formData.sort"
          placeholder="请输入排序"
          type="number"
        ></el-input>
      </el-form-item>
    </el-form>
    <!--操作按钮-->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="onSubmit()" :disabled="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import AccessApi from "@/api/access.js";
import { deepClone, formatModel } from "@/utils/base.js";
export default {
  data() {
    return {
      /*是否加载中*/
      loading: false,
      /*form表单数据对象*/
      formData: {
        /*菜单名称*/
        name: "",
        /*路由地址*/
        path: "",
        /*组件名*/
        views: "",
        /*别名*/
        alias: "",
        /*图标*/
        icon: "",
        /*是否是菜单*/
        isMenu: 0,
        /*是否是路由*/
        isRoute: 1,
        /*是否显示*/
        isShow: 1,
        /*排序*/
        sort: 1,
        /*父集ID*/
        parentId: 0,
        remark: "",
        redirectName: "",
      },
      /*验证规则*/
      formRules: {
        name: [{ required: true, message: "请输入菜单名称", trigger: "blur" }],
        path: [{ required: true, message: "请输入路径", trigger: "blur" }],
        views: [{ required: true, message: "请输入组件名称", trigger: "blur" }],
        alias: [{ required: true, message: "请输入别名", trigger: "blur" }],
      },
      /*当前父集ID*/
      parentsVal: [],
      /*菜单列表*/
      accessList: [],
      /*排序*/
      srot: "1",
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      /*展示数据*/
      propsParam: {
        label: "name",
        value: "accessId",
        checkStrictly: true,
      },
    };
  },
  props: {
    open_add: Boolean,
    add_type: String,
    rawData: Array,
    selectModel: Object,
  },
  created() {
    this.dialogVisible = this.open_add;
    this.accessList = deepClone(this.rawData);
    this.accessList.unshift({ name: "顶级菜单", accessId: 0 });
    if (this.add_type == "copy") {
      this.formData = formatModel(this.formData, this.selectModel);
      this.findParentsID(this.accessList);
    } else if (this.add_type == "child") {
      this.formData.parentId = this.selectModel.accessId;
      this.findParentsID(this.accessList);
    }
  },
  methods: {
    /*选择菜单*/
    handleChange(e) {},

    /*查找父集id*/
    findParentsID(list) {
      let flag = false;
      for (let i = 0; i < list.length; i++) {
        let item = list[i];
        if (item.accessId == this.formData.parentId) {
          this.parentsVal.unshift(item.accessId);
          flag = true;
          break;
        } else {
          let children = item.children;
          if (typeof children != "undefined" && children != null) {
            if (this.findParentsID(children)) {
              this.parentsVal.unshift(item.accessId);
              flag = true;
              break;
            }
          }
        }
      }
      return flag;
    },

    /*添加菜单*/
    onSubmit() {
      let self = this;
      let params = this.formData;
      if (self.parentsVal.length > 0) {
        params.parentId = self.parentsVal[self.parentsVal.length - 1];
      }
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          AccessApi.addAccess(params, true)
            .then((res) => {
              if (res.code == 1) {
                ElMessage({
                  message: res.msg,
                  type: "success",
                });
                self.dialogFormVisible(true);
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        }
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
