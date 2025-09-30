<template>
  <div class="app-container">
    <el-card class="common-card">
      <div class="btn-form">
        <el-radio-group v-model="queryParams.type" style="margin-right: 20px" @change="handleChangeType">
          <el-radio-button label="工资薪金" :value="0"></el-radio-button>
          <el-radio-button label="劳务报酬" :value="1"></el-radio-button>
        </el-radio-group>
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ t('org.button.add') }}
        </el-button>
      </div>
      <div class="title-header">
        <span class="title-header-text" v-if="queryParams.type === 0">个人工资、薪金所得税率表</span>
        <span class="title-header-text" v-else>个人劳务报酬所得税率表</span>
      </div>
      <el-table
          v-loading="loading"
          :data="dataList"
          border
          stripe
      >
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="level" label="层级" align="center" min-width="20">
        </el-table-column>
        <el-table-column prop="minNum" label="应纳税所得额级距" align="left" min-width="150"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <span
                v-if="scope.row.minNum === 0 || scope.row.minNum === null">不超过{{
                formatNumber(scope.row.maxNum)
              }}元的部分</span>
            <span v-else-if="scope.row.maxNum === null">超过{{ formatNumber(scope.row.minNum) }}元的部分</span>
            <span v-else>超过{{ formatNumber(scope.row.minNum) }}元至{{ formatNumber(scope.row.maxNum) }}元的部分</span>
          </template>
        </el-table-column>
        <el-table-column prop="taxRate" label="税率" align="center" min-width="20">
          <template #default="scope">
            {{ scope.row.taxRate }}%
          </template>
        </el-table-column>
        <el-table-column prop="calculationDeduction" label="速算扣除数" align="right" min-width="40">
          <template #default="scope">
            {{ scope.row.calculationDeduction }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="80">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button type="text" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="scope.row.level === dataList.length" content="删除">
              <el-button type="text" icon="Delete" @click="handleDelete(scope.row)"></el-button>
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
    </el-card>
    <edit-form :title="title" :open="open"
               :form-id="id"
               :default-level="dataList.length + 1"
               :tax-type="queryParams.type"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {useI18n} from "vue-i18n";
import editForm from "./tax/edit.vue"
import {deleteOne, fetchPage} from "@/api/config/tax";
import modal from "@/plugins/modal";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;

const dataList: any = ref<any>([]);
const loading: any = ref(false);
const title = ref("");
const id: any = ref(undefined);
const open = ref(false);
const ids = ref([]);
const data: any = reactive({
  queryParams: {
    type: 0,
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});
const {queryParams} = toRefs(data);
const total: any = ref(0);

// 格式化数字：添加千位分隔符
const formatNumber = (value: number | null): string => {
  if (value === null) return ''
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

function handleAdd() {
  title.value = "新增";
  id.value = undefined;
  open.value = true;
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function getList() {
  fetchPage(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      dataList.value = res.data.records;
      total.value = res.data.total;
    }
  })
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除该条数据？').then(function () {
    return deleteOne({listIds: [_ids]});
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

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = "编辑";
  open.value = true;
}

function handleChangeType() {
  getList();
}

getList();
</script>

<style lang="scss" scoped>
.btn-form {
  display: flex;
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.title-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;

  &-text {
    font-size: 1.2em;
  }
}
</style>
