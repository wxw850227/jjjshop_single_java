<template>
  <div class="user">
    <!--添加管理员-->
    <div class="common-level-rail">
      <el-button size="small" type="primary" icon="Plus" @click="addClick" v-auth="'/auth/user/add'">添加管理员</el-button>
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="shopUserId" label="管理员ID"></el-table-column>
          <el-table-column prop="userName" label="用户名"></el-table-column>
          <el-table-column label="所属角色">
            <template #default="scope">
              <div v-if="scope.row.isSuper == 0">
                <span class="mr10 green" v-for="(item, index) in scope.row.roleList" :key="index">{{ item.roleName }}</span>
              </div>
              <div class="gray" v-if="scope.row.isSuper">
                超级管理员
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button v-if="!scope.row.isSuper" @click="editClick(scope.row)" type="text" size="small" v-auth="'/auth/user/edit'">编辑</el-button>
              <el-button v-if="!scope.row.isSuper" @click="deleteClick(scope.row)" type="text" size="small" v-auth="'/auth/user/delete'">删除</el-button>
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
        ></el-pagination>
      </div>
    </div>

    <Add :open="open_add" :roleList="roleList" @close="closeAdd"></Add>

    <Edit :open="open_edit" :roleList="roleList" :model="curModel"  @close="closeEdit"></Edit>

  </div>
</template>

<script>
import AuthApi from '@/api/auth.js';
import Add from './dialog/Add.vue';
import Edit from './dialog/Edit.vue';
export default {
  components: {
    Add,
    Edit
  },
  inject: ['reload'],
  data() {
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
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      curModel: {},
      /*角色列表*/
      roleList:[]
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
      AuthApi.userList(Params, true)
        .then(res => {
          self.loading = false;
          self.tableData = res.data.userList.records;
          self.totalDataNumber = res.data.userList.total;
          self.roleList = res.data.roleList;
        })
        .catch(error => {});
    },

    /*打开添加*/
    addClick() {
      this.open_add=true;
    },

    /*关闭添加*/
    closeAdd(e){
      this.open_add=false;
      if(e && e.type=='success'){
        this.getTableList();
      }
    },

    /*打开编辑*/
    editClick(row) {
      this.curModel=row;
      this.open_edit=true;
    },

    /*关闭添加*/
    closeEdit(e){
      this.open_edit=false;
      if(e && e.type=='success'){
        this.getTableList();
      }
    },

    /*删除*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(() => {
          self.loading = true;
          AuthApi.userDelete(
            {
              shopUserId: row.shopUserId
            },
            true
          )
            .then(data => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage ({
                  message: '恭喜你，该管理员删除成功',
                  type: 'success'
                });
                //刷新页面
                self.getTableList();
              } else {
                self.loading = false;
              }
            })
            .catch(error => {
              self.loading = false;
            });
        })
        .catch(() => {});
    }
  }
};
</script>

<style></style>
