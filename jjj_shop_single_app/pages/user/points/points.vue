<template>
	<view :data-theme='theme()' :class="theme() || ''">
		<view class="points-top theme-bg d-b-c">
			<view class="">
				<text class="f26">积分总数：</text>
				<text class="f34 fb">{{points}}</text>
				<text class="f24 re-btn" @click="isPop=true" v-if="isTransBalance">提现</text>
			</view>
			<button v-if="is_open" type="default" class="theme-borderbtn" @click="gotoShop">积分商城</button>
		</view>
		<!--列表-->

		<view class="p-0-30 bg-white">
			<view class="d-b-c border-b p-30-0" v-for="(item, index) in tableData" :key="index">
				<view class="d-s-s f-w d-c flex-1">
					<text class="f24">{{item.description}}</text>
					<text class="pt10 gray9 f22">{{item.createTime}}</text>
				</view>
				<view class="red" v-if="item.value>0">+{{item.value}}</view>
				<view class="red" v-else>{{item.value}}</view>
			</view>
			<!-- 没有记录 -->
			<view class="d-c-c p30" v-if="tableData.length==0 && !loading">
				<text class="iconfont icon-wushuju"></text>
				<text class="cont">亲，暂无相关记录哦</text>
			</view>
			<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
		</view>
		<recharge :isPop="isPop" :discount_ratio="discountRatio" @close="closePop"></recharge>
	</view>
</template>

<script>
	import uniLoadMore from "@/components/uni-load-more.vue";
	import recharge from "./part/recharge.vue";
	export default {
		components: {
			uniLoadMore,
			recharge
		},
		data() {
			return {
				isPop: false,
				/*是否加载完成*/
				loadding: true,
				indicatorDots: true,
				autoplay: true,
				interval: 2000,
				duration: 500,
				/*顶部刷新*/
				topRefresh: false,
				/*手机高度*/
				phoneHeight: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,
				/*数据列表*/
				tableData: [],
				/*最后一页码数*/
				last_page: 0,
				/*当前页面*/
				page: 1,
				/*每页条数*/
				list_rows: 20,
				no_more: false,
				loading: true,
				points: 0,
				is_open: false,
				discountRatio:'0',
				isTransBalance:false
			};
		},
		computed: {

			/*加载中状态*/
			loadingType() {
				if (this.loading) {
					return 1;
				} else {
					if (this.tableData.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		mounted() {
			/*获取数据*/
			this.getData();
		},
		onReachBottom() {
			let self = this;
			if (self.page < self.last_page) {
				self.page++;
				self.getData();
			}
			self.no_more = true;

		},
		methods: {
			closePop(e) {
				if(e!=null){
					this.page = 1;
					this.tableData = [];
					this.getData();
				}
				this.isPop = false;
			},
			/*获取数据*/
			getData() {
				let self = this;
				let page = self.page;
				let list_rows = self.list_rows;
				self._postBody('points/log/index', {
					pageIndex: page || 1,
					pageSize: list_rows,
				}, function(res) {
					self.loading = false;
					self.points = res.data.points;
					self.discountRatio = res.data.discountRatio;
					self.is_open = res.data.isOpen;
					self.isTransBalance = res.data.isTransBalance;
					self.tableData = self.tableData.concat(res.data.list.records);
					self.last_page = res.data.list.lastPage;
					if (res.data.list.lastPage <= 1) {
						self.no_more = true;
						return false;
					}

				});
			},
			/*跳转积分商城*/
			gotoShop() {
				this.gotoPage('/pages/plus/points/list/list');
			}
		}
	};
</script>

<style>
	.points-top {
		height: 100rpx;
		padding: 0 30rpx;
		color: #FFFFFF;
	}

	.re-btn {
		padding: 0 8rpx;
		color: #FFFFFF;
		border: 1rpx solid #FFFFFF;
		border-radius: 5rpx;
		margin-left: 10rpx;
	}
</style>
