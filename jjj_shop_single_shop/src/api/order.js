import request from '@/utils/request'
let OrderApi = {
  /*订单列表*/
  orderlist (data, errorback) {
    return request._postBody('/shop/order/order/index', data, errorback);
  },
  /*订单详情*/
  orderdetail (data, errorback) {
    return request._post('/shop/order/order/detail', data, errorback);
  },
  /*售后管理*/
  orderrefund (data, errorback) {
    return request._postBody('/shop/order/refund/index', data, errorback);
  },
  /*去发货*/
  delivery (data, errorback) {
    return request._postBody('/shop/order/order/delivery', data, errorback);
  },
  /*确认审核*/
  confirm (data, errorback) {
    return request._postBody('/shop/order/operate/confirmCancel', data, errorback);
  },
  /*售后详情*/
  refundDetail (data, errorback) {
    return request._get('/shop/order/refund/detail', data, errorback);
  },
  /*售后审核*/
  Approval (data, errorback) {
    return request._postBody('/shop/order/refund/audit', data, errorback);
  },
  /*确认收货并退款*/
  receipt (data, errorback) {
    return request._postBody('/shop/order/refund/receipt', data, errorback);
  },
  /*核销*/
  extract (data, errorback) {
    return request._postBody('/shop/order/operate/extract', data, errorback);
  },
  /*修改价格*/
  updatePrice (data, errorback) {
    return request._postBody('/shop/order/order/updatePrice', data, errorback);
  },
  /*批量发货*/
  batchDelivery (formData, errorback) {
    return request._upload('/shop/order.operate/batchDelivery', formData, errorback);
  },
  /*取消订单*/
  orderCancel (formData, errorback) {
    return request._postBody('/shop/order/order/orderCancel', formData, errorback);
  },
  /*虚拟商品发货*/
  virtual (formData, errorback) {
    return request._postBody('/shop/order/order/virtual', formData, errorback);
  },
}

export default OrderApi;
