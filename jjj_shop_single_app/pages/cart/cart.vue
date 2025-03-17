<template>
	<view :data-theme='theme()' :class="theme() || ''">
		<view class="card" v-if="!loadding">
			<!-- #ifdef APP-PLUS -->
			<header-bar></header-bar>
			<!-- #endif -->
			<!--产品列表-->
			<template v-if="tableData.length > 0">
				<view class="address-bar d-e-c">
					<view class="f30" @click="isEdit = !isEdit">
						<button class="f24" v-if="isEdit">完成</button>
						<button class="f24" v-else>编辑</button>
					</view>
				</view>

				<view class="section">
					<view class="supplier_list">
						<view class="item" v-for="(item, index) in tableData" :key="index">
              <checkbox-group @change="checkItem(item, index)">
                <label class="d-c-c" >
                  <checkbox value="cb" class="checkbox" :checked="item.checked" />
                </label>
              </checkbox-group>
							<image :src="item.productImage" class="cover" mode="aspectFit"></image>
							<view class="info">
								<view class="title">{{ item.productName }}</view>
								<view class="describe">{{ item.productAttr }}</view>
								<view class="level-box count_choose">
									<view class="price">
										¥
										<text class="num">{{ item.productPrice }}</text>
									</view>
									<view class="num-wrap">
										<view class="" @tap.stop="reduceFunc(item)" v-if="item.totalNum > 1">
											<image class="reduce_icon" src="../../static/icon/reduce.png" mode="">
											</image>
										</view>
										<view class="text-wrap">{{item.totalNum}}</view>
										<view class="" @tap.stop="addFunc(item)"
											v-if="item.totalNum < item.stockNum ">
											<image class="add_icon" src="../../static/icon/add.png" mode=""></image>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>

				</view>
			</template>
			<template v-else>
				<view class="none-data-box cart_none">
					<image class="cart_none_img" src="/static/cart_none.png" mode="widthFix"></image>
					<text class="f26 gray9 pt10">购物车空空如也</text>
					<button type="default" class="theme-btn mt30 ww100 none_btn" @tap="gotoShop">去购物</button>
				</view>
			</template>

			<!--底部按钮-->
      <view class="bottom-btns f28" v-if="tableData.length > 0">
        <checkbox-group @change="onCheckedAll">
          <label class="d-c-c mr20" >
            <checkbox class="checkbox" :checked="checkedAll" value="cb" />
            全选
          </label>
        </checkbox-group>
				<view class="d-e-c" v-if="!isEdit">
					<view class="total d-s-c flex-1 mr20">
						<text>合计：</text>
						<view class="price f22">
							¥
							<text class="num f40">{{ totalPrice }}</text>
						</view>
					</view>
					<button type="primary" class="buy-btn theme-btn" @click="Submit()">去结算</button>
				</view>
				<view class="" v-else><button type="primary" @click="onDelete()"
						class="delete-btn theme-btn mr20">删除</button>
				</view>
			</view>
			<!--推荐-->
			<recommendProduct :location="10"></recommendProduct>
		</view>
		<tabBar></tabBar>
	</view>
</template>

<script>
	import recommendProduct from '@/components/recommendProduct/recommendProduct.vue';
	export default {
		components: {
			recommendProduct
		},
		data() {
			return {
				/*是否加载完成*/
				loadding: true,
				isEdit: false,
				/*购物车商品*/
				tableData: [],
				arrIds: [],
				// 是否全选
				checkedAll: false,
				totalPrice: 0
			};
		},
		onLoad() {},
		onShow() {
			/*获取产品详情*/
			this.getData();
			this.getTabBarLinks();
		},
		methods: {
			/*获取数据*/
			getData() {
				uni.showLoading({
					title: '加载中'
				});
				let self = this;
				self._post('order/cart/lists', {}, function(res) {
					uni.hideLoading();
					self.tableData = res.data;
					self.loadding = false;
					self._initGoodsChecked();
				});
			},
			/**
			 * 初始化商品选中状态
			 */
			_initGoodsChecked() {
				let self = this;
				let checkedData = self.getCheckedData();
				// 将商品设置选中
				self.tableData.forEach(item => {
					item.checked = self.inArray(`${item.productId}_${item.specSkuId}`, checkedData);
				});
				self.isEdit = false;
				self.checkedAll = checkedData.length == self.tableData.length;
				// 更新购物车已选商品总价格
				self.updateTotalPrice();
			},

			/**
			 * 获取选中的商品
			 */
			getCheckedData() {
				let checkedData = uni.getStorageSync('checkedData');
				return checkedData ? checkedData : [];
			},

			/*单选*/
			checkItem(e, index) {
				e.checked = !e.checked;
        this.tableData[index] = e;

				// 更新选中记录
				this.onUpdateChecked();
				// 更新购物车已选商品总价格
				this.updateTotalPrice();
				// 更新全选状态
				this.checkedAll = this.getCheckedData().length == this.tableData.length;
			},

			/**
			 * 更新商品选中记录
			 */
			onUpdateChecked() {
				let self = this,
					checkedData = [];
				self.tableData.forEach(item => {
					if (item.checked == true) {
						checkedData.push(`${item.productId}_${item.specSkuId}`);
					}
				});
				uni.setStorageSync('checkedData', checkedData);
			},
			/*全选*/
			onCheckedAll() {
				let self = this;
				self.checkedAll = !self.checkedAll;
				self.tableData.forEach(item => {
					item.checked = self.checkedAll;
				});
				self.updateTotalPrice();
				// 更新选中记录
				self.onUpdateChecked();
			},
			updateTotalPrice: function() {
				let total_price = 0;
				let items = this.tableData;
				for (let i = 0; i < items.length; i++) {
					if (items[i]['checked'] == true) {
						total_price += items[i]['totalNum'] * items[i]['productPrice'];
					}
				}
				/*保留2位小数*/
				this.totalPrice = total_price.toFixed(2);
			},
			/*去结算*/
			Submit() {
				let arrIds = [];
				this.tableData.forEach(item => {
					if (item.checked == true) {
						arrIds.push(`${item.productId}_${item.specSkuId}`);
					}
				});
				if (arrIds.length == 0) {
					uni.showToast({
						title: '请选择商品',
						icon: 'none'
					});
					return false;
				}
				this.gotoPage('pages/order/saveOrder?orderType=cart&cartIds=' + arrIds);
			},
			/*添加*/
			addFunc(item) {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});
				self._post(
					'order/cart/add', {
						productId: item.productId,
						specSkuId: item.specSkuId,
						totalNum: 1
					},
					function(res) {
						self.loadding = false;
						self.getData();
					},
					function() {
						self.loadding = false;
					}
				);
			},
			/*减少*/
			reduceFunc(item) {
				let self = this;
				if (item.total_num <= 1) {
					return;
				}
				uni.showLoading({
					title: '加载中'
				});
				self._post(
					'order/cart/sub', {
						productId: item.productId,
						specSkuId: item.specSkuId
					},
					function(res) {
						self.loadding = false;
						self.getData();
					},
					function() {
						self.loadding = false;
					}
				);
			},
			/*删除购物车*/
			onDelete() {
				let self = this;
				let cartIds = self.getCheckedIds();
				if (!cartIds.length) {
					self.showError('您还没有选择商品');
					return false;
				}
				uni.showModal({
					title: '提示',
					content: '您确定要移除选择的商品吗?',
					success(e) {
						e.confirm &&
							self._post(
								'order/cart/delete', {
									cartIds: cartIds
								},
								function(res) {
									// 删除选中的商品
									self.onDeleteEvent(cartIds);
									self.getData();
								}
							);
					}
				});
			},
			/**
			 * 获取已选中的商品
			 */
			getCheckedIds() {
				let self = this;
				let arrIds = [];
				self.tableData.forEach(item => {
					if (item.checked === true) {
						arrIds.push(item.cartId);
					}
				});
				return arrIds;
			},

			/**
			 * 商品删除事件
			 */
			onDeleteEvent(cartIds) {
				let self = this;
				cartIds.forEach(cartId => {
					self.tableData.forEach((item, productIndex) => {
						if (cartId == item.cartId) {
							self.tableData.splice(productIndex, 1);
						}
					});
				});
				// 更新选中记录
				self.onUpdateChecked();
				return true;
			},
			/**
			 * 是否在数组内
			 */
			inArray(search, array) {
				for (var i in array) {
					if (array[i] == search) {
						return true;
					}
				}
				return false;
			},

			/*去购物*/
			gotoShop() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			}
		}
	};
</script>

<style lang="scss">
	page {
		background-color: #f2f2f2;
	}

	.foot_ {
		height: 98rpx;
		width: 100%;
	}

	.card .scroll-Y {
		position: relative;
	}

	.card .checkbox {
		@include font_color('font_color');
		transform: scale(0.7);
	}

	.address-bar {
		padding: 20rpx;
		background-color: #f2f2f2;
	}

	.address-bar button {
		border: none;
		background: none;
		color: #333333;
	}

	.section {
		background: #f2f2f2;
		padding: 20rpx;
	}

	.section .item {
		margin: 0 26rpx;
		display: flex;
		align-items: center;
		// border: 1px solid #f2f2f2;
		padding-right: 19rpx;
		padding-bottom: 29rpx;
		padding-top: 29rpx;
	}

	.section .cover {
		width: 150rpx;
		height: 150rpx;
		border-radius: 8px;
	}

	.section .info {
		flex: 1;
		padding-left: 30rpx;
		box-sizing: border-box;
		overflow: hidden;
	}

	.section .title {
		width: 100%;
		font-size: 26rpx;
		display: -webkit-box;
		overflow: hidden;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.vender .list .describe {
		width: 100%;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.section .describe {
		margin-top: 20rpx;
		font-size: 24rpx;
		color: #999999;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}

	.section .price {
		// color: $dominant-color;
		@include font_color("font_color");
		font-size: 24rpx;
	}

	.section .price .num {
		padding: 0 4rpx;
		font-size: 32rpx;
	}

	.section .level-box {
		margin-top: 20rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.section .level-box .key {
		font-size: 24rpx;
		color: #999999;
	}

	.section .level-box .num-wrap {
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.section .level-box .icon-box {
		width: 33rpx;
		height: 33rpx;
		border: 1px solid #c5c5c5;
		background: #f2f2f2;
	}

	.section .level-box .icon-box .gray {
		color: #cccccc;
	}

	.section .level-box .icon-box .gray3 {
		color: #333333;
	}

	.section .level-box .text-wrap {
		margin: 0 20rpx;
		height: 33rpx;
		border: none;
		background: none;
	}

	.section .level-box .text-wrap input {
		padding: 0 4rpx;
		height: 33rpx;
		line-height: 1;
		width: 40rpx;
		font-size: 32rpx;
		text-align: center;
		display: flex;
		align-items: center;
		min-height: 33rpx;
	}

	.bottom-btns {
		position: fixed;
		width: 100%;
		padding: 0 0 0 20rpx;
		box-sizing: border-box;
		display: flex;
		justify-content: space-between;
		align-items: center;
		height: 90rpx;
		// /* bottom: calc(var(--window-bottom); */
		bottom: var(--window-bottom, 0);
		// bottom: 50px;
		left: 0;
		box-shadow: 0 -2rpx 8rpx rgba(0, 0, 0, 0.1);
		background: #ffffff;
		z-index: 1000;
	}

	.bottom-btns .delete-btn {
		margin: 0;
		padding: 0 30rpx;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 30rpx;
		// background: linear-gradient(90deg, #FF6B6B 4%, #F6220C 100%);
		font-size: 26rpx;
	}

	.bottom-btns .buy-btn {
		margin: 0;
		padding: 0 24rpx;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 30rpx;
		// background: linear-gradient(90deg, #FF6B6B 4%, #F6220C 100%);
		font-size: 26rpx;
	}

	.bottom-btns .price {
		@include font_color("price_color");
	}

	.supplier_list {
		overflow: hidden;
		background-color: #FFFFFF;
		border-radius: 15rpx;
		margin-bottom: 30rpx;
	}

	.supplier_list_tit {
		display: flex;
		align-items: center;
		margin-left: 28rpx;
		margin-top: 28rpx;
	}

	.icon-dianpu1 {
		font-size: 34rpx;
		color: #333333;
		margin: 0 17rpx;
	}

	.cart_none .cart_none_img {
		width: 348rpx;
		height: 222rpx;
	}

	.none_btn {
		font-size: 32rpx;
		border-radius: 40rpx;
	}

	.add_icon,
	.reduce_icon {
		width: 32rpx;
		height: 32rpx;
	}
</style>
