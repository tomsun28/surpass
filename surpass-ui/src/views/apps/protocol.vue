<template>
  <el-table :data="data">
    <el-table-column :label="$t('jbx.apps.icon')" align="center" prop="icon" width="80">
      <template #default="scope">
        <el-image :src="scope.row.icon" style="width: 36px;height: 36px;"></el-image>
      </template>
    </el-table-column>
    <el-table-column :label="$t('jbx.apps.protocol')" align="center" prop="protocol" width="130"/>
    <el-table-column :label="$t('jbx.text.description')" prop="desc"/>
    <el-table-column :label="$t('jbx.history.systemlogsMessageaction')" align="center"
                     class-name="small-padding fixed-width" width="100">
      <template #default="scope">
        <el-button
            type="primary"
            @click="handleProtocolAdd(scope.row)"
        >{{ $t('jbx.text.add') }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
export default {
  name: "Protocol",
  emits: ["click"],
  props: {
    data: Array,

  },
  methods: {
    handleProtocolAdd(row){
      this.$emit("click", row)
    },
  }
}
</script>

<style scoped lang="scss">

</style>