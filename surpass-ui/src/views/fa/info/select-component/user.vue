<template>
  <div class="table-select-input">
    <el-input :model-value="inputValue" readonly>
      <template #append>
        <el-button @click="handleOpenDialog">选择</el-button>
      </template>
    </el-input>

    <el-dialog
        v-model="visible"
        title="选择负责人"
        width="1200px"

        @close="handleClose">
      <div class="app-container">
        <el-card class="common-card">
          <el-form :inline="true" label-width="80px">
            <el-form-item :label="$t('jbx.users.username')">
              <el-input
                  v-model="queryParams.username"
                  clearable
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">查询</el-button>
              <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="common-card">
          <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
            <!-- 多选 -->
            <el-table-column
                type="selection"
                width="55"
                align="center"
                v-if="multiSelect"
            />
            <!-- 单选 -->
            <el-table-column width="45" align="center" :show-overflow-tooltip="true" v-else>
              <template #default="{row}">
                <el-radio v-model="tempSingleSelectId" :value="row.id"></el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="username" :label="$t('jbx.users.username')" align="center"
                             min-width="100" :show-overflow-tooltip="true"/>
            <el-table-column prop="displayName" :label="$t('jbx.users.displayName')" align="center"
                             min-width="120" :show-overflow-tooltip="true"/>
            <el-table-column prop="gender" :label="t('jbx.users.gender')" align="center" min-width="60">
              <template #default="scope">
                <span v-if="scope.row.gender === 1">{{ t('jbx.users.genderFemale') }}</span>
                <span v-if="scope.row.gender === 2">{{ t('jbx.users.genderMale') }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" :label="$t('jbx.users.status')" align="center"
                             min-width="60">
              <template #default="scope">
                <a :title="$t('jbx.users.statusActive')" v-if="scope.row.status === 1">
                  <el-icon color="green">
                    <SuccessFilled
                        class="success"/>
                  </el-icon>
                </a>
                <a :title="$t('jbx.users.statusInactive')" v-if="scope.row.status === 2">
                  <el-icon color="grey">
                    <WarningFilled/>
                  </el-icon>
                </a>
                <a :title="$t('jbx.users.statusForbidden')" v-if="scope.row.status === 4">
                  <el-icon color="grey">
                    <RemoveFilled/>
                  </el-icon>
                </a>
                <a :title="$t('jbx.users.statusLock')" v-if="scope.row.status === 5">
                  <el-icon color="orange">
                    <Lock/>
                  </el-icon>
                </a>
                <a :title="$t('jbx.users.statusDelete')" v-if="scope.row.status === 9">
                  <el-icon color="red">
                    <CircleCloseFilled/>
                  </el-icon>
                </a>
              </template>
            </el-table-column>
          </el-table>
          <pagination
              v-show="total > 0"
              :total="total"
              v-model:page="queryParams.pageNumber"
              v-model:limit="queryParams.pageSize"
              :page-sizes="queryParams.pageSizeOptions"
              @pagination="getList"
          />
        </el-card>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">

import {reactive, watch, computed, ref, getCurrentInstance, toRefs} from "vue";
import {listUsers} from "@/api/system/user";
import {RemoveFilled} from "@element-plus/icons-vue";
import {useI18n} from 'vue-i18n'

const {t} = useI18n()

const props = defineProps({
  modelValue: [String, Array],
  multiSelect: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'select'])

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  form: {}
})

const dataList = ref<any>([])
const loading = ref(true)
const total = ref(0)
const visible = ref(false)
const singleSelectId = ref<string | undefined>()
const selectedIds = ref<string[]>([])
const {proxy} = getCurrentInstance()!;
const {queryParams} = toRefs(data)
const tempSelectedIds = ref<string[]>([])
const tempSingleSelectId = ref<string | undefined>()


// 监听 modelValue 的变化并更新 inputValue
watch(() => props.modelValue, (newValue) => {
  if (newValue !== inputValue.value) {
    if (props.multiSelect) {
      selectedIds.value = newValue as string[]
    } else {
      singleSelectId.value = newValue as string
    }
  }
})
// 输入框的值，与 modelValue 同步
const inputValue = computed(() => {
  const ids = props.multiSelect ? selectedIds.value : [singleSelectId.value]
  return dataList.value.filter((item: any) => {
    return ids.includes(item.id)
  }).map((item: any) => item.displayName).join(',')
})

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.username = undefined
  handleQuery()
}

function getList() {
  loading.value = true
  listUsers(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      dataList.value = res.data.records
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

// 处理打开对话框
function handleOpenDialog() {
  visible.value = true
  // 复制当前已选项到临时变量
  tempSelectedIds.value = [...selectedIds.value]
  tempSingleSelectId.value = singleSelectId.value
  getList()
}

function handleClose() {
  visible.value = false
}

function handleSelectionChange(selection: any) {
  tempSelectedIds.value = selection.map((item: any) => item.id)
}

function handleSubmit() {
  // 把临时值同步到真正的选中值中
  if (props.multiSelect) {
    selectedIds.value = [...tempSelectedIds.value]
  } else {
    singleSelectId.value = tempSingleSelectId.value
  }

  const selectedValue: string | string[] = props.multiSelect
      ? selectedIds.value
      : singleSelectId.value || ''

  emit('update:modelValue', selectedValue)
  emit('select', selectedValue, inputValue.value)
  handleClose()
}

getList()
</script>

<style scoped>
.table-select-input {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-dialog) {
  border-top-left-radius: 0 !important;
  border-top-right-radius: 0 !important;
  /* 可选 - 去掉 margin 顶部空隙 */
  margin-top: 0 !important;
  top: 0 !important;
  transform: none !important;
}

:deep(.pagination-container .el-pagination) {
  right: 5%;
}
</style>

