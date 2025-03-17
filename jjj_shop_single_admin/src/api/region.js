import request from '@/utils/request'

let RegionApi = {
  /*消息列表*/
  regionList(data, errorback) {
    return request._postBody('/admin/region/index', data, errorback);
  },
  toAddRegion(data, errorback) {
    return request._get('/admin/region/add', data, errorback);
  },
  /*添加消息*/
  addRegion(data, errorback) {
    return request._postBody('/admin/region/add', data, errorback);
  },
  /*物流公司详情*/
  regionDetail(data, errorback) {
    return request._get('/admin/region/edit', data, errorback);
  },
  /*修改消息*/
  editRegion(data, errorback) {
    return request._postBody('/admin/region/edit', data, errorback);
  },
  /*删除消息*/
  deleteRegion(data, errorback) {
    return request._post('/admin/region/delete', data, errorback);
  },
}

export default RegionApi;
