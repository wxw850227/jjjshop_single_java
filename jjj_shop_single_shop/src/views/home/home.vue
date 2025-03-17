<template>
  <div class="home" v-loading="loading">
    <!-- <div class="common-form mt16">
      商城统计
    </div> -->
    <div class="operation-wrap" style="background-color: #FFFFFF;">
      <el-row>
        <el-col :span="6" class="d-c-c">
          <div class="grid-content purple">
            <div class="info">
              <h3>{{tableData.productTotal}}</h3>
              <p>商品总量</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="d-c-c">
          <div class="grid-content yellow">
            <div class="info">
              <h3>{{tableData.userTotal}}</h3>
              <p>用户总量</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="d-c-c">
          <div class="grid-content orderred">
            <div class="info">
              <h3>{{tableData.orderTotal}}</h3>
              <p>订单总量</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="d-c-c">
          <div class="grid-content blue">
            <div class="info">
              <h3>{{tableData.productCommentTotal}}</h3>
              <p>评价总量</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="home-index">
      <!--main-index-->
      <div class="flex-1">
        <div class="main-index">
          <div class="common-form mt16" style="font-size: 18px;">
            今日概况
          </div>
          <el-row class="border-b-l">
            <el-col :span="6">
              <div class="grid-content">
                <div class="info">
                  <p class="des">销售额(元)</p>
                  <h3>{{tableData.orderTotalPriceT}}</h3>
                  <p class="yesterday">昨日：{{tableData.orderTotalPriceY}}</p>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content">
                <div class="info">
                  <p class="des">支付订单数</p>
                  <h3>{{tableData.orderTotalT}}</h3>
                  <p class="yesterday">昨日：{{tableData.orderTotalY}}</p>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content">
                <div class="info">
                  <p class="des">新增用户数</p>
                  <h3>{{tableData.userTotalT}}</h3>
                  <p class="yesterday">昨日：{{tableData.userTotalY}}</p>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content">
                <div class="info">
                  <p class="des">下单用户数</p>
                  <h3>{{tableData.payOrderUserTotalT}}</h3>
                  <p class="yesterday">昨日：{{tableData.payOrderUserTotalY}}</p>
                </div>
              </div>
            </el-col>
          </el-row>
          <div>
            <Transaction v-if="!loading"></Transaction>
          </div>
        </div>
      </div>

      <!--待办事项-->
      <div class="matters-wrap">
        <div class="common-form mt16" style="font-size: 18px;">
          待办事项<span class="ml10 f14 gray" style="font-weight: normal;">请尽快处理，以免影响营业</span>
        </div>
        <el-row class="matters_box">
          <el-col :span="24">
            <div class="matters">
              <div class="box">
                <div class="title">订单</div>
                <ul class="matters_item">
                  <li><span class="fb">{{tableData.reviewOrderTotal}}</span>待处理</li>
                  <li><span class="fb">{{tableData.refundTotal}}</span>待审核售后</li>
<!--                  <li><span class="fb">{{tableData.cardOrderTotal}}</span>提货卡待发货</li> -->
                </ul>
              </div>
            </div>
          </el-col>
<!--          <el-col :span="24">
            <div class="matters">
              <div class="box">
                <div class="title">提现</div>
                <ul class="matters_item">
                  <li><span class="fb">{{right_away.cash.percentage}}</span>佣金提现</li>
                </ul>
              </div>
            </div>
          </el-col> -->
          <el-col :span="24">
            <div class="matters">
              <div class="box">
                <div class="title">审核</div>
                <ul class="matters_item">
<!--                  <li><span class="fb">{{right_away.review.percentage}}</span>佣金审核</li> -->
                  <li><span class="fb">{{tableData.reviewCommentTotal}}</span>商品评价</li>
                </ul>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="matters">
              <div class="box">
                <div class="title">库存</div>
                <ul class="matters_item">
                  <li><span class="fb">{{tableData.productStockTotal}}</span>库存告急</li>
                </ul>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
  import IndexApi from '@/api/index.js';
  import Transaction from '@/views/home/part/Transaction.vue';
  export default {
    components: {
      Transaction,
    },
    data() {
      return {
        /*是否加载完成*/
        loading: true,
        /*统计信息*/
        tableData: [],
        // /*总销售额(元)*/
        // order_total_price: [],
        // /*总支付订单数*/
        // order_total: [],
        // /*新增用户数*/
        // new_user_total: [],
        // /*下单用户数*/
        // order_user_total: [],
        /*待办事项*/
        right_away: {
          order: [],
          cash: [],
          review: [],
          stock: [],
         },
      };
    },
    created() {
      /*获取数据*/
      this.getData();
    },
    methods: {
      /*获取数据*/
      getData() {
        let self = this;
        IndexApi.getCount(true).then(res => {
          self.loading = false;
          self.tableData = res.data;
          // self.order_total_price = data.data.data.widget_outline.order_total_price;
          // self.order_total = data.data.data.widget_outline.order_total;
          // self.new_user_total = data.data.data.widget_outline.new_user_total;
          // self.order_user_total = data.data.data.widget_outline.order_user_total;
          // self.right_away = data.data.data.right_away;
        }).catch(error => {

        });
      },
    }
  };
</script>

<style lang="scss" scoped>
  .operation-wrap {
    width: 100%;
    height: 164px;
    border-radius: 8px;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    padding: 0 20px;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    overflow: hidden;
    background-size: 100% 100%;
    color: #ffffff;
  }

  .operation-wrap .grid-content h3 {
    font-size: 36px;
    line-height: 40px;
  }

  .operation-wrap .grid-content .info {
    text-align: center;
    margin-left: 10px;
  }

  .home .operation-wrap .grid-content {
    width: 90%;
    height: 150px;
    justify-content: flex-start;
    align-items: center;
    padding-left: 23px;
  }

  .operation-wrap .grid-content.blue {
    background: url(../../../static/imgs/total_shop.png) no-repeat;
    background-size: 100% 100%;
  }

  .operation-wrap .grid-content.yellow {
    background: url(../../../static/imgs/total_user.png) no-repeat;
    background-size: 100% 100%;
  }

  .operation-wrap .grid-content.purple {
    background: url(../../../static/imgs/total_volume.png) no-repeat;
    background-size: 100% 100%;
  }

  .operation-wrap .grid-content.orderred {
    background: url(../../../static/imgs/total_order.png) no-repeat;
    background-size: 100% 100%;
  }

  .operation-wrap .grid-content .info h3 {
    font-size: 40px;
    line-height: 40px;
    color: #FFFFFF;
    text-align: left;
    margin-bottom: 20px;
  }

  .operation-wrap .grid-content .info {
    margin-left: 10px;
    text-align: center;
    font-size: 14px;
    color: #FFFFFF;
  }

  .operation-wrap .grid-content .svg-icon {
    color: rgba(255, 255, 255, .3);
    font-size: 300%;
  }

  .home-index {
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-direction: row;
    flex-direction: row;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    min-width: 1000px;
    overflow-x: auto;
  }

  .main-index {
    flex: 1;
    margin: 20px;
  }

  .main-index .grid-content,
  .operation-wrap .grid-content {

    display: -ms-flexbox;
    display: flex;
    -webkit-box-direction: row;
    -ms-flex-direction: row;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }

  .main-index .grid-content {
    height: 120px;
  }

  .main-index .grid-content .pic {
    margin-right: 10px;
  }

  .main-index .grid-content h3 {
    font-size: 20px;
    font-weight: normal;
  }

  .main-index .grid-content .yesterday {
    color: #ccc;
  }

  .main-index .grid-content .svg-icon {
    color: #3a8ee6;
  }

  .matters-wrap {
    padding-bottom: 15px;
    width: 40%;
  }

  .matters .box {
    width: 100%;
  }

  .matters-wrap .matters {
    display: -ms-flexbox;
    display: flex;
    -webkit-box-direction: column;
    -ms-flex-direction: column;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    // height: 120px;
    margin-bottom: 30px;
  }

  .matters-wrap .matters .title {
    font-size: 16px;
    color: #333333;
    display: inline-block;
    height: 20px;
    line-height: 0;
    padding: 11px;
    text-align: center;
    margin-bottom: 20px;
  }

  .matters-wrap .matters ul {
    color: #999999;
  }

  .matters-wrap .matters ul span {
    padding-right: 6px;
    color: #3a8ee6;
  }

  .border-b {
    display: flex;
    flex-direction: column;
  }

  .border-b-l {
    flex-direction: initial;
  }

  .matters_item {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .matters_item li {
    width: 72px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-right: 16px;
  }

  .grid-content .info h3 {
    font-weight: bold;
    color: #5d75e3;
    text-align: center;
  }

  .grid-content .info .des {
    font-size: 16px;
    margin-bottom: 6px;
  }

  .grid-content .info .yesterday {
    font-size: 13px;
    text-align: center;
  }

  .matters_box {
    width: 90%;
    border-top: 1px solid #d9d9d9;
    padding-top: 20px;
  }

  .matters-wrap .matters_item li .fb {
    font-size: 16px;
    color: #5d75e3;
  }
</style>
