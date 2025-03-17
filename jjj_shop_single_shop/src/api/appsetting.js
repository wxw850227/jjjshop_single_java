import request from '@/utils/request'

let AppSettingApi = {
  /*小程序*/
  appwxDetail(data, errorback) {
    return request._get('/shop/appSetting/appWx/index', data, errorback);
  },
  /*小程序*/
  editAppWx(data, errorback) {
    return request._post('/shop/appSetting/appWx/edit', data, errorback);
  },
  /*app*/
  appDetail(data, errorback) {
    return request._get('/shop/appSetting/app/index', data, errorback);
  },
  /*app*/
  editApp(data, errorback) {
    return request._post('/shop/appSetting/app/edit', data, errorback);
  },
  /*支付方式*/
  payDetail(data, errorback) {
    return request._get('/shop/appSetting/app/pay', data, errorback);
  },
  /*支付方式*/
  editPay(data, errorback) {
    return request._postBody('/shop/appSetting/app/editPay', data, errorback);
  },
  /*上传*/
  uploadP12(formData, errorback) {
      return request._upload('/shop/appSetting/app/uploadP12', formData, errorback);
  },
}

export default AppSettingApi;
