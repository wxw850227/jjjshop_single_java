<template>
	<view>
		<button @click="login">apple登录</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			login(){
				let self = this;
				uni.login({  
				    provider: 'apple',  
				    success: function (loginRes) {  
				        // 登录成功  
				        uni.getUserInfo({  
				            provider: 'apple',  
				            success(res) {  
				                if (res.errMsg !== 'getUserInfo:ok') {
				                	return false;
				                }
								uni.showLoading({
									title: "正在登录",
									mask: true
								});
								self._post('user.userapple/login', {
									invitationId: self.invitationId,
									openId: res.userInfo.openId,
									nickName: res.userInfo.fullName.giveName?res.userInfo.fullName.giveName:'',
									refereeId: uni.getStorageSync('refereeId'),
									source: 'apple'
								}, result => {
									// 记录token userId
									uni.setStorageSync('token', result.data.token);
									uni.setStorageSync('userId', result.data.userId);
									// 执行回调函数
									uni.navigateBack();
								}, false, () => {
									uni.hideLoading();
								});
				            }  
				        })  
				    },  
				    fail: function (err) {  
				        // 登录失败  
						console.log('登录失败');
						console.log(err);
				    }  
				});  
			}
		}
	}
</script>

<style>

</style>
