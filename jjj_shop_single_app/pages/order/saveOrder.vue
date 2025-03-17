<template>
	<view class="wrap" :data-theme='theme()' :class="theme() || ''" v-if="!loading">
		<!--tab-->
		<view class="top-tabbar" v-if="OrderData.delivery != 30">
			<block v-if="hasType(10)">
				<view :class="tab_type == 0 ? 'tab-item active' : 'tab-item'" @click="tabFunc(0)">快递配送</view>
			</block>
			<block v-if="hasType(20)">
				<view :class="tab_type == 1 ? 'tab-item active' : 'tab-item'" @click="tabFunc(1)">上门自提</view>
			</block>
		</view>
		<view class="p-0-23">
			<Myinfo :dis="options.orderType == 'retainage'" v-if="tab_type == 0 && OrderData.delivery != 30"
				:Address="Address" :existAddress="existAddress">
			</Myinfo>

			<Storeinfo v-if="tab_type == 1 && OrderData.delivery != 30" ref="getShopinfoData" :extractStore="extractStore"
				:lastExtract="lastExtract"></Storeinfo>

			<!--购买的产品-->
			<view class="vender br6">
				<view class="list">
					<view class="d-b-c f24 fb mb30 top-title">
						<view>商品信息</view>
						<view>共{{ orderTotalNum }}件</view>
					</view>
					<view class="item" v-for="(item, index) in ProductData" :key="index">
						<view class="d-f">
							<view class="cover">
								<image :src="item.productImage" mode="aspectFit"></image>
							</view>
							<view class="info">
								<view class="d-b-s">
									<view class="flex-1">
										<view class="title f32 gray3">{{ item.productName }}</view>
										<view class="theme-price mt10 f18">
											¥<text class="f26">{{ item.productPrice }}</text>
										</view>
										<view class="describe mt10 f24">{{ item.productSku.productAttr }}</view>
									</view>
									<view>
										<view class=" count_choose">
											<view class="num-wrap">
												<view class="f22 tr">×{{ item.totalNum }}</view>
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
						<view class="mt10 tr f28" v-if="item.isUserGrade == true">
							<text class="f26">会员折扣价：</text>
							<text class="theme-price f32">￥{{ item.gradeProductPrice }}</text>
						</view>
						<view class="mt10 tr f28" v-if="item.productReduceMoney > 0">
							<text class="f26">立减：</text>
							<text class="theme-price f32">￥{{ item.productReduceMoney }}</text>
						</view>
					</view>
				</view>
			</view>

			<!--其它费用-->
			<view class="buy-checkout br6">
				<template v-if="true">
					<view class="item">
						<text class="key f26">商品金额：</text>
						<view class="f24">￥{{ OrderData.orderTotalPrice }}</view>
					</view>
					<view class="item">
						<text class="key">配送费用：</text>
						<view>
							<text class="f24">￥{{ OrderData.expressPrice }}</text>
						</view>
					</view>
					<view class="item" v-if="settledRule.isCoupon">
						<text class="key">优惠券：</text>
						<block v-if="couponList.length > 0">
							<view class="f24 theme-price" v-if="OrderData.couponId > 0"
								@click="onTogglePopupCoupon(couponList)">-￥{{ OrderData.couponMoney }}</view>
							<view v-else class="hascoupon" @click="onTogglePopupCoupon(couponList)">
								{{ couponList.length }}张可用<text class="icon iconfont icon-jiantou"></text></view>
						</block>
						<text v-else class="f24 gray9">无优惠券可用</text>
					</view>
					<view class="item" v-if="OrderData.product_reduce_money > 0">
						<text class="key">商品立减：</text>
						<view>
							<text class="theme-price f24">-￥{{ OrderData.product_reduce_money }}</text>
						</view>
					</view>
					<view class="item" v-if="OrderData.reduce">
						<text class="key">满减({{ OrderData.reduce.activeName }})：</text>
						<view>
							<text class="theme-price f24">-￥{{ OrderData.reduce.reducedPrice }}</text>
						</view>
					</view>
					<view class="item" v-if="OrderData.reduce_money">
						<text class="key">尾款抵扣：</text>
						<view>
							<text class="theme-price f24">-￥{{ OrderData.reduce_money }}</text>
						</view>
					</view>
					<view class="item" v-if="OrderData.isAllowPoints && OrderData.pointsMoney > 0">
						<text class="key">可用积分抵扣：</text>
						<view class="">
							<text class="price">-￥{{ OrderData.pointsMoney }}</text>
							<switch :checked="is_use_points" style="transform: scale(0.7); margin-right: -10rpx;"
								@change="onShowPoints" />
						</view>
					</view>
				</template>

				<view class="item">
					<view class="border-t-d9 d-e-c p-30-0 ww100">
						<text class="key f24">小计：</text>
						<view class="f38 fb"><text class="f24">￥</text>{{ toDecimal2(OrderData.orderPayPrice) }}</view>
					</view>
				</view>
			</view>
			<!--买家留言-->
			<view class="buyer-message uni-textarea"><textarea class="textarea" v-model="remark"
					placeholder-style="color:#cccccc;" placeholder="选项:买家留言" /></view>
		</view>


		<!--底部支付-->
		<view class="foot-pay-btns">
			<template v-if="options.orderType == 'deposit'">
				<view>
					应付
					<text class="fb theme-price">¥</text>
					<text class="num theme-price fb f38">{{ OrderData.order_total_front_price }}</text>
				</view>
			</template>
			<template v-else>
				<view v-if="!settledRule.forcePoints">
					应付
					<text class="fb theme-price">¥</text>
					<text class="num theme-price fb f38">{{ OrderData.orderPayPrice }}</text>
				</view>
				<view class="price" v-if="settledRule.forcePoints">
					<text class="gray9">所需积分</text>
					<text class="num theme-price fb">{{ OrderData.pointsNum }}</text>
					<template v-if="OrderData.orderPayPrice > 0">
						<text class="theme-price">+</text>
						<text class="theme-price">¥</text>
						<text class="num fb theme-price">{{ OrderData.orderPayPrice }}</text>
					</template>
				</view>
			</template>

			<button @click="SubmitOrder">提交订单</button>
		</view>

		<!--优惠券-->
		<Coupon :isCoupon="isCoupon" :couponList="couponList" @close="closeCouponFunc"></Coupon>
	</view>
</template>

<script>
import Myinfo from './confirm-order/my-info.vue';
import Storeinfo from './confirm-order/store-info.vue';
import Coupon from './confirm-order/coupon.vue';
import {
	pay
} from '@/common/pay.js';
export default {
	components: {
		Myinfo,
		Storeinfo,
		Coupon
	},
	data() {
		return {
			/*是否加载完成*/
			loading: true,
			options: {},
			indicatorDots: true,
			autoplay: true,
			interval: 2000,
			duration: 500,
			tab_type: 0,
			/*商品id*/
			product_id: '',
			/*商品数量*/
			product_num: '',
			/*商品数据*/
			ProductData: [],
			/*订单数据数据*/
			OrderData: [],
			/*结算规则*/
			settledRule: {},
			// 是否存在收货地址
			existAddress: false,
			/*默认地址*/
			Address: {
				region: []
			},
			extractStore: [],
			lastExtract: {},
			product_sku_id: 0,
			/*配送方式*/
			delivery: 0,
			/*自提店id*/
			storeId: 0,
			/*优惠券id*/
			coupon_id: 0,
			/*是否使用积分*/
			is_use_points: 1,
			linkman: '',
			phone: '',
			remark: '',
			/*支付方式*/
			pay_type: 20,
			/*是否显示优惠券*/
			isCoupon: false,
			couponId: 0,
			/*优惠券列表*/
			couponList: [],
			deliverySetting: [],
			/*优惠券数量*/
			coupon_num: 0,
			/*消息模板*/
			temlIds: [],
			// 支付相关
			isCheckout: false,
			order_id: 0,
			order_type: '',
			orderTotalNum: 0, //商品数量
		};
	},
	onLoad(options) {
		let self = this;
		self.options = options;
		self.$fire.on('selectStoreId', function (store) {
			self.storeId = store.storeId;
			self.extractStore = store;
		});
		self.$fire.on('extract', function (params) {
			self.lastExtract = params;
		});
	},
	onShow() {
		this.getData();
	},
	methods: {
		/**/
		hasType(e) {
			if (this.deliverySetting.indexOf(e) != -1) {
				return true;
			} else {
				return false;
			}
		},
		/*是否使用积分选择*/
		onShowPoints: function (e) {
			let self = this;
			if (e.detail.value == true) {
				self.is_use_points = 1;
			} else {
				self.is_use_points = 0;
			}
			self.getData();
		},
		/*选择优惠卷*/
		onTogglePopupCoupon(e) {
			let self = this;
			self.isCoupon = true;
			self.couponList = e;
		},
		/*关闭优惠券*/
		closeCouponFunc(e) {
			this.isCoupon = false;
			if (e == null) {
				return;
			}
			let self = this;
			self.couponId = e;
			self.getData();
		},
		/*获取数据*/
		getData() {
			let self = this;
			uni.showLoading({
				title: '加载中'
			});

			let callback = function (res) {
				self.orderTotalNum = res.data.orderTotalNum;
				self.OrderData = res.data.orderData;
				self.temlIds = res.data.templateArr;
				self.existAddress = self.OrderData.existAddress;
				self.Address = self.OrderData.address;
				self.extractStore = self.OrderData.extractStore;
				if (self.extractStore == null) {
					self.extractStore = {};
				}
				self.lastExtract = self.OrderData.lastExtract;
				self.settledRule = res.data.settledRule;
				self.ProductData = res.data.productList;
				if (res.data.couponList) {
					self.couponList = res.data.couponList;
				}
				self.delivery = self.OrderData.delivery;
				if (self.OrderData.delivery == 20) {
					self.tab_type = 1;
				}
				self.deliverySetting = self.OrderData.deliverySetting;
				if (self.OrderData.order_pay_price == 0) {
					self.pay_type = 10;
				}
				self.loading = false;
			};

			// 请求的参数
			let params = {
				delivery: self.delivery,
				storeId: self.storeId,
				couponId: self.couponId,
				isUsePoints: self.is_use_points,
				paySource: self.getPlatform(),
			};

			//直接购买
			if (self.options.orderType === 'buy') {
				self._postBody(
					'order/order/toBuy',
					Object.assign({}, params, {
						productId: self.options.productId,
						productNum: self.options.productNum,
						specSkuId: self.options.specSkuId
					}),
					function (res) {
						callback(res);
					}, err => {
						uni.navigateBack()
					}
				);
			}
			//定金
			if (self.options.orderType === 'deposit') {
				self._get(
					'plus.advance.Order/frontBuy',
					Object.assign({}, params, {
						product_id: self.options.product_id,
						product_num: self.options.product_num,
						product_sku_id: self.options.product_sku_id,
						advance_product_sku_id: self.options.advance_product_sku_id,
						advance_product_id: self.options.advance_product_id
					}),
					function (res) {
						callback(res);
					}
				);
			}
			//尾款
			if (self.options.orderType === 'retainage') {
				self._get(
					'plus.advance.Order/buy',
					Object.assign({}, params, {
						order_id: self.options.order_id,
					}),
					function (res) {
						callback(res);
					}
				);
			}
			// 购物车结算
			else if (self.options.orderType === 'cart') {
				self._postBody(
					'order/order/toCart',
					Object.assign({}, params, {
						cartIds: self.options.cartIds || 0
					}),
					function (res) {
						callback(res);
					}
				);
			}
			// 积分兑换结算
			else if (self.options.orderType == 'points') {
				self._postBody(
					'plus/points/order/toBuy',
					Object.assign({}, params, {
						pointProductId: self.options.pointProductId,
						pointProductSkuId: self.options.pointProductSkuId,
						productSkuId: self.options.productSkuId,
						productNum: self.options.productNum
					}),
					function (res) {
						callback(res);
					}, err => {
						uni.navigateBack()
					}
				);
			}
			// 限时秒杀
			else if (self.options.orderType === 'seckill') {
				params.num = self.options.num;
				self._postBody(
					'plus/seckill/order/toBuy',
					Object.assign({}, params, {
						seckillProductId: self.options.seckillProductId,
						productSkuId: self.options.productSkuId,
						seckillProductSkuId: self.options.seckillProductSkuId,
						productNum: self.options.productNum
					}),
					function (res) {
						callback(res);
					}, err => {
						uni.navigateBack()
					}
				);
			}
			//砍价
			else if (self.options.orderType === 'bargain') {
				self._postBody(
					'plus/bargain/order/toBuy',
					Object.assign({}, params, {
						bargainProductId: self.options.bargainProductId,
						productSkuId: self.options.productSkuId,
						bargainProductSkuId: self.options.bargainProductSkuId,
						bargainTaskId: self.options.bargainTaskId
					}),
					function (res) {
						callback(res);
					}, err => {
						uni.navigateBack()
					}
				);
			}
			//拼团
			else if (self.options.orderType === 'assemble') {
				self._postBody(
					'plus/assemble/order/toBuy',
					Object.assign({}, params, {
						assembleProductId: self.options.assembleProductId,
						productSkuId: self.options.productSkuId,
						assembleProductSkuId: self.options.assembleProductSkuId,
						productNum: self.options.productNum,
						assembleBillId: self.options.assembleBillId,
					}),
					function (res) {
						callback(res);
					}, err => {
						uni.navigateBack()
					}
				);
			}
		},

		/*选择配送方式*/
		tabFunc(e) {
			if (this.options.orderType == 'retainage') {
				return
			}
			this.tab_type = e;
			if (e == 0) {
				this.delivery = 10;
			} else {
				this.delivery = 20;
			}

			this.getData();
		},

		/*提交订单*/
		SubmitOrder() {
			let self = this;
			uni.showLoading({
				title: '加载中',
				mask: true
			});
			let params = {
				delivery: self.delivery,
				storeId: self.storeId || 0,
				couponId: self.couponId,
				isUsePoints: self.is_use_points,
				phone: self.lastExtract.phone,
				linkman: self.lastExtract.linkman,
				remark: self.remark
			};

			// 创建订单-直接下单
			let url = '';
			if (self.options.orderType === 'buy') {
				url = 'order/order/buy';
				params = Object.assign(params, {
					productId: self.options.productId,
					productNum: self.options.productNum,
					specSkuId: self.options.specSkuId
				});
			}
			//定金
			if (self.options.orderType === 'deposit') {
				url = 'plus.advance.Order/frontBuy';
				params = Object.assign(params, {
					product_id: self.options.product_id,
					product_num: self.options.product_num,
					product_sku_id: self.options.product_sku_id,
					advance_product_sku_id: self.options.advance_product_sku_id,
					advance_product_id: self.options.advance_product_id
				});
			}
			//尾款
			if (self.options.orderType === 'retainage') {
				url = 'plus.advance.Order/buy'
				params = Object.assign(params, {
					order_id: self.options.order_id
				});
			}
			// 创建订单-购物车结算
			if (self.options.orderType === 'cart') {
				url = 'order/order/cart';
				params = Object.assign(params, {
					cartIds: self.options.cartIds || 0
				});
			}

			// 创建订单-积分兑换
			if (self.options.orderType === 'points') {
				url = 'plus/points/order/buy';
				params = Object.assign(params, {
					pointProductId: self.options.pointProductId,
					pointProductSkuId: self.options.pointProductSkuId,
					productSkuId: self.options.productSkuId,
					productNum: self.options.productNum
				});
			}
			// 创建订单-限时秒杀
			if (self.options.orderType === 'seckill') {
				url = 'plus/seckill/order/buy';
				params.num = self.options.num;
				params = Object.assign(params, {
					seckillProductId: self.options.seckillProductId,
					productSkuId: self.options.productSkuId,
					seckillProductSkuId: self.options.seckillProductSkuId,
					productNum: self.options.productNum
				});
			}
			// 创建订单-砍价
			if (self.options.orderType === 'bargain') {
				url = 'plus/bargain/order/buy';
				params = Object.assign(params, {
					bargainProductId: self.options.bargainProductId,
					productSkuId: self.options.productSkuId,
					bargainProductSkuId: self.options.bargainProductSkuId,
					bargainTaskId: self.options.bargainTaskId
				});
			}

			// 创建订单-限时拼团
			if (self.options.orderType === 'assemble') {
				url = 'plus/assemble/order/buy';
				params = Object.assign(params, {
					assembleProductId: self.options.assembleProductId,
					productSkuId: self.options.productSkuId,
					assembleProductSkuId: self.options.assembleProductSkuId,
					assembleBillId: self.options.assembleBillId,
					productNum: self.options.productNum,
				});
			}
			let callback = function () {
				self._postBody(url, params, function (res) {
					let pages = '/pages/order/cashier?orderId=' + res.data;
					if (self.options.orderType == 'deposit') {
						pages = '/pages/order/cashier?orderType=40&orderId=' + res.data;
					}
					self.gotoPage(pages, 'reLaunch');
				});
			};
			self.subMessage(self.temlIds, callback);
		},
		toDecimal2(x) {
			var f = parseFloat(x);
			if (isNaN(f)) {
				return "0.00";
			}
			var f = Math.round(x * 100)
			var n = Math.round(x * 1000)
			var r = n.toString().split("");
			r = r[r.length - 1];
			if (r >= 5) {
				f = (f - 1) / 100
			} else {
				f = f / 100
			}
			var s = f.toString();
			var rs = s.indexOf('.');
			if (rs < 0) {
				rs = s.length;
				s += '.';
			}
			while (s.length <= rs + 2) {
				s += '0';
			}
			return s;
		},
		closeCheckoutFunc() {
			this.isCheckout = false;
		},
	}
};
</script>

<style lang="scss" scoped>
page {
	background-color: #F2F2F2;
}

.f-d-c {
	flex-direction: column;
}

.wrap {
	padding-bottom: 110rpx;
}

.d-f-c {
	display: flex;
	justify-content: space-between;
	align-items: center;
	overflow: hidden;
	padding: 30rpx;
	border-bottom: 1rpx solid #D9D9D9;
}

.dfjac {
	display: flex;
	align-items: center;
}

.d-txar {
	white-space: nowrap;
	width: 200px;
	margin-right: 34rpx;
}

.extp {
	color: #9e9e9e;
	margin-left: 34rpx;
}

.vender .list .item {
	border-bottom: none;
}

.vender .top-title {
	padding: 30rpx 25rpx;
}

.icon-jiantou {
	margin-left: 15rpx;
}

.icon-dianpu1 {
	margin-right: 15rpx;
	font-size: 28rpx;
	color: #333333;
}

.sup_itemtit {
	padding: 40rpx 30rpx;
	display: flex;
	align-items: center;
}

.vender .total-box {
	height: 87rpx;
	line-height: 87rpx;
	border-bottom: 16rpx solid #f2f2f2;
}

.d-f {
	display: flex;
}

.zt {
	padding: 2rpx 10rpx;
	margin-right: 10rpx;
	background: #E2231A;
	color: #FFFFFF;
	border-radius: 8rpx;
	font-size: 22rpx;
}

.icon-box .icon-zhifubao {
	color: #1296db;
	font-size: 50rpx;
}

.icon-dianpu1 {}

.text_area {
	width: 100%;
	height: 120rpx;
	background: #f2f2f2;
	border-radius: 6rpx;
	padding: 20rpx;
	box-sizing: border-box;
	font-size: 24rpx;
}

.icon-xuanze {
	font-size: 38rpx;
}

.tab-item {
	font-size: 28rpx;
	color: #333333;
	font-weight: 400;
}

.tab-item.active {
	font-size: 28rpx;
	color: #333333;
	font-weight: 400;
}

.tab-item.active::after {
	width: 115rpx;
	height: 6rpx;
}

.hascoupon {
	padding: 0 18rpx;
	box-sizing: border-box;
	height: 38rpx;
	line-height: 38rpx;
	border-radius: 200rpx;
	color: #FFFFFF;
	background: linear-gradient(180deg, #FC4133, #FF7A04);
	font-size: 20rpx;

	.icon.icon-jiantou {
		color: #FFFFFF;
		font-size: 20rpx;
		margin-left: 2rpx;
	}
}

.buy-checkout {
	.item {
		padding: 16rpx 30rpx;
		border-bottom: none;
	}
}
</style>
