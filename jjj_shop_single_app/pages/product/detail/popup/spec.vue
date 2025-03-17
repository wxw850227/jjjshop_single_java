<template>
	<view :class="Visible ? 'product-popup open' : 'product-popup close'" @touchmove.stop.prevent="" @click="closePopup">
		<view class="popup-bg"></view>
		<view class="main" v-on:click.stop>
			<view class="header">
				<image :src="form.showSku.skuImage" mode="aspectFit" class="avt"></image>
				<view class="price">
					¥
					<text class="num">{{ form.showSku.productPrice }}</text>
					<text class="old-price" v-if="form.showSku.linePrice">¥{{ form.showSku.linePrice }}</text>
				</view>
				<view class="stock">库存：{{ form.showSku.stockNum }}</view>
				<view class="select_spec">{{ selectSpec }}</view>
				<view class="close-btn" @click="closePopup"><text class="icon iconfont icon-guanbi"></text></view>
			</view>

			<view class="body">
				<!--规格-->
				<view>
					<scroll-view scroll-y="true" class="specs mt20" style="max-height: 600rpx;" v-if="form.specData != null">
						<view class="specs mt20" v-for="(item_attr, attr_index) in form.specData.specAttr" :key="attr_index">
							<view class="specs-hd p-20-0">
								<text class="f26 gray3">{{ item_attr.groupName }}</text>
							</view>
							<view class="specs-list">
								<button
									:class="item.checked ? 'btn-checked' : 'btn-checke'"
									v-for="(item, item_index) in item_attr.specItems"
									:key="item_index"
									@click="selectAttr(attr_index, item_index)"
								>
									{{ item.specValue }}
								</button>
							</view>
						</view>
					</scroll-view>
				</view>
				<!--选择数量-->
				<view class="level-box count_choose">
					<text class="key">数量</text>
					<view class="d-s-c">
						<view class="icon-box minus d-c-c" @click="sub()" :class="{ 'num-wrap': !issub }">
							<text class="icon iconfont icon-jian" style="font-size: 20rpx;color: #333333;"></text>
						</view>
						<view class="text-wrap"><input type="text" v-model="form.showSku.sum"   /></view>
						<view class="icon-box plus d-c-c" :class="{ 'num-wrap': !isadd }" @click="add()">
							<text class="icon iconfont icon-jia" style="font-size: 20rpx;color: #333333;"></text>
						</view>
					</view>
				</view>
			</view>
			<view class="btns"><button class="confirm-btn" @click="confirmFunc(form)">确认</button></view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			/*是否可见*/
			Visible: false,
			/*表单对象*/
			form: {
				detail: {},
				showSku: {
					skuImage: ''
				}
			},
			/*当前商品总库存*/
			stock: 0,
			/*选择提示*/
			selectSpec: '',
			/*是否打开过多规格选择框*/
			isOpenSpec: false,
			type: ''
		};
	},
	props: ['isPopup', 'productModel'],
	onLoad() {},
	mounted() {},
	computed: {
		/*判断增加数量*/
		isadd: function() {
			return this.form.showSku.sum >= this.stock || this.form.showSku.sum >= this.form.detail.limitNum;
		},

		/*判断减少数量*/
		issub: function() {
			return this.form.showSku.sum <= 1;
		}
	},
	watch: {
		isPopup: function(n, o) {
			if (n != o) {
				this.Visible = n;
				if (!this.isOpenSpec) {
					this.form = this.productModel;
					this.isOpenSpec = true;
					this.initShowSku();
				}
				this.form.type = this.productModel.type;
			}
		},
		'form.specData': {
			handler(n, o) {
				let str = '',
					select = '';
				this.isAll = true;
				if (n) {
					for (let i = 0; i < n.specAttr.length; i++) {
						console.log(this.form.productSpecArr);
						console.log('i=' + i);
						if (this.form.productSpecArr[i] == null) {
							this.isAll = false;
							str += n.specAttr[i].groupName + ' ';
						} else {
							n.specAttr[i].specItems.forEach(item => {
								if (this.form.productSpecArr[i] == item.itemId) {
									select += '"' + item.specValue + '" ';
								}
							});
						}
					}
					if (!this.isAll) {
						str = '请选择: ' + str;
					} else {
						select = '已选: ' + select;
					}
				}
				this.selectSpec = this.isAll ? select : str;
			},
			deep: true,
			immediate: true
		}
	},
	methods: {
		/*初始化*/
		initShowSku() {
			this.form.showSku.skuImage = this.form.detail.image[0].filePath;
			this.form.showSku.productPrice = this.form.detail.productPrice;
			if (this.form.detail.specType == 20) {
				this.form.showSku.productPrice = this.form.detail.productPrice + '-' + this.form.detail.highPrice;
			}
			this.form.showSku.specSkuId = 0;
			this.form.showSku.linePrice = this.form.detail.linePrice;
			this.form.showSku.stockNum = this.form.detail.productStock;
			this.form.showSku.sum = 1;
			this.stock = this.form.detail.productStock;
			if (this.form.plusName == 'seckill') {
				this.form.showSku.productPrice = this.form.plusSku[0].seckillPrice;
				this.form.showSku.linePrice = this.form.plusSku[0].productPrice;
				this.form.showSku.skuImage = this.form.plusSku[0].productSku.imagePath ? this.form.plusSku[0].productSku.imagePath : this.form.detail.image[0].filePath;
				this.form.showSku.stockNum = this.form.plusSku[0].seckillStock;
				this.stock = this.form.plusSku[0].seckillStock;
			}
		},

		/*选择属性*/
		selectAttr(attr_index, item_index) {
			let self = this;
			let items = self.form.specData.specAttr[attr_index].specItems;
			let curItem = items[item_index];
			if (curItem.checked) {
				curItem.checked = false;
				self.form.productSpecArr[attr_index] = null;
			} else {
				for (let i = 0; i < items.length; i++) {
					items[i].checked = false;
				}
				curItem.checked = true;
				self.form.productSpecArr[attr_index] = curItem.itemId;
			}

			for (let i = 0; i < self.form.productSpecArr.length; i++) {
				if (self.form.productSpecArr[i] == null) {
					self.initShowSku();
					return;
				}
			}

			// 更新商品规格信息
			self.updateSpecProduct();
		},

		/*更新商品规格信息*/
		updateSpecProduct() {
			let self = this;
			let specSkuId = self.form.productSpecArr.join('_');
			// 查找skuItem
			let specList = self.form.specData.specList;
			if (self.form.plusSku != null) {
				specList = self.form.plusSku;
			}
			let skuItem = specList.find(val => {
				if (self.form.plusName) {
					return val.productSku.specSkuId == specSkuId;
				} else {
					return val.specSkuId == specSkuId;
				}
			});
			if (!skuItem) {
				self.clock = true;
				self.initShowSku();
				return;
			} else {
				self.clock = false;
			}
			if (self.form.plusName && !skuItem.specForm) {
				skuItem.specForm = skuItem.productSku;
			}
			// 记录product_sku_id
			// 更新商品价格、划线价、库存
			if (typeof skuItem === 'object') {
				/*保存当前规格*/
				console.log(skuItem);
				self.stock = skuItem.specForm.stockNum;
				if (self.form.showSku.sum > self.stock) {
					self.form.showSku.sum = self.stock > 0 ? self.stock : 1;
				}
				self.form.showSku.specSkuId = specSkuId;
				self.form.showSku.productPrice = skuItem.specForm.productPrice;
				self.form.showSku.linePrice = skuItem.specForm.linePrice;
				if (skuItem.specForm.imageId > 0) {
					self.form.showSku.skuImage = skuItem.specForm.imagePath;
				} else {
					self.form.showSku.skuImage = self.form.detail.image[0].filePath;
				}
				self.form.showSku.stockNum = skuItem.specForm.stockNum;
				if (self.form.plusName) {
					self.form.showSku.productPrice = skuItem.productPrice;
					if (self.form.plusName == 'seckill') {
						self.form.showSku.productPrice = skuItem.seckillPrice;
					}
					self.form.showSku.stockNum = skuItem[self.form.plusName + 'Stock'];
					self.form.showSku.linePrice = '';
					self.form.showSku.skuImage = skuItem.specForm.imagePath ? skuItem.specForm.imagePath : self.form.detail.imagePath;
					self.form.showSku.advanceProductId = skuItem.specForm.imagePath ? skuItem.specForm.imagePath : self.form.detail.imagePath;
				}
			}
		},

		/*关闭弹窗*/
		closePopup() {
			this.$emit('close', this.form.specData, null);
		},

		/*确认提交*/
		confirmFunc() {
			if (this.form.specData != null) {
				for (let i = 0; i < this.form.productSpecArr.length; i++) {
					if (this.form.productSpecArr[i] == null) {
						uni.showToast({
							title: '请选择规格',
							icon: 'none',
							duration: 2000
						});
						return;
					}
				}
			}
			if (this.form.type == 'card') {
				this.addCart();
			} else if (this.form.type == 'order') {
				this.createdOrder();
			} else if (this.form.type == 'deposit') {
				this.createdOrder();
			}
		},

		/*加入购物车*/
		addCart() {
			let self = this;
			let productId = self.form.detail.productId;
			let totalNum = self.form.showSku.sum;
			//let specSkuId = self.form.showSku.productSkuId;
			let specSkuId = self.form.showSku.specSkuId;
			console.log('specSkuId='+specSkuId);
			if (self.form.detail.specType == 20 && specSkuId == 0) {
				uni.showToast({
					title: '请选择属性',
					icon: 'none',
					duration: 2000
				});
				return false;
			}

			self._post(
				'order/cart/add',
				{
					productId: productId,
					totalNum: totalNum,
					specSkuId: specSkuId
				},
				function(res) {
					uni.showToast({
						title: res.msg,
						duration: 2000
					});
					self.$emit('close', null, res.data);
				}
			);
		},

		/*创建订单*/
		createdOrder() {
			let productId = this.form.detail.productId;
			let productNum = this.form.showSku.sum;
			let productSkuId = this.form.showSku.productSkuId;
			let specSkuId = this.form.showSku.specSkuId;
			let url = 'pages/order/saveOrder?productId=' + productId + '&productNum=' + productNum + '&specSkuId=' + specSkuId + '&orderType=buy';
			if (this.form.type == 'deposit') {
				if (this.form.plusName == 'advance') {
					let skuItem = this.form.detail.advance.sku.find(val => {
						return val.productSku.specSkuId == specSkuId;
					});
					url =
						'pages/order/saveOrder?productId=' +
						productId +
						'&productNum=' +
						productNum +
						'&productSkuId=' +
						specSkuId +
						'&advanceProductSkuId=' +
						skuItem.advanceProductSkuId +
						'&advanceProductId=' +
						skuItem.advanceProductId +
						'&orderType=deposit';
				}
				if (this.form.plusName == 'seckill') {
					let skuItem = this.form.detail.secKill.seckillSku.find(val => {
						return val.productSku.specSkuId == specSkuId;
					});
					console.log(skuItem);
					console.log(specSkuId);
					url =
						'pages/order/saveOrder?seckillProductId=' +
						skuItem.seckillProductId +
						'&productNum=' +
						productNum +
						'&productSkuId=' +
						skuItem.productSku.specSkuId +
						'&seckillProductSkuId=' +
						skuItem.seckillProductSkuId +
						'&orderType=seckill';
				}
			}
			this.gotoPage(url);
		},

		/*商品增加*/
		add() {
			if (this.stock <= 0) {
				return;
			}
			if (this.form.showSku.sum >= this.stock) {
				uni.showToast({
					title: '数量超过了库存',
					icon: 'none',
					duration: 2000
				});
				return false;
			}
			if (this.form.detail.limitNum > 0 && this.form.showSku.sum >= this.form.detail.limitNum) {
				uni.showToast({
					title: '数量超过了限购数量',
					icon: 'none',
					duration: 2000
				});
				return false;
			}
			this.form.showSku.sum++;
		},

		/*商品减少*/
		sub() {
			if (this.stock <= 0) {
				return;
			}
			if (this.form.showSku.sum < 2) {
				uni.showToast({
					title: '商品数量至少为1',
					icon: 'none',
					duration: 2000
				});
				return false;
			}
			this.form.showSku.sum--;
		}
	}
};
</script>

<style lang="scss">
.product-popup {
}

.product-popup .popup-bg {
	position: fixed;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	background: rgba(0, 0, 0, 0.6);
	z-index: 99;
}

.product-popup .main {
	position: fixed;
	width: 100%;
	bottom: 0;
	min-height: 200rpx;
	// max-height: 1050rpx;
	background-color: #fff;
	transform: translate3d(0, 980rpx, 0);
	transition: transform 0.2s cubic-bezier(0, 0, 0.25, 1);
	// bottom: env(safe-area-inset-bottom);
	z-index: 99;
	border-radius: 12rpx;
}

.product-popup.open .main {
	transform: translate3d(0, 0, 0);
	z-index: 99;
}

.product-popup.close .popup-bg {
	display: none;
}

.product-popup.close .main {
	display: none;
	z-index: -1;
}

.product-popup .header {
	height: 200rpx;
	padding: 40rpx 0 10rpx 250rpx;
	position: relative;
	border-bottom: 1px solid #eeeeee;
}

.product-popup .header .avt {
	position: absolute;
	top: 40rpx;
	left: 30rpx;
	width: 180rpx;
	height: 180rpx;
	border: 2px solid #ffffff;
	background: #ffffff;
	border-radius: 12rpx;
}

.product-popup .header .stock {
	font-size: 26rpx;
	color: #999999;
}

.product-popup .close-btn {
	position: absolute;
	width: 40rpx;
	height: 40rpx;
	top: 40rpx;
	right: 30rpx;
}

.product-popup .price {
	@include font_color('price_color');
	font-size: 24rpx;
}

.product-popup .price .num {
	padding: 0 4rpx;
	font-size: 40rpx;
}

.product-popup .old-price {
	margin-left: 10rpx;
	font-size: 26rpx;
	color: #999999;
	text-decoration: line-through;
}

.product-popup .body {
	padding: 20rpx 30rpx 39rpx 30rpx;
	// max-height: 600rpx;
	overflow-y: auto;
}

.product-popup .level-box {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.product-popup .level-box .key {
	font-size: 24rpx;
	color: #999999;
}

.product-popup .level-box .icon-box {
	width: 48rpx;
	height: 40rpx;
	background: #e0e0e0;
}

.product-popup .num-wrap .iconfont {
	color: #666;
}

.product-popup .num-wrap.no-stock .iconfont {
	color: #cccccc;
}

.product-popup .level-box .text-wrap {
	margin: 0 4rpx;
	height: 60rpx;
	border: none;
	background: #ffffff;
}

.product-popup .level-box .text-wrap input {
	padding: 0 10rpx;
	height: 60rpx;
	line-height: 60rpx;
	width: 80rpx;
	font-size: 32rpx;
	text-align: center;
}

.specs .specs-list {
	display: flex;
	justify-content: flex-start;
	align-items: center;
	flex-wrap: wrap;
	gap: 20rpx;
}

.specs .specs-list button {
	margin-right: 10rpx;
	margin-bottom: 10rpx;
	font-size: 24rpx;
}

.specs .specs-list button:after,
.product-popup .btns button,
.product-popup .btns button:after {
	height: 88rpx;
	line-height: 88rpx;
	border: 0;
	border-radius: 0;
	margin-bottom: 55rpx;
}

.product-popup .btns .confirm-btn {
	width: 710rpx;
	height: 80rpx;
	@include background_linearmore('cart_right1', 'cart_right2', 'right_deg', 0%, 100%);
	border-radius: 40rpx;
	margin: 0 auto;
	margin-bottom: 55rpx;
	color: #ffffff;
	line-height: 80rpx;
	font-size: 32rpx;
}

.btn-checked {
	border: 1rpx solid #f6220c;
	@include border_color('background_color');
	border-radius: 6px;
	@include font_color('font_color');
	font-size: 26rpx;
	background-color: #ffffff;
	margin: 0 !important;
}

.btn-checke {
	border: 1rpx solid #d9d9d9;
	border-radius: 6rpx;
	font-size: 26rpx;
	color: #333333;
	background-color: #ffffff;
	margin: 0 !important;
}
</style>
