<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="orgRef" label-width="110px" inline-message>
        <el-tabs v-model="activeName" :stretch="true">
          <el-tab-pane :label="$t('jbx.organizations.tabBasic')" name="first">
            <el-form-item prop="orgCode" :label="$t('jbx.organizations.code')" :required="true">
              <el-input v-model="form.orgCode"/>
            </el-form-item>
            <el-form-item prop="orgName" :label="$t('jbx.organizations.name')" :required="true">
              <el-input v-model="form.orgName"/>
            </el-form-item>
            <el-form-item prop="fullName" :label="$t('jbx.organizations.fullName')" :required="true">
              <el-input v-model="form.fullName"/>
            </el-form-item>
            <el-form-item prop="type" :label="$t('jbx.organizations.type')" :required="true">
              <el-select v-model="form.type" clearable>
                <el-option
                    v-for="dict in orgType"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item prop="parentId" :label="$t('jbx.organizations.parentName')">
              <el-tree-select
                  v-model="form.parentId"
                  :data="deptOptions"
                  :props="defaultProps"
                  check-strictly
                  value-key="id"
                  :placeholder="t('org.placeholder.parent')"
                  @change="handleTreeChange"
              />
            </el-form-item>
            <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
              <el-input-number v-model="form.sortIndex" :min="0" :max="100000"></el-input-number>
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
          </el-tab-pane>
          <el-tab-pane :label="$t('jbx.organizations.tabExtra')" name="second">
            <el-form-item prop="codePath" :label="$t('jbx.organizations.codePath')">
              <el-input v-model="form.codePath"/>
            </el-form-item>
            <el-form-item prop="namePath" :label="$t('jbx.organizations.namePath')">
              <el-input v-model="form.namePath"/>
            </el-form-item>
            <el-form-item prop="level" :label="$t('jbx.organizations.level')">
              <el-input v-model="form.level"/>
            </el-form-item>
            <el-form-item prop="division" :label="$t('jbx.organizations.division')">
              <el-input v-model="form.division"/>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane :label="$t('jbx.organizations.tabAddress')" name="third">
            <el-form-item prop="country" :label="$t('jbx.organizations.country')">
              <el-input v-model="form.country"/>
            </el-form-item>
            <el-form-item prop="region" :label="$t('jbx.organizations.region')">
              <el-input v-model="form.region"/>
            </el-form-item>
            <el-form-item prop="locality" :label="$t('jbx.organizations.locality')">
              <el-input v-model="form.locality"/>
            </el-form-item>
            <el-form-item prop="street" :label="$t('jbx.organizations.street')">
              <el-input v-model="form.street"/>
            </el-form-item>
            <el-form-item prop="address" :label="$t('jbx.organizations.address')">
              <el-input v-model="form.address"/>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane :label="$t('jbx.organizations.tabContact')" name="fourth">
            <el-form-item prop="contact" :label="$t('jbx.organizations.contact')">
              <el-input v-model="form.contact"/>
            </el-form-item>
            <el-form-item prop="phone" :label="$t('jbx.organizations.phone')">
              <el-input v-model="form.phone"/>
            </el-form-item>
            <el-form-item prop="email" :label="$t('jbx.organizations.email')">
              <el-input v-model="form.email"/>
            </el-form-item>
            <el-form-item prop="fax" :label="$t('jbx.organizations.fax')">
              <el-input v-model="form.fax"/>
            </el-form-item>
            <el-form-item prop="postalCode" :label="$t('jbx.organizations.postalCode')">
              <el-input v-model="form.postalCode"/>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
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
import modal from "@/plugins/modal";
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch,
  defineComponent
} from "vue";

;
import {addDept, getDept, updateDept} from "@/api/system/dept";
import {useI18n} from "vue-i18n";
import exAttrsForm from "@/components/expandAttrsForm.vue";
import {ElForm} from "element-plus";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const orgRef: any = ref<InstanceType<typeof ElForm> | null>(null);
const childEx: any = ref<InstanceType<typeof exAttrsForm> | null>(null);
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: Boolean,
  formId: {
    default: undefined
  },
  orgType: {
    type: Array as () => Array<{ value: string; label: string }>,
    default: () => [],
  },
  deptOptions: {
    type: Array,
    default: [],
  },
  currentTreeId: {
    type: String,
    default: undefined,
  },
  currentTreeParentId: {
    type: String,
    default: null,
  },
  currentTreeInstId: {
    type: String,
    default: null,
  }
});


const emit: any = defineEmits(['dialogOfClosedMethods']);

const data: any = reactive({
  form: {
    sortIndex: 0,
    parentId: props.currentTreeId,
    status: 1,
    extraAttrs: null,
    orgCode: null,
    orgName: null,
    fullName: null,
    codePath: null,
    namePath: null,
    type: null,
    level: null,
    division: null,
    country: null,
    region: null,
    locality: null,
    street: null,
    address: null,
    contact: null,
    phone: null,
    email: null,
    fax: null,
    postalCode: null,
    parentName: null
  },
  rules: {
    orgName: [
      {required: true, message: t('jbx.organizations.codeError'), trigger: "blur"},
    ],
    orgCode: [
      {required: true, message: t('jbx.organizations.nameError'), trigger: "blur"},
    ],
    fullName: [
      {required: true, message: t('jbx.organizations.fullError'), trigger: "blur"},
    ],
    type: [
      {required: true, message: t('jbx.organizations.typeError'), trigger: ['blur', 'change']},
    ],
   /* parentId: [
      {required: true, message: t('jbx.organizations.parentError'), trigger: "blur"},
    ]*/
  }
})

const {form, rules} = toRefs(data)
const dialogStatus: any = ref(false);
const activeName: any = ref('first');
const isGetData: any = ref(false)
// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          //修改,查询当前组织
          getDept(props.formId).then((res: any) => {
            if (res.code === 0) {
              form.value = res.data;
              isGetData.value = true;
            }
          })
        } else {
          reset();
          isGetData.value = true;
        }
      } else {
        isGetData.value = false
        reset();
      }
      activeName.value = 'first'
    },
    {immediate: true}
);
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
  disabled: disabledFilter
})

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

/** 重置操作表单 */
function reset(): any {
  activeName.value = 'first'
  form.value = {
    sortIndex: 0,
    parentId: props.currentTreeId,
    status: 1,
    extraAttrs: null,
    orgCode: null,
    orgName: null,
    fullName: null,
    codePath: null,
    namePath: null,
    type: null,
    level: null,
    division: null,
    country: null,
    region: null,
    locality: null,
    street: null,
    address: null,
    contact: null,
    phone: null,
    email: null,
    fax: null,
    postalCode: null,
    parentName: null,
  };
  orgRef?.value?.resetFields();
}

/** 提交表单 */
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

  orgRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation: any = props.formId ? updateDept : addDept;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}

function disabledFilter(data: any, node: any): any {
  if (!props.formId) {
    return false;
  }

  if (props.currentTreeParentId == null || props.currentTreeParentId == '-1' || props.currentTreeParentId == '0'
      || props.formId == props.currentTreeInstId) {
    return true;
  } else {
    return new RegExp(`/${props.formId}(/|$)`).test(data.codePath)
  }
}

function handleTreeChange(value: any): any {
  const findLabel: any = (nodes: any, value: any) => {
    for (let node of nodes) {
      if (node.key === value) {
        return node.title;
      }
      if (node.children) {
        const title: any = findLabel(node.children, value);
        if (title) {
          return title;
        }
      }
    }
    return null;
  };

  form.value.parentName = findLabel(props.deptOptions, value);
}

defineComponent({
  name: 'OrgEdit'
})
</script>

<style lang="scss" scoped>

</style>
