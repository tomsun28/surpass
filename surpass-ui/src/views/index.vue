<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
      </el-col>
      <el-col :xs="24" :sm="24" :md="16" :lg="12" :xl="12">
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
      </el-col>
      <el-col :span="24">
      </el-col>
    </el-row>
    <Footer position="relative"></Footer>
  </div>
</template>


<script setup name="Index" lang="ts">
import {ref, getCurrentInstance} from "vue";
import {useI18n} from 'vue-i18n'
import {useRoute, useRouter} from "vue-router";
import Footer from "@/components/Footer/index.vue"
const {t} = useI18n()
const route: any = useRoute();
const router: any = useRouter();
const {proxy} = getCurrentInstance()!;


</script>
<style scoped lang="scss">
:deep(.el-card) {
  --el-card-border-radius: 10px;
}

.el-col {
  margin-bottom: 20px;
}
</style>
