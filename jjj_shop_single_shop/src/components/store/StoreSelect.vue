<template>
  <el-dialog title="选择门店" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
    :close-on-press-escape="false" width="900px">

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="storeList" border style="width: 100%" highlight-current-row v-loading="loading"
          @selection-change="tableCurrentChange">
          <el-table-column width="70" label="门店图片">
            <template #default="scope">
              <img :src="scope.row.logoImagePath" width="30" height="30" />
            </template>
          </el-table-column>
          <el-table-column prop="storeName" label="门店名称"></el-table-column>
          <el-table-column prop="linkman" label="联系人"></el-table-column>
          <el-table-column prop="phone" label="联系电话"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
          <el-table-column prop="createTime" width="140" label="添加时间"></el-table-column>
          <el-table-column type="selection" :selectable="selectableFunc" width="44"></el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
          :current-page="curPage" :page-sizes="[2, 10, 20, 50, 100]" :page-size="pageSize"
          layout="total, prev, pager, next, jumper" :total="totalDataNumber"></el-pagination>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="dialogVisible=false">取 消</el-button>
        <el-button size="small" type="primary" @click="addClerk">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
  import storeApi from '@/api/store.js';
  export default {
    data() {
      return {
        /*是否加载完成*/
        loading: true,
        /*当前是第几页*/
        curPage: 1,
        /*一页多少条*/
        pageSize: 20,
        /*一共多少条数据*/
        totalDataNumber: 0,
        formInline: {},
        //商品列表
        storeList: [],
        multipleSelection: [],
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        dialogVisible: false,
        /*结果类别*/
        type: 'error',
        /*传出去的参数*/
        params: null
      };
    },
    props: ['isstore', 'excludeIds', 'islist'],
    watch: {
      isstore: function(n, o) {
        if (n != o) {
          if (n) {
            this.dialogVisible = n;
            this.type = 'error';
            this.params = null;
            this.getData();
          }
        }
      }
    },
    created() {

    },
    methods: {

      /*是否可以勾选*/
      selectableFunc(e) {
        if (typeof e.noChoose !== 'boolean') {
          return true;
        }
        return e.noChoose;
      },

      /*选择第几页*/
      handleCurrentChange(val) {
        this.curPage = val;
        this.getData();
      },

      /*每页多少条*/
      handleSizeChange(val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getData();
      },

      /*获取商品列表*/
      getData() {
        let self = this;
        let params = self.formInline;
        params.pageIndex = self.curPage;
        params.pageSize = self.pageSize;
        storeApi.storeLists(params, true)
          .then(res => {
            self.loading = false;
            /*判断是否需要去重*/
            if (self.excludeIds && typeof(self.excludeIds) != 'undefined' && self.excludeIds.length > 0) {
              res.data.list.data.forEach(item => {
                if (self.excludeIds.indexOf(item.storeId) > -1) {
                  item.noChoose = false;
                } else {
                  item.noChoose = true;
                }
              });
            }
            self.storeList = res.data.records;
            self.totalDataNumber = res.data.total;
          })
          .catch(error => {});
      },

      //点击确定
      addClerk() {
        let self = this;
        let params = null;
        let type = 'success';
        if (self.multipleSelection.length < 1) {
          ElMessage ({
            message: '请至少选择一个门店！',
            type: 'error'
          });
          return;
        }
        if (self.islist && typeof(self.islist) != 'undefined') {
          params = self.multipleSelection;
        } else {
          params = self.multipleSelection[0];
        }
        self.params = params;
        self.type = 'success';
        self.dialogVisible = false;
      },

      /*关闭弹窗*/
      dialogFormVisible() {
        this.$emit('closeDialog', {
          type: this.type,
          openDialog: false,
          params: this.params
        });
      },

      /*监听复选按钮选中事件*/
      tableCurrentChange(val) {
        this.multipleSelection = val;
      }
    }
  };
</script>

<style></style>
