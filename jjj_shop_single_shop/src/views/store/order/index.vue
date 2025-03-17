<template>
  <div class="user">

    <div class="common-seach-wrap">
      <el-form size="small" :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="所属门店">
          <el-select v-model="formInline.storeId" placeholder="请选择">
            <el-option label="全部" value="0"></el-option>
            <el-option v-for="(item,index) in storeList" :key="index" :label="item.storeName" :value="item.storeId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="核销员姓名">
          <el-input v-model="formInline.search" placeholder="请输入核销员姓名"></el-input>
        </el-form-item>
        <el-form-item label="起始时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
              size="small"
              v-model="formInline.createTime"
              type="daterange"
              value-format="YYYY-MM-DD"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="storeId" label="ID"></el-table-column>
          <el-table-column prop="storeName" label="核销门店">
            <template #default="scope">
              <span class="fb orange">{{scope.row.storeName}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="realName" label="核销员"></el-table-column>
          <el-table-column prop="orderNo" label="订单号"></el-table-column>
          <el-table-column prop="orderTypeText" label="订单类型"></el-table-column>
          <el-table-column prop="createTime" label="核销时间"></el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background :current-page="curPage"
          :page-size="pageSize" layout="total, prev, pager, next, jumper"
          :total="totalDataNumber">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
  import StoreApi from '@/api/store.js';
  export default {
    data() {
      return {
        /*是否加载完成*/
        loading: true,
        /*列表数据*/
        tableData: [],
        /*所属门店数据*/
        storeList: [],
        /*一页多少条*/
        pageSize: 20,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
        /*横向表单数据模型*/
        formInline: {
          storeId: '',
          search: '',
          createTime:''
        },
        /*是否打开添加弹窗*/
        open_add: false,
        /*是否打开编辑弹窗*/
        open_edit: false,
        /*当前编辑的对象*/
        userModel: {}
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
         self.loading = true;
        StoreApi.orderList(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.list.records;
            self.totalDataNumber = res.data.list.total;
            self.storeList = res.data.storeList;
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*搜索查询*/
      onSubmit() {
        this.curPage = 1;
        this.getTableList();
      },

    }
  };
</script>

<style></style>
