<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="55%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true"
                 @submit.native.prevent>
          <el-form-item :label="t('jbx.roles.member')">
            <el-input
                v-model="queryParams.memberName"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
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
            type="primary"
            @click="handleAdd"
        >
          {{ $t('jbx.text.add') }}
        </el-button>
<!--        <el-button
            @click="handleAddPost"
        >
          {{$t('jbx.roles.addPost')}}
        </el-button>-->
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="onBatchDelete"
        >
          {{ $t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.roles.type.type')" align="center" prop="type" min-width="70">
          <template #default="scope">
            <dict-tag :options="role_members_type" :value="scope.row.type"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.roles.member')" align="center"
                         :show-overflow-tooltip="true"
                         prop="memberName" min-width="130"/>
        <el-table-column :label="$t('jbx.users.jobTitle')"
                         :show-overflow-tooltip="true"
                         align="center" prop="jobTitle" min-width="120"/>
        <el-table-column :label="$t('jbx.users.gender')" align="center" prop="gender" min-width="70">
          <template #default="scope">
            <dict-tag :options="use_gender" :value="scope.row.gender"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.users.department')"
                         :show-overflow-tooltip="true"
                         align="center" prop="department" min-width="120"/>
        <el-table-column :label="$t('jbx.history.systemlogsMessageaction')" align="center" width="80">
          <template #default="scope">
            <el-tooltip content="移除">
              <el-button link icon="Delete" @click="handleDelete(scope.row)"></el-button>
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

      <el-dialog :title="dialogTitle" v-model="memberFlag" width="800px" append-to-body>
        <selectRolemembers ref="selectMember" :open="memberFlag"
                           @onSubmitSuccess="onSuccess"
                           :table-id="formId" :title="title"></selectRolemembers>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="confirmAdd">{{ t('jbx.text.confirm') }}</el-button>
            <el-button @click="cancelAdd">{{ t('systemCancel') }}</el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog :title="dialogTitle" v-model="postFlag" width="800px" append-to-body>
        <select-posts ref="selectPost" :open="postFlag"
                           @onSubmitSuccess="onSuccess"
                           :table-id="formId" :title="title"></select-posts>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="confirmAddPost">{{ t('jbx.text.confirm') }}</el-button>
            <el-button @click="cancelAdd">{{ t('systemCancel') }}</el-button>
          </div>
        </template>
      </el-dialog>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {deleteUserGroup, listUserGroup} from "@/api/system/group.js";
import {set2String} from "@/utils";
import selectRolemembers from './selectRolemembers.vue'
import selectPosts from './selectPosts.vue'

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const emit: any = defineEmits(['memberDrawerClose'])
const props: any = defineProps({
      title: {
        type: String,
        default: ""
      },
      open: {
        type: Boolean,
        default: false
      },
      formId: {
        type: String,
        default: null
      },
      formName: {
        type: String,
        default: null
      },
      role_members_type: {
        type: Array,
        default: []
      },
      use_gender: {
        type: Array,
        default: []
      }
    }
)

const data: any = reactive({
  queryParams: {
    roleName: '',
    roleId: null,
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  },
  form: {},
  rules: {}
})
const {queryParams, form, rules} = toRefs(data);
const dialogStatus: any = ref(false);
const dataList: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true);
const memberFlag: any = ref(false);
const postFlag: any = ref(false);
const dialogTitle: any = ref('');
const ids: any = ref<any>([]);
const selectionlist: any = ref<any>([]);

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val && props.formId) {
        dialogStatus.value = props.open;
        queryParams.value.roleId = props.formId;
        queryParams.value.roleName = props.formName;
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);

function reset(): any {
  queryParams.value = {
    roleName: '',
    roleId: null,
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  };
  loading.value = true;
}

function getList(): any {
  listUserGroup(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('memberDrawerClose', val);
}

/** 搜索按钮操作 */
function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery(): any {
  queryParams.value.memberName = undefined;
  handleQuery();
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) =>  item.id);
}

function handleDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.memberName + t('org.deleteTip2')).then(function () {
    return deleteUserGroup({ids: row.id});
  }).then((res: any) =>  {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds: any = set2String(ids.value);
    return deleteUserGroup({ids: setIds});
  }).then((res: any) =>  {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function handleAdd(): any {
  dialogTitle.value = t('jbx.text.add')
  memberFlag.value = true;
}

function handleAddPost(): any {
  dialogTitle.value = t('jbx.roles.addPost')
  postFlag.value = true;
}

function cancelAdd(): any {
  memberFlag.value = false;
  postFlag.value = false;
}

function confirmAdd(): any {
  proxy.$refs.selectMember.submitAdd();
}

function confirmAddPost(): any {
  proxy.$refs.selectPost.submitAdd();
}


function onSuccess(): any {
  handleQuery();
  memberFlag.value = false;
  postFlag.value = false;
}

</script>

<style lang="scss" scoped>
.button-top {
  margin-bottom: 10px;
}
</style>

