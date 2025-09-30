<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="55%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent>
          <el-form-item label="账套">
            <el-input
                v-model="queryParams.bookName"
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
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="id" label="编码" min-width="80" header-align="center" align="left"/>
        <el-table-column prop="name" label="账套" min-width="100" header-align="center" align="left"/>
        <el-table-column prop="standardId" label="会计准则" header-align="center" align="left" min-width="100" :show-overflow-tooltip="true">
          <template #default="scope">
            <el-select v-model="scope.row.standardId" disabled >
              <el-option
                  v-for="dict in standardList"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
              />
            </el-select>
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
  </el-drawer>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import {listStandardsAll} from "@/api/system/standard/standard";
import {bookFetch} from "@/api/config/institutions";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const emit: any = defineEmits(['groupDrawerClose'])


const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  bookOpen: {
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
const dataList: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const addPermissionFlag: any = ref(false);
const addPermissionTitle: any = ref('')
//会计准则
const standardList: any = ref<any>([]);

// 监听 open 变化
watch(
    () => props.bookOpen,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.bookOpen
        if (props.formId) {
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
  getStandards()
  queryParams.value.id = props.formId;
  bookFetch(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function getStandards(): any {
  listStandardsAll({status: 1}).then((res: any) => {
    if (res.code === 0) {
      standardList.value = res.data;
    }
  });
}

function reset(): any {
  queryParams.value = {
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  };
  loading.value = true;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('bookDrawerClose', val);
}

function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

function resetQuery(): any {
  queryParams.value.pageNumber = 1;
  queryParams.value.bookName = undefined;
  getList();
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) =>  item.id);
}
</script>

<style lang="scss" scoped>
.button-top {
  margin-bottom: 10px;
}
</style>
