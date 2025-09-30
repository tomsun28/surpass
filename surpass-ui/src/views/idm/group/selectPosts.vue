<template>
  <div class="queryForm">
    <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent>
      <el-form-item :label="$t('jbx.posts.postName')">
        <el-input
            v-model="queryParams.memberName"
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
  <el-table ref='multipleTable'
            border
            v-loading="loading"
            :data="dataList"
            @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50" align="center"></el-table-column>
    <el-table-column :label="$t('jbx.posts.postCode')" align="center" prop="postCode"/>
    <el-table-column :label="$t('jbx.posts.postName')" align="center" prop="postName"/>
    <el-table-column :label="$t('jbx.users.departmentId')" align="center" prop="departmentId"/>
    <el-table-column :label="$t('jbx.users.department')" align="center" prop="department"/>
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
import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";;
import {addMemberGroup, memberPostNotInGroup} from "@/api/system/group.js";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

// 声明 emits
const emit: any = defineEmits(['onSubmitSuccess']);

const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  tableId: {
    type: String,
    default: null
  }
})

const data: any = reactive({
  queryParams: {
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  }
})

const {queryParams} = toRefs(data)

const loading: any = ref(false)
const dataList: any = ref<any>([])
const total: any = ref(0);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const names: any = ref<any>([]);

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val && props.tableId) {
        queryParams.value.groupId = props.tableId;
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
    pageSizeOptions: [10, 20, 30]
  };
  loading.value = true;
}

function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

function resetQuery(): any {
  queryParams.value.pageNumber = 1;
  queryParams.value.memberName = undefined;
  getList();
}

function getList(): any {
  memberPostNotInGroup(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function submitAdd(): any {
  addMemberGroup({memberIds: ids.value, type: 'POST', groupId: props.tableId, memberNames: names.value}).then(
      (res: any) =>  {
        if (res.code === 0) {
          handleQuery();
          modal.msgSuccess(t('org.success.add'));
          emit('onSubmitSuccess');
        } else {
          modal.msgError(t('jbx.alert.update.error'));
        }
      }
  )
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  const idsAndNames: any = selectionlist.value.reduce((acc: any, item: any) => {
    acc.ids.push(item.id);
    acc.names.push(item.groupName);
    return acc;
  }, { ids: [], names: [] });

  ids.value = idsAndNames.ids;
  names.value = idsAndNames.names;
}


defineExpose({submitAdd})

</script>
