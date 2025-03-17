import request from '@/utils/request'

let TagApi = {
  /*等级列表*/
  tagList (data, errorback) {
    return request._postBody('/shop/user/tag/index', data, errorback);
  },
  /*添加等级*/
  addTag (data, errorback) {
    return request._postBody('/shop/user/tag/add', data, errorback);
  },
  /*修改等级*/
  editTag (data, errorback) {
    return request._postBody('/shop/user/tag/edit', data, errorback);
  },
  /*删除等级*/
  deleteTag (data, errorback) {
    return request._post('/shop/user/tag/delete', data, errorback);
  },
}

export default TagApi;
