<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-tabs v-model="activeName" class="demo-tabs">
          <el-tab-pane label="基本信息" name="first">
            <el-form-item label="工号" prop="employeeNumber">
              <el-input v-model="form.employeeNumber" placeholder="请输入工号"/>
            </el-form-item>
            <el-form-item label="姓名" prop="displayName">
              <el-input v-model="form.displayName" placeholder="请输入姓名"/>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" style="width: 100%">
                <el-option
                    v-for="dict in GendersEnum.getAllGenders()"
                    :key="dict.value"
                    :label="dict.name"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.birthDate"
                              type="date"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择出生日期">
              </el-date-picker>
            </el-form-item>
           
            <el-form-item label="证件类型">
              <el-select v-model="form.idType" style="width: 100%" :required="true">
                <el-option v-for="item in usersIdType" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
            <el-form-item label="证件编码" prop="idCardNo" :required="true">
              <el-input v-model="form.idCardNo" placeholder="请输入证件编码"/>
            </el-form-item>
            <el-form-item label="员工类型" :required="true" prop="employeeType">
              <el-select v-model="form.employeeType" style="width: 100%">
                <el-option v-for="item in employee_types" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
              <el-form-item label="员工状态" :required="true" prop="employeeStatus">
                <el-select v-model="form.employeeStatus" style="width: 100%">
                  <el-option v-for="item in employee_statuses" :label="item.label" :value="item.value"/>
                </el-select>
              </el-form-item>
            <el-form-item prop="departmentId" label="所属部门">
              <el-tree-select
                  ref="resTreeRef"
                  v-model="form.departmentId"
                  :data="orgOptions"
                  :props="defaultProps"
                  check-strictly
                  value-key="id"
                  show-checkbox
                  clearable
                  :default-checked-keys="cheackdData"
              />
            </el-form-item>
            <el-form-item label="经理编号" prop="managerId">
              <el-input v-model="form.managerId" placeholder="请输入经理编号"/>
            </el-form-item>
            <el-form-item label="职务" prop="jobTitle">
              <el-input v-model="form.jobTitle" placeholder="请输入职务"/>
            </el-form-item>
            <el-form-item label="电话号码" prop="mobile">
              <el-input v-model="form.mobile" placeholder="请输入电话号码"/>
            </el-form-item>
            <el-form-item label="邮箱地址" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱地址"/>
            </el-form-item>
          
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" style="width: 100%">
                <el-option
                    v-for="dict in EducationsEnum.getAllEducations()"
                    :key="dict.value"
                    :label="dict.name"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="毕业院校" prop="graduateFrom">
              <el-input v-model="form.graduateFrom" placeholder="请输入毕业院校"/>
            </el-form-item>
            <el-form-item label="毕业时间" prop="graduateDate">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.graduateDate"
                              type="date"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择毕业时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="住址" prop="homeAddress">
              <el-input v-model="form.homeAddress" placeholder="请输入住址"/>
            </el-form-item>
            
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.entryDate"
                              type="date"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择入职日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="离职日期" prop="quitDate">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.quitDate"
                              type="date"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择离职日期">
              </el-date-picker>
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
          <el-tab-pane label="工资信息" name="second">
            <el-form-item label="基本工资" prop="payBasic" v-if="form.employeeType === 'NORMAL'|| form.employeeType === 'INTERN' || form.employeeType === 'RETIREMENT'">
              <el-input-number :min="0" :precision="2" v-model="form.payBasic" style="width: 200px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>

            <el-form-item label="绩效奖金" prop="payMerit" v-if="form.employeeType === 'NORMAL'|| form.employeeType === 'INTERN' || form.employeeType === 'RETIREMENT'">
              <el-input-number :min="0" :precision="2" v-model="form.payMerit" style="width: 200px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>

            <el-form-item label="岗位工资" prop="payPost" v-if="form.employeeType === 'NORMAL'|| form.employeeType === 'INTERN' || form.employeeType === 'RETIREMENT'">
              <el-input-number :min="0" :precision="2" v-model="form.payPost" style="width: 200px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>

            <el-form-item label="劳务费" prop="laborFee" v-if="form.employeeType === 'PARTTIME'">
            <el-input-number :min="0" :precision="2" v-model="form.laborFee" style="width: 200px">
              <template #suffix>
                <span>元</span>
              </template>
            </el-input-number>
          </el-form-item>
          <el-form-item label="社保缴费基数"  v-if="form.employeeType === 'NORMAL'">
            <el-radio-group v-model="form.payBaseRule" >
              <el-radio :value="0" size="large">统一配置标准基数</el-radio>
              <el-radio :value="1" size="large">自定义缴纳基数</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="缴费基数" prop="payBaseNumber" v-if="form.payBaseRule === 1 &&form.employeeType === 'NORMAL'" >
              <el-input-number :min="0" :precision="2" v-model="form.payBaseNumber" style="width: 200px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
          </el-form-item>
          <el-form-item label="银行名称" prop="bankName">
              <el-input v-model="form.bankName" placeholder="请输入银行名称"/>
          </el-form-item>
          <el-form-item label="银行卡号" prop="bankCardNo">
              <el-input v-model="form.bankCardNo" placeholder="请输入银行卡"/>
          </el-form-item>
          <el-form-item label="社保卡号" prop="insuranceFundCard">
              <el-input v-model="form.insuranceFundCard" placeholder="请输入社保卡号"/>
          </el-form-item>
          <el-form-item label="医保卡号" prop="medicalCard">
              <el-input v-model="form.medicalCard" placeholder="请输入医保卡号"/>
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
import {useI18n} from "vue-i18n";
import {getCurrentInstance, reactive, ref, toRefs, watch} from "vue";
import {ElForm} from "element-plus";
import * as GendersEnum from "@/utils/enums/GendersEnum";
import * as EducationsEnum from "@/utils/enums/EducationsEnum";
import booksSetStore from "@/store/modules/bookStore";
import {addEmployee, getEmployee, updateEmployee} from "@/api/system/hr/employee";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
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
  currentTreeParentId: {
    type: String,
    default: null,
  },
  orgOptions: {
    type: Array,
    default: [],
  },
  usersIdType: {
    type: Array<any>,
    default: [],
  },
  employee_types: {
    type: Array<any>,
    default: [],
  },
  employee_statuses: {
    type: Array<any>,
    default: [],
  },
})

const cheackdData: any = ref<any>([]);
const booksSet = booksSetStore()
const data: any = reactive({
  form: {
    status: 1,
    gender: 0,
    payBaseRule: 0,
  },
  rules: {
    displayName: [
      {required: true, message: "姓名不能为空", trigger: "blur"},
    ],
    departmentId: [
      {required: true, message: "所属部门不能为空", trigger: "blur"},
    ],
    employeeNumber: [
      {required: true, message: "工号不能为空", trigger: "blur"},
    ],
    idCardNo: [
      {required: true, message: "证件编码不能为空", trigger: "blur"},
    ],
    idType: [
      {required: true, message: "证件类型不能为空", trigger: "blur"},
    ]
  }
})

const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name'
})

const {form, rules} = toRefs(data);
const isEdit: any = ref(false);
const activeName = ref('first')
const dialogStatus: any = ref(false);
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          isEdit.value = true;
          getEmployee(props.formId).then((res: any) => {
            form.value = res.data;
          });
        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

/** 重置操作表单 */
function reset(): any {
  form.value = {
    status: 1,
    gender: 0,
    payBaseRule: 0
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
      const operation: any = props.formId ? updateEmployee : addEmployee;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  children?: TreeNode[]; // 子节点可能不存在
}
</script>

<style lang="scss" scoped>
.optionsLabel {
  margin-left: 10px;
}

.section-title {
  font-size: 16px; /* text-lg */
  font-weight: 500; /* font-medium */
  margin-bottom: 4px; /* mb-4 */
}
</style>
