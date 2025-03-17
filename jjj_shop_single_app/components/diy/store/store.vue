<template>
	<view class="diy-store" :style="{ background: itemData.style.background }">
		<view class="diy-head d-b-c">
			<view class="left d-s-c">
				<view class="name">
					线下门店
				</view>
			</view>
		</view>
		<view class="store-item d-s-c p-30-0" :key="index" v-for="(store, index) in itemData.data">
			<view class="store-item-logo mr30"><image :src="store.logoImage" mode="aspectFill"></image></view>
			<view class="flex-1 o-h">
				<view class="ww100 f34 gray3 text-ellipsis">
					<text>{{ store.storeName }}</text>
				</view>
				<view class="text-ellipsis mt10 f24 gray3"><text class="gray3">门店地址：</text><text class="gray6">{{ store.province }}{{ store.city }}{{ store.region }}{{ store.address }}</text></view>
				<view class="mt10 f24">
					<text class="gray3">联系电话：</text><text class="gray6">{{ store.phone }}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	},
	props: ['itemData'],
	methods: {
		
	}
};
</script>

<style>
.diy-store {
	margin: 20rpx;
	border-radius: 6rpx;
	padding: 0 20rpx 20rpx;
	background: #ffffff;
	box-shadow: 0px 8rpx 3rpx 0px rgba(6, 0, 1, 0.03);
}
.diy-store .diy-head {
	height: 100rpx;
}
.diy-store .diy-head .name {
	font-size: 36rpx;
	font-weight: bold;
}
.diy-store .store-item {
	border-top: 1rpx solid #eeeeee;
}
.diy-store .store-item-logo,
.diy-store .store-item-logo image {
	width: 120rpx;
	height: 120rpx;
}
</style>
