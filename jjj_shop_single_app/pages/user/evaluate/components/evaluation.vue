<template>
    <view class="listItem">
        <view class="time">
            <span class="data">31</span>
            <span class="line"></span>
            <span class="mouth">03<span class="unit">月</span></span>
            <span class="line"></span>
            <span class="year">2022<span class="unit">年</span></span>
        </view>
        <view class="con">
            知识来源于生活。所以说，勤于观察和思考往往比创造发明更重要。没有知识，又何谈发明呢？我们要向……学习，细心留心观察身边的奥秘。
        </view>
        <view class="prodcut">
            <image src="https://qn-cdn.jjjshop.net/20230407112243488.PNG"></image>
            <view class="r">
                <view class="title">DOLL原创蝴蝶结吊带背心</view>
                <view class="spec">杏色[现货]M</view>
            </view>
        </view>
    </view>
</template>

<script>
	export default {
		data() {
			return {
				loadding: true,
				state_active: 0,
			}
		},
		onLoad: function(options) {},
		onShow: function() {},
		methods: {
            gotoEvaluate(e) {
                this.gotoPage('/pages/order/evaluate/evaluate?orderId=' + e);
            },
		}
	}
</script>

<style lang="scss">
.listItem{
    background-color: #fff;
    .time{
        .unit{
            color:rgba(0,0,0,0.9);
            font-weight: 400;
        }
        span{
            font-size: 26rpx;
            color: #000000;
            font-weight: 800;
        }
        .data{
            font-size: 38rpx;
        }
        .line{
            width: 4rpx;
            height: 22rpx;
            background-color: #FD6A03;
            display: inline-block;
            margin: 0 6rpx;
            transform: rotate(18deg);
        }
    }
    .con{
        letter-spacing: 1rpx;
        line-height: 42rpx;
        margin: 60rpx 0 30rpx 0;
    }
    .prodcut{
        display: flex;
        align-items: center;
        background: rgba(235,235,235,0.8);
        font-size: 26rpx;
        image{
            width: 90rpx;
            height: 90rpx;
            margin-right: 6rpx;
            border-radius: 6rpx;
        }
        .spec{
            color: #959595;
            font-size: 24rpx;
            margin-top: 6rpx;
        }
    }
}
</style>
