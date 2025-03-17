<template>
	<view class="home-push-wrap">
		<Popup :show="isPopup" :width="width" :height="height" :backgroundColor="backgroundColor" :boxShadow="boxShadow" :padding="0" @hidePopup="hidePopupFunc">
			<view class="home-push" :class="type == 1 || type == 3 ? 'home-push-bg' : ''">
				<!--图文-->
				<template v-if="type == 1">
					<view class="image-text">
						<view class="pic"><image :src="form.filePath" mode="aspectFill"></image></view>
						<view class="title">{{ form.title }}</view>
						<view class="des">{{ form.remark }}</view>
						<view class="btns">
							<button type="primary" @click="jumpPage(form.link.url)">{{ form.des }}</button>
						</view>
					</view>
				</template>
				<!--纯图-->
				<template v-if="type == 2">
					<view class="image-only" @click="jumpPage(form.link.url)">
						<view class="pic"><image :src="form.filePath" mode="aspectFill"></image></view>
					</view>
				</template>
				<!--优惠券-->
				<template v-if="type == 3">
					<view class="cuopon">
						<view class="cuopon-title d-c-c" v-if="form.filePath != null && form.filePath != ''"><image :src="form.filePath" mode="aspectFill"></image></view>
						<view class="list">
							<view class="item" v-for="(item, index) in coupon" :key="index">
								<view class="info">
									<template v-if="item.couponType == 10">
										<view class="price">
											<text class="f32 fb">减{{ item.reducePrice * 1 }}元</text>
										</view>
									</template>
									<template v-if="item.couponType == 20">
										<text class="f32 fb">{{ item.discount / 10 }}</text>
										<text>折</text>
									</template>
									<view class="f26">{{ item.minPrice > 0 ? '满' + item.minPrice * 1 + '元可用' : '无门槛' }}</view>
								</view>
								<view class="explan">
									<text class="name">{{ item.couponType == 10 ? '满减券' : '折扣券' }}</text>
								</view>
							</view>
						</view>
						<view class="btns"><button type="primary" @click="getCoupon()">立即领取</button></view>
					</view>
				</template>
			</view>
			<view class="close-btns" @click="hidePopupFunc(true)"><text class="icon iconfont icon-guanbi"></text></view>
		</Popup>
	</view>
</template>

<script>
import Popup from '@/components/uni-popup.vue';
export default {
	components: {
		Popup
	},
	data() {
		return {
			/*是否显示*/
			isPopup: false,
			/*展示类别*/
			type: 0,
			/*宽度*/
			width: 600,
			/*高度*/
			height: 800,
			/*背景颜色*/
			backgroundColor: 'none',
			/*阴影*/
			boxShadow: 'none',
			form: {},
			coupon: []
		};
	},
	props: ['homepush_data'],
	created() {},
	mounted() {
		/*获取弹出层内容*/
		this.setData();
	},
	methods: {
		/*获取弹出层内容*/
		setData() {
			this.isPopup = true;
			this.form = this.homepush_data;
			this.type = this.homepush_data.type;
			this.coupon = this.homepush_data.coupon;
		},
		/*关闭弹窗*/
		hidePopupFunc(e) {
			uni.setStorageSync('homepush_name', this.homepush_data.name);
			this.isPopup = false;
		},

		/*跳转页面*/
		jumpPage(e) {
			this.hidePopupFunc();
			this.gotoPage('/' + e);
		},

		/*领取优惠券*/
		getCoupon() {
			let self = this;
			let couponIds = [];
			self.coupon.forEach(item => {
				couponIds.push(item.couponId);
			});
			console.log(couponIds);
			self._get(
				'user/coupon/receiveList',
				{
					couponIds: JSON.stringify(couponIds)
					//couponIds: couponIds,
				},
				function(res) {
					uni.showToast({
						title: '领取成功',
						icon: 'success',
						mask: true,
						duration: 2000
					});
					self.hidePopupFunc();
				}
			);
		}
	}
};
</script>

<style lang="scss">
.home-push {
	width: 100%;
	border-radius: 20upx;
	overflow: hidden;
}

.home-push-bg {
	background: #ffffff;
	box-shadow: 0 0 30upx rgba(0, 0, 0, 0.1);
}

.home-push .title {
	padding: 10rpx 20rpx;
	text-align: center;
	font-size: 30rpx;
	font-weight: bold;
}

.home-push .des {
	padding: 20rpx;
	color: #999999;
}

.home-push .btns {
	padding: 20rpx;
	display: flex;
	justify-content: center;
	align-items: center;
}

.home-push .btns button {
	width: 200rpx;
	padding: 0;
	height: 80rpx;
	line-height: 80rpx;
	font-size: 28rpx;
	border-radius: 45rpx;
	background: $dominant-color;
}

.home-push-wrap .image-text .pic {
	width: 600rpx;
	height: 300rpx;
}

.home-push-wrap .image-only .pic {
	width: 600rpx;
	height: 420rpx;
}

.home-push .cuopon .cuopon-title {
	width: 600rpx;
	height: 250rpx;
	overflow: hidden;
}

.home-push-wrap image {
	width: 100%;
	height: 100%;
}

.home-push .cuopon .list {
	padding: 20rpx;
}

.home-push .cuopon .list .item {
	margin-top: 20rpx;
	display: flex;
	justify-content: flex-start;
	background: #fff9eb;
}

.home-push .cuopon .item .info {
	flex: 1;
	padding: 20rpx;
	border: 1px solid #ffe4d8;
	border-radius: 16rpx;
}

.home-push .cuopon .item .info .num {
	color: $dominant-color;
}

.home-push .cuopon .item .info .sub {
}

.home-push .cuopon .item .explan {
	position: relative;
	width: 120rpx;
	padding: 20rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	border: 1px solid #ffe4d8;
	border-left: 2px dashed #ffe4d8;
	border-radius: 16rpx;
	background: #ff553c;
	color: #ffffff;
}

.home-push .cuopon .item .explan .name {
	font-size: 30rpx;
	color: #fff9eb;
}

.home-push .cuopon .item .explan .sub {
	color: #ff9897;
}

.home-push .cuopon .list .explan::before,
.home-push .cuopon .list .explan::after {
	display: block;
	position: absolute;
	content: '';
	width: 20rpx;
	height: 10rpx;
	background: #ffffff;
}

.home-push .cuopon .list .explan::before {
	left: -10rpx;
	top: -1px;
	border-radius: 0 0 10rpx 10rpx;
}

.home-push .cuopon .list .explan::after {
	left: -10rpx;
	bottom: -1px;
	border-radius: 10rpx 10rpx 0 0;
}

.home-push-wrap .close-btns {
	margin: 50rpx auto 0;
	width: 60rpx;
	height: 60rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	border-radius: 50%;
	background: none;
	border: 2px solid #ffffff;
}

.home-push-wrap .close-btns .iconfont {
	color: #ffffff;
}
</style>
