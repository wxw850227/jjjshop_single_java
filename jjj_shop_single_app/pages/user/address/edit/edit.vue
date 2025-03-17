<template>
	<view class="address-form">
		<form @submit="formSubmit" @reset="formReset">
			<view class="bg-white p-0-30 f30">
				<view class="d-s-c border-b-d9">
					<text class="key-name">收货人</text>
					<input class="ml20 f32 flex-1 p-30-0" name="name" type="text" v-model="address.name" placeholder-class="grary9"
					 placeholder="请输入收货人姓名" />
				</view>
				<view class="d-s-c border-b-d9">
					<text class="key-name">联系方式</text>
					<input class="ml20 f32 flex-1 p-30-0" name="phone" type="text" v-model="address.phone" placeholder-class="grary9"
					 placeholder="请输入收货人手机号" />
				</view>
				<view class="d-s-c border-b-d9">
					<text class="key-name">所在地区</text>
					<view class="input-box flex-1">
						<input class="ml20 f32 flex-1 p-30-0" type="text"   placeholder-class="grary9" placeholder="" v-model="selectCity"
						 disabled="true" @click="showMulLinkageThreePicker" />
					</view>
				</view>
				<view class="d-s-c border-b-d9">
					<text class="key-name">详细地址</text>
					<textarea class="ml20 flex-1 f32 p-30-0 lh150" name="detail" placeholder-class="grary9" :auto-height="true"
					 v-model="address.detail" placeholder="请输入街道小区楼牌号等"></textarea>
				</view>
			</view>
			<view class="p30"><button type="primary" form-type="submit" class="btn-gcred f32 mt60 addBtn">保存</button></view>
		</form>
		<mpvue-city-picker v-if="is_load" ref="mpvueCityPicker" :province="province" :city="city" :area="area" :pickerValueDefault="cityPickerValueDefault" @onConfirm="onConfirm"></mpvue-city-picker>
	</view>
</template>

<script>
	import mpvueCityPicker from '@/components/mpvue-citypicker/mpvueCityPicker.vue';
	export default {
		components: {
			mpvueCityPicker
		},
		data() {
			return {
				cityPickerValueDefault: [0, 0, 0],
				selectCity: '选择省,市,区',
				provinceId: 0,
				cityId: 0,
				regionId: 0,
				/*地址id*/
				addressId: 0,
				/*地址数据*/
				address: {},
				/*地区*/
				region: {},
				is_load: false,
				province: [],
				city: [],
				area: [],
			};
		},
		onLoad(e) {
			this.addressId = e.addressId;
		},
		mounted() {
			/*获取地址数据*/
			this.getData();
		},
		methods: {
			/*获取数据*/
			getData() {
				let self = this;
				let addressId = self.addressId;
				self._get(
					'user/address/detail', {
						addressId: addressId
					},
					function(res) {
						self.address = res.data.detail;
						self.addressId = res.data.detail.addressId;
						self.provinceId = res.data.detail.provinceId;
						self.cityId = res.data.detail.cityId;
						self.regionId = res.data.detail.regionId;
						self.region = res.data.detail.region;
						self.selectCity = self.region.province + self.region.city + self.region.region;
						self.province = res.data.regionData[0];
						self.city = res.data.regionData[1];
						self.area = res.data.regionData[2];
						self.is_load = true;
					}
				);
			},

			/*提交地址*/
			formSubmit: function(e) {
				let self = this;
				var formdata = e.detail.value;
				formdata.provinceId = self.provinceId;
				formdata.cityId = self.cityId;
				formdata.regionId = self.regionId;
				formdata.addressId = self.addressId;
				formdata.region = self.region;

				if (formdata.name == '') {
					uni.showToast({
						title: '请输入收货人姓名',
						duration: 1000,
						icon: 'none'
					});
					return false;
				}

				if (formdata.phone == '') {
					uni.showToast({
						title: '请输入手机号码',
						duration: 1000,
						icon: 'none'
					});
					return false;
				}


				if (formdata.provinceId == 0 || formdata.cityId == 0 || formdata.regionId) {
					if (formdata.detail == '') {
						uni.showToast({
							title: '请选择完整省市区',
							duration: 1000,
							icon: 'none'
						});
						return false;
					}
				}

				self._postBody('user/address/edit', formdata, function(res) {
					self.showSuccess(res.msg, function() {
						// #ifdef  H5
						uni.navigateBack({
							delta: 2
						});
						//#endif
						// #ifndef  H5
						uni.navigateBack({
							delta: 1
						});
						//#endif
					});
				});
			},

			/*清空数据*/
			formReset: function(e) {
				console.log('清空数据');
			},

			/*三级联动选择*/
			showMulLinkageThreePicker() {
				this.$refs.mpvueCityPicker.show();
			},

			/*选择之后绑定*/
			onConfirm(e) {
				this.region = e.label.split(',');
				this.selectCity = e.label;
				this.provinceId = e.cityCode[0];
				this.cityId = e.cityCode[1];
				this.regionId = e.cityCode[2];
			}
		}
	};
</script>

<style>
	page {
		background-color: #FFFFFF;
	}

	.address-form {
		border-top: 16rpx solid #f2f2f2;
	}

	.address-form .key-name {
		width: 140rpx;
		font-size: 32rpx;
	}

	.address-form .btn-red {
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		box-shadow: 0 8rpx 16rpx 0 rgba(226, 35, 26, .6);
	}

	.addBtn {
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
	}
</style>
