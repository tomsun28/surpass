<template>
    <div class="queryForm">
      <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent>
        <el-form-item label="角色名称">
          <el-input
              v-model="queryParams.roleName"
              clearable
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
          <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table v-loading="loading" :data="groupNoList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"/>
      <el-table-column prop="roleName" label="角色名称" min-width="100" align="center"/>
      <el-table-column prop="category" label="角色类型" align="center" min-width="80">
        <template #default="scope">
          <dict-tag :options="group_category_options" :value="scope.row.category"/>
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
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch,
  PropType
} from "vue";
import {addNoUserGroup, listNoUserGroup} from "@/api/system/group";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const emit: any = defineEmits(['onSubmitSuccess'])

const props: any = defineProps({
  memberOpen: {
    type: Boolean,
    default: false
  },
  username: {
    type: String,
    default: undefined
  },
  group_category_options: {
    type: Array as PropType<{ value: string; label: string }[]>,
    default: () => [],
  }
})

const data: any = reactive({
  queryParams: {
    username: '',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  }
})
const loading: any = ref(true);
const groupNoList: any = ref<any>([]);
const total: any = ref(0);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const names: any = ref<any>([]);

const {queryParams} = toRefs(data)

// 监听 open 变化
watch(
    () => props.memberOpen,
    (val: any) => {
      if (val && props.username) {
        queryParams.value.username = props.username;
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);

function reset(): any {
  queryParams.value = {
    username: '',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  }
  loading.value = true;
}

function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

function resetQuery(): any {
  queryParams.value.pageNumber = 1;
  queryParams.value.roleName = undefined;
  getList();
}

function getList(): any {
  listNoUserGroup(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      groupNoList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;

  const idsAndNames: any = selectionlist.value.reduce((acc: any, item: any) => {
    acc.ids.push(item.id);
    // acc.names.push(item.groupName);
    return acc;
  }, { ids: [], names: [] });

  ids.value = idsAndNames.ids;
  // names.value = idsAndNames.names;
}

function submitAdd(): any {
  // addNoUserGroup({groupIds: ids.value, groupNames: names.value, username: props.username}).then((res: any) =>  {
  addNoUserGroup({groupIds: ids.value, username: props.username}).then((res: any) =>  {
    if (res.code === 0) {
      modal.msgSuccess(t('org.success.add'));
      emit('onSubmitSuccess');
      handleQuery();
    } else {
      modal.msgError(res.message);
    }
  })
}

defineExpose({submitAdd})
</script>
