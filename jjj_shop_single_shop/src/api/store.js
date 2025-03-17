import request from "@/utils/request";

let StoreApi = {
  toAddShop(data, errorback) {
    return request._get("/shop/store/store/toAdd", data, errorback);
  },
  /*添加门店*/
  addShop(data, errorback) {
    return request._postBody("/shop/store/store/add", data, errorback);
  },

  /*门店详情*/
  shopDetail(data, errorback) {
    return request._get("/shop/store/store/toEdit", data, errorback);
  },

  /*门店编辑*/
  editShop(data, errorback) {
    return request._postBody("/shop/store/store/edit", data, errorback);
  },

  /*删除门店*/
  deleteShop(data, errorback) {
    return request._post("/shop/store/store/delete", data, errorback);
  },

  /*门店列表*/
  shoplist(data, errorback) {
    return request._postBody("/shop/store/store/index", data, errorback);
  },

  /*门店选择列表*/
  storeLists(data, errorback) {
    return request._postBody("/shop/data/store/lists", data, errorback);
  },

  /*店员列表*/
  clerklist(data, errorback) {
    return request._postBody("/shop/store/clerk/index", data, errorback);
  },
  /*门店选择列表*/
  toAddClerk(data, errorback) {
    return request._get("/shop/store/clerk/toAdd", data, errorback);
  },
  /*店员添加*/
  addClerk(data, errorback) {
    return request._postBody("/shop/store/clerk/add", data, errorback);
  },
  /*店员详情*/
  clerkDetail(data, errorback) {
    return request._get("/shop/store/clerk/toEdit", data, errorback);
  },
  /*店员编辑*/
  editClerk(data, errorback) {
    return request._postBody("/shop/store/clerk/edit", data, errorback);
  },
  /*删除店员*/
  deleteClerk(data, errorback) {
    return request._post("/shop/store/clerk/delete", data, errorback);
  },
  /*弹出层查询*/
  search(data, errorback) {
    return request._post("/shop/store/clerk/search", data, errorback);
  },
  /*核销订单列表*/
  orderList(data, errorback) {
    return request._postBody("/shop/store/order/index", data, errorback);
  }
};

export default StoreApi;
