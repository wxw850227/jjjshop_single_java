import request from '@/utils/request'

let CouponApi = {

  /*优惠券列表*/
  couponList (data, errorback) {
    return request._postBody('/shop/plus/coupon/coupon/index', data, errorback);
  },
  /*添加优惠券*/
  addCoupon (data, errorback) {
    return request._postBody('/shop/plus/coupon/coupon/add', data, errorback);
  },
  /*获取优惠券信息*/
  couponDetail (data, errorback) {
    return request._post('/shop/plus/coupon/coupon/couponDetail', data, errorback);
  },
  /*修改优惠券*/
  editCoupon (data, errorback) {
    return request._postBody('/shop/plus/coupon/coupon/edit', data, errorback);
  },
  /*删除优惠券*/
  deleteCoupon (data, errorback) {
    return request._post('/shop/plus/coupon/coupon/delete', data, errorback);
  },
  /*优惠券领取记录*/
  receiveList (data, errorback) {
    return request._postBody('/shop/plus/coupon/coupon/receive', data, errorback);
  },
  /*去发送优惠券*/
  toSendCoupon (data, errorback) {
    return request._get('/shop/plus/coupon/coupon/toSendCoupon', data, errorback);
  },
  /*发送优惠券*/
  SendCoupon (data, errorback) {
    return request._postBody('/shop/plus/coupon/coupon/sendCoupon', data, errorback);
  },
}

export default CouponApi;
