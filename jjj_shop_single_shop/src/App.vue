<template>
	<el-config-provider :locale="locale">
		<router-view />
	</el-config-provider>
</template>
<script>
import {
	reactive,
	toRefs,
	defineComponent
} from 'vue';
import {
	ElConfigProvider
} from 'element-plus';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
export default defineComponent({
	components: {
		[ElConfigProvider.name]: ElConfigProvider,
	},
	setup() {
		let locale = zhCn;
		const state = reactive({});
		return {
			...toRefs(state),
			locale,
		};
	}
});
</script>
<style lang="scss">
	@use '@/assets/font/iconfont.css';
	// @use '@/assets/font/myIcon.css';
	@use '@/styles/diy.scss';

	* {
		margin: 0;
		padding: 0;
	}
</style>