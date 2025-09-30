<template>
  <!--用户数据-->
  <div class="queryForm">
    <el-form :model="userParams" ref="queryForm" :inline="true" @submit.native.prevent>
      <el-form-item :label="$t('jbx.users.username')">
        <el-input
            v-model="userParams.username"
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
  <el-row :gutter="20">
    <el-col :span="4" :xs="24">
      <div style="max-height: 600px;overflow-y: auto">
        <el-tree
            node-key="key"
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            :default-expanded-keys="treeData"
            @node-click="handleNodeClick"
            highlight-current
            v-slot="{ node, data }"
        >
            <span>
              <span v-if="node.label.length<=10">{{ node.label }}</span>
              <span v-else>
                 <el-tooltip class="item" effect="dark" :content="node.label" placement="right">
                   <span>{{ node.label.slice(0, 10) + '...' }}</span>
                </el-tooltip>
              </span>
            </span>
        </el-tree>
      </div>
    </el-col>
    <el-col :span="20">
      <el-table border
                v-loading="loading"
                :data="usersList"
                class="accountTable"
                ref="accountTable"
                @select="handleSelectionChangeAccount"
      >
        <el-table-column type="selection" width="50" align="center">
        </el-table-column>
        <el-table-column prop="username" :label="$t('jbx.users.username')" align="center"
                         min-width="100"/>
        <el-table-column prop="displayName" :label="$t('jbx.users.displayName')" align="center"
                         min-width="120"/>
        <el-table-column prop="employeeNumber" :label="$t('jbx.users.employeeNumber')" align="center"
                         min-width="100"/>
        <el-table-column prop="department" :label="$t('jbx.users.department')" align="center"
                         min-width="100"/>
        <el-table-column prop="gender" :label="$t('jbx.users.gender')" align="center"
                         min-width="60">
          <template #default="scope">
            <span v-if="scope.row.gender === 1">{{ $t('jbx.users.genderFemale') }}</span>
            <span v-else-if="scope.row.gender === 2">{{ $t('jbx.users.genderMale') }}</span>
            <span v-else>{{ $t('jbx.resources.resourceType.oTHER') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total"
                  v-model:page="userParams.pageNumber"
                  v-model:limit="userParams.pageSize"
                  :page-sizes="userParams.pageSizeOptions"
                  @pagination="getList"/>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {listUsers} from "@/api/system/user";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;

const emit: any = defineEmits(['updateFlag', 'inputUserValue'])

const data: any = reactive({
  userParams: {
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 50]
  }
})
const {userParams} = toRefs(data);
const defaultProps: any = ref({
  children: "children",
  label: "title"
});
const loading: any = ref(true);
const usersList: any = ref<any>([]);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const total: any = ref(0);
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  deptOptions: {
    type: Array,
    default: []
  },
  treeData: {
    type: Array,
    default: []
  }
});
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
  userParams.value = {
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 50]
  }
  selectedRows.value = [];
  selectObject.value = {};
  loading.value = true;
}

function getList(): any {
  listUsers(userParams.value).then((res: any) =>  {
    usersList.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  })
}

/** 通过条件过滤节点  */
const filterNode: any = (value: any, data: any) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 节点单击事件 */
function handleNodeClick(data: any): any {
  userParams.value.departmentId = data.key;
  handleQuery();
}

function handleQuery(): any {
  userParams.value.pageNumber = 1;
  getList();
}

//重置
function resetQuery(): any {
  userParams.value.username = undefined;
  handleQuery();
}

/**
 * 账号单选
 */
function handleSelectionChangeAccount(selection: any, row: any): any {
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
  emit('inputUserValue', selectObject.value)
}

defineExpose({confirmSelect})
</script>

<style lang="scss" scoped>
.accountTable :deep(.el-table__header-wrapper  .el-checkbox) {
  display:none
}

</style>
