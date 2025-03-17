<template>
	<view class="product-detail pr" :data-theme="theme()" :class="theme() || ''">
		<!-- #ifdef MP-WEIXIN || APP-PLUS -->
		<view class="tc  header" :style="topBarHeight() == 0 ? '' : 'height:' + topBarHeight() + 'px;padding-top:' + topBarTop() + 'px'">
			<view class="reg180" :style="topBarHeight() == 0 ? '' : 'height:' + topBarHeight() + 'px;'">
				<image @click="goback" src="/static/icon/back-jianatou.png" mode="" style="width: 48rpx;height: 48rpx;"></image>
			</view>
		</view>
		<!-- #endif -->
		<scroll-view scroll-y="true" class="scroll-Y scroll-box" :style="'height:' + scrollviewHigh + 'px'" v-if="!loadding">
			<!--图片-->
			<view class="ww100" :style="'height:' + topBarTop() + 'px'"></view>
      <view class="ww100" :style="'height:' + topBarTop() + 'px'"></view>
      <view class="product-pic">
        <swiper
            class="swiper"
            indicator-active-color="#ffffff"
            indicator-color="rgba(255,255,255,.3)"
            :indicator-dots="indicatorDots"
            :autoplay="autoplay"
            :interval="interval"
            :duration="duration"
            @change="changeSwiper">
          <swiper-item v-if="detail.videoId != 0">
            <view v-if="!isVideoPlay" class="icon iconfont icon-bofang" @click="isVideoPlay = true"></view>
            <image v-if="!isVideoPlay" :src="detail.posterId != 0 ? detail.posterFilePath : detail.image[0].filePath" mode="" @click="isVideoPlay = true"></image>
            <video
                v-if="isVideoPlay"
                :src="detail.videoFilePath"
                :autoplay="isVideoPlay"
                @click="isVideoPlay = false"
                class="video"
                :controls="false"
                :show-center-play-btn="false"
                :show-play-btn="false" :enable-progress-gesture="false"
            ></video>
          </swiper-item>
          <swiper-item v-for="(item, index) in detail.image" :key="index"><image :src="item.filePath" mode="aspectFit"></image></swiper-item>
        </swiper>
      </view>
			<view class="bg-white p30 mb22">
				<view class="price-wrap">
					<view class="d-s-s d-c ww100">
						<view class="d-s-c pr ww100 mb16">
							<view class="new-price theme-price">
								<text class="" v-if="detail.isUserGrade">会员价</text>
								<text>¥</text>
								<text class="num">{{ detail.productSku.productPrice }}</text>
								<text v-if="detail.specType == 20">-{{ detail.highPrice }}</text>
							</view>
							<text class="old-price ">¥{{ detail.productSku.linePrice }}</text>
							<!--分享-->
							<view class="share-box">
								<button @click="showShare">
									<image class="share_img" src="/static/icon/fx.png" mode=""></image>
									<text class="f26">分享</text>
								</button>
							</view>
							<view class="sc-box">
								<button @click="favorite()">
									<image :class="isFav ? '' : 'img_gray'" class="share_img" src="/static/icon/sc.png" mode=""></image>
									<text class="f22" :class="isFav ? 'red' : 'gray3'">收藏</text>
								</button>
							</view>
						</view>
						<view>
							<text class="already-sale">已售{{ detail.productSales }}件</text>
						</view>
					</view>
				</view>
				<view class="product-name text-ellipsis-2">{{ detail.productName }}</view>
				<view class="product-describe" v-if="detail.sellingPoint">{{ detail.sellingPoint }}</view>
			</view>
			<view class="o-h p-0-30 bg-white mb21 pb44" v-if="show_discount" @click="openCoupon()">
				<!--已选择-->
				<view class="already-choice">
					<view class="group-hd d-b-s" style="height: auto;">
						<view class="left"><text class="min-name gray3">优惠：</text></view>
					</view>
					<view class="d-b-c">
						<view class="flex-1">
							<view class="pb20 line-h-50 f22 gray3 " v-if="discount.give_points > 0">
								<text class="text-box">返积分</text>
								商城购物返积分，订单完成后最高返{{ discount.give_points }}积分
							</view>
							<view class="flex-1 d-b-c" v-if="discount.product_coupon.length > 0">
								<view class="f22 gray3 line-h-50 pb20 flex-1">
									<view class=" d-b-s">
										<text class="text-box ">领券</text>
										<view class="flex-1 text-coupon-list">
                      <template v-for="(item, index) in discount.product_coupon" :key="index">
                        <view class="ml20 mb10 text-coupon-box " v-if="index <= 2" :label="item" :value="index">
                          <view class="text-coupon-left"></view>
                          <view class="text-coupon">{{ item.name }}</view>
                          <view class="text-coupon-right"></view>
                        </view>
                      </template>
										</view>
									</view>
								</view>
							</view>
						</view>
						<view><text class="icon iconfont icon-jiantou" style="color: #999999;font-size: 22rpx;"></text></view>
					</view>
				</view>
			</view>
			<view class="o-h p-0-30 bg-white mb21">
				<!--已选择-->
				<view class="already-choice" v-if="detail.spec_type == 20" @click="openPopup(ispresale ? 'deposit' : 'order')">
					<view class="group-hd">
						<view class="left"><text class="min-name gray3">选择：</text></view>
						<view class="flex-1 p-0-20 center-content f28 text-ellipsis o-h">{{ alreadyChioce }}</view>
						<view class="right"><text class="icon iconfont icon-jiantou" style="font-size: 22rpx;color: #9A9A9A;"></text></view>
					</view>
				</view>
			</view>

			<!--评价-->
			<view class="product-comment">
				<view class="p-0-30 d-b-c">
					<view class="group-hd left">
						<text class="min-name f28">评价({{ commentDataCount }})</text>
					</view>
					<view class="right" @click="lookEvaluate(detail.productId)">
						<text class="more">查看全部</text>
						<text class="icon iconfont icon-jiantou" style="font-size: 22rpx;color: #9A9A9A;"></text>
					</view>
				</view>
				<view class="comment-list" v-if="commentDataCount > 0">
					<view class="item" v-if="index <= 1" v-for="(item, index) in commentData" :key="index">
						<view class="cmt-user">
							<view class="left">
								<image class="photo" :src="item.avatarUrl" mode="aspectFill"></image>
								<text class="name">{{ item.nickName }}</text>
							</view>
							<text class="datetime">{{ item.createTime }}</text>
						</view>
						<view class="mt20 lh150 f24">{{ item.content }}</view>
					</view>
				</view>
			</view>

			<!--详情内容-->
			<view class="product-content">
				<view class="p-0-30  border-b-e">
					<view class="group-hd d-s-c"><text class="min-name f28">商品介绍</text></view>
				</view>
				<view v-if="detail.isPicture == 0" class="content-box" v-html="detail.content"></view>
				<view v-if="detail.isPicture == 1" class="content-box">
					<view class="ww100" v-for="(item, index) in detail.contentImage" :key="index"><image class="ww100" :src="item.filePath" mode="widthFix"></image></view>
				</view>
			</view>
		</scroll-view>

		<!--底部按钮-->
		<view class="btns-wrap">
			<view class="icon-box d-c-c" @click="gotoPage('/pages/index/index')">
				<button class="d-c-c d-c bg-white">
					<text class="btn_btom pr icon iconfont icon-shouye gray9" style="font-size: 36rpx;height: 50rpx;line-height: 60rpx;"></text>
					<text class="f22 gray9" style="height: 50rpx;line-height: 40rpx;">首页</text>
				</button>
			</view>
			<!-- #ifdef MP-WEIXIN -->
			<view class="icon-box d-c-c">
				<button class="d-c-c d-c bg-white" open-type="contact" session-from="wxapp" show-message-card="true">
					<text class="icon iconfont icon-kefu gray9" style="font-size: 36rpx;height: 50rpx;line-height: 60rpx;"></text>
					<text class="f22 gray9" style="height: 50rpx;line-height: 40rpx;">客服</text>
				</button>
			</view>
			<!-- #endif -->
			<!-- #ifndef MP-WEIXIN -->
			<view class="icon-box d-c-c" @click="openMaservice">
				<button class="d-c-c d-c bg-white">
					<text class="icon iconfont icon-kefu gray9" style="font-size: 36rpx;height: 50rpx;line-height: 60rpx;"></text>
					<text class="f22 gray9" style="height: 50rpx;line-height: 40rpx;">客服</text>
				</button>
			</view>
			<!-- #endif -->
			<view class="icon-box d-c-c" @click="gotocart">
				<button class="pr d-c-c d-c bg-white">
					<text class="gray9 icon iconfont icon-gouwuche1" style="font-size: 36rpx;height: 50rpx;line-height: 60rpx;"></text>
					<text class="f22 gray9" style="height: 50rpx;line-height: 40rpx;">购物车</text>
					<text v-if="cart_total_num > 0" class="cart_num">{{ cart_total_num }}</text>
				</button>
			</view>
			<button v-if="!isVirtual" class="add-cart" @click="openPopup('card')">加入购物车</button>
			<button class="buy" @click="openPopup('order')">立即购买</button>
		</view>

		<!--购物确定-->
		<spec :isPopup="isPopup" :productModel="productModel" @close="closePopup"></spec>
		<!--底部弹窗-->
		<share :isbottmpanel="isbottmpanel" :productId="productId" @close="closeBottmpanel"></share>
		<!--生成图片-->
		<uniPopup :show="isCreatedImg" type="middle" @hidePopup="hidePopupFunc">
			<view class="d-c-c d-c create-img">
				<image :src="poster_img" mode="widthFix"></image>
				<!-- #ifdef MP -->
				<button class="btn-red mt20" type="default" @click="savePosterImg">保存图片</button>
				<!-- #endif  -->
				<!-- #ifdef H5 -->
				<view class="mt20 f34 red" type="default">长按保存图片</view>
				<!-- #endif -->
			</view>
		</uniPopup>
		<!--客服-->
		<Mpservice v-if="isMpservice" :isMpservice="isMpservice" @close="closeMpservice"></Mpservice>
		<!--优惠券-->
		<coupon :isCoupon="isCoupon" :discount="discount" :couponList="discount.product_coupon" @close="closeCouponFunc"></coupon>
	</view>
</template>

<script>
import coupon from './popup/coupon.vue';
import share from './popup/share.vue';
import spec from './popup/spec.vue';
import uniPopup from '@/components/uni-popup.vue';
import Mpservice from '@/components/mpservice/Mpservice.vue';
import utils from '@/common/utils.js';
import Countdown from '@/components/countdown/countdown-presale.vue';
export default {
	components: {
		spec,
		share,
		uniPopup,
		Mpservice,
		coupon,
		Countdown
	},
	data() {
		return {
			isVirtual: false,
			/*手机高度*/
			phoneHeight: 0,
			/*可滚动视图区域高度*/
			scrollviewHigh: 0,
			/*是否加载完成*/
			loadding: true,
			/*是否显示面板指示点*/
			indicatorDots: true,
			/*是否知道切换*/
			autoplay: false,
			/*自动切换时间间隔*/
			interval: 2000,
			/*滑动动画时长*/
			duration: 500,
			/*是否确定购买弹窗*/
			isPopup: false,
			/*商品id*/
			productId: null,
			/*商品详情*/
			detail: {
				productSku: {},
				showSku: {
					productPrice: '',
					productSkuId: 0,
					linePrice: '',
					stockNum: 0,
					skuImage: ''
				}
			},
			/*商品属性*/
			specData: null,
			/*子级页面传参*/
			productModel: {},
			buyNow: false,
			url: '',
			/*规格数组*/
			productSpecArr: [],
			/*购物车商品数量*/
			cart_total_num: 0,
			/*分享*/
			isbottmpanel: false,
			/*是否生成图片*/
			isCreatedImg: false,
			poster_img: '',
			/*是否打开客服*/
			isMpservice: false,
			/*已经选择的规格*/
			alreadyChioce: '',
			isVideoPlay: false,
			isFav: false,
			isCoupon: false,
			couponList: [],
			show_discount: '',
			discount: {
				product_coupon: [],
				product_reduce: [],
				give_points: ''
			},
			/*倒计时配置*/
			countdownConfig: {
				/*开始时间*/
				startstamp: 0,
				/*结束时间*/
				endstamp: 0
			},
			// 评论
			commentDataCount: 0,
			commentData: [],

			/* 活动字段 */
			activeName: '',
			activePrice: '',
			activeText: '',
			skuName: ''
		};
	},
	onReady() {
		uni.setNavigationBarColor({
			frontColor: '#000000',
			backgroundColor: '#F5F5F5'
		});
	},
	onLoad(e) {
		/*商品id*/
		let scene = utils.getSceneData(e);
		this.productId = e.productId ? e.productId : scene.gid;
		// 公众号分享
		if (this.getPlatform() == 'mp') {
			this.url = window.location.href;
		}
	},
	mounted() {
		this.init();
		/*获取产品详情*/
		this.getData();
	},
	methods: {
		/*初始化*/
		init() {
			let _this = this;
			uni.getSystemInfo({
				success(res) {
					_this.phoneHeight = res.windowHeight;
					// 计算组件的高度
					let view = uni.createSelectorQuery().select('.btns-wrap');
					view.boundingClientRect(data => {
						let h = _this.phoneHeight - data.height;
						_this.scrollviewHigh = h;
					}).exec();
				}
			});
		},

		/*获取数据*/
		getData() {
			let self = this;
			uni.showLoading({
				title: '加载中'
			});
			self._get(
				'product/product/detail',
				{
					productId: self.productId,
					url: self.url
				},
				function(res) {
					self.cart_total_num = res.data.cartTotalNum;

					/*详情内容格式化*/
					res.data.detail.content = utils.format_content(res.data.detail.content);

					// 初始化商品多规格
					if (res.data.detail.specType == 20) {
						self.initSpecData(res.data.specData);
					}

					self.detail = res.data.detail;
					self.isVirtual = res.data.detail.isVirtual;
					self.isFav = res.data.isFav;
					self.commentDataCount = res.data.commentDataCount;
					self.commentData = res.data.commentData;
					self.show_discount = res.data.show_discount;
					self.loadding = false;
					uni.hideLoading();
				}
			);
		},

		/*多规格商品*/
		initSpecData(data) {
			for (let i in data.specAttr) {
				for (let j in data.specAttr[i].specItems) {
					data.specAttr[i].specItems[j].checked = false;
				}
			}
			this.specData = data;
			if (this.specData.specAttr) {
				this.specData.specAttr.forEach(item => {
					this.alreadyChioce += item.groupName;
					this.alreadyChioce += ' / ';
				});
				this.alreadyChioce = this.alreadyChioce.replace(/(\s\/\s)$/gi, '');
			}
		},

		/*选规格*/
		openPopup(e) {
			let obj = {
				specData: this.specData,
				detail: this.detail,
				productSpecArr: this.specData != null ? new Array(this.specData.specAttr.length) : [],
				showSku: {
					skuImage: '',
					productSkuId: 0,
					linePrice: 0,
					sum: 1
				},
				plusSku: null,
				type: e,
				plusName: ''
			};
			this.productModel = obj;
			this.isPopup = true;
		},

		/*关闭弹窗*/
		closePopup(e, cart_total_num) {
			this.isPopup = false;
			if (e && e.specAttr) {
				this.alreadyChioce = '';
				let has = '已选：';
				let noone = '';
				e.specAttr.forEach(item => {
					if (item.specItems) {
						let h = '';
						for (let i = 0; i < item.specItems.length; i++) {
							let child = item.specItems[i];
							if (child.checked) {
								h = child.specValue + ' / ';
								break;
							}
						}
						if (h != '') {
							has += h;
						} else {
							noone += item.groupName;
						}
					}
				});
				if (noone != '') {
					this.alreadyChioce = noone;
				} else {
					has = has.replace(/(\s\/\s)$/gi, '');
					this.alreadyChioce = has;
				}
			}
			if (cart_total_num) {
				this.cart_total_num = cart_total_num;
			}
		},
		openCoupon() {
			this.isCoupon = !this.isCoupon;
			console.log(this.isCoupon);
		},
		/*关闭优惠券*/
		closeCouponFunc(e) {
			this.isCoupon = false;
		},
		/*查看更多评价*/
		lookEvaluate(productId) {
			this.gotoPage('/pages/product/detail/look-evaluate/look-evaluate?productId=' + productId);
		},

		/*分享*/
		onShareAppMessage() {
			let self = this;
			// 构建页面参数
			let params = self.getShareUrlParams({
				productId: self.productId
			});
			return {
				title: self.detail.productName,
				path: '/pages/product/detail/detail?' + params
			};
		},

		/*跳转购物车*/
		gotocart() {
			uni.switchTab({
				url: '/pages/cart/cart'
			});
		},

		//关闭分享
		closeBottmpanel(data) {
			this.isbottmpanel = false;
			if (data.type == 2) {
				this.poster_img = data.poster_img;
				this.isCreatedImg = true;
			}
		},
		//分享按钮
		showShare() {
			let self = this;
			//#ifndef APP-PLUS
			self.isbottmpanel = true;
			//#endif
		},

		//关闭生成图片
		hidePopupFunc() {
			this.isCreatedImg = false;
		},

		//保存海报图片
		savePosterImg() {
			let self = this;
			uni.showLoading({
				title: '加载中'
			});
			// 下载海报图片
			uni.downloadFile({
				url: self.poster_img,
				success(res) {
					uni.hideLoading();
					// 图片保存到本地
					uni.saveImageToPhotosAlbum({
						filePath: res.tempFilePath,
						success(data) {
							uni.showToast({
								title: '保存成功',
								icon: 'success',
								duration: 2000
							});
							// 关闭商品海报
							self.isCreatedImg = false;
						},
						fail(err) {
							if (err.errMsg === 'saveImageToPhotosAlbum:fail auth deny') {
								uni.showToast({
									title: '请允许访问相册后重试',
									icon: 'none',
									duration: 1000
								});
								setTimeout(() => {
									uni.openSetting();
								}, 1000);
							}
						},
						complete(res) {
							console.log('complete');
						}
					});
				}
			});
		},
		goback() {
			let routes = getCurrentPages(); // 获取当前打开过的页面路由数组
			console.log(routes.length);
			if (routes.length <= 1) {
				this.gotoPage('/pages/index/index');
			} else {
				uni.navigateBack();
			}
		},
		/*打开客服*/
		openMaservice() {
			this.isMpservice = true;
		},

		/*关闭客服*/
		closeMpservice() {
			this.isMpservice = false;
		},
		changeSwiper() {
			this.isVideoPlay = false;
		},
		//收藏商品
		favorite() {
			let self = this;
			self._post(
				'user/favorite/fav',
				{
					productId: self.productId
				},
				res => {
					if (self.isFav) {
						self.isFav = false;
						uni.showToast({
							title: res.msg
						});
					} else {
						self.isFav = true;
						uni.showToast({
							title: res.msg
						});
					}
				}
			);
		},
		returnValFunc(e) {}
	}
};
</script>

<style lang="scss" scoped>
.product-detail {
}

.sage-bottom {
	width: 100%;
	height: calc(100rpx + env(safe-area-inset-bottom));
}

.product-detail .product-pic,
.product-detail .product-pic .swiper,
.product-detail .product-pic image {
	width: 750rpx;
	height: 750rpx;
}

.product-detail .product-pic .swiper .icon-bofang {
	position: absolute;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	margin: auto;
	font-size: 48rpx;
	color: #ffffff;
	border-radius: 50%;
	border: 4rpx solid #ffffff;
	width: 120rpx;
	height: 120rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	background-color: #00000080;
	padding-left: 16rpx;
	box-sizing: border-box;
	z-index: 10;
}

.product-detail .price-wrap {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.product-detail .price-wrap .left {
	display: flex;
	justify-content: flex-start;
	align-items: flex-end;
}

.product-detail .price-wrap .new-price {
	color: $dominant-color;
	font-size: 38rpx;
	font-weight: bold;
	margin-right: 14rpx;
}

.product-detail .price-wrap .new-price .num {
	padding: 0 4rpx;
	font-size: 42rpx;
}

.product-detail .price-wrap .old-price {
	font-size: 28rpx;
	color: #999999;
	text-decoration: line-through;
}

.product-detail .price-wrap .is-user-grade {
	padding: 0 10rpx;
	border-radius: 12rpx;
	margin-left: 10rpx;
	border: 2rpx solid $dominant-color;
	color: $dominant-color;
}

.product-detail .already-sale {
	font-size: 24rpx;
	color: #999999;
}

.product-detail .product-name {
	padding-top: 26rpx;
	font-size: 30rpx;
	font-weight: 500;
	color: #333333;
}

.product-detail .product-describe {
	line-height: 38rpx;
	font-size: 24rpx;
	color: #666666;
	word-break: break-all;
	margin-top: 28rpx;
}

.already-choice {
	background: #ffffff;
}

.already-choice .center-content {
	// line-height: 90rpx;
}

.product-comment,
.product-content {
	margin-top: 20rpx;
	background: #ffffff;
}

.product-content .content-box p image {
	width: 100%;
}

.product-content .content-box {
	font-size: 36rpx;
}

.btns-wrap {
	position: fixed;
	height: 100rpx;
	right: 0;
	bottom: 0;
	left: 0;
	display: flex;
	background: #ffffff;
	align-items: center;
	padding-left: 5rpx;
	padding-bottom: env(safe-area-inset-bottom);
}

.btns-wrap .icon-box {
	width: 92rpx;
	height: 100rpx;
}

.btns-wrap .icon-box .iconfont {
	font-size: 40rpx;
	color: #999999;
}

.btns-wrap .icon-box .iconfont .num {
	position: absolute;
	top: 10rpx;
	left: 50%;
	height: 30rpx;
	min-width: 30rpx;
	overflow: hidden;
	line-height: 32rpx;
	border-radius: 15rpx;
	font-size: 20rpx;
	color: #ffffff;
	background: red;
}

.btns-wrap button,
.btns-wrap button:after {
	height: 100rpx;
	line-height: 100rpx;
	margin: 0;
	padding: 0;
	flex: 1;
	border-radius: 0;
	border: 0;
}

.btns-wrap button.add-cart {
	font-size: 28rpx;
	width: 214rpx;
	height: 75rpx;
	line-height: 75rpx;
	border-top-left-radius: 40rpx;
	border-bottom-left-radius: 40rpx;
	margin-left: 17rpx;
	@include font_color('text_color2');
  @include background_linearmore('cart_left1', 'cart_left2', 'left_deg', 0%, 100%);
}

.btns-wrap button.add-cart-no {
	font-size: 28rpx;
	width: 214rpx;
	height: 75rpx;
	line-height: 75rpx;
	border-top-left-radius: 40rpx;
	border-bottom-left-radius: 40rpx;
	color: #ffffff;
	margin-left: 17rpx;
	background: #cccccc;
}

.btns-wrap button.buy {
	font-size: 28rpx;
	width: 214rpx;
	height: 75rpx;
	line-height: 75rpx;
	border-top-right-radius: 40rpx;
	border-bottom-right-radius: 40rpx;
	margin-right: 30rpx;
	@include font_color('text_color1');
  @include background_linearmore('cart_right1', 'cart_right2', 'right_deg', 0%, 100%);
}

.btns-wrap button.buy.ispresale {
	line-height: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}

.red {
	color: #f6220c !important;
}

.shoucang-box {
	position: fixed;
	padding-right: 10rpx;
	width: 80rpx;
	height: 80rpx;
	right: 0;
	bottom: 270rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	border-radius: 16rpx 0 0 16rpx;
	background: rgba(0, 0, 0, 0.8);
}

.shoucang-box button {
	padding: 0;
	background: 0;
	line-height: 60rpx;
}

.shoucang-box .iconfont {
	margin-bottom: 10rpx;
	font-size: 50rpx;
	color: #ffffff;
	position: relative;
	top: 5rpx;
}

.share-box {
	position: absolute;
	width: 60rpx;
	height: 60rpx;
	right: 0;
	bottom: -16rpx;
	display: flex;
	justify-content: center;
	align-items: center;
}

.share-box button {
	padding: 0;
	background: 0;
	line-height: 60rpx;
	border-radius: 0;
}

.share-box .iconfont {
	margin-bottom: 10rpx;
	font-size: 50rpx;
	color: #ffffff;
}

.sc-box {
	position: absolute;
	width: 60rpx;
	height: 60rpx;
	right: 78rpx;
	bottom: -16rpx;
	display: flex;
	justify-content: center;
	align-items: center;
}

.sc-box button {
	padding: 0;
	background: 0;
	line-height: 60rpx;
	border-radius: 0;
}

.sc-box .iconfont.icon {
	font-size: 38rpx;
}

.create-img {
	width: 100%;
	padding: 20rpx;
	box-sizing: border-box;
}

.create-img image {
	width: 100%;
}

.create-img button {
	width: 100%;
	height: 88rpx;
	line-height: 88rpx;
	border-radius: 44rpx;
	box-shadow: 0 8rpx 16rpx 0 rgba(226, 35, 26, 0.6);
}

.shop_head_info {
	margin: 20rpx;
	padding: 30rpx;
	box-sizing: border-box;
	background-color: white;
	border-radius: 12rpx;
}

.shop_list_body_item_shop {
	width: 100%;
	height: 120rpx;
	display: flex;
	justify-content: space-between;
}

.shop_list_body_item_shop_logo {
	width: 120rpx;
	height: 120rpx;
}

.shop_list_body_item_shop_logo image {
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.1);
	border-radius: 12rpx;
}

.shop_list_body_item_shop_info {
	box-sizing: border-box;
	margin-left: 20rpx;
	padding-top: 0;
	display: flex;
	justify-content: space-between;
	flex-direction: column;
}

.shop_list_body_item_shop_others {
	box-sizing: border-box;
	display: flex;
	justify-content: space-between;
	flex-direction: column;
	text-align: right;
	padding-top: 0;
}

.shop_list_body_item_shop_others button {
	width: 160rpx;
	height: 60rpx;
	border: 1rpx solid #f6220c;
	border-radius: 30rpx;
	line-height: 60rpx;
	font-size: 26rpx;
	font-family: PingFang SC;
	font-weight: 500;
	color: #f6220c;
	text-align: center;
	padding: 0;
	background-color: #ffffff;
}

.h1 {
	font-size: 32rpx;
}

.h2 {
	font-size: 28rpx;
}

.h3 {
	font-size: 24rpx;
}

.h4 {
	font-size: 20rpx;
}

.h5 {
	font-size: 16rpx;
}

.h6 {
	font-size: 12rpx;
}

.collect text {
	color: #f6220c;
}

.store_type {
	display: inline-block;
	background-color: #f6220c;
	color: #ffffff;
	border-radius: 7rpx;
	font-weight: 200;
	height: 35rpx;
	line-height: 35rpx;
	font-size: 21rpx;
	padding: 0 10rpx;
	margin-right: 20rpx;
}

.share_img {
	width: 42rpx;
	height: 42rpx;
	margin: 0 auto;
	margin-bottom: 4rpx;
}

.share_img.img_gray {
	-webkit-filter: grayscale(100%);
	filter: grayscale(100%);
}

.share_text {
	line-height: 34rpx;
}

.reg180 {
	padding-right: 20rpx;
	text-align: center;
	transform: rotateY(180deg);
	display: flex;
	justify-content: flex-end;
	align-items: center;
}

.header {
	z-index: 99;
	position: fixed;
	height: 30px;
	line-height: 30px;
	top: 0;
	left: 0;
	width: 100%;
	padding-top: var(--status-bar-height);
}

.header .reg180 .icon-jiantou {
	color: #ffffff;
	background: rgba($color: #000000, $alpha: 0.6);
	display: block;
	width: 44rpx;
	height: 44rpx;
	line-height: 44rpx;
	border-radius: 50%;
}

.btn_btom {
	height: 90rpx;
	line-height: 45rpx;
}

.btnname {
	position: absolute;
	bottom: -16px;
	left: 0;
	right: 0;
}

.icon-dianpu1 {
	color: #333333;
}

.icon-kefu2 {
	color: #333333;
}

.cart_num {
	position: absolute;
	background: #f6220c;
	width: 30rpx;
	height: 30rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 22rpx;
	border-radius: 50%;
	color: #ffff;
	right: 4rpx;
	top: 4rpx;
}

.video {
	width: 100%;
	height: 100%;
}

.scroll-box {
	// padding-bottom: env(safe-area-inset-bottom);
	box-sizing: border-box;
}

.coupon_item {
	height: 40rpx;
	line-height: 40rpx;
	border-radius: 6rpx;
	background-color: #fff2f1;
	color: $dominant-color;
	padding: 0 16rpx;
	text-align: center;
	font-size: 22rpx;
	margin-left: 10rpx;
}

.group-hd {
	position: relative;
	padding-left: 24rpx;
	box-sizing: border-box;
	display: flex;
	justify-content: flex-start;
	align-items: center;
}

.text-box {
	padding: 2rpx 6rpx;
	background-color: #fbe9e7;
	color: #fd5342;
	margin-right: 10rpx;
	border-radius: 4rpx;
	font-size: 20rpx;
	height: 32rpx;
	line-height: 32rpx;
}

.group-hd.d-b-s {
	height: auto;
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
}

.group-hd::after {
	content: '';
	width: 8rpx;
	height: 33rpx;
	position: absolute;
	top: 30rpx;
	left: 0;
	@include background_linear('titleft1', 'titleft2', 180deg, 0%, 100%);
}

.hide.group-hd::after {
	width: 0;
}

.group-hd .line-h-90 {
	line-height: 90rpx;
}

.group-hd .line-h-50 {
	line-height: 50rpx;
}

.text-coupon-list {
	display: flex;
	justify-content: flex-start;
	align-items: center;
	flex-wrap: wrap;
}

.text-coupon-box {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 32rpx;
	border: 1rpx solid #dddddd;
	border-radius: 5rpx;
	position: relative;
	box-sizing: border-box;
	padding: 0 16rpx;
}

.text-coupon {
	color: #ff5649;
	font-size: 18rpx;
}

.text-coupon-left {
	position: absolute;
	left: -5rpx;
	width: 10rpx;
	height: 10rpx;
	border-radius: 50%;
	border-right: 1rpx solid #dddddd;
	z-index: 2;
	background: #ffffff;
}

.text-coupon-right {
	position: absolute;
	right: -5rpx;
	width: 10rpx;
	height: 10rpx;
	border-radius: 50%;
	border-left: 1rpx solid #dddddd;
	z-index: 2;
	background: #ffffff;
}

/* 预售 */
.product-detail .limited-spike {
	position: relative;
	z-index: 2;
	margin-top: -112rpx;
	padding: 0 35rpx;
	color: #ffffff;
	background: linear-gradient(140deg, #f11e0b 0%, #f77737 100%);
	border-radius: 15rpx;
	height: 278rpx;
	padding: 40rpx 21rpx;
	box-sizing: border-box;
}

.product-detail .limited-spike .left-name {
	font-size: 22rpx;
	color: #ffffff;
}

.product-detail .limited-spike .right-time {
	position: absolute;
	right: 20rpx;
	top: 58rpx;
}

.product-detail .limited-spike .right-time .jiantou {
	width: 20rpx;
	height: 20rpx;
	margin-left: 9rpx;
}

.mt-down-box {
	margin-top: -107rpx;
	z-index: 3;
	position: relative;
	border-radius: 15rpx;
}

.tips-box {
	margin-left: 8rpx;
	height: 40rpx;
	line-height: 40rpx;
	padding: 0 24rpx 0 14rpx;
	box-sizing: border-box;
	font-size: 20rpx;
	font-weight: 500;
	color: #ffffff;
	background-color: rgba($color: #ffffff, $alpha: 0.45);
	border-radius: 20rpx;
}

.product-detail .product-presale {
	padding: 26rpx;
	line-height: 40rpx;
	font-size: 26rpx;
	color: #666666;
	background-color: rgba($color: #31c19e, $alpha: 0.1);
	border-radius: 12rpx;
	word-break: break-all;
	margin-top: 28rpx;
}
</style>
