<template>
  <div class="user">
    <div class="common-form">物流公司列表</div>
    <!--添加等级-->
    <div class="common-level-rail">
      <el-button size="small" type="primary" @click="addClick" v-auth="'/setting/express/add'">添加</el-button>
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="expressId" label="物流公司ID"></el-table-column>
          <el-table-column prop="expressName" label="物流公司名称"></el-table-column>
          <el-table-column prop="expressCode" label="物流公司代码"></el-table-column>
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button @click="editClick(scope.row)" type="text" size="small" v-auth="'/setting/express/edit'" >编辑</el-button>
              <el-button @click="deleteClick(scope.row)" type="text" size="small"  v-auth="'/setting/express/delete'">删除</el-button>
            </template>
          </el-table-column>
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
  import SettingApi from '@/api/setting.js';
  export default {
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
        let Params = {};
        Params.pageIndex = self.curPage;
        Params.pageSize = self.pageSize;
        SettingApi.expressList(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.records;
            self.totalDataNumber = res.data.total
          })
          .catch(error => {

          });
      },
      /*打开添加*/
      addClick() {
        this.$router.push('/setting/express/add');
      },

      /*打开编辑*/
      editClick(item) {
        let self = this;
        this.$router.push({
          path: '/setting/express/edit',
          query: {
            expressId: item.expressId
          }
        })
      },


      /*删除用户*/
      deleteClick(row) {
        let self = this;
        ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          self.loading = true;
          SettingApi.deleteExpress({
              id: row.expressId
            }, true).then(data => {
              self.loading = false;
              ElMessage ({
                message: data.msg,
                type: 'success'
              });
              self.getData();

            })
            .catch(error => {
              self.loading = false;
            });

        }).catch(() => {});
      }

    }
  };
</script>

<style></style>
