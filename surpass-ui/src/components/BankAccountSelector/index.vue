<template>
  <div style="width: 100%">
    <el-input
        :model-value="selectedAccount?.accName || ''"
        placeholder="请选择账户"
        readonly
        @click="openDialog"
    >
      <template #append>
        <el-button icon="Search" @click="openDialog"></el-button>
      </template>
    </el-input>

    <el-dialog v-model="dialogVisible" title="选择账户" width="800px" @closed="onDialogClosed">
      <el-table
          :data="list"
          v-loading="loading"
          border
          highlight-current-row
          @current-change="handleRowClick"
      >
        <el-table-column prop="accName" label="账户名称"/>
        <el-table-column prop="accCode" label="账户编码"/>
        <el-table-column prop="category" label="类型"/>
        <el-table-column prop="currency" label="币种"/>
        <el-table-column prop="balance" label="余额" :formatter="(r,c,v) => formatAmount(v)" align="right"/>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :disabled="!selectedRow" @click="confirmSelection">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, watch, defineEmits, defineProps} from 'vue'
import {fetch} from '@/api/journal/journalaccountservice'
import {formatAmount} from '@/utils'

const props = defineProps<{
  modelValue: string | number | undefined
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: any): void
  (e: 'change', account: any): void
}>()

const dialogVisible = ref(false)
const list = ref<any[]>([])
const loading = ref(false)
const selectedRow = ref<any>(null)
const selectedAccount = ref<any>(null)

watch(() => props.modelValue, (val) => {
  if (!val) {
    selectedAccount.value = null
  }
}, {immediate: true})

function openDialog() {
  dialogVisible.value = true
  getList()
}

function getList() {
  loading.value = true
  fetch({pageNumber: 1, pageSize: 100}).then(res => {
    loading.value = false
    if (res.code === 0) {
      list.value = res.data.records
      if (props.modelValue) {
        selectedAccount.value = res.data.records.find((r: any) => r.id === props.modelValue)
      }
    }
  })
}

function handleRowClick(row: any) {
  selectedRow.value = row
}

function confirmSelection() {
  selectedAccount.value = selectedRow.value
  emit('update:modelValue', selectedRow.value.id)
  emit('change', selectedRow.value)
  dialogVisible.value = false
}

function onDialogClosed() {
  selectedRow.value = null
}
</script>
