<template>
  <div class="d-a-c lh30 ww100 pt16">
    <div class="pt30 tc" style="width: 80px;">
      <p class="gray9">今日</p>
      <p class="gray9">昨日</p>
    </div>
    <div class="flex-1 tc">
      <p>成交额</p>
      <p class="f20 fb gray3">{{dataModel.order.orderTotalPriceToday}}</p>
      <p class="gray">{{dataModel.order.orderTotalPriceYesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>客单价</p>
      <p class="f20 fb gray3">{{dataModel.order.orderPerPriceToday}}</p>
      <p class="gray">{{dataModel.order.orderPerPriceYesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>付款订单</p>
      <p class="f20 fb gray3">{{dataModel.order.orderTotalToday}}</p>
      <p class="gray">{{dataModel.order.orderTotalYesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>付款人数</p>
      <p class="f20 fb gray3">{{dataModel.order.orderUserTotalToday}}</p>
      <p class="gray">{{dataModel.order.orderUserTotalYesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>成功退款金额</p>
      <p class="f20 fb gray3">{{dataModel.order.orderRefundMoneyToday}}</p>
      <p class="gray">{{dataModel.order.orderRefundMoneyYesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>退款订单</p>
      <p class="f20 fb gray3">{{dataModel.order.orderRefundTotalToday}}</p>
      <p class="gray">{{dataModel.order.orderRefundTotalYesterday}}</p>
    </div>
  </div>
</template>
<script>

export default {
  components:{
  },
  data() {
    return {
      /*是否正在加载*/
      loading: true,

    };
  },
  inject: ['dataModel'],
  created() {

  },
  methods: {


  }
};
</script>

<style>
</style>
