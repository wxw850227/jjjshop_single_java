<template>
	<view :data-theme='theme()' :class="theme() || ''">
		<view class="top-tabbar">
			<view :class="state_active == 0 ? 'tab-item active' : 'tab-item'" @click="stateFunc(0)">全部订单</view>
			<view :class="state_active == 1 ? 'tab-item active' : 'tab-item'" @click="stateFunc(1)">待付款</view>
			<view :class="state_active == 2 ? 'tab-item active' : 'tab-item'" @click="stateFunc(2)">待发货</view>
			<view :class="state_active == 3 ? 'tab-item active' : 'tab-item'" @click="stateFunc(3)">待收货</view>
			<view :class="state_active == 4 ? 'tab-item active' : 'tab-item'" @click="stateFunc(4)">待评价</view>
		</view>
		<!--列表-->
		<scroll-view scroll-y="true" class="scroll-Y" :style="'height:' + scrollviewHigh + 'px;'" lower-threshold="50"
			@scrolltolower="scrolltolowerFunc">
			<view :class="topRefresh ? 'top-refresh open' : 'top-refresh'">
				<view class="circle" v-for="(circle, n) in 3" :key="n"></view>
			</view>
			<view class="order-list">
				<view class="item" v-for="(item, index) in listData" :key="index">
					<view class="order-head d-b-c">
						<view>
							<text class="state-text">{{ item.orderSourceText }}</text>
							<text class="shop-name flex-1 fb">订单号：{{ item.orderNo }}</text>
						</view>
						<view class="state">
							<text class="red">{{ item.orderStatusText }}</text>
						</view>
					</view>
					<!--多个商品显示-->
					<view class="product-list pr" v-if="item.product.length > 1" @click="gotoOrder(item.orderId)">
						<scroll-view scroll-x="true">
							<view class="list d-s-c pr100">
								<view class="cover mr10" v-for="(img, num) in item.product" :key="num">
									<image :src="img.productImage" mode="aspectFit"></image>
								</view>
							</view>
						</scroll-view>
						<view class="total-count">
							<view class="left-shadow"></view>
							<view class="price f22 theme-price">
								¥
								<text class="f40 theme-price">{{ item.orderPrice }}</text>
							</view>
							<view class="count">共{{ item.totalNum }}件</view>
						</view>
					</view>
					<!--一个商品显示-->
					<view class="one-product d-s-c" v-else @click="gotoOrder(item.orderId)">
						<view class="cover" v-for="(img, num) in item.product" :key="num">
							<image :src="img.productImage" mode="aspectFit"></image>
						</view>
						<view class="pro-info flex-1">{{ item.product[0].productName }}</view>
						<view class="total-count">
							<view class="left-shadow"></view>
							<view class="price theme-price f22">
								¥
								<text class="f40 theme-price" v-if="item.orderSource==80">{{ item.payPrice }}</text>
								<text class="f40 theme-price" v-else>{{ item.payPrice }}</text>
							</view>
							<view class="count">共{{ item.totalNum }}件</view>
						</view>
					</view>
					<view class="order-bts">
						<block v-if="item.orderStatus ==10">
							<!-- 未支付取消订单 -->
							<button @click="cancelOrder(item.orderId)" class="theme-borderbtn"
								v-if="item.payStatus == 10">取消</button>
							<!-- 已支付取消订单 -->
							<block v-if="item.payStatus == 20 && item.deliveryStatus == 10">
								<button @click="cancelOrder(item.orderId)" class="theme-borderbtn">申请取消</button>
							</block>
							<!-- 订单核销码 -->
							<block
								v-if="item.payStatus == 20 && item.deliveryType == 20 && item.deliveryStatus == 10">
								<button @click="onQRCode(item.orderId)" class="theme-borderbtn">核销码</button>
							</block>
							<!-- 订单付款 -->
							<block v-if="item.payStatus == 10"><button class="theme-btn"
									@click="onPayOrder(item.orderId)">付款</button></block>
							<!-- 确认收货 -->
							<block v-if="item.deliveryStatus == 20 && item.receiptStatus == 10">
								<button class="theme-btn" @click="orderReceipt(item.orderId)">确认收货</button>
							</block>
						</block>
						<text v-if="item.orderStatus == 21" class="count">取消申请中</text>
						<!-- 订单评价 -->
						<button class="theme-btn" v-if="item.orderStatus == 30 && item.isComment == 0"
							@click="gotoEvaluate(item.orderId)">评价</button>
						<template v-if="item.assembleStatus == 10 && item.orderSource == 30">
							<button class="theme-btn"
								@click="gotoAssembleShare(item.product[0].billSourceId)">邀请好友拼单</button>
						</template>
					</view>
				</view>
				<view class="d-c-c p30" v-if="listData.length == 0 && !loading">
					<text class="iconfont icon-wushuju"></text>
					<text class="cont">亲，暂无相关记录哦</text>
				</view>
				<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
			</view>
		</scroll-view>
		<!--核销二维码-->
		<Popup :show="isCodeImg" type="middle" @hidePopup="hideCodePopupFunc">
			<view class="ww100 p20 box-s-b">
				<image class="ww100" :src="codeImg" mode="widthFix"></image>
			</view>
		</Popup>
		<tabBar></tabBar>
	</view>
</template>

<script>
	import Popup from '@/components/uni-popup.vue';
	import uniLoadMore from '@/components/uni-load-more.vue';
	import {
		pay
	} from '@/common/pay.js';
	export default {
		components: {
			Popup,
			uniLoadMore
		},
		data() {
			return {
				/*手机高度*/
				phoneHeight: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,
				/*状态选中*/
				state_active: 0,
				/*顶部刷新*/
				topRefresh: false,
				/*数据*/
				listData: [],
				/*数据类别*/
				dataType: 'all',
				/*是否显示支付类别弹窗*/
				isPayPopup: false,
				/*支付方式*/
				pay_type: 20,
				/*订单id*/
				order_id: 0,
				/*最后一页码数*/
				last_page: 0,
				/*当前页面*/
				page: 1,
				/*每页条数*/
				list_rows: 10,
				/*有没有等多*/
				no_more: false,
				/*是否正在加载*/
				loading: true,
				/*是否显示核销二维码*/
				isCodeImg: false,
				codeImg: ''
			};
		},
		computed: {
			/*加载中状态*/
			loadingType() {
				if (this.loading) {
					return 1;
				} else {
					if (this.listData.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		onLoad(e) {
			if (typeof e.dataType != 'undefined') {
				this.dataType = e.dataType;
			}
			if (this.dataType == 'payment') {
				this.state_active = 1;
			} else if (this.dataType == 'received') {
				this.state_active = 3;
			} else if (this.dataType == 'comment') {
				this.state_active = 4;
			} else if (this.dataType == 'delivery') {
				this.state_active = 2;
			}
		},
		mounted() {
			this.init();
		},
		onShow() {
			/*获取订单列表*/
			this.page = 1;
			this.listData = [];
			this.getData();
		},
		methods: {
			/*初始化*/
			init() {
				let self = this;
				uni.getSystemInfo({
					success(res) {
						self.phoneHeight = res.windowHeight;
						// 计算组件的高度
						let view = uni.createSelectorQuery().select('.top-tabbar');
						view.boundingClientRect(data => {
							let h = self.phoneHeight - data.height;
							self.scrollviewHigh = h;
						}).exec();
					}
				});
			},

			/*状态切换*/
			stateFunc(e) {
				let self = this;
				if (self.state_active != e) {
					self.page = 1;
					self.loading = true;
					self.state_active = e;
					switch (e) {
						case 0:
							self.listData = [];
							self.dataType = 'all';
							break;
						case 1:
							self.listData = [];
							self.dataType = 'payment';
							break;
						case 2:
							self.listData = [];
							self.dataType = 'delivery';
							break;
						case 3:
							self.listData = [];
							self.dataType = 'received';
							break;
						case 4:
							self.listData = [];
							self.dataType = 'comment';
							break;
					}
					self.getData();
				}
			},

			/*可滚动视图区域到底触发*/
			scrolltolowerFunc() {
				let self = this;
				if (self.no_more) {
					return;
				}
				self.page++;
				if (self.page <= self.last_page) {
					self.getData();
				} else {
					self.no_more = true;
				}
			},

			/*获取数据*/
			getData() {
				let self = this;
				self.loading = true;
				let dataType = self.dataType;
				self._postBody(
					'user/order/lists', {
						type: dataType,
						pageIndex: self.page,
						pageSize: self.list_rows
					},
					function(res) {
						self.loading = false;
						self.listData = self.listData.concat(res.data.records);
						self.last_page = res.data.lastPage;
						if (res.data.lastPage <= 1) {
							self.no_more = true;
						} else {
							self.no_more = false;
						}
					}
				);
			},
			/*计算时分秒*/
			countDown(t) {
				let time = t - (Date.now() / 1000);
				//天数
				let day = Math.floor(time / (60 * 60 * 24));
				//取模（余数）
				let modulo = time % (60 * 60 * 24);
				//小时数
				let hour = Math.floor(modulo / (60 * 60));
				modulo = modulo % (60 * 60);
				//分钟
				let minute = Math.floor(modulo / 60);
				//秒
				let second = modulo % 60;
				day = this.convertTwo(day);
				hour = this.convertTwo(hour);
				minute = this.convertTwo(minute);
				second = this.convertTwo(second);
				let text = parseInt(day) > 0 ? (parseInt(day) + '天 ') : '' + parseInt(hour) + '时' + parseInt(minute) +
					'分' + parseInt(second) + '秒';
				return text
			},

			/*跳转页面*/
			gotoOrder(e) {
				this.gotoPage('/pages/order/order-detail?orderId=' + e);
			},

			/*隐藏支付方式*/
			hidePopupFunc() {
				this.isPayPopup = false;
			},

			/*去支付*/
			payTypeFunc(payType) {
				let self = this;
				self.isPayPopup = false;
				uni.showLoading({
					title: '加载中'
				});
				self._post(
					'user.order/pay', {
						payType: payType,
						order_id: self.order_id,
						pay_source: self.getPlatform()
					},
					function(res) {
						pay(res, self);
					}
				);
			},
			depositPay(e) {
				this.gotoPage('pages/order/saveOrder?order_type=retainage&order_id=' + e)
			},
			/*支付方式选择*/
			onPayOrder(orderId) {
				let self = this;
				self.gotoPage('/pages/order/cashier?orderId=' + orderId)
			},

			/*确认收货*/
			orderReceipt(orderId) {
				let self = this;
				wx.showModal({
					title: '提示',
					content: '您确定要收货吗?',
					success: function(o) {
						if (o.confirm) {
							uni.showLoading({
								title: '正在处理'
							});
							self._post(
								'user/order/receipt', {
									orderId: orderId
								},
								function(res) {
									uni.hideLoading();
									uni.showToast({
										title: res.msg,
										duration: 2000,
										icon: 'success'
									});
									self.listData = [];
									self.getData();
								}
							);
						} else {
							uni.showToast({
								title: '取消收货',
								duration: 1000,
								icon: 'none'
							});
						}
					}
				});
			},
			cancelAdvance(e) {
				let self = this;
				let order_id = e;
				uni.showModal({
					title: '提示',
					content: '您确定要取消吗?',
					success: function(o) {
						if (o.confirm) {
							uni.showLoading({
								title: '正在处理'
							});
							self._get(
								'plus.advance.Order/cancelFront', {
									order_id: order_id
								},
								function(res) {
									uni.hideLoading();
									uni.showToast({
										title: '操作成功',
										duration: 2000,
										icon: 'success'
									});
									self.listData = [];
									self.getData();
								}
							);
						}
					}
				});
			},
			/*取消订单*/
			cancelOrder(e) {
				let self = this;
				uni.showModal({
					title: '提示',
					content: '您确定要取消吗?',
					success: function(o) {
						if (o.confirm) {
							uni.showLoading({
								title: '正在处理'
							});
							self._post(
								'user/order/cancel', {
									orderId: e
								},
								function(res) {
									uni.hideLoading();
									uni.showToast({
										title: '操作成功',
										duration: 2000,
										icon: 'success'
									});
									self.listData = [];
									self.getData();
								}
							);
						}
					}
				});
			},
			nowOverTime(t) {

				let now = new Date().getTime();
				let time = new Date(t * 1000).getTime();
				return now >= time

			},
			/*去评论*/
			gotoEvaluate(e) {
				this.gotoPage('/pages/order/evaluate/evaluate?order_id=' + e);
			},

			/*核销码*/
			onQRCode(e) {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});
				self._get(
					'user/order/qrcode', {
						orderId: e,
						source: self.getPlatform()
					},
					function(res) {
						uni.hideLoading();
						self.isCodeImg = true;
						self.codeImg = res.data;
					}
				);
			},
			/*关闭核销二维码*/
			hideCodePopupFunc() {
				this.isCodeImg = false;
			},

			/*分享拼团*/
			gotoAssembleShare(e) {
				let url = '/pages/plus/assemble/fight-group-detail/fight-group-detail?assembleBillId=' + e;
				this.gotoPage(url);
			}
		}
	};
</script>

<style lang="scss">
	@import url("css/myorder.css");
</style>
