<template>
  <div>
    <div class="common-level-rail">
      <el-button size="small" type="primary" icon="Plus" @click="add">添加菜单</el-button>
    </div>
    <div class="table-wrap">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="菜单名称"></el-table-column>
        <el-table-column prop="address" label="图标" width="250">
          <template #default="scope">
            <img v-img-url="scope.row.icon" width="80" height="50" />
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="100"></el-table-column>
        <el-table-column prop="isShow" label="是否显示" width="80">
          <template #default="scope">
            <el-switch v-model="scope.row.isShow" :active-value="1" :inactive-value="0"
              @change="changeStatus(scope.row)" active-color="#13ce66" inactive-color="#cccccc"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="添加时间" width="140"></el-table-column>
        <el-table-column prop="name" label="操作" width="120">
          <template #default="scope">
            <el-button @click="edit(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="del(scope.row)" type="text" size="small" v-if="scope.row.sysTag==''">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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
  import PageApi from '@/api/page.js';
  export default {
    components: {},
    data() {
      return {
        /*表单数据*/
        tableData: [],
        /*是否打开添加弹窗*/
        open_add: false,
        /*是否打开编辑弹窗*/
        open_edit: false,
        /*当前编辑的对象*/
        userModel: {},
        commentData: [],
        loading: true,
        /*一页多少条*/
        pageSize: 15,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1
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
        let Params = {};
        Params.pageIndex = self.curPage;
        Params.pageSize = self.pageSize;
        PageApi.menuList(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.records;
            self.totalDataNumber = res.data.total;
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*添加*/
      add() {
        this.$router.push({
          path: '/page/center/add'
        });
      },

      /*编辑*/
      edit(row) {
        this.$router.push({
          path: '/page/center/edit',
          query: {
            menuId: row.menuId
          }
        });
      },
      /*修改状态*/
      changeStatus(item) {
        let self = this;
        const loading = ElLoading.service({
          lock: true,
          text: '正在处理',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        PageApi.statusMenu({
            menuId: item.menuId,
            isShow: item.isShow
          })
          .then(data => {
            loading.close();
          })
          .catch(data => {
            loading.close();
            ElMessage ({
              message: '处理失败，请重试',
              type: 'warning'
            });
          });
      },

      /*删除*/
      del(row) {
        let self = this;
        ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then(() => {
            self.loading = true;
            PageApi.deleteMenu({
                  menuId: row.menuId
                },
                true
              )
              .then(res => {
                ElMessage ({
                  message: res.msg,
                  type: 'success'
                });
                self.loading = false;
                self.getTableList();
              })
              .catch(error => {});
          })
          .catch(() => {});
      },
    }
  };
</script>

<style></style>
