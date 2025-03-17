<template>
	<view class="address-form" :data-theme='theme()' :class="theme() || ''">
		<view class="bg-white p-0-30 f30">
			<view class="d-b-c border-b info-item avatar">
				<text class="key-name">头像 </text>
				<!-- #ifndef MP-WEIXIN -->
				<view class="d-e-c" @click="changeAvatarUrl">
					<view class="info-image">
						<image :src="userInfo.avatarurl || '/static/default.png'" mode=""></image>
					</view>
					<text class="icon iconfont icon-jiantou"></text>
				</view>
				<!-- #endif -->
				<!-- #ifdef MP-WEIXIN -->
				<view class="d-e-c">
					<view class="info-image">
						<button style="padding: 0;" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
							<image :src="userInfo.avatarurl || '/static/default.png'" mode=""></image>
						</button>
					</view>
					<text class="icon iconfont icon-jiantou"></text>
				</view>
				<!-- #endif -->
			</view>
			<view class="d-b-c p-30-0 border-b">
				<text class="key-name">会员ID</text>
				<view class="d-e-c">
					<text class="mr20">{{userInfo.userId}}</text>
				</view>
			</view>
			<view class="d-b-c p-30-0 border-b">
				<text class="key-name">昵称</text>
				<view class="d-e-c" @click="changeName('nickname')">
					<text class="mr20">{{userInfo.nickname}}</text>
					<text class="icon iconfont icon-jiantou"></text>
				</view>
			</view>
			<view class="d-b-c p-30-0">
				<text class="key-name">手机号码</text>
				<view class="d-e-c" v-if="userInfo.mobile">
					<text class="mr20">{{userInfo.mobile}}</text>
				</view>
				<view class="d-e-c" v-if="!userInfo.mobile" @click="gotoBind">
					<text class="mr20">未绑定</text>
					<text class="iconfont icon-jiantou"></text>
				</view>
			</view>
			<!-- #ifdef APP-PLUS -->
			<view class="d-b-c p-30-0">
				<text class="key-name">当前版本</text>
				<view class="d-e-c">
					<text class="mr20">{{versionNo}}</text>
				</view>
			</view>
			<!-- #endif -->
			<view class="setup-btn theme-btn" @tap="logout()">退出登录</view>
		</view>
		<!-- 修改资料 -->
		<Popup :show="isPopup" type='bottom' :width="750" :padding="0" @hidePopup="hidePopupFunc">
			<form @submit="subName" class="ww100">
				<view class="d-s-s d-c p20 mpservice-wrap">
					<view class="tc f32 fb ww100">修改</view>
					<template
						v-if="type == 'mobile' || type == 'nickname' || type == 'user_name' || type == 'email' || type == 'idcard'">
						<view class="pop-input d-b-c">
							<!-- #ifdef MP-WEIXIN -->
							<input name='newName' :type="type=='nickname'?'nickname':'text'" class="flex-1"
								placeholder="请输入" :value="newName" @input="changeinput" />
							<!-- #endif -->
							<!-- #ifndef MP-WEIXIN -->
							<input :type="type=='text'" name='newName' class="flex-1" placeholder="请输入" :value="newName"
								@input="changeinput" />
							<!-- #endif -->
							<view class="icon-guanbi icon iconfont" @click="clearName"></view>
						</view>
					</template>
					<view class="d-a-c ww100">
						<button class="btn-gray-border" @click="hidePopupFunc">取消</button>
						<button class="theme-btn" form-type="submit">确认</button>
					</view>
				</view>
			</form>
		</Popup>
		<!-- 上传头像 -->
		<Upload v-if="isUpload" :num='1' @getImgs="getImgsFunc"></Upload>
	</view>
</template>

<script>
	import Upload from '@/components/upload/upload.vue';
	import Popup from '@/components/uni-popup.vue';
	export default {
		components: {
			Upload,
			Popup
		},
		data() {
			return {
				userInfo: {},
				isUpload: false,
				isPopup: false,
				newName: '',
				type: '',
				version_no: ''
			};
		},
		onShow() {
			/*获取个人中心数据*/
			this.getData();
		},
		methods: {
			onChooseAvatar(e) {
				console.log(e);
				let self = this;
				let params = {};
				params.userId = self.userInfo.userId;
				params.updateType = "avatarUrl";
				let uploadParams = {
					token: uni.getStorageSync('token'),
					appId: self.getAppId()
				};
				// 上传文件
				uni.uploadFile({
					url: self.websiteUrl + '/api/front/file/upload/image',
					filePath: e.detail.avatarUrl,
					name: 'iFile',
					formData: uploadParams,
					success: function(res) {
						let result = typeof res.data === 'object' ? res.data : JSON.parse(res.data);
						params.updateValue = result.data.filePath;
						self.update(params);
					},
					complete: function() {

					}
				});
			},
			/*获取数据*/
			getData() {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});
				self._get('user/index/setting', {}, function(res) {
					self.userInfo = res.data.userInfo;
					uni.hideLoading();
				});
				// #ifdef APP-PLUS
				plus.runtime.getProperty(plus.runtime.appid, function(widgetInfo) {
					self.version_no = widgetInfo.version;
				});
				// #endif
			},
			gotoBind() {
				this.gotoPage("/pages/user/modify-phone/modify-phone");
			},
			logout() {
				uni.removeStorageSync('token');
				uni.removeStorageSync('userId');
				this.gotoPage('/pages/index/index');
			},
			changeName(type) {
				let self = this;
				if (type == 'mobile') {
					self.oldmobile = self.userInfo.mobile;
				}
				self.type = type;
				self.newName = self.userInfo[type];
				self.isPopup = true;
			},
			hidePopupFunc() {
				this.newName = '';
				this.isPopup = false;
			},
			changeinput(e) {
				this.newName = e.detail.value;
			},
			clearName() {
				this.newName = '';
			},
			subName(e) {
				let self = this;
				if (self.loading) {
					return;
				}
				let params = {};
				this.newName = e.detail.value.newName;
				params.userId = self.userInfo.userId;
				params.updateType = self.type;
				params.updateValue = this.newName;
				self.update(params);

			},
			/* 修改头像 */
			changeAvatarUrl() {
				let self = this;
				self.isUpload = true;
			},
			/*获取上传的图片*/
			getImgsFunc(e) {
				let self = this;
				self.isUpload = false;
				if (e && typeof(e) != 'undefined') {
					let self = this;
					let params = {};
					params.userId = self.userInfo.userId;
					params.updateType = "avatarurl";
					params.updateValue = e[0].filePath;
					self.update(params);
				}
			},
			update(params) {
				let self = this;
				if (self.loading) {
					return
				}
				uni.showLoading({
					title: '加载中'
				})
				self.loading = true;
				self._post('user/user/updateInfo', params, function(res) {
					self.showSuccess('修改成功', function() {
							self.loading = false;
							self.isPopup = false;
							uni.hideLoading();
							self.getData();
						},
						function(err) {
							uni.hideLoading();
							self.loading = false;
							self.isPopup = false;
						}
					)
				});
			},
		}
	};
</script>

<style lang="scss">
	.address-form .key-name {
		width: 200rpx;
	}

	.address-form .btn-red {
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		box-shadow: 0 8rpx 16rpx 0 rgba(226, 35, 26, .6);
	}

	.setup-btn {
		position: fixed;
		bottom: 20rpx;
		left: 5%;
		width: 90%;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 80rpx;
		// background-color: #fd3826;
		color: #fff;
		font-size: 30rpx;
		display: flex;
		justify-content: center;
		margin: 0 auto;
	}

	.info-item.avatar {
		height: 162rpx;
	}

	.info-image {
		width: 112rpx;
		height: 112rpx;
		border-radius: 10rpx;
		margin-right: 20rpx;

		image {
			width: 112rpx;
			height: 112rpx;
			border-radius: 10rpx;
		}
	}

	.make-item {
		height: 60rpx;

	}

	.btn-red.code-btn {
		height: 54rpx;
		line-height: 54rpx;
	}

	.btn-red.code-btn.issend {
		background: #666666;
		border: none;
		color: #FFFFFF;
		box-shadow: none;
	}

	.pop-input {
		width: 100%;
		margin: 26rpx 0;
		box-sizing: border-box;
		border-bottom: 2rpx solid #D9D9D9;
	}

	.pop-input input {
		width: 100%;
		padding-left: 15rpx;

		font-size: 26rpx;
		color: #333333;
		margin: 16rpx 0;
		text-align: left;
		height: 60rpx;
		line-height: 60rpx;
	}

	.pop-input .icon.icon-guanbi {
		width: 38rpx;
		height: 38rpx;
		background-color: #999999;
		color: #FFFFFF;
		font-size: 22rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 50%;
		opacity: 0.8;
	}

	.sub-box {
		padding: 40rpx 0;

		.setup-btn {
			width: 686rpx;
			height: 84rpx;
			background: #E03325;
			border-radius: 6rpx;
			line-height: 84rpx;
			color: #fff;
			font-size: 30rpx;
			display: flex;
			justify-content: center;
			margin: 0 auto;
		}
	}
</style>