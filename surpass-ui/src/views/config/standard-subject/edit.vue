<template>
  <el-drawer v-if="dialogStatus" v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
        <el-form-item label="会计准则">
          <el-select v-model="form.standardId" @change="changeStandard">
            <el-option
                v-for="item in standardList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="category" :label="$t('subjectCategory')" :required="true">
          <el-select v-model="form.category" clearable>
            <el-option
                v-for="dict in subjects_category"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item prop="code" :label="t('subjectCode')" :required="true">
          <el-input v-model="form.code"/>
        </el-form-item>
        <el-form-item prop="name" :label="t('subjectName')" :required="true">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item prop="displayName" :label="t('subjectDisplayName')" >
            <el-input v-model="form.displayName"  disabled/>
          </el-form-item>
        <el-form-item prop="pinyinCode" :label="t('subjectPinyinCode')">
          <el-input v-model="form.pinyinCode"/>
        </el-form-item>
        <el-form-item prop="pinyinDisplayCode" :label="t('subjectPinyinDisplayCode')">
            <el-input v-model="form.pinyinDisplayCode" disabled/>
          </el-form-item>
        <el-form-item prop="direction" :label="$t('subjectBalanceDirection')">
          <el-radio-group v-model="form.direction">
            <el-radio value="0">{{ t('subjectDirectionNone') }}</el-radio>
            <el-radio value="1">{{ t('subjectDebit') }}</el-radio>
            <el-radio value="2">{{ t('subjectCredit') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="auxiliary" :label="$t('subjectAuxiliary')">
          <el-select v-model="form.auxiliary" clearable multiple>
            <template #header>
              <div style="display: flex;justify-content: space-between;padding: 0 40px 0 10px">
                <span>类型名称</span>
                <span>是否必填</span>
              </div>
            </template>
            <el-option
                v-for="dict in subjects_category_dicts"
                :key="dict.id"
                :label="dict.label"
                value-key="value"
                :value="dict">
              <span style="float: left">{{ dict.label }}</span>
              <el-switch @click.stop
                         style="float: right;color: var(--el-text-color-secondary);margin-right: 10px;"
                         v-model="dict.must" @change="handle_subjects_category_dicts(dict)"/>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="classify" :label="t('subjectClassify')">
          <el-input v-model="form.classify"/>
        </el-form-item>
        <el-form-item prop="scope" :label="t('subjectScope')">
          <el-input v-model="form.scope"/>
        </el-form-item>
        <el-form-item prop="parentId" :label="$t('subjectParent')">
          <el-tree-select
              ref="resTreeRef"
              v-model="form.parentId"
              :data="deptOptions"
              :props="defaultProps"
              check-strictly
              value-key="id"
              show-checkbox
              clearable
              :default-checked-keys="cheackdData"
          />
        </el-form-item>
        <el-form-item prop="isCash" label="是否为现金科目">
          <el-radio-group v-model="form.isCash">
            <el-radio :value="1">是</el-radio>
            <el-radio :value="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="status" :label="$t('jbx.text.status.status')">
          <el-switch
              :width="44"
              v-model="form.status"
              :active-value="1"
              :inactive-value="0"
              active-icon-class="el-icon-close"
              inactive-icon-class="el-icon-check">
          </el-switch>
        </el-form-item>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {useI18n} from "vue-i18n";
import {getCurrentInstance, PropType, reactive, ref, toRefs, watch} from "vue";
import {ElForm} from "element-plus";
import {getOneSubject, getTree, saveSubject, updateSubject} from "@/api/system/standard/standard-subject";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {subjects_category, subjects_auxiliary} = proxy?.useDict("subjects_category", "subjects_auxiliary");
const subjects_category_dicts = reactive(subjects_auxiliary.value.map((t: any) => {
  return {
    id: t.value + (Math.random() * 9999).toFixed(0),
    label: t.label,
    value: t.value,
  }
}))
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const emit: any = defineEmits(['dialogOfClosedMethods'])

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
    default: undefined
  },
  subOptions: {
    type: Array,
    default: [],
  },
  standardList: {
    type: Array as PropType<{ id: string; name: string }[]>,
    default: () => [],
  },
  currentTreeParentId: {
    type: String,
    default: null,
  },
  defaultStandardId: {
    type: String,
    default: null,
  }
})

const data: any = reactive({
  form: {
    direction: "1",
    status: 1,
    auxiliary: [],
    standardId: props.defaultStandardId,
    isCash: 0
  },
  rules: {
    name: [
      {required: true, message: t('subjectNameError'), trigger: "blur"},
    ],
    code: [
      {required: true, message: t('subjectCodeError'), trigger: "blur"},
    ],
    category: [
      {required: true, message: t('subjectCategoryError'), trigger: "blur"},
    ],
    standardId: [
      {required: true, message: "会计准则不能为空", trigger: "blur"},
    ]
  }
})

const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
  disabled: disabledFilter
})

const {form, rules} = toRefs(data);
const isEdit: any = ref(false);
const dialogStatus: any = ref(false);
const deptOptions: any = ref<any[]>([]);
const resTreeRef: any = ref<any>({});
const cheackdData: any = ref<any>([]);

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        if (props.formId) {
          isEdit.value = true;
          getOneSubject(props.formId).then((res: any) => {
            try {
              res.data.auxiliary = res.data.auxiliary ? JSON.parse(res.data.auxiliary) : '';
              res.data.auxiliary.forEach((t: any) => {
                subjects_category_dicts.forEach((v: any) => {
                  if (v.value === t.value) {
                    v.must = t.must
                  }
                })
              })
            } catch (err) {
              res.data.auxiliary = []
            }
            form.value = res.data;
          });
        } else {
          reset();
        }
        dialogStatus.value = props.open;
        deptOptions.value = props.subOptions;
      } else {
        reset();
      }
    },
    {immediate: true}
);

/** 重置操作表单 */
function reset(): any {
  form.value = {
    direction: "1",
    status: 1,
    standardId: props.defaultStandardId,
    isCash: 0
  };
  isEdit.value = false;
  formRef?.value?.resetFields();
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function submitForm(): any {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      proxy?.$modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      proxy?.$modal.msgError(res.message);
    }
  };

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation: any = props.formId ? updateSubject : saveSubject;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');

      const fornData = {...form.value}
      fornData.auxiliary = fornData.auxiliary && fornData.auxiliary instanceof Array
          ? JSON.stringify(fornData.auxiliary) : '[]';
      operation(fornData).then((res: any) => handleResponse(res, successMessage));
    }
  });
}

function disabledFilter(data: any, node: any): any {
  if (!props.formId) {
    return false;
  }
  return new RegExp(`/${props.formId}(/|$)`).test(data.idPath)
}


interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  children?: TreeNode[]; // 子节点可能不存在
}

function changeStandard() {
  getTree({standardId: form.value.standardId}).then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
    form.value.parentId = undefined;
    cheackdData.value = [];
    resTreeRef.value.setCheckedKeys(cheackdData.value)
  });
}

function handle_subjects_category_dicts(item: any) {
  const auxiliary = form.value.auxiliary || []
  let find = false
  auxiliary.forEach((t: any) => {
    if (t.value === item.value) {
      t.must = item.must
      find = true
    }
  })
  if (!find) {
    auxiliary.push(item)
  }
}
</script>
