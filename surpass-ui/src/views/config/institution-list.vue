<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true">
          <el-form-item label="机构名称" prop="name">
            <el-input
                v-model="queryParams.instName"
                placeholder=""
                clearable
                @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="机构类型" prop="name">
           <el-select style="width: 200px" v-model="queryParams.instType" clearable>
             <el-option
                 v-for="item in inst_types"
                 :key="item.value"
                 :label="item.label"
                 :value="item.value"
             />
           </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
            <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
<!--      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ t('org.button.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="onBatchDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>-->
      <el-table v-loading="loading" :data="dataList"
                border
                @selection-change="handleSelectionChange"
                highlight-current-row>
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="机构名称" prop="instName" header-align="center" align="left" min-width="100"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="机构类型" align="center" prop="instType" min-width="40">
          <template #default="scope"  >
            <dict-tag-number :options="inst_types" :value="scope.row.instType"/>
          </template>
        </el-table-column>
        <el-table-column label="联系人" prop="contact" header-align="center" align="left" min-width="90"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="邮箱" prop="email" header-align="center" align="left" min-width="90"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="负责人" prop="ownerName" header-align="center" align="left" min-width="90"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="注册时间" prop="createdDate" align="center" min-width="90"
                :show-overflow-tooltip="true"/>
        <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column label="账套" align="center" width="60"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <el-button link @click="bookDrawerOpen(scope.row)">
              <el-icon><Notebook /></el-icon>
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="t('jbx.text.action')" align="center" width="80">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
<!--            <el-tooltip content="移除">
              <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>-->
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
    </el-card>
    <edit-form :title="title" :open="open"
               :form-id="id"
               :inst-types="inst_types"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>

    <!--账套权限-->
    <book :title="title" :form-id="id"
          @bookDrawerClose="dialogOfClosedMethods" :book-open="bookOpen"></book>
  </div>
</template>

<script setup lang="ts">
import {useI18n} from "vue-i18n";
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {
  fetch,deleteBatch
} from "@/api/config/institutions";
import modal from "@/plugins/modal";
import editForm from "./institution-list/edit.vue";
import SvgIcon from "@/components/SvgIcon/index.vue";
import book from './institution-list/index.vue'

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const {inst_types}
    = proxy?.useDict("inst_types");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);

const dataList: any = ref<any>([]);
const loading: any = ref(true);
const ids: any = ref<any>([]);
const selectionlist: any = ref<any>([]);
const total: any = ref(0);
const id: any = ref(undefined);
const title: any = ref("");
const open: any = ref(false);
const bookOpen: any = ref(false);

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 分页列表 */
function getList(): any {
  loading.value = true;
  fetch(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
    }
  });
}

/** 搜索按钮操作 */
function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery(): any {
  queryParams.value.instName = undefined;
  queryParams.value.instType = undefined;
  handleQuery();
}

function handleAdd(): any {
  id.value = undefined;
  title.value = t('jbx.text.add')
  open.value = true;
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  id.value = row.id;
  title.value = t('org.titleEdit');
  open.value = true;
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.name + t('org.deleteTip2')).then(function () {
    return deleteBatch({listIds: [row.id]});
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  bookOpen.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return deleteBatch({listIds: ids.value});
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

function bookDrawerOpen(row: any): any {
  id.value = row.id;
  bookOpen.value = true;
  title.value = "账套"
  console.log("bookDrawerOpen");
}


getList();
</script>

<style lang="scss" scoped>

::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

.auths {
  display: flex;
}

.auths .cleft {
  width: 50%;
}

.md {
  color: #ccc;
  font-size: 12px;
  margin-left: 20px
}

</style>
