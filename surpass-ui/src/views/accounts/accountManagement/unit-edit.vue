<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form ref="accountRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item :label="t('jbx.apps.name')" :required="true" prop="appName">
          <el-input v-model="form.appName" disabled/>
        </el-form-item>
        <el-form-item :label="t('jbx.users.displayName')" :required="true" prop="displayName">
          <el-input v-model="form.displayName" readonly>
            <template #append>
              <el-button @click="selectOneUser">{{t('jbx.text.select')}}</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item :label="t('jbx.users.username')" :required="true" prop="username">
          <el-input v-model="form.username" disabled/>
        </el-form-item>
        <el-form-item :label="t('jbx.accounts.relatedUsername')" :required="true" prop="relatedUsername">
          <el-input v-model="form.relatedUsername">
            <template #append>
              <el-button @click="selectOneAccount">{{t('jbx.text.select')}}</el-button>
            </template>
          </el-input>
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
  <el-dialog v-model="dialogOpen" width="1100" :title="dialogTitle" align-center :close-on-click-modal="false">
    <select-user ref="selectUserComponent" :title="dialogTitle" :open="dialogOpen"
                 :tree-data="treeData"
                 :deptOptions="deptOptions"
                 @inputUserValue="inputUserValue"
                 @updateFlag="updateFlag"></select-user>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" :disabled="confirmFlag" @click="confirmSelect">{{ t('jbx.text.confirm') }}</el-button>
        <el-button @click="cancelAdd">{{ t('systemCancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog v-model="dialogOpenStg" width="1100" :title="dialogTitle" align-center :close-on-click-modal="false">
    <selectAccount ref="selectAccountComponent" :title="dialogTitle" :open="dialogOpenStg"
                     :app-id="appId"
                     @inputAccountValue="inputAccountValue"
                     @updateFlag="updateFlag"></selectAccount>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" :disabled="confirmFlag" @click="confirmSelectAccount">{{ t('jbx.text.confirm') }}</el-button>
        <el-button @click="cancelAdd">{{ t('systemCancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ElForm } from "element-plus";
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {useI18n} from "vue-i18n";
import {ElMessage} from "element-plus";
import selectUser from './select-users.vue'
import selectAccount from './select-unit.vue'
import {addAccounts} from "@/api/system/account";


const {t} = useI18n()
defineOptions({
  name: 'AccountEdit'
})
const {proxy} = getCurrentInstance()!;
const accountRef = ref<InstanceType<typeof ElForm> | null>(null);
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  appId: {
    type: String,
    default: undefined
  },
  appName: {
    type: String,
    default: undefined
  },
  deptOptions: {
    type: Array,
    default: []
  },
  treeData: {
    type: Array,
    default: []
  }
});

const emit: any = defineEmits(['dialogOfClosedMethods']);
const data: any = reactive({
  form: {
    appId: props.appId,
    appName: props.appName,
    category: 'united',
    strategyId: '',
    createType: "manual",
    sortIndex: 1,
    status: 1
  },
  rules: {
    displayName: [
      {required: true, message: t('jbx.users.displayNameError'), trigger: "blur"},
    ],
    relatedUsername: [
      {required: true, message: t('hintAppNameError'), trigger: "blur"},
    ],
  }
})
const {form, rules} = toRefs(data)
const dialogStatus: any = ref(false);
const dialogOpen: any = ref(false)
const dialogOpenStg: any = ref(false)
const dialogTitle: any = t('jbx.text.select')
const confirmFlag: any = ref(true);

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
      }
      reset();
    },
    {immediate: true}
);


/** 重置操作表单 */
function reset(): any {
  form.value = {
    appId: props.appId,
    appName: props.appName,
    category: 'united',
    strategyId: '',
    createType: "manual",
    sortIndex: 1,
    status: 1
  };
  accountRef?.value?.resetFields();;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function selectOneUser(): any {
  dialogOpen.value = true;
}

function selectOneAccount(): any {
  dialogOpenStg.value = true;
}

function submitForm(): any {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      ElMessage({
        message: res.message, // 这里的 res.message 是后端返回的 HTML 字符串
        type: 'error',
        dangerouslyUseHTMLString: true
      });
    }
  };

  accountRef?.value?.validate((valid: any) =>  {
    if (valid) {
      const operation: any = addAccounts;
      const successMessage: any = t('jbx.operate.success');

      operation(form.value).then((res: any) =>  handleResponse(res, successMessage));

      dialogOfClosedMethods(true);
    }
  });
}


function cancelAdd(): any {
  confirmFlag.value = true;
  dialogOpen.value = false;
  dialogOpenStg.value = false;
}

function updateFlag(val: any): any {
  confirmFlag.value = val;
}

function confirmSelect(): any {
  proxy.$refs.selectUserComponent.confirmSelect();
}

function inputUserValue(val: any): any {
  form.value.userId = val.id;
  form.value.username = val.username;
  form.value.displayName = val.displayName;
  confirmFlag.value = true;
  dialogOpen.value = false;
}

function confirmSelectAccount(): any {
  proxy.$refs.selectAccountComponent.confirmSelect();
}

function inputAccountValue(val: any): any {
  form.value.relatedUsername = val.username;
  confirmFlag.value = true;
  dialogOpenStg.value = false;
}
</script>
