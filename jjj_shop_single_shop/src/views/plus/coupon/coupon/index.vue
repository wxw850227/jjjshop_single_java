<template>
  <div class="user">
    <!--搜索表单-->
    <div class="common-seach-wrap">
    </div>
    <div class="common-level-rail">
      <el-button size="small" type="primary" icon="Plus" @click="addClick">添加优惠券</el-button>
    </div>
    <div class="product-content">
      <el-form ref="form" :model="form" label-width="100px">
        <div class="table-wrap">
          <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="couponId" label="优惠券ID" width="70"></el-table-column>
            <el-table-column label="优惠券类型">
              <template #default="scope">
                <span v-if="scope.row.couponType && scope.row.couponType == 10">满减券</span>
                <span v-if="scope.row.couponType && scope.row.couponType == 20">折扣券</span>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="优惠券名称"></el-table-column>
            <el-table-column prop="minPrice" label="最低消费金额"></el-table-column>
            <el-table-column prop="seckillStock" label="优惠方式">
              <template #default="scope">
                <div v-if="scope.row.couponType==10">
                  <span>立减 <strong class="orange">{{scope.row.reducePrice}}</strong> 元</span>
                </div>
                <div v-if="scope.row.couponType==20">
                  <span>打 <strong class="orange">{{scope.row.discount/10}}</strong> 折</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="seckillStock" label="有效期">
              <template #default="scope">
                <div v-if="scope.row.expireType==10">
                  <span>领取 <strong>{{scope.row.expireDay}}</strong> 天内有效</span>
                </div>
                <div v-if="scope.row.expireType==20">
                  <span>
                    {{scope.row.startTimeText}}
                    ~
                    {{scope.row.endTimeText}}
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="receiveNum" label="发放总数量">
              <template #default="scope">
                <div v-if="scope.row.totalNum==-1">
                  <span>不限制</span>
                </div>
                <div v-else>
                  <span>{{scope.row.totalNum}}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="receiveNum" label="已领取数量"></el-table-column>
            <el-table-column prop="sort" label="排序"></el-table-column>
            <el-table-column prop="createTime" label="添加时间" width="135"></el-table-column>
            <el-table-column fixed="right" label="操作" width="120">
              <template #default="scope">

                <el-button @click="editClick(scope.row)" type="text" size="small">编辑</el-button>
                <el-button @click="deleteClick(scope.row)" type="text" size="small">删除</el-button>


              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>
      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
          :current-page="curPage" :page-size="pageSize" layout="total, prev, pager, next, jumper"
          :total="totalDataNumber">
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
  import CouponApi from '@/api/coupon.js';
  export default {
    data () {
      return {
        formInline: {},
        form: {},
        tableData: [],
        /*一页多少条*/
        pageSize: 15,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
        /*是否加载完成*/
        loading: true,
      };
    },
    created () {
      /*获取列表*/
      this.getData();
    },
    methods: {
      /*获取列表*/
      getData () {
        let self = this;
        let Params = {};
        Params.pageIndex = self.curPage;
        Params.pageSize = self.pageSize;
        CouponApi.couponList(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.records;
            self.totalDataNumber = res.data.total
          })
          .catch(error => { });
      },
      /*选择第几页*/
      handleCurrentChange (val) {
        let self = this;
        self.curPage = val;
        self.loading = true;
        self.getData();
      },

      /*每页多少条*/
      handleSizeChange (val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getTableList();
      },
      /*添加优惠券*/
      addClick () {
        this.$router.push('/plus/coupon/coupon/add');
      },
      /*修改优惠券*/
      editClick (e) {
        let self = this;
        this.$router.push({
          path: '/plus/coupon/coupon/edit',
          query: {
            id: e.couponId
          }
        })
      },
      /*删除优惠券*/
      deleteClick (e) {
        let self = this;
        ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          self.loading = true;
          CouponApi.deleteCoupon({
            id: e.couponId
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

        }).catch(() => {
          self.loading = false;
        });
      }

    }
  };
</script>

<style>
</style>
