<template>
	<view class="order-refund-detail pb100" :data-theme='theme()' :class="theme() || ''">
		<!--售后状态-->
		<view class="order-state d-s-c">
			<view class="icon-box"><span class="icon iconfont icon-gantanhao"></span></view>
			<view class="state-cont flex-1">
				<view class="state-txt d-b-c">
					<text class="desc f34">{{detail.stateText}}</text>
				</view>
			</view>
			<view class="dot-bg"></view>
		</view>

		<!--商品信息-->
		<view class="p30 mt20 bg-white">
			<view class="one-product border-b-e d-s-c pb20">
				<view class="cover">
					<image :src="detail.orderProduct && detail.orderProduct.productImage" mode="aspectFit"></image>
				</view>
				<view class="flex-1">
					<view class="pro-info">{{detail.orderProduct && detail.orderProduct.productName || ''}}</view>
					<view class="pt10 p-0-30">
						<text class="f24 gray9">
							{{detail.orderProduct && detail.orderProduct.productAttr || ''}}
						</text>
					</view>
				</view>
			</view>
			<view class="d-e-c pt20 lh150">
				<view class="f24">
					商品金额：
					<text class="red">¥{{totalPrice}}</text>
				</view>
			</view>
			<view class="d-e-c pt10 lh150">
				<view class="f24">
					订单实付金额：
					<text class="red">¥{{totalPayPrice}}</text>
				</view>
			</view>
			<view class="d-e-c pt10 lh150" v-if="detail.orderM && detail.orderM.orderSource==80">
				<view class="f24" v-if="detail.orderproduct.advance">
					定金({{detail.orderproduct.advance.moneyReturn?'可退':'不可退'}})：
					<text class="red">¥{{detail.orderproduct.advance.payPrice}}</text>
				</view>
			</view>
		</view>

		<!-- 已退款金额 -->
		<view class="group bg-white" v-if=" detail.status == 20 && detail.type == 10 ">
			<text class="gray9">已退款金额：</text>
			<text class="gray9">￥{{ detail.refundMoney }}</text>
		</view>

		<!--申请售后信息-->
		<view class="group bg-white f24">
			<view class="p-20-0 border-b f34">
				申请退货信息
			</view>
			<view class="p-20-0 f28">
				<text class="gray9">售后类型：</text>
				<text>{{detail.typeText}}</text>
			</view>
			<view class="p-20-0 f28">
				<text class="gray9">申请原因：</text>
				<text>
					{{detail.applyDesc||''}}
				</text>
			</view>
			<view class="p-20-0 upload-list d-s-s f28">
				<text class="gray9">申请凭证：</text>
				<view class="d-s-s f-w">
					<block v-if="detail && detail.images && detail.images.length>0">
						<view class="item" v-for="(imgs,img_num) in detail.images" :key="img_num">
							<image :src="imgs.filePath" mode="aspectFit"></image>
						</view>
					</block>
					<block v-else>
						无
					</block>
				</view>
			</view>
		</view>

		<!-- 售后信息-->
		<view v-if="detail.status == 10" class="group bg-white">
			<view class="p-20-0 border-b f34">
				拒绝理由
			</view>
			<view class="p-20-0">
				<text class="red f28">{{ detail.refuseDesc }}</text>
			</view>
		</view>

		<!--退货地址-->
		<view class="group bg-white" v-if="detail.isAgree == 10 && detail.address">
			<view class="p-20-0 border-b f34">
				退货地址
			</view>
			<view class="pt30 f28">
				<text class="gray9">收货人：</text>
				<text>{{detail.address.name}}</text>
			</view>
			<view class="pt30 f28">
				<text class="gray9">联系电话：</text>
				<text>{{detail.address.phone}}</text>
			</view>
			<view class="pt30 f28">
				<text class="gray9">详情地址：</text>
				<text>{{detail.address.detail}}</text>
			</view>
			<view class="pt30 f28" v-if="detail.expressNo">
				<text class="gray9">物流公司：</text>
				<text>{{detail.expressName}}</text>
			</view>
			<view class="pt30 f28" v-if="detail.expressNo">
				<text class="gray9">物流单号：</text>
				<text>{{detail.expressNo}}</text>
			</view>
			<view class="pt30 f28" v-if="detail.isPlateSend">
				<text class="gray9">换货物流公司：</text>
				<text>{{detail.sendExpressName}}</text>
			</view>
			<view class="pt30 f28" v-if="detail.isPlateSend"
				@click="gotoPage('/pages/order/express/refund-express?orderId=' + detail.orderRefundId);">
				<text class="gray9">换货物流单号：</text>
				<text>{{detail.sendExpressNo}}</text>
			</view>
			<view class="mt20 pb20 border-t gray9">
				<view class="pt20">
					· 未与卖家协商一致情况下，请勿寄到付或平邮
				</view>
				<view class="pt10">
					· 请填写真实有效物流信息
				</view>
			</view>
		</view>
		<!-- 填写物流信息 -->
		<form @submit="formSubmit"
			v-if="detail.type !=30 && detail.isAgree == 10 && detail.isUserSend == 0 " report-submit>
			<view class="group bg-white">
				<view class="p-20-0 border-b f34">
					填写物流信息
				</view>
				<view class="p-20-0 d-s-c">
					<view class="gray9">物流公司：</view>
					<view class="flex-1 p20 border">
						<picker mode="selector" @change="onExpressChange" :range="expressList" range-key="expressName"
							:value="index">
							<text v-if="index > -1 ">{{expressList[index].expressName}}</text>
							<text v-else class="col-80">请选择物流公司</text>
						</picker>
					</view>
				</view>
				<view class="p-20-0 d-s-c">
					<view class="gray9">物流单号：</view>
					<view class="flex-1 border">
						<input class="p10" placeholder="请填写物流单号" name="expressNo" />
					</view>
				</view>
				<view class="mt20">
					<button class="btn-red" formType="submit">确认发货</button>
				</view>
			</view>
		</form>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				/*是否加载完成*/
				loadding: true,
				indicatorDots: true,
				autoplay: true,
				interval: 2000,
				duration: 500,
				expressList: {},
				index: -1,
				ordeRefundId: 0,
				/*退货详情*/
				detail: {
					address: {},
				},
				expressId: 0,
				totalPrice: 0,
				totalPayPrice: 0,
				/*消息模板*/
				temlIds: [],
			}
		},
		onLoad(e) {
			this.orderRefundId = e.orderRefundId;
		},
		mounted() {
			/*获取详情*/
			this.getData();
		},
		methods: {
			/*获取数据*/
			getData() {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});
				let orderRefundId = self.orderRefundId;
				self._get(
					'user/refund/detail', {
						orderRefundId: orderRefundId,
						platform: self.getPlatform()
					},
					function(res) {
						uni.hideLoading();
						self.detail = res.data;
						self.expressList = res.data.expressList;
						self.temlIds = res.data.templateArr;
						self.totalPrice = self.detail.orderProduct.totalPrice;
						self.totalPayPrice = self.detail.orderProduct.totalPayPrice;
					}
				);
			},
			/*选择物流*/
			onExpressChange: function(e) {
				this.index = e.detail.value;
				this.expressId = this.expressList[this.index].expressId;
			},

			/*发货*/
			formSubmit: function(e) {
				let self = this;
				var formdata = e.detail.value;
				formdata.orderRefundId = self.orderRefundId;
				formdata.expressId = self.expressId;
				let callback = function() {
					uni.showLoading({
						title: '正在提交',
						mask: true
					});
					self._postBody('user/refund/delivery', formdata, function(res) {
						uni.hideLoading();
						uni.showToast({
							title: res.msg,
							duration: 3000,
							complete: function() {
								self.gotoPage(
									'/pages/order/refund/detail/detail?orderRefundId=' +
									self.orderRefundId, 'redirectTo');
							}
						});
					});
				};
				self.subMessage(self.temlIds, callback);
			},
		}
	}
</script>

<style>
	.order-refund-detail .btn-red {
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		box-shadow: 0 8rpx 16rpx 0 rgba(226, 35, 26, .6);
	}
</style>
