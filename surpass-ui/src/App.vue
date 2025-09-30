<template>
  <el-config-provider :locale="locale">
    <RouterView/>
  </el-config-provider>
</template>

<script setup lang="ts">
import useSettingsStore from '@/store/modules/settings'
import {handleThemeStyle} from '@/utils/Theme'
import {ElConfigProvider} from 'element-plus'
import {onMounted, nextTick, ref, computed} from "vue"
import elZhCN from 'element-plus/es/locale/lang/zh-cn'
import elZhTW from 'element-plus/es/locale/lang/zh-tw'
import elEnUS from 'element-plus/es/locale/lang/en'
import {getLocale} from "@/languages"

const language = ref('zh-CN')
language.value = getLocale()
const locale = computed(() => (language.value === 'zh-CN' ? elZhCN : language.value === 'en-US' ? elEnUS : elZhTW))

onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
  })
})
</script>
