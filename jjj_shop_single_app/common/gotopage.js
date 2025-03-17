import config from '../env/config.js'
/*导航菜单白名单*/
const tabBarLinks = [
	'/pages/index/index',
	'/pages/product/category',
	'/pages/cart/cart',
	'/pages/user/index/index'
];

/*分享页面白名单*/
const shareLinks = [
	'/pages/plus/assemble/fight-group-detail/fight-group-detail',
	'/pages/plus/bargain/haggle/haggle',
	'/pages/user/invite/invite',
	'/pages/product/detail/detail',
	'/pages/plus/seckill/detail/detail',
	'/pages/plus/assemble/detail/detail',
	'/pages/plus/bargain/detail/detail',
	'/pages/plus/points/detail/detail'
]

/*
 * 跳转页面
 */
export const gotopage = (url, type) => {
	if (!url || url.length == 0) {
		return false;
	}

	if (url.substr(0, 1) !== '/') {
		url = '/' + url;
	}
	let p = url;
	if (url.indexOf('?') != -1) {
		p = url.substr(0, url.indexOf('?'));
		// #ifdef  H5
		if (url.search("appId") == -1) {
			url = url + '&appId=' + config.appId;
		}
		// #endif
	} else {
		// #ifdef  H5
		if (url.search("appId") == -1) {
			url = url + '?appId=' + config.appId;
		}
		// #endif
	}
	let diyTabBarLinks = uni.getStorageSync('TabBar').list;
	if(diyTabBarLinks == null){
		diyTabBarLinks = [];
	}
	let res = diyTabBarLinks.some(item => {
		if (item.linkUrl == p) {
			return true
		}
	})
	if (res) {
		uni.reLaunch({
			url: url
		});
		return
	}
	// tabBar页面
	if (tabBarLinks.indexOf(p) > -1 && diyTabBarLinks.length <= 0) {
		uni.reLaunch({
			url: url
		});
	} else {
		if (process.env.NODE_ENV === 'production') {
			//判断是否分享页面
			if (shareLinks.indexOf(p) > -1) {
				//公众号
				// #ifdef  H5
				if(config.app_url.search(/h5/) < 0){
					window.location.href = config.app_url + config.h5_addr + url;
				}
				return;
				// #endif
			}
		}
		if (type == 'redirect') {
			uni.redirectTo({
				url: url
			});
			return
		}
		if (type == 'reLaunch') {
			uni.reLaunch({
				url: url
			});
			return
		}
		// 普通页面
		uni.navigateTo({
			url: url
		});
	}
}
