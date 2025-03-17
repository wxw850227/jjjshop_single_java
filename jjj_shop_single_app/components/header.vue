<template>
	<view class="pb10 theme-bg">
		<view class="status_bar"></view>
		<view class="top_view"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		props: ['bcolor'],
		watch:{
			'bcolor':function(n,o){
				if(n!=o){
					console.log(n)
				}
			}
		}	
	}
</script>

<style>
	.s{
		flex-shrink: 0;
	}
</style>
