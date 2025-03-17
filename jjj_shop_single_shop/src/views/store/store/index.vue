<template>
  <div class="user">
    <!--添加门店-->
    <div class="common-level-rail">
      <el-button size="small" type="primary" icon="Plus" @click="addClick" v-auth="'/store/store/add'">添加门店
      </el-button>
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="storeId" label="门店ID" width="60"></el-table-column>
          <el-table-column label="Logo" width="60">
            <template #default="scope">
              <img v-img-url="scope.row.logoImagePath" :width="30" :height="30" />
            </template>
          </el-table-column>
          <el-table-column prop="storeName" label="门店名称"></el-table-column>
          <el-table-column prop="shopHours" label="营业时间"></el-table-column>
          <el-table-column prop="linkman" label="联系人"></el-table-column>
          <el-table-column prop="phone" label="联系电话" width="100"></el-table-column>
          <el-table-column prop="address" label="门店地址" width="240"></el-table-column>
          <el-table-column prop="isCheck" label="自提核销">
            <template #default="scope">
              <span :class="{ green: scope.row.isCheck == 1, gray: scope.row.isCheck == 0 }">{{
                scope.row.isCheckText }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="门店状态">
            <template #default="scope">
              <span :class="{ green: scope.row.status== 1, gray: scope.row.status == 0 }">{{
                scope.row.statusText }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="140"></el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button @click="editClick(scope.row)" type="text" size="small" v-auth="'/store/store/edit'">编辑
              </el-button>
              <el-button @click="deleteClick(scope.row)" type="text" size="small" v-auth="'/store/store/delete'">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
          :current-page="curPage" :page-size="pageSize" layout="total, prev, pager, next, jumper"
          :total="totalDataNumber"></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  import StoreApi from '@/api/store.js';
  export default {
    components: {},
    inject: ['reload'],
    data () {
      return {
        /*是否加载完成*/
        loading: true,
        /*列表数据*/
        tableData: [],
        /*一页多少条*/
        pageSize: 20,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
        /*横向表单数据模型*/
        formInline: {
          user: '',
          region: ''
        },
        /*是否打开添加弹窗*/
        open_add: false,
        /*是否打开编辑弹窗*/
        open_edit: false,
        /*当前编辑的对象*/
        userModel: {}
      };
    },
    created () {
      /*获取列表*/
      this.getTableList();
    },
    methods: {
      /*选择第几页*/
      handleCurrentChange (val) {
        let self = this;
        self.curPage = val;
        self.loading = true;
        self.getTableList();
      },

      /*每页多少条*/
      handleSizeChange (val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getTableList();
      },

      /*获取列表*/
      getTableList () {
        let self = this;
        let Params = {};
        Params.pageIndex = self.curPage;
        Params.pageSize = self.pageSize;
        StoreApi.shoplist(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.records;
            self.totalDataNumber = res.data.total;
          })
          .catch(error => { });
      },

      /*打开添加*/
      addClick () {
        this.$router.push('/store/store/add');
      },

      /*打开编辑*/
      editClick (row) {
        let self = this;
        let params = row.storeId;
        this.$router.push({
          path: '/store/store/edit',
          // name: 'mallList',
          query: {
            storeId: params
          }
        });
      },

      /*删除*/
      deleteClick (row) {
        let self = this;
        ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then(() => {
            StoreApi.deleteShop(
              {
                storeId: row.storeId
              },
              true
            )
              .then(data => {
                ElMessage ({
                  message: '恭喜你，门店删除成功',
                  type: 'success'
                });
                self.getTableList();
              })
              .catch(error => { });
          })
          .catch(() => { });
      }
    }
  };
</script>

<style></style>
