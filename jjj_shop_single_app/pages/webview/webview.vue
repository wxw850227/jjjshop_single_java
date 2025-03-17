<template>
	<view>
		<web-view :src="url" @onPostMessage="handlePostMessage"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				url: ''
			}
		},
		onLoad(e) {
			this.url = e.url;
		},
		methods: {
			// webview向外部发送消息
			handlePostMessage: function(data) {
				console.log("接收到消息：" + JSON.stringify(data.detail));
			},
		}
	}
</script>

<style>

</style>