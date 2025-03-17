<template>
	<view :data-theme="theme()" :class="'order-datail pb100' + theme()" v-if="!loadding">
		<!--详情状态-->
		<view class="order-state d-s-c">
			<view class="icon-box">
				<span v-if="detail.stateText == '已付款,待发货'" class="icon iconfont icon-icon"></span>
				<span v-if="detail.stateText == '待付款'" class="icon iconfont icon-icon"></span>
				<span v-if="detail.stateText == '已发货,待收货'" class="icon iconfont icon-daishouhuo"></span>
				<span v-if="detail.stateText == '已完成'" class="icon iconfont icon-xuanze"></span>
				<span v-if="detail.stateText == '已取消'" class="icon iconfont icon-gantanhao"></span>
			</view>
			<view class="state-cont flex-1">
				<view class="state-txt d-b-c">
					<text class="desc f34">{{ detail.stateText }}</text>
				</view>
				<view class="status-price pt10" v-if="detail.payStatus == 10">应付金额：¥ {{ detail.payPrice }}</view>
				<view class="countdown-datetime" v-if="detail.payEndTimeText">
					<text>剩{{ detail.payEndTimeText }}自动关闭</text>
				</view>
			</view>
			<view class="dot-bg"></view>
		</view>

		<!--物流地址-->
		<view class="order-express p30 d-s-c" v-if="detail.deliveryType == 10">
			<view class="icon-box"><image style="width: 42rpx;height: 42rpx;" src="../../static/icon/address_icon.png" mode=""></image></view>
			<view class="cont-text flex-1 o-h ml20 f30">
				<view class="express-text f32">
					{{ detail.address.name }}
					<text class="f26 gray9">{{ detail.address.phone }}</text>
				</view>
				<view class="gray3 f26 pt10">
					{{ detail.address.region.province }}{{ detail.address.region.city }}{{ detail.address.region.region }}{{ detail.address.detail }}
				</view>
			</view>
			<view class="icon iconfont icon-jiantou"></view>
		</view>
		<!-- 上门自提：自提门店 -->
		<view class="order-express p30 d-s-s" v-if="detail.deliveryType == 20">
			<view class="flow-delivery__title m-top20"><span class="icon iconfont icon-dizhi1">自提门店</span></view>
			<view class="cont-text flex-1 o-h ml20 f30">
				<view class="express-text">
					{{ extractStore.storeName }} {{ extractStore.phone }}
					<view class="f24 gray9 pt10">{{ extractStore.province }} {{ extractStore.city }} {{ extractStore.region }} {{ extractStore.address }}</view>
				</view>
			</view>
		</view>

		<!-- 物流信息 -->
		<view class="group bg-white" v-if="detail.deliveryType == 10 && detail.deliveryStatus == 20" @click="gotoExpress(detail.orderId)">
			<view class="d-b-c">
				<view class="f28">
					<view class="p-20-0">
						<text class="gray9">物流公司：</text>
						<text>{{ detail.express.expressName }}</text>
					</view>
					<view class="p-20-0">
						<text class="gray9">物流单号：</text>
						<text>{{ detail.expressNo }}</text>
					</view>
				</view>
				<view><text class="icon iconfont icon-jiantou"></text></view>
			</view>
		</view>

		<!--购物列表-->
		<view class="shops group bg-white">
			<view class="list">
				<view class="one-product p-20-0" v-for="(item, index) in detail.product" :key="index">
					<view class="d-s-c">
						<view class="cover"><image :src="item.productImage" mode="aspectFit"></image></view>
						<view class="flex-1">
							<view class="p-0-30 text-ellipsis-2 gray3 f30">{{ item.productName }}</view>
							<view class="pt10 p-0-30 gray6 f24">{{ item.productAttr }}</view>
							<view class="pt10 p-0-30 d-b-c">
								<template v-if="item.isUserGrade != 1">
									<view class="price f22">
										¥
										<text class="f40">{{ item.productPrice }}</text>
									</view>
								</template>
								<template v-else>
									<view class="text-l-t f22">
										¥
										<text class="f40">{{ item.productPrice }}</text>
									</view>
								</template>
								<view class="f24 gray9">x{{ item.totalNum }}</view>
							</view>
							<view class="mt10 tr f28" v-if="item.isUserGrade == 1">
								<text class="red">会员折扣价：</text>
								<text class="red">{{ item.gradeProductPrice }}</text>
							</view>
						</view>
					</view>
					<view v-if="item.tableId > 0 && item.tableRecordId > 0" class="supplement-box">
						<view class="p20" v-for="(table_item, table_index) in item.tableData" :key="table_index">
							<view class="d-s-s" v-if="table_item.type=='image'">
								<text class="gray6 mr15">{{table_item.name}}:</text>
								<image style="width: 80rpx;height: 80rpx;" @click="yulan(table_item.value,0)"
									:src="table_item.value" mode="aspectFill"></image>
							</view>
							<template v-else>
								<text class="gray6 mr15" >{{table_item.name}}:</text><text>{{table_item.value}}</text>
							</template>
							
						</view>
					</view>
					<view class="pt10 d-e-c">
						<!-- 申请售后 -->
						<view class="m-top20 dis-flex flex-x-end">
							<text v-if="item.refund" class="red">{{item.refundState}}</text>
							<view v-else-if="detail.isAllowRefund" @click="onApplyRefund(item.orderProductId)"><button type="default">申请售后</button></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!--订单信息-->
		<view class="group bg-white f26">
			<view class="p-20-0">
				<text class="">订单编号：</text>
				<text>{{ detail.orderNo }}</text>
			</view>
			<view class="p-20-0">
				<text class="">下单时间：</text>
				<text>{{ detail.createTime }}</text>
			</view>
			<view class="p-20-0">
				<text class="">支付方式：</text>
				<text>{{ detail.payTypeText }}</text>
			</view>
			<view class="p-20-0">
				<text class="">配送方式：</text>
				<text>{{ detail.deliveryTypeText }}</text>
			</view>
			<view class="p-20-0" v-if="detail.deliveryType == 30 && detail.orderStatus == 30 && detail.virtualContent != ''">
				<text class="">发货信息：</text>
				<text>{{ detail.virtualContent }}</text>
			</view>
			<view class="p-20-0">
				<text class="">备注：</text>
				<text>{{ detail.buyerRemark }}</text>
			</view>
			<view class="p-20-0" v-if="detail.orderStatus == 20 && detail.cancelRemark != ''">
				<text class="">商家备注：</text>
				<text>{{ detail.cancelRemark }}</text>
			</view>
		</view>

		<view class="group bg-white f26">
			<view class="p-20-0 d-b-c" v-if="detail.orderSource == 20">
				<text class="">扣除积分数：</text>
				<text>-{{ detail.pointsNum }}</text>
			</view>
			<view class="p-20-0 d-b-c">
				<text class="gray9">订单总额</text>
				<text>¥ {{ detail.totalPrice }}</text>
			</view>
			<view class="p-20-0 d-b-c" v-if="detail.updatePrice != '0.00'">
				<text class="gray9">订单差价</text>
				<text>¥ {{ detail.updatePrice }}</text>
			</view>
			<view class="p-20-0 d-b-c">
				<text class="">运费</text>
				<text>¥ {{ detail.expressPrice }}</text>
			</view>
			<view class="p-20-0 d-b-c" v-if="detail.pointsMoney > 0">
				<text class="">积分抵扣</text>
				<text>-¥ {{ detail.pointsMoney }}</text>
			</view>
			<view class="p-20-0 d-b-c" v-if="detail.couponMoney > 0">
				<text class="">优惠券</text>
				<text>-¥ {{ detail.couponMoney }}</text>
			</view>
			<view class="p-20-0 d-e-c fb f34">
				实付金额：
				<text class="red" v-if="detail.orderSource == 80">¥{{ (detail.payPrice * 1 + detail.advance.payPrice * 1).toFixed(2) }}</text>
				<text class="red" v-else>¥ {{ detail.payPrice }}</text>
			</view>
		</view>
		<template v-if="detail.orderSource != 80">
			<view v-if="detail.orderSource != 20 && detail.orderSource != 30" class="foot-btns">
				<!-- 取消订单 -->
				<button class="theme-borderbtn" v-if="detail.payStatus == 10 && detail.orderStatus!=20" @click="cancelOrder(detail.orderId)">取消订单</button>

				<block v-if="detail.orderStatus != 21">
					<block v-if="detail.payStatus == 20 && detail.deliveryStatus == 10">
						<button @click="cancelOrder(detail.orderId)" class="theme-borderbtn">申请取消</button>
					</block>
				</block>
				<text v-else class="count f28 gray9">取消申请中</text>
				<block v-if="detail.payStatus == 10 && detail.orderStatus!=20">
					<!-- 订单付款 -->
					<button @click="onPayOrder(detail.orderId)" v-if="detail.payStatus == 10" class="ml10 theme-btn">去支付</button>
				</block>
				<!-- 确认收货 -->
				<block v-if="detail.deliveryStatus == 20 && detail.receiptStatus == 10"><button class="theme-btn" @click="orderReceipt(detail.orderId)">确认收货</button></block>
			</view>
		</template>
		<!-- 操作栏 -->
	</view>
</template>

<script>
import Popup from '@/components/uni-popup.vue';
import { pay } from '@/common/pay.js';
export default {
	data() {
		return {
			/*是否加载完成*/
			loadding: true,
			indicatorDots: true,
			autoplay: true,
			interval: 2000,
			duration: 500,
			/*是否显示支付类别弹窗*/
			isPayPopup: false,
			/*订单id*/
			orderId: 0,
			/*订单详情*/
			detail: {
				orderStatus: [],
				address: {
					region: []
				},
				product: [],
				payType: 0,
				deliveryType: 0,
				payStatus: 0
			},
			extractStore: {},
			/*是否显示拼团*/
			is_fightgroup: false
		};
	},
	components: {
		Popup
	},
	onLoad(e) {
		this.orderId = e.orderId;
	},
	mounted() {
		uni.showLoading({
			title: '加载中'
		});
	},
	onShow() {
		/*获取订单详情*/
		this.getData();
	},
	methods: {
		/*获取数据*/
		getData() {
			let self = this;
			let orderId = self.orderId;
			self._get(
				'user/order/detail',
				{
					orderId: orderId
				},
				function(res) {
					self.detail = res.data;
					self.extractStore = res.data.extractStore;
					self.loadding = false;
					uni.hideLoading();
				}
			);
		},
		/*显示支付方式*/
		hidePopupFunc() {
			this.isPayPopup = false;
		},

		/*取消订单*/
		cancelOrder(e) {
			let self = this;
			let order_id = e;
			wx.showModal({
				title: '提示',
				content: '您确定要取消当前订单吗?',
				success: function(o) {
					if (o.confirm) {
						uni.showLoading({
							title: '正在处理'
						});
						self._post(
							'user/order/cancel',
							{
								orderId: order_id
							},
							function(res) {
								uni.hideLoading();
								uni.showToast({
									title: '操作成功',
									duration: 2000,
									icon: 'success'
								});
								self.getData();
							}
						);
					}
				}
			});
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
							'user/order/receipt',
							{
								orderId: orderId
							},
							function(res) {
								uni.hideLoading();
								uni.showToast({
									title: res.msg,
									duration: 2000,
									icon: 'success'
								});
								self.getData();
							}
						);
					}
				}
			});
		},
		/*查看物流*/
		gotoExpress(orderId) {
			this.gotoPage('/pages/order/express/express?order_id=' + orderId);
		},
		/*申请售后*/
		onApplyRefund(e) {
			this.gotoPage('/pages/order/refund/apply/apply?orderProductId=' + e);
		},
		/*去支付*/
		payTypeFunc(payType) {
			let self = this;
			let order_id = self.order_id;
			self.isPayPopup = false;
			uni.showLoading({
				title: '加载中'
			});
			self._post(
				'user.order/pay',
				{
					payType: payType,
					order_id: order_id,
					pay_source: self.getPlatform()
				},
				function(res) {
					uni.hideLoading();
					pay(res, self);
				}
			);
		},

		/*支付方式选择*/
		onPayOrder(orderId) {
			let self = this;
			self.gotoPage('/pages/order/cashier?orderId=' + orderId);
		}
	}
};
</script>

<style scoped lang="scss">
page {
}

.order-express {
	background: #ffffff;
	margin: 0 20rpx;
	border-radius: 12rpx;
	margin-top: 20rpx;
}

.order-express .icon-box .iconfont {
	font-size: 50rpx;
}

.order-datail {
	padding-bottom: 90rpx;
	background-color: #f2f2f2;
}

.order-datail .fight-users {
	margin: 0 auto;
}

.order-datail .fight-users .user-box {
	width: 80rpx;
	height: 80rpx;
	margin: 10rpx;
	border-radius: 50%;
	border: 1px dashed #cccccc;
}

.order-datail .fight-users {
	padding: 30rpx;
}

.order-datail .fight-users .user-box image {
	width: 80rpx;
	height: 80rpx;
	border-radius: 50%;
}

.order-datail .fight-users .user-box .leader {
	position: absolute;
	top: -20rpx;
	left: 50%;
	margin-left: -30rpx;
	width: 60rpx;
	height: 30rpx;
	line-height: 30rpx;
	text-align: center;
	color: #ffffff;
	border-radius: 30rpx;
	border: 1px solid #ffffff;
	background: red;
}

.order-datail .fight-users .user-box.user-who {
	font-size: 50rpx;
	color: #999999;
}

.state-cont .countdown-datetime {
	margin-top: 16rpx;
}

.state-cont .countdown-datetime text {
	padding: 4rpx 8rpx;
	border-radius: 4rpx;
	background: rgba(0, 0, 0, 0.4);
}

.icon-dianpu1 {
	margin-right: 15rpx;
}

.kefu {
	border-top: 1rpx solid #cacaca;
	display: flex;
	justify-content: center;
	align-items: center;
}

.kefu .icon-kefu2 {
	color: #1296db;
	margin-right: 8rpx;
}

.group {
	margin: 0 20rpx;
	margin-top: 20rpx;
	border-radius: 12rpx;
}

.foot-btns button {
	height: 60rpx;
	line-height: 60rpx;
	border-radius: 30rpx;
}

.supplement-box {
	margin-top: 20rpx;
	@include background_color('bg-tips');
	@include border_color('border_color');
	border: 1rpx solid;
	border-radius: 12rpx;
	line-height: 1.5;
}
</style>
