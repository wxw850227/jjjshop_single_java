import request from '@/utils/request'

let PlusApi = {
  /*插件列表*/
  pluslist(data, errorback) {
    return request._post('/admin/plus/index', data, errorback);
  },
  /*获取插件*/
  getplus(data, errorback) {
    return request._get('/admin/plus/add', data, errorback);
  },
  /*添加插件*/
  addplus(data, errorback) {
    return request._post('/admin/plus/add', data, errorback);
  },
  /*编辑插件*/
  editplus(data, errorback) {
    return request._post('/admin/plus/edit', data, errorback);
  },
  /*删除插件*/
  deleteplus(data, errorback) {
    return request._post('/admin/plus/delete', data, errorback);
  },
  /*修改插件状态*/
  updatePlusStatus(data, errorback) {
    return request._post('/admin/plus/updateStatus', data, errorback);
  },
  /*是否推荐*/
  updatePlusRecom(data, errorback) {
    return request._post('/admin/plus/updateRecom', data, errorback);
  }

}

export default PlusApi;
