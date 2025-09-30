<template>
  <div class="queryForm">
    <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent>
      <el-form-item :label="$t('jbx.accounts.username')">
        <el-input
            v-model="queryParams.username"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">{{ $t('jbx.text.query') }}
        </el-button>
        <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-table border
            v-loading="loading"
            :data="accountList"
            class="accountTable"
            ref="accountTable"
            @select="handleSelectionChange"
  >
    <el-table-column type="selection" width="50" align="center">
    </el-table-column>
    <el-table-column prop="username" :label="$t('jbx.users.username')" align="center"
                     min-width="100" :show-overflow-tooltip="true"/>
    <el-table-column prop="displayName" :label="$t('jbx.users.displayName')" align="center"
                     min-width="120" :show-overflow-tooltip="true"/>
    <el-table-column prop="email" :label="$t('jbx.users.email')" align="center"
                     min-width="120" :show-overflow-tooltip="true"/>
    <el-table-column prop="phoneNumber" :label="$t('jbx.organizations.phone')" align="center"
                     min-width="100" :show-overflow-tooltip="true"/>
    <el-table-column prop="department" :label="$t('jbx.users.department')" align="center"
                     min-width="100" :show-overflow-tooltip="true"/>
  </el-table>
  <pagination v-show="total > 0" :total="total"
              v-model:page="queryParams.pageNumber"
              v-model:limit="queryParams.pageSize"
              :page-sizes="queryParams.pageSizeOptions"
              @pagination="getList"/>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {accountsUnitList} from "@/api/system/account";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const emit: any = defineEmits(['updateFlag', 'inputAccountValue'])
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  appId: {
    default: undefined
  },
});
const data: any = reactive({
  queryParams: {
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 50]
  }
})
const {queryParams} = toRefs(data)
const loading: any = ref(true);
const accountList: any = ref<any>([]);
const total: any = ref(0);

const selectedRows: any = ref<any>([])
const selectObject: any = ref<any>({})

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        queryParams.value.appId = props.appId;
        //获取用户列表
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);


function reset(): any {
  queryParams.value = {
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 50]
  }
  selectedRows.value = [];
  selectObject.value = {};
  loading.value = true;
}

function getList(): any {
  accountsUnitList(queryParams.value).then((res: any) =>  {
    accountList.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  })
}

function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

//重置
function resetQuery(): any {
  queryParams.value.username = undefined;
  handleQuery();
}

/**
 * 账号单选
 */
function handleSelectionChange(selection: any, row: any): any {
  proxy.$refs.accountTable.clearSelection()
  if (selection.length === 0) { // 判断selection是否有值存在
    selectObject.value = {};
    emit('updateFlag', true);
    return
  }

  if (row) {
    selectObject.value = row;
    proxy.$refs.accountTable.toggleRowSelection(row, true)
    emit('updateFlag', false);
  }
}

function confirmSelect(): any {
  emit('inputAccountValue', selectObject.value)
}

defineExpose({confirmSelect})
</script>

<style lang="scss" scoped>
.accountTable :deep(.el-table__header-wrapper  .el-checkbox) {
  display:none
}

</style>
