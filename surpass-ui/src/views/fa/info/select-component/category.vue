<template>
  <div class="table-select-input">
    <el-input :model-value="inputValue" readonly>
      <template #append>
        <el-button @click="handleOpenDialog" :disabled="isConvert">选择</el-button>
      </template>
    </el-input>

    <el-dialog
        v-model="visible"
        title="选择线索"
        width="1200px"
        @close="handleClose">
      <div class="app-container">
        <el-card class="common-card">
          <div class="queryForm">
            <el-form :model="queryParams" :inline="true" label-width="68px" >
              <el-form-item label="分类编码">
                <el-input v-model="queryParams.categoryCode"/>
              </el-form-item>
              <el-form-item label="分类名称">
                <el-input v-model="queryParams.categoryName"/>
              </el-form-item>
              <el-form-item>
                <el-button @click="handleQuery">查询</el-button>
                <el-button @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
        <el-card class="common-card">
          <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" v-if="multiSelect"></el-table-column>
            <!-- 单选 -->
            <el-table-column width="45" align="center" :show-overflow-tooltip="true" v-else>
              <template #default="{row}">
                <el-radio v-model="tempSingleSelectId" :value="row.id"></el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="categoryCode" label="分类编码" align="center" min-width="100"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="categoryName" label="分类名称" align="center" min-width="100"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="depreciationSubjectName" label="折旧方法" align="center" min-width="60"
                             :show-overflow-tooltip="true">
              <template #default="scope">
                <dict-tag-number :options="depreciation_method" :value="scope.row.depreciationMethod"/>
              </template>
            </el-table-column>
            <el-table-column prop="depreciationYears" label="预计使用年限" align="center" min-width="70"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="residualRate" label="残值率" align="center" min-width="50"
                             :show-overflow-tooltip="true">
              <template #default="scope">
                <span>{{scope.row.residualRate}}%</span>
              </template>
            </el-table-column>
            <el-table-column prop="assetSubjectName" label="固定资产科目" align="center" min-width="100"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="depreciationSubjectName" label="累计折旧科目" align="center" min-width="100"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="expenseSubjectName" label="折旧费用科目" align="center" min-width="100"
                             :show-overflow-tooltip="true">
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
import {computed, getCurrentInstance, reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import {fetchPage} from "@/api/fa/category";
import {formatAmount} from "@/utils";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {depreciation_method}
    = proxy?.useDict("depreciation_method");

const props = defineProps({
  modelValue: [String, Array],
  multiSelect: {
    type: Boolean,
    default: false
  },
  isConvert: {
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
  }
})


const dataList = ref<any>([])
const loading = ref(true)
const total = ref(0)
const visible = ref(false)
const singleSelectId = ref<string | undefined>()
const selectedIds = ref<string[]>([])
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
  }).map((item: any) => item.categoryName).join(',')
})

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.categoryCode = undefined;
  queryParams.value.categoryName = undefined;
  handleQuery()
}

function getList() {
  loading.value = true
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

function handleClose() {
  visible.value = false
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

function handleSelectionChange(selection: any) {
  tempSelectedIds.value = selection.map((item: any) => item.id)
}

// 处理打开对话框
function handleOpenDialog() {
  visible.value = true
  // 复制当前已选项到临时变量
  tempSelectedIds.value = [...selectedIds.value]
  tempSingleSelectId.value = singleSelectId.value
  getList()
}

getList()
</script>

<style scoped lang="scss">
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

.common-card {
  margin-bottom: 10px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

:deep(.pagination-container .el-pagination) {
  right: 5%;
}

</style>
