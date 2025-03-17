import request from '@/utils/request'

let SettingApi = {

  /*商城设置模板变量*/
  storeDetail(data, errorback) {
    return request._get('/shop/setting/store/index', data, errorback);
  },
  /*保存商城设置*/
  editStore(data, errorback) {
    return request._postBody('/shop/setting/store/index', data, errorback);
  },
  /*交易设置模板变量*/
  tradeDetail(data, errorback) {
    return request._get('/shop/setting/trade/index', data, errorback);
  },
  /*保存交易设置*/
  editTrade(data, errorback) {
    return request._postBody('/shop/setting/trade/index', data, errorback);
  },
  /*短信通知模板变量*/
  smsDetail(data, errorback) {
    return request._get('/shop/setting/sms/index', data, errorback);
  },
  /*保存短信通知*/
  editSms(data, errorback) {
    return request._postBody('/shop/setting/sms/index', data, errorback);
  },
  /*发送短信*/
  sendSms(data, errorback) {
    return request._post('/shop/setting.sms/smsTest', data, errorback);
  },
  /*上传设置模板变量*/
  storageDetail(data, errorback) {
    return request._get('/shop/setting/storage/index', data, errorback);
  },
  /*保存上传设置*/
  editStorage(data, errorback) {
    return request._postBody('/shop/setting/storage/index', data, errorback);
  },
  /*打印设置模板变量*/
  printingDetail(data, errorback) {
    return request._get('/shop/setting/printing/index', data, errorback);
  },
  /*保存打印设置*/
  editPrinting(data, errorback) {
    return request._postBody('/shop/setting/printing/index', data, errorback);
  },
  /*打印设置模板变量*/
  clearDetail(data, errorback) {
    return request._get('/shop/setting.clear/index', data, errorback);
  },
  /*保存缓存*/
  editCache(data, errorback) {
    return request._post('/shop/setting.clear/index', data, errorback);
  },
  /*物流公司列表*/
  expressList(data, errorback) {
    return request._postBody('/shop/setting/express/index', data, errorback);
  },
  /*添加物流公司*/
  addExpress(data, errorback) {
    return request._postBody('/shop/setting/express/add', data, errorback);
  },
  /*物流公司详情*/
  expressDetail(data, errorback) {
    return request._get('/shop/setting/express/toEdit',data, errorback);
  },
  /*修改物流公司*/
  editExpress(data, errorback) {
    return request._postBody('/shop/setting/express/edit', data, errorback);
  },
  /*删除物流公司*/
  deleteExpress(data, errorback) {
    return request._post('/shop/setting/express/delete', data, errorback);
  },
  /*退货地址列表*/
  addressList(data, errorback) {
    return request._postBody('/shop/setting/address/index', data, errorback);
  },
  /*添加退货地址*/
  addAddress(data, errorback) {
    return request._postBody('/shop/setting/address/add', data, errorback);
  },
  /*退货地址详情*/
  addressDetail(data, errorback) {
    return request._get('/shop/setting/address/toEdit', data, errorback);
  },
  /*修改退货地址*/
  editAddress(data, errorback) {
    return request._postBody('/shop/setting/address/edit', data, errorback);
  },
  /*删除退货地址*/
  deleteAddress(data, errorback) {
    return request._post('/shop/setting/address/delete', data, errorback);
  },
  /*打印机列表*/
  printerList(data, errorback) {
    return request._postBody('/shop/setting/printer/index', data, errorback);
  },
  /*打印机类型*/
  printerType(data, errorback) {
    return request._get('/shop/setting/printer/toAdd', data, errorback);
  },
  /*添加打印机*/
  addPrinter(data, errorback) {
    return request._postBody('/shop/setting/printer/add', data, errorback);
  },
  /*打印机详情*/
  printerDetail(data, errorback) {
    return request._get('/shop/setting/printer/toEdit', data, errorback);
  },
  /*修改打印机*/
  editPrinter(data, errorback) {
    return request._postBody('/shop/setting/printer/edit', data, errorback);
  },
  /*删除打印机*/
  deletePrinter(data, errorback) {
    return request._post('/shop/setting/printer/delete', data, errorback);
  },
  /*运费模板详情*/
  toAdd(data, errorback) {
    return request._get('/shop/setting/delivery/toAdd', data, errorback);
  },
  /*运费模板列表*/
  deliveryList(data, errorback) {
    return request._postBody('/shop/setting/delivery/index', data, errorback);
  },
  /*添加运费模板*/
  addDelivery(data, errorback) {
    return request._postBody('/shop/setting/delivery/add', data, errorback);
  },
  /*运费模板详情*/
  toAddDelivery(data, errorback) {
    return request._get('/shop/setting/delivery/toAdd', data, errorback);
  },
  /*修改运费模板*/
  toEditDelivery(data, errorback) {
    return request._get('/shop/setting/delivery/toEdit', data, errorback);
  },
  /*修改运费模板*/
  editDelivery(data, errorback) {
    return request._postBody('/shop/setting/delivery/edit', data, errorback);
  },
  /*删除运费模板*/
  deleteDelivery(data, errorback) {
    return request._post('/shop/setting/delivery/delete', data, errorback);
  },
  /*物流公司编码表*/
  getCompany(data, errorback) {
    return request._post('/shop/setting.express/companyList', data, errorback);
  },
  /*获取客服设置*/
  getMpService(data, errorback) {
    return request._get('/shop/setting/mpservice/index', data, errorback);
  },
  /*修改客服设置*/
  setMpService(data, errorback) {
    return request._postBody('/shop/setting/mpservice/index', data, errorback);
  },
  /*获取手机号设置*/
  getPhoneDetail(data, errorback) {
    return request._get('/shop/setting/message/getPhone', data, errorback);
  },
  /*保存获取手机号设置*/
  editGetPhone(data, errorback) {
    return request._postBody('/shop/setting/message/getPhone', data, errorback);
  },
}

export default SettingApi;
