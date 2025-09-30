<template>
  <div @click="click">
    <svg-icon icon-class="tool"/>
    <span style="margin-left: 5px">清理本地缓存</span>
  </div>
</template>

<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import modal from "@/plugins/modal";
export default {
  name: 'CleanSession',
  methods: {
    click() {
      this.$confirm('确定要清理本地缓存数据吗？', this.$t('systemNotice'), {
        confirmButtonText: this.$t('jbx.text.confirm'),
        cancelButtonText: this.$t('systemCancel'),
        type: 'warning'
      }).then(() => {
        window.localStorage.clear()
        window.location.reload()
      }).catch(() => {
      });
    },
  }
}
</script>

<style scoped>
.tool2 {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
