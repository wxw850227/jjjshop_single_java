<template>
  <div class="common-seach-wrap">
    <el-tabs v-model="activeName">
      <el-tab-pane label="等级列表" name="list"></el-tab-pane>
      <el-tab-pane label="升级日志" name="log"></el-tab-pane>
    </el-tabs>

    <!--积分设置-->
    <List v-if="activeName == 'list'"></List>
    <!--积分明细-->
    <Log v-if="activeName == 'log'"></Log>

  </div>
</template>
<script>
import List from './part/list.vue';
import Log from './part/log.vue';
export default {
  components: {
    List,
    Log
  },
  data() {
    return {
      activeName: 'list'
    };
  },
  methods: {}
};
</script>

<style>

</style>
