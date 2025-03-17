<template>
	<view :class="Visible ? 'pop-bg open' : 'pop-bg close'" @click.stop="closePop(null)">
		<view class="pop-content" @click.stop>
			<image @click="closePop(null)" class="close-img" src="/static/icon/close.png" mode=""></image>
			<view class="f30 gray3 ww100 mb35 tc">积分提现</view>
			<view class="input-box"><input type="text" v-model="value" @input="handleInput" />
				<image class="input-err" src="/static/icon/input-err.png" mode="" @click="value = ''"></image>
			</view>
			<view class="gray9 f24 tc mt20 mb20">1积分 = {{discount_ratio}} 余额</view>
			<!-- <view class="gray9 f24 tc mt20 mb20">{{value || 0}} 积分 = {{(discount_ratio * value) || 0}} 余额</view> -->
			<view class="ww100 d-c-c">
				<view class="sub-btn theme-btn" @click="submit()">确认</view>
				<view class="close-btn" @click="closePop(null)">取消</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniLoadMore from "@/components/uni-load-more.vue";
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				input_len: 6,
				/*是否可见*/
				Visible: false,
				value: '',
				is_send: false,
			}
		},
		props: ['isPop', 'discount_ratio'],
		watch: {
			isPop: function(n, o) {
				let self = this;
				if (n != o) {
					self.Visible = n;
				}
			},
		},
		methods: {
			submit() {
				let self = this;
				if (self.is_send) {
					return
				}
				self.is_send = true;
				let page = self.page;
				let list_rows = self.list_rows;
				self._post('user/user/transPoints', {
					points: self.value
				}, function(res) {
					self.is_send = false;
					self.showSuccess(res.msg, () => {
						self.closePop(true);
					})
				}, (err) => {
					self.is_send = false;
				});
			},
			handleInput(){
							 this.value = this.value.replace(/[^\d]/g, '');
			},
			closePop(e) {
				this.$emit('close', e);
				this.value = '';
			},
		}
	}
</script>

<style lang="scss">
	.input-box {
		width: 443rpx;
		height: 68rpx;
		border: 1rpx solid #EEEEEE;
		border-radius: 42rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 0 18rpx 0 27rpx;
		margin: 0 auto;

		input {
			border: none;
			font-size: 24rpx;
			color: #999999;
			flex: 1;
		}
	}

	.input-err {
		width: 42rpx;
		height: 42rpx;
		flex-shrink: 0;
		margin-left: 18rpx;
	}

	.sub-btn {
		width: 184rpx;
		height: 64rpx;
		background: #72DEED;
		border-radius: 36rpx;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		justify-content: center;
		align-content: center;
		margin-right: 67rpx;
		line-height: 64rpx;
	}

	.close-btn {
		width: 184rpx;
		height: 64rpx;
		background: #222222;
		border-radius: 36rpx;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		justify-content: center;
		align-content: center;
		line-height: 64rpx;
	}

	.dominant {
		color: $dominant-color;
	}

	.pop-bg {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100vh;
		background: rgba(0, 0, 0, 0.5);
		z-index: 99;

		.pop-content {
			position: fixed;
			z-index: 100;
			bottom: 0;
			left: 0;
			right: 0;
			top: 0;
			margin: auto;
			width: 597rpx;
			height: 385rpx;
			padding: 74rpx 0 32rpx 0;
			box-sizing: border-box;
			transform: translate3d(0, 0, 0);
			transition: transform 0.2s cubic-bezier(0, 0, 0.25, 1);
			background-color: #FFFFFF;
			border-radius: 10rpx;
		}
	}

	.pop-bg.close {
		// display: none;
		height: 0;

		.pop-content {
			transform: translate3d(0, 2000rpx, 0);
		}
	}

	.code-image {
		width: 360rpx;
		height: 360rpx;

	}

	.icon-img {
		width: 40rpx;
		height: 40rpx;
		margin-right: 16rpx;

	}

	.close-img {
		width: 32rpx;
		height: 32rpx;
		position: absolute;
		right: 32rpx;
		top: 32rpx;
	}

	.input-item {
		width: 72rpx;
		height: 96rpx;
		background: #ffffff;
		border-radius: 0px;
		font-size: 40rpx;
		color: rgba(0, 0, 0, 0.87);
		font-weight: 600;
		display: flex;
		justify-content: center;
		align-items: center;
		border-bottom: 2rpx solid #00BDBD;
		margin-right: 16rpx;
		text-align: center;
		line-height: 96rpx;
	}

	.input-item:last-child {
		margin-right: 0;
	}

	.input-hid {
		margin-left: -100%;
	}

	.get-code-btn {
		background-color: #FFFFFF;
		color: $dominant-color;
		background: none;
		padding: 0;
		margin: 0 auto;
	}

	button[disabled]:not([type]).get-code-btn {
		background-color: #FFFFFF;
		color: #999999;
	}
</style>
