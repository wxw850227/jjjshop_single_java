<template>
  <div class="user">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="formInline"
        class="demo-form-inline"
      >
        <el-form-item label="用户昵称">
          <el-input
            v-model="formInline.nickName"
            placeholder="请输入昵称"
          ></el-input>
        </el-form-item>
        <el-form-item label="余额变动场景">
          <el-select v-model="formInline.sceneText" placeholder="请选择">
            <el-option label="全部" value="全部"></el-option>
            <el-option
              v-for="(item, index) in scene"
              :key="index"
              :label="item.text"
              :value="item.text"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起始日期">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
              v-model="formInline.startDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择开始日期"
            >
            </el-date-picker>
            <span>-</span>
            <el-date-picker
              v-model="formInline.endDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择结束日期"
            >
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column
            prop="userId"
            label="ID"
            width="80"
          ></el-table-column>
          <el-table-column label="微信头像">
            <template #default="scope">
              <img :src="scope.row.avatarurl" :width="30" :height="30" />
            </template>
          </el-table-column>
          <el-table-column prop="nickName" label="昵称"></el-table-column>
          <el-table-column prop="scene" label="余额变动场景">
            <template #default="scope">
              <span v-if="scope.row.scene == 10" style="color: #409eff">{{
                scope.row.sceneText
              }}</span>
              <span v-if="scope.row.scene == 20" style="color: #67c23a">{{
                scope.row.sceneText
              }}</span>
              <span v-if="scope.row.scene == 30" style="color: #f56c6c">{{
                scope.row.sceneText
              }}</span>
              <span v-if="scope.row.scene == 40" style="color: #e6a23c">{{
                scope.row.sceneText
              }}</span>
              <span v-if="scope.row.scene == 50" style="color: #409eff">{{
                scope.row.sceneText
              }}</span>
              <span v-if="scope.row.scene == 60" style="color: #409eff">{{
                scope.row.sceneText
              }}</span>
              <span v-if="scope.row.scene == 70" style="color: #67c23a">{{
                scope.row.sceneText
              }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="money" label="变动金额	">
            <template #default="scope">
              <p v-if="scope.row.money > 0">+{{ scope.row.money }}</p>
              <p v-else="">{{ scope.row.money }}</p>
            </template>
          </el-table-column>
          <el-table-column
            prop="describe"
            label="描述/说明"
            width="200"
          ></el-table-column>
          <el-table-column prop="remark" label="管理员备注">
            <template #default="scope">
              <p v-if="scope.row.remark == ''">--</p>
              <p v-else="">{{ scope.row.remark }}</p>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            width="140"
          ></el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page="curPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="totalDataNumber"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import UserApi from "@/api/user.js";
export default {
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*一页多少条*/
      pageSize: 15,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*横向表单数据模型*/
      formInline: {
        nickName: null,
        sceneText: "",
      },
      /*场景*/
      scene: [],
      /*时间*/
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        },
      },
    };
  },
  created() {
    /*获取列表*/
    this.getTableList();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      self.getTableList();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getTableList();
    },

    /*获取列表*/
    getTableList() {
      let self = this;
      let Params = self.formInline;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      UserApi.BalanceLog(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.list.records;
          self.totalDataNumber = res.data.list.total;
          self.scene = res.data.scene;
        })
        .catch((error) => {});
    },

    /*搜索查询*/
    onSubmit() {
      let self = this;
      self.loading = true;
      let Params = self.formInline;
      self.getTableList();
    },

    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getTableList();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getTableList();
        }
      }
    },
  },
};
</script>
<style></style>
