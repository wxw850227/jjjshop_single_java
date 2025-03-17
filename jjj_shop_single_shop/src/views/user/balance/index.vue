<template>
  <div class="common-seach-wrap">
    <el-tabs v-model="activeName">
      <el-tab-pane label="余额明细" name="log"></el-tab-pane>
    </el-tabs>

    <!--余额明细-->
    <Log v-if="activeName == 'log'"></Log>

  </div>
</template>
<script>
import Log from './part/Log.vue';
export default {
  components: {
    Log
  },
  data() {
    return {
      form: {
        status: 0
      },
      activeName: 'log'
    };
  },
  methods: {}
};
</script>

<style>

</style>
