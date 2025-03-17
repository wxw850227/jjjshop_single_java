<template>
	<view class="coupon-wrap bg-white" v-if="!loadding">
		<block v-if="DataList.length > 0">
			<view class="item-wrap" v-for="(item, index) in DataList" :key="index">
				<view :class="'coupon-item coupon-item-'+item.colorText" @click="lookRule(item)">
					<!--装饰用的小圆-->
					<view class="circles">
						<text v-for="(circle,num) in 8" :key="num"></text>
					</view>
					<view class="info">
						<view >{{item.couponTypeText}}</view>
					</view>
					<view class="operation d-b-c w-b">
						<view class="flex-1 coupon-content">
							<view :class="item.isExpire==0&&item.isUse==0?item.colorText:''">
								<template v-if=" item.couponType == 10 ">
									<view class="price" >
										<text class="f40 fb">减{{ item.reducePrice*1 }}元</text>
									</view>
								</template>
								<template v-if="item.couponType == 20 ">
									<text class="f40 fb">{{ item.discount/10 }}</text><text>折</text>
								</template>
							</view>
							<view class="f30">{{item.minPrice>0?'满'+item.minPrice*1+'元可用':'无门槛'}}</view>
							<view class="f24">
								<template v-if="item.expireType ==10">
									有效期：领取{{ item.expireDay }}天内有效
								</template>
								<template v-if="item.expireType ==20">
									有效期：{{item.startTimeText}}至{{item.endTimeText}}
								</template>
							</view>
						</view>
						<view class="btns d-c-c">
							<button type="default" v-if="item.state.value>0" :class="'btn-'+item.colorText" v-on:click.stop="receive(item.couponId )">
								立即领取
							</button>
							<button type="default" v-else class="btn-gray" v-on:click.stop>
								{{ item.state.text }}
							</button>
						</view>
					</view>
				</view>
				<view class="range_item d-b-c" v-if="item.applyRange == 20" @click.stop="gotoPage('/pages/coupon/detail?couponId='+ item.couponId+'&applyRange='+item.applyRange)">
					<view>仅可购买指定部分商品</view>
					<view><text class="icon iconfont icon-jiantou" style="color: #999999; font-size: 24rpx;"></text></view>
				</view>
				<view class="range_item d-b-c" v-if="item.applyRange == 30" @click.stop="gotoPage('/pages/coupon/detail?couponId='+ item.couponId+'&applyRange='+item.applyRange)">
					<view>仅可购买指定分类商品</view>
					<view><text class="icon iconfont icon-jiantou" style="color: #999999; font-size: 24rpx;"></text></view>
				</view>
			</view>
		</block>
		<block v-else>
			<view class="none-data-box">
				<image src="/static/none.png" mode="widthFix"></image>
				<text>暂无数据</text>
			</view>
		</block>
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
				DataList: [],
				/*当前页面*/
				page: 1,
				/*每页条数*/
				list_rows: 10,
			};
		},
		onShow() {
			uni.showLoading({
				title: '加载中'
			});
			/*获取优惠券列表*/
			this.getData();
		},
		methods: {
			
			/*获取数据*/
			getData() {
				let self = this;
				self._get('coupon/coupon/lists', {}, function(res) {
					self.DataList = res.data;
					console.log(self.DataList)
					self.loadding = false;
					uni.hideLoading();
				});
			},
			/*查看规则*/
			lookRule(item) {
				item.rule = true;
			},

			/*关闭规则*/
			closeRule(item) {
				item.rule = false;
			},

			/*领取优惠券*/
			receive(e) {

				let self = this;
				uni.showLoading({
					title: '领取中'
				});
				self._post('user/coupon/receive', {
					couponId: e,
				}, function(res) {
					uni.hideLoading();
					self.getData();
					uni.showToast({
						title: '领取成功',
						duration: 2000,
						icon: 'success'
					});
				});
				self.getData();
			},

		}
	};
</script>

<style>
	.coupon-wrap {
		padding: 30rpx;
	}

	.item-wrap {
		margin-bottom: 20rpx;
	}
	.range_item{
		border: 1rpx solid #D9D9D9;
		border-top: none;
		padding: 8rpx;
		border-bottom-left-radius:10rpx ;
		border-bottom-right-radius:10rpx ;
		color: #666666;
		box-shadow: 0 0 8rpx rgba(0, 0, 0, 0.1);
	}
</style>
