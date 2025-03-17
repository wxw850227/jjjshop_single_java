<template>
  <el-dialog title="选择优惠券" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
    :close-on-press-escape="false">


    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table :data="tableData" border style="width: 100%" v-loading="loading"
          @current-change="handleCurrentChange">
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="minPrice" label="最低消费"></el-table-column>
          <el-table-column prop="totalNum" label="数量">
            <template #default="scope">
              <span v-if="scope.row.totalNum>0">{{scope.row.totalNum}}</span>
              <span v-else>无限制</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="scope">
              <el-button type="primary" size="small" @click="selectUser(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
          :current-page="curPage" :page-sizes="[2, 10, 20, 50, 100]" :page-size="pageSize"
          layout="total, prev, pager, next, jumper" :total="totalDataNumber"></el-pagination>
      </div>
    </div>

<!--    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="dialogFormVisible">取 消</el-button>
      <el-button size="small" type="primary" @click="addClerk">确 定</el-button>
    </div> -->
  </el-dialog>
</template>

<script>
  import CouponApi from '@/api/coupon.js';
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
        formInline: {
          name: '',
        },
        //会员列表
        tableData: [],
        formRules: {
          name: [{
              required: true,
              message: '请输入等级名称',
              trigger: 'blur'
            },
            {
              min: 3,
              max: 5,
              message: '长度在 3 到 5 个字符',
              trigger: 'blur'
            }
          ]
        },
        multipleSelection: [],
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        dialogVisible: false
      };
    },
    props: ['open_add'],
    created() {
      this.dialogVisible = this.open_add;
      /*获取列表*/
      this.getTableList();
    },
    methods: {
      /*选择第几页*/
      handleCurrentChange(val) {
        this.curPage = val;
        this.getTableList();
      },

      /*每页多少条*/
      handleSizeChange(val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getTableList();
      },
      getTableList() {
        let self = this;
        let params = {};
        params.pageIndex = self.curPage;
        params.pageSize = self.pageSize;
        CouponApi.couponList(params, true)
          .then(res => {
            if (res.code == 1) {
              self.loading = false;
              self.tableData = res.data.records;
              self.totalDataNumber =  res.data.total;
              self.gradeList =  res.data.grade;
            } else {
              ElMessage .error('错了哦，这是一条错误消息');
            }
          })
          .catch(error => {});
      },


      //点击确定
      selectUser(e) {
        let self = this;
        let params = e;
        this.$emit('closeDialog', {
          type: 'success',
          openDialog: false,
          params: params
        });
      },

      /*关闭弹窗*/
      dialogFormVisible(e) {
        if (e) {
          this.$emit('closeDialog', {
            type: 'success',
            openDialog: false
          });
        } else {
          this.$emit('closeDialog', {
            type: 'error',
            openDialog: false
          });
        }
      },
    }
  };
</script>

<style></style>
