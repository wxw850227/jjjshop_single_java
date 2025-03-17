import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
	...App
})
app.$mount()
// #endif
// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
import onfire from '@/common/onfire.js'
// 公共组件
import headerBar from './components/header.vue'
/*底部数据*/
import tabBar from "@/components/tabbar/footTabbar.vue"
/* 配置 */
import config from "./env/config.js";
import {
	gotopage
} from '@/common/gotopage.js'
import requestFun from '@/utils/request.js';
import validator from '@/utils/validator.js';
import store from "./store/index.js"
//#ifdef H5
import jweixin from 'weixin-js-sdk';
//#endif
export function createApp() {
	const app = createSSRApp(App)

	app.component("header-bar", headerBar)
	app.component("tabBar", tabBar)

	app.use(store)
	app.config.globalProperties.$store = store;
	app.config.globalProperties.footTabberData = {
		active: 'home'
	}
	app.config.globalProperties.$fire = new onfire()
	app.config.globalProperties.config = config
	app.config.globalProperties.websiteUrl = config.app_url;
	app.config.globalProperties.appId = config.appId;
	//h5发布路径
	app.config.globalProperties.h5_addr = config.h5_addr;
	// #ifdef H5
	app.config.globalProperties.configWx = function(signPackage, shareParams, params) {
		if (signPackage == null) {
			return;
		}
		let self = this;
		jweixin.config({
			debug: false,
			appId: signPackage.appId, // 必填，公众号的唯一标识
			timestamp: ""+signPackage.timestamp, // 必填，生成签名的时间戳
			nonceStr: signPackage.nonceStr, // 必填，生成签名的随机串
			signature: signPackage.signature, //必填，签名，见附录1
			jsApiList: [
				'updateAppMessageShareData',
				'updateTimelineShareData'
			]
		});

		let url_params = self.getShareUrlParams(params);

		jweixin.ready(function(res) {
			jweixin.updateAppMessageShareData({
				title: shareParams.title,
				desc: shareParams.desc,
				link: self.websiteUrl + self.h5_addr + shareParams.link + '?' + url_params,
				imgUrl: shareParams.imgUrl,
				success: function() {

				}
			});
			jweixin.updateTimelineShareData({
				title: shareParams.title,
				desc: shareParams.desc,
				link: self.websiteUrl + self.h5_addr + shareParams.link + '?' + url_params,
				imgUrl: shareParams.imgUrl,
				success: function() {

				}
			});
		});
	};

	app.config.globalProperties.configWxScan = function(signPackage) {
		if (signPackage == '') {
			return;
		}
		jweixin.config(JSON.parse(signPackage));
		return jweixin;
	};
	// #endif
	/*页面跳转*/
	app.config.globalProperties.gotoPage = gotopage;
	app.config.globalProperties.font_url = config.font_url;

	app.config.globalProperties.points_name = function(e) {
		if (!e) {
			return store.state.points_name;
		} else {
			let re = new RegExp("积分", "g");
			return e.replace(re, store.state.points_name);
		}
	}
	//#ifdef H5
	// app.$router.afterEach((to, from) => {
	// 	const u = navigator.userAgent.toLowerCase()
	// 	if (u.indexOf("like mac os x") < 0 || u.match(/MicroMessenger/i) != 'micromessenger') return
	// 	if (to.path !== global.location.pathname) {
	// 		location.assign(config.h5_addr + to.fullPath);
	// 	}
	// })
	//#endif

	//请求
	requestFun(app);

	validator(app);

	app.config.globalProperties.theme = function() {
		return 'theme' + this.$store.state.theme || ''
	}
	app.config.globalProperties.callPhone = function(phone) {
		uni.makePhoneCall({
			phoneNumber: phone //仅为示例
		});
	}
	app.config.globalProperties.openmap = function(latitude, longitude) {
		uni.openLocation({
			longitude: Number(longitude),
			latitude: Number(latitude),
		})
	}
	app.config.globalProperties.footTab = function() {
		return this.$store.state.footTab || ''
	}

	app.config.globalProperties.getTabBarLinks = function() {
		uni.request({
			url: this.config.app_url + '/api/front/index/nav',
			data: {
				appId: this.config.appId
			},
			success: (res) => {
				let nav = res.data.data.nav;
				let theme = res.data.data.theme.theme;
				this.$store.commit('changeTheme', theme);
				uni.setStorageSync('TabBar', nav);
				if (nav.isAuto == 0) {
					uni.showTabBar();
					nav.list = [];
					let color = ['#ff5704', '#19ad57', '#ffcc00', '#1774ff', '#e4e4e4', '#c8ba97',
						'#623ceb'
					]
					uni.setTabBarStyle({
						color: '#333333',
						selectedColor: color[theme],
					})
					uni.setTabBarItem({
						index: 0,
						text: '首页',
						iconPath: '/static/tabbar/home.png',
						selectedIconPath: '/static/tabbar/home_' + theme + '.png'
					})
					uni.setTabBarItem({
						index: 1,
						text: '分类',
						iconPath: '/static/tabbar/category.png',
						selectedIconPath: '/static/tabbar/category_' + theme + '.png'
					})
					uni.setTabBarItem({
						index: 2,
						text: '购物车',
						iconPath: '/static/tabbar/cart.png',
						selectedIconPath: '/static/tabbar/cart_' + theme + '.png'
					})
					uni.setTabBarItem({
						index: 3,
						text: '我的',
						iconPath: '/static/tabbar/user.png',
						selectedIconPath: '/static/tabbar/user_' + theme + '.png'
					})
					console.log('-----show--------')
					uni.setStorageSync('theme', theme);
				} else {
					console.log('-----hide--------')
					uni.hideTabBar();
				}



			}
		});
	}

	app.config.globalProperties.getThemeColor = function() {
		let theme = this.$store.state.theme;
		let color = ['#ff5704', '#19ad57', '#ffcc00', '#1774ff', '#e4e4e4', '#c8ba97', '#623ceb'];
		return color[theme]
	}
	app.config.globalProperties.getTimeData = function(timestamp) {
		if (typeof timestamp === 'string') {
			timestamp = Number(timestamp);
		}
		if (typeof timestamp !== 'number') {
			alert("输入参数无法识别为时间戳");
		}
		let date = new Date(timestamp);
		let Y = date.getFullYear() + '-';
		let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		let D = date.getDate() + ' ';
		let h = date.getHours() + ':';
		let m = date.getMinutes() + ':';
		let s = date.getSeconds();
		return Y + M + D + h + m + s;
	}

	app.config.globalProperties.footTab = function() {
		return this.$store.state.footTab || ''
	}

	/* 小数点截取 */
	app.config.globalProperties.subPrice = function(str, val) {
		let strs = String(str);
		if (val == 1) {
			return strs.substring(0, strs.indexOf("."));
		} else if (val == 2) {
			let indof = strs.indexOf(".");
			return strs.slice(indof + 1, indof + 3);
		}
	}


	return {
		app,
	}
}
// #endif