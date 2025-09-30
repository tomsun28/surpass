<template>
  <div class="queryForm">
    <el-form :model="stgParams" ref="queryForm" :inline="true" @submit.native.prevent>
      <el-form-item :label="$t('jbx.accountsstrategy.name')">
        <el-input
            v-model="stgParams.name"
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
                :data="stgList"
                class="stgTable"
                ref="stgTable"
                @select="handleSelectionChangeStg"
      >
        <el-table-column type="selection" width="50" align="center">
        </el-table-column>
        <el-table-column :label="$t('jbx.accountsstrategy.name')" align="center" prop="name"/>
        <el-table-column :label="$t('jbx.accountsstrategy.appIcon')" align="center" prop="appIcon">
          <template #default="scope">
            <el-image :src="scope.row.appIcon" style="width: 50px"></el-image>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.accountsstrategy.appName')" align="center" prop="appName"/>
        <el-table-column :label="$t('jbx.accountsstrategy.createType')" align="center" prop="createType">
          <template #default="scope">
            <dict-tag :options="create_type" :value="scope.row.createType"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.history.loginMessage')" align="center" prop="status">
          <template #default="scope">
            <span v-if="scope.row.status == '1'">{{t('jbx.text.status.active')}}</span>
            <span v-else-if="scope.row.status == '0'">{{t('jbx.text.status.inactive')}}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total"
                  v-model:page="stgParams.pageNumber"
                  v-model:limit="stgParams.pageSize"
                  :page-sizes="stgParams.pageSizeOptions"
                  @pagination="getList"/>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {accountsstrategy} from "@/api/system/config";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {create_type} = proxy.useDict("create_type")

const emit: any = defineEmits(['updateFlag', 'inputStgValue'])

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
  stgParams: {
    appId: props.appId,
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 50]
  }
})
const {stgParams} = toRefs(data)
const loading: any = ref(true);
const stgList: any = ref<any>([]);
const total: any = ref(0);

const selectedRows: any = ref<any>([])
const selectObject: any = ref<any>({})

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        //获取用户列表
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);

function reset(): any {
  stgParams.value = {
    appId: props.appId,
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 50]
  }
  selectedRows.value = [];
  selectObject.value = {};
  loading.value = true;
}

function getList(): any {
  accountsstrategy(stgParams.value).then((res: any) =>  {
    stgList.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  })
}

function handleQuery(): any {
  stgParams.value.pageNumber = 1;
  getList();
}

//重置
function resetQuery(): any {
  stgParams.value.name = undefined;
  handleQuery();
}

/**
 * 账号单选
 */
function handleSelectionChangeStg(selection: any, row: any): any {
  proxy.$refs.stgTable.clearSelection()
  if (selection.length === 0) { // 判断selection是否有值存在
    selectObject.value = {};
    emit('updateFlag', true);
    return
  }

  if (row) {
    selectObject.value = row;
    proxy.$refs.stgTable.toggleRowSelection(row, true)
    emit('updateFlag', false);
  }
}


function confirmSelect(): any {
  emit('inputStgValue', selectObject.value)
}

defineExpose({confirmSelect})
</script>

<style lang="scss" scoped>
.stgTable :deep(.el-table__header-wrapper  .el-checkbox) {
  display:none
}

</style>
