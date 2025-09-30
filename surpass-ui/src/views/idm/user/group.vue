<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="55%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
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
      <div class="button-top">
        <el-button
            @click="handleAdd"
            type="primary">
          {{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="onBatchDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="groupsList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="roleName" label="角色名称" min-width="100" align="center"/>
        <el-table-column prop="type" :label="$t('jbx.roles.type.type')" min-width="50" align="center">
          <template #default="scope">
            <span v-if="scope.row.type === 'USER'"> {{ t('jbx.roles.type.user') }}</span>
            <span v-if="scope.row.type === 'USER-DYNAMIC'"> {{ t('jbx.roles.type.userDynamic') }}</span>
            <span v-if="scope.row.type === 'POST'"> {{ t('jbx.roles.type.post') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="memberName" :label="$t('jbx.roles.member')" min-width="100" align="center"/>
        <el-table-column :label="$t('jbx.text.action')" min-width="50" align="center" width="80">
          <template #default="scope">
            <el-tooltip v-if="scope.row.type !== 'USER-DYNAMIC'" content="移除">
              <el-button link icon="Delete" @click="onDelete(scope.row)"></el-button>
            </el-tooltip>
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
      <!--新增对话框-->
      <el-dialog v-model="memberFlag" width="800" :title="memberTitle" align-center :close-on-click-modal="false">
        <memberGroup ref="addMember" :member-open="memberFlag" :username="username"
                     :group_category_options="group_category_options"
                     @onSubmitSuccess="addSuccess"></memberGroup>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="confirmAdd">{{ t('jbx.text.confirm') }}</el-button>
            <el-button @click="cancelAdd">{{ t('systemCancel') }}</el-button>
          </div>
        </template>
      </el-dialog>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import {deleteUserGroup, listUserGroup} from "@/api/system/group";
import memberGroup from "./addGroupMember.vue"
import {set2String} from '@/utils';

const {t} = useI18n()

defineOptions({
  name: 'userGroup'
})

const {proxy} = getCurrentInstance()!;

const {group_category_options}
    = proxy?.useDict("group_category_options");
const emit: any = defineEmits(['groupDrawerClose'])


const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  groupOpen: {
    type: Boolean,
    default: false
  },
  formId: {
    type: String,
    default: null
  },
  username: {
    type: String,
    default: ''
  }
})

const data: any = reactive({
  queryParams: {
    userId: '',
    username: '',
    memberName: '',
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  }
})

const {queryParams} = toRefs(data)

const dialogStatus: any = ref(false);
const groupsList: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const memberFlag: any = ref(false);
const memberTitle: any = ref('')

// 监听 open 变化
watch(
    () => props.groupOpen,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.groupOpen
        if (props.formId) {
          queryParams.value.userId = props.formId;
          queryParams.value.username = props.username;
          queryParams.value.memberName = props.username;
          getList();
        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

function getList(): any {
  listUserGroup(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      groupsList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function reset(): any {
  queryParams.value = {
    userId: '',
    username: '',
    memberName: '',
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  };
  loading.value = true;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('groupDrawerClose', val);
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

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/**
 * 新增用户组
 */
function handleAdd(): any {
  memberFlag.value = true;
  memberTitle.value = t('jbx.text.add')
}

function cancelAdd(): any {
  memberFlag.value = false;
}

/**
 * 确认新增
 */
function confirmAdd(): any {
  proxy.$refs.addMember.submitAdd();
}

function addSuccess(): any {
  handleQuery();
  memberFlag.value = false;
}

function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds: any = set2String(ids.value);
    return deleteUserGroup({ids: setIds});
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function onDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.roleName + t('org.deleteTip2')).then(function () {
    return deleteUserGroup({ids: row.id});
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}
</script>

<style lang="scss" scoped>
.button-top {
  margin-bottom: 10px;
}
</style>
