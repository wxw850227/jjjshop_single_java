import request from '@/utils/request'

let StatisticsApi = {
  /*订单数据统计*/
  getOrderTotal(data, errorback) {
    return request._post('/shop/statistics/sales/index', data, errorback);
  },
  /*订单时间段统计*/
  getOrderByDate(data, errorback) {
    return request._postBody('/shop/statistics/sales/order', data, errorback);
  },
  /*商品时间段统计*/
  getProductByDate(data, errorback) {
    return request._postBody('/shop/statistics/sales/product', data, errorback);
  },
  /*会员数据统计*/
  getUserTotal(data, errorback) {
    return request._postBody('/shop/statistics/user/index', data, errorback);
  },
  /*成交占比*/
  getUserScale(data, errorback) {
    return request._post('/shop/statistics/user/scale', data, errorback);
  },
  /*新增会员*/
  getNewUser(data, errorback) {
    return request._postBody('/shop/statistics/user/newuser', data, errorback);
  },
  /*成交会员数*/
  getPayUser(data, errorback) {
    return request._postBody('/shop/statistics/user/payuser', data, errorback);
  },
}

export default StatisticsApi;
