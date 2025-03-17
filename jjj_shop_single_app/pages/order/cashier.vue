<template>
	<view :data-theme='theme()' :class="theme() || ''">
		<view class="tc buy-checkout-top">
			<view class="f32 mb20">待支付</view>
			<view class="redA8 f60 fb">￥{{payPrice || ''}}</view>
		</view>
		<view class="buy-checkout p-0-30">
			<view v-for="(item,index) in checkedPay" :key='index'>
				<view v-if="item==20" :class="pay_type == 20 ? 'item active' : 'item'" @tap="payTypeFunc(20)">
					<view class="d-s-c">
						<view class="icon-box d-c-c mr10"><span class="icon iconfont icon-weixin"></span></view>
						<text class="key">微信支付：</text>
					</view>
					<view class="icon-box d-c-c"><span class="icon iconfont icon-xuanze"></span></view>
				</view>
				<view v-if="item==30" :class="pay_type == 30 ? 'item active' : 'item'" @tap="payTypeFunc(30)">
					<view class="d-s-c">
						<view class="icon-box d-c-c mr10"><span class="icon iconfont icon-zhifubao"></span></view>
						<text class="key">支付宝支付：</text>
					</view>
					<view class="icon-box d-c-c"><span class="icon iconfont icon-xuanze"></span></view>
				</view>
			</view>
			<view v-if="hasBanlance" class="item">
				<view class="d-s-c">
					<view class="icon-box d-c-c mr10"><span class="icon iconfont icon-yue"></span></view>
					<text class="key">余额抵扣：(剩余：{{balance}})</text>
				</view>
				<switch :color="getTheme()" style="transform:scale(0.7);margin-right: -20rpx;" :checked="balanceType"
					@change="switch2Change" />
			</view>
		</view>
		<view class="bottom-btn" @click="submit">
			<button type="default">立即支付</button>
		</view>
	</view>
</template>

<script>
	import {
		pay
	} from '@/common/pay.js';
	export default {
		data() {
			return {
				balance: '',
				balanceType: false,
				type: 0,
				loading: true,
				orderId: 0,
				orderType: 0,
				pay_type: 20,
				checkedPay: [],
				payPrice: '',
			}
		},
		computed: {
			hasBanlance() {
				if (this.orderType == 30) {
					return false;
				}
				if(this.balance == '' || this.balance == 0){
					return false;
				}
				let n = this.checkedPay.indexOf(10);
				if (n == -1) {
					return false;
				} else {
					return true;
				}
			},
		},
		onLoad(e) {
			let self = this;
			// #ifdef MP
			wx.enableAlertBeforeUnload({
				message: "您的订单如在规定时间内未支付将被取消，请尽快完成支付",
				success: function(res) {
					console.log("方法注册成功：", res);
				},
				fail: function(errMsg) {
					console.log("方法注册失败：", errMsg);
				},
			});
			// #endif
			this.orderId = e.orderId;
			if(e.orderType){
				this.orderType = e.orderType;
			}
			this.paySource = this.getPlatform();
			this.getData();
		},
		onBackPress(options) {
			if (options.from === 'navigateBack') {
				return true;
			}
			this.back();
			return true;
		},
		methods: {
			back() {
				let self = this;
				uni.showModal({
					title: '提示',
					content: '您的订单如在规定时间内未支付将被取消，请尽快完成支付',
					cancelText: '继续支付',
					confirmText: '确认离开',
					success: function(res) {
						if (res.confirm) {
							uni.reLaunch({
								ulr: '/pages/order/order-detail?orderId=' + self.orderId
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			},
			getTheme() {
				let name = this.theme();
			},
			getData() {
				let self = this;
				self.loading = true;
				uni.showLoading({
					title: '加载中'
				});
				let url = 'user/order/toPay';
				if(self.orderType==20){
					url = 'plus/package/package/toPay';
				}
				if(self.orderType==30){
					url = 'balance/plan/toPay';
				}
				let params = {
					orderId: self.orderId,
					paySource: self.paySource
				}
				self._get(
					url, params,
					function(res) {
						self.loading = false;
						let list = [];
						res.data.payTypes.forEach(item => {
							list.push(item * 1);
						})
						self.checkedPay = list;
						self.payPrice = res.data.payPrice;
						self.balance = res.data.balance || '';
						if (self.checkedPay[0] != 10) {
							self.pay_type = self.checkedPay[0] || self.checkedPay[1] || 20;
						} else {
							self.pay_type = self.checkedPay[1] || self.checkedPay[0];
						}
						uni.hideLoading();
					})
			},
			switch2Change(e) {
				this.balanceType = e.detail.value;
			},
			submit() {
				let self = this;
				self.loading = true;
				uni.showLoading({
					title: '加载中'
				});
				let url = 'user/order/pay';
				if(self.orderType==20){
					url = 'plus/package/package/pay';
				}
				if(self.orderType==30){
					url = 'balance/plan/pay';
				}
				let use_balance = self.balanceType == true ? 1 : 0;
				if (self.payPrice == 0) {
					use_balance = 1;
				}
				let payType=self.pay_type;
				if(payType==10){
					payType=0;
				}
				let params = {
					orderId: self.orderId,
					paySource: self.getPlatform(),
					payType: payType,
					useBalance: use_balance,
				}
				self._postBody(url, params,
					function(res) {
						console.log(res);
						self.loading = false;
						uni.hideLoading();
						pay(res, self, self.paySuccess, self.payError);
					})
			},
			paySuccess(result) {
				let self = this;
				if (self.orderType == 30) {
					self.gotoPage('/pages/user/index/index')
				} else if (self.orderType == 20) {
					self.gotoPage('/pages/user/index/index')
				} else if (self.orderType == 40) {
					self.gotoPage('/pages/order/myorder')
				} else {
					self.gotoPage('/pages/order/pay-success/pay-success?orderId=' + result.data.orderId, 'reLaunch');
				}
			},
			payError(result) {
				let self = this;
				let url = '/pages/order/myorder';
				if (self.orderType == 50 ||self.orderType == 40 ||self.orderType == 30 || self.orderType == 20) {
					uni.navigateBack();
				} else {
					self.gotoPage('/pages/order/order-detail?orderId=' + result.data.orderId, 'redirect');
				}
			},
			payTypeFunc(n) {
				this.pay_type = n;
			}
		}
	}
</script>

<style lang="scss">
	.buy-checkout-top {
		padding: 100rpx 0;
	}

	.buy-checkout {
		.item {
			background-color: #FFFFFF;
			margin-bottom: 28rpx;
			box-shadow: 0rpx 13rpx 27rpx 0rpx rgba(172, 172, 172, 0.09);
			border-bottom: none;
			border-radius: 2rpx;
		}
	}

	.bottom-btn {
		position: fixed;
		bottom: 0;
		@include background_color("background_color");
		@include font_color('text_color2');
		width: 100%;
		padding-bottom: env(safe-area-inset-bottom);
	}

	.bottom-btn>button {
		width: 100%;
		height: 116rpx;
		line-height: 116rpx;
		@include background_color("background_color");
		@include font_color('text_color2');
		border: none;
		border-radius: 0;
		font-size: 35rpx;
		font-weight: bold;
		display: flexd;
		justify-content: center;
		align-items: center;
	}
	.pay {
		display: block;
		height: 88rpx;
		line-height: 88rpx;
		font-size: 30rpx;
		color: #FFFFFF;
		text-align: center;
		border-radius: 8rpx;
		color: #FFFFFF;
		background: #04be01;
	}
</style>
