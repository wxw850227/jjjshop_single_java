import request from '@/utils/request'

let PlusApi = {

    /*插件列表*/
    plusList(data, errorback) {
        return request._postBody('/shop/plus/plus/index', data, errorback);
    }
}

export default PlusApi;
