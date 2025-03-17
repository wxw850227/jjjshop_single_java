<template>
  <div class="user">
    <div class="common-form">地区列表</div>
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="名称"
          ><el-input
            v-model="searchForm.name"
            placeholder="请输入名称"
          ></el-input
        ></el-form-item>
        <el-form-item label="地区类型">
          <div>
            <el-select v-model="searchForm.level">
              <el-option label="全部" :value="0"></el-option>
              <el-option label="省份" :value="1"></el-option>
              <el-option label="城市" :value="2"></el-option>
              <el-option label="地区" :value="3"></el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label="选择上级">
          <el-select
            v-model="searchForm.provinceId"
            placeholder="省"
            @change="initCity"
          >
            <el-option label="全部" value=""></el-option>
            <el-option
              :label="item.name"
              :value="item.id"
              v-for="(item, index) in areaList"
              :key="index"
            ></el-option>
          </el-select>
          <el-select
            v-if="searchForm.provinceId != ''"
            v-model="searchForm.cityId"
            placeholder="市"
          >
            <el-option label="全部" value=""></el-option>
            <el-option
              :label="item1.name"
              :value="item1.id"
              v-for="(item1, index1) in cityList"
              :key="index1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          ><el-button type="primary" icon="Search" @click="onSubmit"
            >查询</el-button
          ></el-form-item
        >
      </el-form>
    </div>
    <!--添加等级-->
    <div class="common-level-rail">
      <el-button type="primary" @click="addClick">添加</el-button>
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
          <el-table-column prop="shortname" label="简称"></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="mergerName" label="名称"></el-table-column>
          <el-table-column prop="zipCode" label="邮编"></el-table-column>
          <el-table-column label="级别">
            <template #default="scope">
              <span v-if="scope.row.level == 1">省份</span>
              <span v-if="scope.row.level == 2">城市</span>
              <span v-if="scope.row.level == 3">地区</span>
            </template>
          </el-table-column>
          <el-table-column prop="lng" label="经度"></el-table-column>
          <el-table-column prop="lat" label="纬度"></el-table-column>
          <el-table-column fixed="right" label="操作" width="90">
            <template #default="scope">
              <el-button @click="editClick(scope.row)" type="text" size="small"
                >编辑</el-button
              >
              <el-button
                @click="deleteClick(scope.row)"
                type="text"
                size="small"
                >删除</el-button
              >
            </template>
          </el-table-column>
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
import RegionApi from "@/api/region.js";
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
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      userModel: {},
      searchForm: {
        name: "",
        level: 0,
        provinceId: "",
        cityId: "",
      },
      /*省市区*/
      areaList: [],
      cityList: [],
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      let Params = self.searchForm;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      RegionApi.regionList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.list.records;
          self.totalDataNumber = res.data.list.total;
          self.areaList = res.data.regionData;
        })
        .catch((error) => {});
    },
    onSubmit() {
      this.curPage = 1;
      this.getData();
    },
    /*打开添加*/
    addClick() {
      this.$router.push("/region/add");
    },

    /*打开编辑*/
    editClick(item) {
      let self = this;
      this.$router.push({
        path: "/region/edit",
        query: {
          id: item.id,
        },
      });
    },
    /*初始化城市id*/
    initCity(e) {
      this.searchForm.city_id = "";
      this.areaList.forEach((item) => {
        if (item.id == e) {
          this.cityList = item.children;
        }
      });
    },

    /*删除用户*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          RegionApi.deleteRegion(
            {
              id: row.id,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: data.msg,
                type: "success",
              });
              self.getData();
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style></style>
