<template>
	<view class="refund-apply pb100">
		<form @submit="formSubmit" @reset="formReset">
			<view class="one-product d-s-c p30 bg-white ">
				<view class="cover">
					<image :src="product.productImage" mode="aspectFit"></image>
				</view>
				<view class="flex-1">
					<view class="pro-info">{{product.productName}}</view>
					<view class="pt10 p-0-30 f24 gray9">
						<text class="">
							单价：¥{{product.linePrice}}
						</text>
						<text class="ml20">
							数量：{{product.totalNum}}
						</text>
					</view>
				</view>
			</view>

			<!-- 服务类型 -->
			<view class="group bg-white"  v-if="product && product.orderM && product.orderM.deliveryType!=30">
				<view class="group-hd border-b-e">
					<view class="left">
						<text class="min-name">服务类型</text>
					</view>
				</view>
				<view class="d-s-c p-20-0">
					<button type="default"
						:class="type==10?'btn-red-border':''" @click="tabType(10)">退货/退款</button>
					<button type="default"
						:class="type==20?'ml20 btn-red-border':'ml20'" @click="tabType(20)">换货</button>
					<button type="default"
						:class="type==30?'ml20 btn-red-border':'ml20'" @click="tabType(30)">仅退款</button>
				</view>
			</view>

			<!--申请原因-->
			<view class="group bg-white">
				<view class="group-hd">
					<view class="left">
						<text class="min-name">申请原因</text>
					</view>
				</view>
				<view class="d-s-c">
					<textarea class="p10 box-s-b border flex-1 f28 lh150"   name="content"
						placeholder="请详细填写申请原因，注意保持商品的完好，建议您先与卖家沟通" />
				</view>
			</view>

			<!--退款金额-->
			<view class="group bg-white" v-if="type==10 || type==30">
				<view class="group-hd">
					<view class="left">
						<text class="min-name">退款金额：</text>
						<text class="red f30">¥{{product.totalPayPrice}}</text>
					</view>
				</view>
			</view>

			<!--上传图片-->
			<view class="group bg-white">
				<view class="group-hd">
					<view class="left">
						<text class="min-name">上传凭证</text>
						<text class="gray9">(最多6张)</text>
					</view>
				</view>
				<view class="upload-list d-s-c">
					<view class="item" v-for="(imgs,img_num) in images" :key="img_num" @click="deleteFunc(imgs)">
						<image :src="imgs.filePath" mode="aspectFit"></image>
					</view>
					<view class="item d-c-c d-stretch" v-if="images.length<6">
						<view class="upload-btn d-c-c d-c flex-1" @click="openUpload()">
							<text class="icon iconfont icon-xiangji f34"></text>
							<text class="gray9">上传图片</text>
						</view>
					</view>
				</view>
			</view>

			<view class="foot-btns">
				<button form-type="submit" class="btn-red">确认提交</button>
				<!--<button type="primary" >确认提交</button>-->
			</view>
		</form>
		<!--上传图片-->
		<Upload v-if="isUpload" @getImgs="getImgsFunc"></Upload>
	</view>


</template>

<script>
	import Upload from '@/components/upload/upload.vue';
	export default {
		components: {
			Upload
		},
		data() {
			return {
				/*是否加载完成*/
				loadding: true,
				indicatorDots: true,
				autoplay: true,
				interval: 2000,
				duration: 500,
				type: 10,
				/*是否打开上传图片*/
				isUpload: false,
				/*订单商品id*/
				orderProductId: 0,
				/*订单商品*/
				product: {},
				images: [],
				/*小程序订阅消息*/
				temlIds: [],
			}
		},
		onLoad(e) {
      this.orderProductId = e.orderProductId;
      this.getData();
    },
		mounted() {
			/*获取订单详情*/
			this.getData();
		},
		methods: {
			/*获取数据*/
			getData() {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});
				let orderProductId = self.orderProductId;
				self._get('user/refund/toApply', {
					orderProductId: orderProductId,
					platform: self.getPlatform()
				}, function(res) {
					self.product = res.data;
					self.temlIds = res.data.template_arr;
          console.log("deliveryType1",self.product.orderM.deliveryType);
					if (self.product.orderM.deliveryType == 30) {
						self.type = 30;
					}
					uni.hideLoading();
				});
			},
			/*切换服务类型*/
			tabType(e) {
				this.type = e;
			},

			/*提交表单*/
			formSubmit: function(e) {
				let self = this;
				var formdata = e.detail.value;
				formdata.type = self.type;
				formdata.orderProductId = self.orderProductId;
				formdata.images = self.images;
				let callback = function() {
					uni.showLoading({
						title: '正在提交',
						mask: true
					});
					self._postBody('user/refund/apply', formdata, function(res) {
						uni.hideLoading();
						uni.showToast({
							title: res.msg,
							duration: 3000,
							complete: function() {
								self.gotoPage('/pages/order/refund/index/index');
							}
						});
					});
				}
				self.subMessage(self.temlIds, callback);
			},

			/*打开上传图片*/
			openUpload() {
				this.isUpload = true;
			},

			/*获取上传的图片*/
			getImgsFunc(e) {
				let self = this;
				self.isUpload = false;
				if (e && typeof(e) != 'undefined') {
					let this_length = self.images.length,
						upload_length = e.length;
					if (this_length + upload_length < 7) {
						self.images = self.images.concat(e);
					} else {
						let leng = 6 - this_length;
						for (let i = 0; i < leng; i++) {
							self.images.push(e[i]);
						}
					}
				}
			},

			/*删除图片*/
			deleteFunc(e) {
				this.images.splice(e, 1);
			}
		}
	}
</script>

<style>

</style>
