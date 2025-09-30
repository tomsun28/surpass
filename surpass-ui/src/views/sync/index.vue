<template>
   <div class="app-container">
     <el-tabs v-model="activeName" class="datya-tabs">
       <el-tab-pane label="钉钉" name="dingding">
         <component-dingding v-if="activeName == 'dingding'" />
       </el-tab-pane>
       <el-tab-pane label="飞书" name="feishu">
         <component-feishu v-if="activeName == 'feishu'" />
       </el-tab-pane>
       <el-tab-pane label="企业微信" name="workweixin">
         <component-workweixin v-if="activeName == 'workweixin'" />
       </el-tab-pane>
     </el-tabs>
   </div>
</template>

<script setup name="Sync-Jobs" lang="ts">
import modal from "@/plugins/modal";

import ComponentDingding from "@/views/sync/components/dingding.vue"
import ComponentFeishu from "@/views/sync/components/feishu.vue"
import ComponentWorkweixin from "@/views/sync/components/workweixin.vue"
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";

const activeName: any = ref('dingding')

</script>
