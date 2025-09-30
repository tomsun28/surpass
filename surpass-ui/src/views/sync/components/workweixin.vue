<template>
  <div class="app-container1">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="150px">
      <el-form-item :label="t('sync.common.label.baseApi')" prop="baseApi">
        <el-input v-model="form.baseApi" placeholder="https://qyapi.weixin.qq.com" maxlength="255"  show-word-limit/>
      </el-form-item>
      <el-form-item :label="t('sync.common.label.rootId')">
        <el-input v-model="form.rootId" placeholder="default root 1" maxlength="64"  show-word-limit/>
      </el-form-item>
      <el-form-item :label="t('sync.common.label.clientId')"  prop="clientId">
        <el-input v-model="form.clientId" :placeholder="t('sync.common.placeholder.clientId')" maxlength="255"  show-word-limit/>
      </el-form-item>
      <el-form-item :label="t('sync.common.label.clientSecret')"  prop="clientSecret">
        <el-input v-model="form.clientSecret" :placeholder="t('sync.common.placeholder.clientSecret')" type="password" show-password />
      </el-form-item>
      <el-form-item :label="t('sync.common.label.userClientSecret')"  prop="userClientSecret">
        <el-input v-model="form.userClientSecret" :placeholder="t('sync.common.placeholder.userClientSecret')"  type="password" show-password />
      </el-form-item>
      <el-form-item :label="t('sync.common.label.status')">
        <el-radio-group v-model.number="form.status">
          <el-radio
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :value="dict.value"
          >{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('sync.common.label.cron')" prop="cron">
        <el-select v-model="form.cron">
          <el-option
              v-for="dict in sys_cron_list"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('text.description')">
        <el-input v-model="form.description" type="textarea" :placeholder="t('text.description')" maxlength="200" show-word-limit></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm"  v-hasPermi="['sync:submit']">{{t('sync.common.button.submitForm')}}</el-button>
        <el-button @click="submitSync"  v-hasPermi="['sync:sync']">{{t('sync.common.button.submitSync')}}</el-button>
        <el-button @click="testLink"  v-hasPermi="['sync:test']">{{t('sync.common.button.testLink')}}</el-button>
        <el-button @click="logs"  v-hasPermi="['sync:logs']">{{t('sync.common.button.logs')}}</el-button>
      </el-form-item>
    </el-form>
    <sync-logs ref="syncLogsRef"/>
  </div>
</template>

<script setup name="Sync-Component-workweixin" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import SyncLogs from "@/views/sync/logsSync/index.vue"
const syncLogsRef: any = ref(undefined);
import {useI18n} from "vue-i18n";

const { t } = useI18n()

import {
  getSynchronizers,
  syncSynchronizers,
  testSynchronizers,
  updateSynchronizers
} from "@/api/system/synchronizers.js";
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);


const { sys_normal_disable ,sys_cron_list } = proxy.useDict("sys_normal_disable","sys_cron_list");

const data: any = reactive({
  form: {
    id: 4,
    baseApi: "https://qyapi.weixin.qq.com",
    rootId: "1",
    clientId: undefined,
    clientSecret: undefined,
    userClientSecret: undefined,
    status: 0,
    cron: "0 0 0/6 * * ?",
    description: undefined
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    objectFrom: undefined,
    status: undefined
  },
  rules: {

    baseApi: [{ required: true, message: t('sync.common.rules.baseApi'), trigger: "blur" }],
    clientId: [{ required: true, message: t('sync.common.rules.clientId'), trigger: "blur" }],
    clientSecret: [{ required: true, message: t('sync.common.rules.clientSecret'), trigger: "blur" }],
    cron: [{ required: true, message: t('sync.common.rules.cron'), trigger: "blur" }],
    userClientSecret: [{ required: true, message: t('sync.common.rules.userClientSecret'), trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 重置新增的表单以及其他数据  */
function reset(): any {
  form.value = {
    id: 4,
    baseApi: "https://qyapi.weixin.qq.com",
    rootId: "1",
    clientId: undefined,
    clientSecret: undefined,
    userClientSecret: undefined,
    status: 0,
    cron: "0 0 0/6 * * ?",
    description: undefined,
  };
}

function get(): any {
  getSynchronizers(form.value.id).then((res: any) =>  {
    if (res.success) {
      form.value = res.data
    }
  });
}
function submitForm(): any {
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      if (form.value.id != undefined) {
        updateSynchronizers(form.value).then((res: any) =>  {
          if (res.success) {
            modal.msgSuccess(t('text.success.edit'));
            get();
          } else {
            modal.msgError(t('text.error.edit'));
          }
        });
      }
    } else {
      modal.msgError(t('sync.common.message.validate'));
    }
  });
}


function submitSync(): any {
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      syncSynchronizers(form.value.id).then((res: any) =>  {
        if (res.success) {
          modal.msgSuccess(t('sync.common.message.syncSuccess'));
        } else {
          modal.msgError(t('sync.common.message.syncFail'));
        }
      });
    } else {
      modal.msgError(t('sync.common.message.validate'));
    }
  });
}

/** 测试连接 */
function testLink(): any {
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      testSynchronizers(form.value).then((res: any) =>  {
        if (res.success) {
          modal.msgSuccess(t('sync.common.message.testSuccess'));
        } else {
          modal.msgError(t('sync.common.message.testFail'));
        }
      });
    } else {
      modal.msgError(t('sync.common.message.validate'));
    }
  });
}

/** 测试连接 */
function logs(): any {
  syncLogsRef.value.openLogs(form.value.id)
}
get()
</script>
