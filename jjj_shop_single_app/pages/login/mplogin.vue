<template>
	<view>

	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		},
		onLoad(options) {
			uni.setStorageSync('token', options.token);
			uni.setStorageSync('userId', options.userId);
			// 获取登录前页面
			let url = '/' + uni.getStorageSync('currentPage');
			let pageOptions = uni.getStorageSync('currentPageOptions');
			if(Object.keys(pageOptions).length > 0){
				url += '?';
				for(let i in pageOptions){
					url += i + '=' + pageOptions[i] + '&';
				}
				url = url.substring(0, url.length - 1);
			}
			
			// 执行回调函数
			this.gotoPage(url, 'reLaunch');
		},
	}
</script>

<style>

</style>
