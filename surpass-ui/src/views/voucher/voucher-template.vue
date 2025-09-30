<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="会计准则">
            <el-select v-model="queryParams.standardId" @change="handleQuery" style="width: 200px">
              <el-option
                  v-for="item in standardList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item  label="类型：">
            <el-radio-group v-model="queryParams.category" @change="getList">
              <el-radio-button :value="1">期末</el-radio-button>
              <el-radio-button :value="2">计提</el-radio-button>
              <el-radio-button :value="3">支付</el-radio-button>
              <el-radio-button :value="4">常规</el-radio-button>
            </el-radio-group>
        </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <el-table v-loading="loading" :data="vouchertemplateList" border
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                row-key="id" default-expand-all
                height="650"
                @cell-mouse-enter="cellMouseEnter"
                @cell-mouse-leave="cellMouseLeave">
        <el-table-column label="编码" align="left" header-align="center" prop="code" >
        </el-table-column>
        <el-table-column label="名称" align="left" header-align="center" prop="name" >
        </el-table-column>
        <el-table-column label="字头" align="center" prop="wordHead" />
        <el-table-column label="备注" align="left" prop="remark" />
        <el-table-column label="排序" align="left" prop="sortIndex" />
        <el-table-column  label="操作" align="center" header-align="center" width="120" prop="sortIndex">
          <template #default="scope">
           <el-tooltip content="新增">
            <el-button type="primary" link @click="handleAdd(scope.row)" icon="Plus"></el-button>
          </el-tooltip>
          <el-tooltip content="编辑">
            <el-button type="primary" link @click="handleEdit(scope.row)" icon="Edit"></el-button>
          </el-tooltip>
          <el-tooltip content="移除">
            <el-button type="primary" link @click="handleDel(scope.row, 'asset')" icon="Delete"></el-button>
          </el-tooltip>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-text">暂无数据</div>
          <el-button @click="handleAdd(null)">立即添加</el-button>
        </template>
      </el-table>
    </el-card>

    <el-dialog v-model="dialog.visible" :close-on-click-modal="false" width="800" style="margin-top: 30vh !important;">
      <template #default>
        <el-form :model="form" :items="items" ref="voucherTemplateRef" label-width="68px"
                 inline-message>
          <el-form-item label="ID" prop="id"  style="display:none">
            <el-input style="width: 300px" v-model="form.id" placeholder="请输入id"/>
          </el-form-item>
          <el-form-item label="编码" prop="code" :required="true">
            <el-input style="width: 300px" v-model="form.code" placeholder="请输入编码"/>
          </el-form-item>
          <el-form-item label="名称" prop="name" :required="true">
            <el-input style="width: 300px" v-model="form.name" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item label="分类" prop="category" :required="true">
            <el-select  v-model="form.category" placeholder="选择" style="width: 300px">
                  <el-option label="期末" value="1"></el-option>
                  <el-option label="计提" value="2"></el-option>
                  <el-option label="支付" value="3"></el-option>
                  <el-option label="常规" value="4"></el-option>
                </el-select>
          </el-form-item>
          <el-form-item label="字头" prop="wordHead" :required="true">
            <el-select  v-model="form.wordHead" placeholder="选择" style="width: 300px">
                  <el-option label="记" value="记"></el-option>
                  <el-option label="收" value="收"></el-option>
                  <el-option label="付" value="付"></el-option>
                  <el-option label="转" value="转"></el-option>
                </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="remark" :required="true">
            <el-input style="width: 300px"  v-model="form.remark" placeholder="请输入备注"/>
          </el-form-item>
          <el-form-item label="排序" prop="sortIndex" :required="true">
            <el-input style="width: 300px"  v-model="form.sortIndex" placeholder="请输入排序"/>
          </el-form-item>
          <el-table v-loading="loading" :data="form.items" border size="small"
                    :cell-class-name="tableCellClassName"
                    @cell-click="cellMouseEnter"
                    :row-style="{height: '46px'}">
            <el-table-column label="摘要" align="left" header-align="center" prop="summary">
              <template #default="scope">
                <span v-if="!scope.row.editing || scope.row.columnIndex !== 0">
                  {{ scope.row.summary }}
                </span>
                <el-input v-else  v-model="scope.row.summary" placeholder="请输入备注"  />
              </template>
            </el-table-column>      
            <el-table-column label="科目" align="left" header-align="center" prop="subjectCode">
              <template #default="scope">
                <span v-if="!scope.row.editing || scope.row.columnIndex !== 1">
                  {{ scope.row.subjectCode+" "+subjectKeyIdItem[scope.row.subjectCode]?.displayName }}
                </span>
                <el-cascader v-else style="width: 100%" filterable
                             v-model="scope.row.subjectCode"
                             :options="subjectList"
                             :props="cascaderSubjectPropsOwn"
                             @change="handleSubjectChange(scope, $event)"
                             :filter-method="cascaderSubjectPropsOwn.filterMethod"
                             @visible-change="handleSubjectVisibleChange"/>
              </template>
            </el-table-column>
            <el-table-column label="借/贷" align="center" prop="direction" width="100">
              <template #default="scope">
                <span v-if="!scope.row.editing || scope.row.columnIndex !== 2">
                 {{ scope.row.direction == "1"?"借":"贷" }}
                </span>
                <el-select v-else v-model="scope.row.direction" placeholder="借/贷" @blur="closeEditAll">
                  <el-option label="借" value="1"></el-option>
                  <el-option label="贷" value="2"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="取数规则" align="center" prop="rule">
              <template #default="scope">
                 <span v-if="!scope.row.editing || scope.row.columnIndex !== 3">
                   <dict-tag :options="account_income_balance_type" :value="scope.row.rule"></dict-tag>
                </span>
                <el-select v-else v-model="scope.row.rule" placeholder="选择" @blur="closeEditAll">
                  <el-option
                      v-for="dict in account_income_balance_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="110">
              <template #default="scope">
                <el-popconfirm title="确认删除吗？" @confirm="form.items.splice(scope.$index, 1)">
                  <template #reference>
                    <el-button size="small" icon="Delete"></el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-button  icon="Plus" style="width: 100%" @click="form.items.push({direction:1})" ></el-button>
        </el-form>
      </template>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="dialog.visible = false">{{ t('org.cancel') }}</el-button>
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ReportBalanceSheet" lang="ts">
import {getCurrentQuarter, parseTime} from '@/utils/Jinbooks'
import {getCurrentInstance, h, ref, shallowRef, reactive, toRefs} from 'vue'
import bookStore from "@/store/modules/bookStore";
import {ElForm, FormInstance} from "element-plus";
import {cascaderSubjectProps} from "@/utils/Subjects"
import * as voucherTemplateService from "@/api/system/voucher/voucher-template";
import {useI18n} from "vue-i18n";
import DictTag from "@/components/DictTag/index.vue";
import * as subjectApi from "@/api/system/standard/standard-subject";
import {listStandardsAll} from "@/api/system/standard/standard";

const {t} = useI18n()
const {proxy} = getCurrentInstance();
const {account_income_balance_type} = proxy?.useDict("account_income_balance_type");
const currBookStore = bookStore()
const cascaderSubjectPropsOwn = ref<any>({...cascaderSubjectProps})
cascaderSubjectPropsOwn.value.checkStrictly = true
// 会计科目数据
const subjectList = ref<any>([])
const vouchertemplateList = ref<any>([]);
const subjectKeyIdItem = ref<any>({})
const loading = ref(true);
const buttonLoading = ref(false);
const showSearch = ref(true);
const level = ref(1);
const visibleSubjectStatus = ref(false);
//会计准则
const standardList: any = ref<any>([]);
const dialog = reactive<any>({
  visible: false,
  title: ''
});
const voucherTemplateRef = ref<FormInstance>();
const initFormData: any = {
  bookId: currBookStore.bookId,
  parentCode: undefined,
  sortIndex: 1,
  itemName: "",
  items: [{
    subjectCode: undefined,
  }]
}
const data = reactive({
  form: {...initFormData},
  queryParams: {
    periodType: 'month',
    standardId:'',
    category:1,
    date: parseTime(new Date(), "{y}-{m}"),
    reportQuarter: getCurrentQuarter(),
    reportDate: parseTime(new Date(), "{y}-{m}"),
  },
  items: {
    itemCode: [
      {required: true, message: '编码不能为空', trigger: 'blur'}
    ],
    itemName: [
      {required: true, message: '名称不能为空', trigger: 'blur'}
    ],
  }
});

const {queryParams, form, items} = toRefs(data);

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})

const disabledDate = (time: any) => {
  const now = new Date();
  return time.getTime() > now.getTime(); // 禁用过去的日期
}

/** 查询列表 */
function getList() {
  loading.value = true;
  voucherTemplateService.list(queryParams.value.standardId,queryParams.value.category).then((response: any) => {
    vouchertemplateList.value = response.data.records;
    //total.value = response.data.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

function handleExport() {

}

function closeEditAll() {
  form.value.items.forEach((item: any) => {
    item.editing = false
  })
}

const cellMouseEnter = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
    closeEditAll()
    row.columnIndex = column.index
    row.editing = true

  event.stopPropagation()
}

const cellMouseLeave = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  row.editing = false
}

// 更新会计科目ID关联
const updateSubjectKeys = (items: any) => {
  for (let valueKey in items) {
    const item = items[valueKey]
    subjectKeyIdItem.value[item.code] = item
    if (item.children && item.children.length > 0) {
      updateSubjectKeys(item.children)
    }
  }
}

function getSubjectList() {
  //传入当前账套ID
  subjectApi.getTree({ standardId:queryParams.value.standardId}).then((res: any) => {
    subjectList.value = res.data
    updateSubjectKeys(subjectList.value)
  })
}

const handleSubjectChange = (scope: any, value: any) => {
  const subject = subjectKeyIdItem.value[scope.row.subjectCode]
  scope.row.subjectId = subject.id
}

const handleSubjectVisibleChange = (show: any) => {
  if (show) {
    visibleSubjectStatus.value = true
    setTimeout(() => {
      visibleSubjectStatus.value = false
    }, 500)
  }
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  voucherTemplateRef.value?.resetFields();
};

function handleAdd(row?: any) {
  reset();
  form.value.relatedId = queryParams.value.standardId;
  dialog.visible = true;
  dialog.title = "添加";
}

function handleEdit(row: any) {
  reset();
  voucherTemplateService.get(queryParams.value.standardId,row.id).then((res: any) => {
    form.value = res.data
    if (!form.value.items || form.value.items.length === 0) {
      form.value.items = [{}]
    }
    dialog.visible = true;
    dialog.title = "修改";
  })
}

/**
 * 删除
 * @param row
 * @param assetOrLiability
 */
function handleDel(row: any, assetOrLiability?: string) {
  voucherTemplateService.del(row.id).then(() => {
    proxy?.$modal.msgSuccess('删除成功');
    getList();
  })
}

/** 提交按钮 */
const submitForm = () => {
  voucherTemplateRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.items = form.value.items.filter((item: any) => {
        console.log("subjectCode "+item.subjectCode+ " summary " + item.summary +" direction "+ item.direction);
        return item.subjectCode && item.summary && item.direction
      })
      form.value.relatedId =form.value.relatedId||queryParams.value.standardId;

      buttonLoading.value = true;
      await voucherTemplateService.save(form.value).finally(() => buttonLoading.value = false);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

function tableCellClassName({row, column, rowIndex, columnIndex}: any) {
  //注意这里是解构
  //利用单元格的 className 的回调方法，给行列索引赋值
  row.index = rowIndex;
  column.index = columnIndex;
  return ""
}

/*获取准则列表*/
function getStandardList() {
  listStandardsAll({}).then((res: any) => {
    if (res.code === 0) {
      if (Array.isArray(res.data) && res.data.length > 0) {
        standardList.value = res.data;
        queryParams.value.standardId = standardList.value[0].id;
      } else {
        // 如果数据为空数组时，确保有默认处理逻辑
        standardList.value = [];
        queryParams.value.standardId = ''; // 或设置为适当的默认值
      }
      getSubjectList();
      getList(); // 确保在赋值完成后调用
    }
  });
}

getStandardList();

</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}
</style>
