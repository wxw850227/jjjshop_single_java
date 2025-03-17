<template>
	<view class="diy-notice" :style="{ padding: itemData.style.paddingTop + 'px' + ' 10px', background: itemData.style.background }" @click="gotoPages(item)">
		<view class="notice-icon"><image :src="itemData.params.icon" mode="aspectFill"></image></view>
		<view class="notice-text flex-1 text-ellipsisss">
			<view class="transtext" :style="'color:'+itemData.style.textColor+';transform:translateX('+n+'%)'">{{ textData }}</view>
			<!-- <view class="transtext" :style="'color:'+itemData.style.textColor+';transform:translateX('+n+'%)'">{{ textData }}</view> -->
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			textData:'',
			n:0
		};
	},
	props: ['itemData'],
	created() {
		this.textData=this.itemData.params.text;
		this.horseRaceLamp();
	},
	methods: {
		
		horseRaceLamp(){
			let self=this;
			setTimeout(function(){
				self.n--;
				if(self.n <=-100){
					self.n = 100
				}
				// let firstWord=self.textData.slice(0,1);
				// let afterWord=self.textData.slice(1,self.textData.length);
				// self.textData=afterWord+firstWord;
				self.horseRaceLamp();
				
			},100);
		},
		
		/*跳转页面*/
		gotoPages(e) {
			this.gotoPage(e.linkUrl);
		}
	}
};
</script>

<style>
.diy-notice {
	margin: 20rpx;
	display: flex;
	justify-content: flex-start;
	align-items: center;
	border-radius: 6rpx;
	/* box-shadow: 0 0 8rpx rgba(0, 0, 0, 0.1); */

}
.diy-notice .notice-icon {
	width: 64rpx;
	height: 64rpx;
	margin-right: 20rpx;
}
.diy-notice .notice-icon image {
	width: 100%;
	height: 100%;
}
.diy-notice .notice-text {
	overflow: hidden;
	white-space: nowrap;
	
}
.transtext{
	min-width: 586rpx;
	flex-shrink: 0;
	white-space: nowrap;
	float: left;
}
</style>
