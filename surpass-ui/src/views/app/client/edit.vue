<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="60%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="clientRef" label-width="160px" inline-message>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="clientName" label="客户端名称" :required="true">
              <el-input v-model="form.clientName" placeholder="请输入客户端名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="clientType" label="客户端类型" :required="true">
              <el-select v-model="form.clientType" placeholder="请选择客户端类型" style="width: 100%">
                <el-option v-for="item in clientType"
                           :key="item.value"
                           :value="item.value"
                           :label="item.label"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="isEdit">
          <el-col :span="12">
            <el-form-item prop="clientId" label="客户端ID">
              <el-input v-model="form.clientId" placeholder="请输入客户端ID" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="clientSecret" label="客户端密钥">
              <el-input
                  readonly
                  v-model="form.clientSecret"
                  :placeholder="isEdit ? '不修改请留空' : '请输入客户端密钥'">
                <template #append>
                  <el-button type="primary" @click="generateSecret">生成</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="contactName" label="联系人姓名">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="contactPhone" label="联系电话">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="contactEmail" label="联系邮箱">
              <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="department" label="所属部门">
              <el-input v-model="form.department" placeholder="请输入所属部门"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item prop="ipWhitelist" label="IP白名单">
          <el-input
              v-model="form.ipWhitelist"
              type="textarea"
              :rows="3"
              placeholder="多个IP用逗号分隔，例如：192.168.1.1,192.168.1.2"/>
        </el-form-item>

        <el-form-item prop="description" label="描述信息">
          <el-input
              v-model="form.description"
              type="textarea"
              :rows="3"
              placeholder="请输入描述信息"/>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
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
          </el-col>
        </el-row>

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
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch
} from "vue";
import {addClient, getClient, generate, updateClient} from "@/api/api-service/client";
import {useI18n} from "vue-i18n";
import {ElForm} from "element-plus";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const clientRef: any = ref<InstanceType<typeof ElForm> | null>(null);
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: Boolean,
  formId: {
    default: undefined
  },
  clientType: {
    type: Array as () => Array<{ value: number; label: string }>,
    default: () => [],
  }
});

const emit: any = defineEmits(['dialogOfClosedMethods']);

const data: any = reactive({
  form: {
    status: 1,
    clientName: null,
    clientId: null,
    clientSecret: null,
    clientType: 1,
    contactName: null,
    contactPhone: null,
    contactEmail: null,
    department: null,
    ipWhitelist: null,
    accessTokenValidity: 7200,
    refreshTokenValidity: 86400,
    expireTime: null,
    description: null
  },
  rules: {
    clientName: [
      {required: true, message: "请输入客户端名称", trigger: "blur"},
    ],
    clientType: [
      {required: true, message: "请选择客户端类型", trigger: "change"},
    ],
    contactEmail: [
      {type: 'email', message: "请输入正确的邮箱地址", trigger: "blur"},
    ],
    contactPhone: [
      {
        validator: (rule, value, callback) => {
          if (!value || value.trim() === '') {
            return callback(); // 允许为空
          }
          const phone = value.trim();
          // 手机号正则：11位，13-19开头
          const mobileReg = /^1[3-9]\d{9}$/;
          // 座机正则（允许带区号，含分隔符）
          // 先清理掉非数字字符（保留数字用于长度判断），但校验时用原始格式
          const telClean = phone.replace(/[^\d]/g, '');
          // 座机：总长度 10~12 位（区号3~4位 + 号码7~8位）
          const isTel = telClean.length >= 10 && telClean.length <= 12 && telClean.startsWith('0');
          // 严格匹配常见座机格式（可选，增强用户体验）
          const telPattern = /^(?:\(0\d{2,3}\)|0\d{2,3})[-\s]?\d{7,8}$/;
          if (mobileReg.test(phone)) {
            callback(); // 手机号合法
          } else if (telPattern.test(phone)) {
            callback(); // 座机格式合法
          } else if (isTel && telClean.length >= 10) {
            // 宽松模式：只要是以0开头、10~12位数字，就算合法（如 01012345678）
            callback();
          } else {
            callback(new Error("请输入有效的手机号或座机号码（如：13812345678 或 010-12345678）"));
          }
        },
        trigger: "blur"
      }
    ],
    ipWhitelist: [
      {
        validator: (rule, value, callback) => {
          if (!value || value.trim() === '') {
            return callback(); // 允许为空
          }
          // 支持多个 IP/CIDR，用逗号或分号或换行分隔
          const items = value
              .split(/[,;\s\n]+/)
              .map(item => item.trim())
              .filter(item => item);
          // IP + CIDR 正则（IPv4 地址或 CIDR，简单校验）
          const ipOrCidrReg = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\/(?:3[0-2]|[12]?[0-9]))?$/;
          for (const item of items) {
            if (!ipOrCidrReg.test(item)) {
              return callback(new Error(`"${item}" 不是有效的 IPv4 地址或 CIDR（如 192.168.1.1 或 10.0.0.0/24）`));
            }
          }
          callback();
        },
        trigger: "blur"
      }
    ]
  }
})

const {form, rules} = toRefs(data)
const dialogStatus: any = ref(false)
const isEdit: any = ref(false);

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          isEdit.value = true;
          // 查询当前客户端
          getClient(props.formId).then((res: any) => {
            if (res.code === 0) {
              form.value = res.data;
            }
          })
        } else {
          isEdit.value = false;
          reset();
        }
      } else {
        isEdit.value = false;
        reset();
      }
    },
    {immediate: true}
);

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function generateSecret(val: any): any {
  generate(props.formId).then((res: any) => {
    if (res.code === 0) {
      form.value = res.data;
    }
  })
}

/** 重置操作表单 */
function reset(): any {
  form.value = {
    status: 1,
    clientName: null,
    clientId: null,
    clientSecret: null,
    clientType: 1,
    contactName: null,
    contactPhone: null,
    contactEmail: null,
    department: null,
    ipWhitelist: null,
    description: null
  };
  clientRef?.value?.resetFields();
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

  clientRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation: any = props.formId ? updateClient : addClient;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}
</script>
