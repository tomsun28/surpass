<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="55%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <div class="queryForm">
        <el-form :model="postParams" ref="queryForm" :inline="true" @submit.native.prevent>
          <el-form-item :label="$t('jbx.posts.postName')">
            <el-input
                v-model="postParams.postName"
                clearable
                @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item :label="$t('jbx.users.status')">
            <el-select v-model="postParams.assign" @change="handleQuery" style="width: 200px">
              <el-option
                  v-for="item in post_options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table v-loading="loading" :data="userPost" border>
        <el-table-column prop="postCode" :label="$t('jbx.posts.postCode')" min-width="80" align="center"/>
        <el-table-column prop="postName" :label="$t('jbx.posts.postName')" min-width="100" align="center"/>
        <el-table-column prop="departmentId" :label="$t('jbx.posts.departmentId')" min-width="80" align="center"/>
        <el-table-column prop="department" :label="$t('jbx.posts.department')" min-width="100" align="center"/>
        <el-table-column prop="status" :label="$t('jbx.text.status.status')" min-width="50" align="center">
          <template #default="scope">
            <a :title="$t('jbx.users.statusActive')" v-if="scope.row.status === 1">
              <el-icon color="green"><SuccessFilled
                  class="success"/></el-icon>
            </a>
            <a :title="$t('jbx.users.statusInactive')" v-if="scope.row.status === 2">
              <el-icon color="grey"><WarningFilled /></el-icon>
            </a>
            <a :title="$t('jbx.users.statusDelete')" v-if="scope.row.status === 9">
              <el-icon color="red"><CircleCloseFilled /></el-icon>
            </a>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" min-width="80" align="center">
          <template #default="scope">
            <el-button type="primary" v-if="postParams.assign === 'assignable'"
                       @click="onAdd(scope.row)">
              {{$t("jbx.text.add")}}
            </el-button>
            <el-button type="danger" v-if="postParams.assign === 'owns'"
                       @click="onDelete(scope.row)">
              {{$t("jbx.text.delete")}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="postParams.pageNumber"
          v-model:limit="postParams.pageSize"
          :page-sizes="postParams.pageSizeOptions"
          @pagination="getList"
      />
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('jbx.text.close') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {useI18n} from "vue-i18n";
import {deletePost, getUsersPosts, addPost} from "@/api/system/user";

const {t} = useI18n()

defineOptions({
  name: 'postDrawer'
})

const {proxy} = getCurrentInstance()!;
const emit: any = defineEmits(['postDrawerClose'])

const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  postOpen: {
    type: Boolean,
    default: false
  },
  formId: {
    type: String,
    default: null
  },
  post_options: {
    type: Array,
    default: []
  }
})

const data: any = reactive({
  postParams: {
    assign: 'owns',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  }
})

const {postParams} = toRefs(data)

const dialogStatus: any = ref(false);
const userPost: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true)

// 监听 open 变化
watch(
    () => props.postOpen,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.postOpen
        if (props.formId) {
          postParams.value.userId = props.formId;
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

function reset(): any {
  postParams.value = {
    assign: 'owns',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  }
  loading.value = true;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('postDrawerClose', val);
}

function getList(): any {
  getUsersPosts(postParams.value).then((res: any) =>  {
    userPost.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  })
}

function handleQuery(): any {
  postParams.value.pageNumber = 1;
  getList();
}

function resetQuery(): any {
  postParams.value.pageNumber = 1;
  postParams.value.assign = 'owns';
  postParams.value.postName = null;
  getList();
}

function onAdd(row: any): any {
  addPost({postId: row.id,userId: postParams.value.userId}).then((res: any) =>  {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.add.success'));
    } else {
      modal.msgError(res.message);
    }
  })
}

function onDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.postName + t('org.deleteTip2')).then(function () {
    return deletePost(row.id);
  }).then((res: any) =>  {
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.delete.success'));
      getList();
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

</script>
