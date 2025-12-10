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
                  disabled
                  v-model="form.clientSecret"
                  :placeholder="isEdit ? '不修改请留空' : '请输入客户端密钥'"/>
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

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="accessTokenValidity" label="Token有效期(秒)">
              <el-input-number v-model="form.accessTokenValidity" :min="60" :max="86400" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="refreshTokenValidity" label="刷新Token有效期(秒)">
              <el-input-number v-model="form.refreshTokenValidity" :min="3600" :max="604800" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="expireTime" label="账号过期时间">
              <el-date-picker
                  v-model="form.expireTime"
                  type="datetime"
                  placeholder="选择过期时间"
                  style="width: 100%"
                  value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
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
import {addClient, getClient, updateClient} from "@/api/api-service/client";
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
    accessTokenValidity: 7200,
    refreshTokenValidity: 86400,
    expireTime: null,
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
