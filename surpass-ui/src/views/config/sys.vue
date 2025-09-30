<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form style="margin: 20px 0 " label-position="right" label-width="200">
        <template v-for="(item, index) in sysCfgList">
          <el-form-item :key="index" :label="item.configName" required
                        v-if="item.configKey !== 'sys.subject.codes.length'">
            <el-date-picker v-if="['sys.payment.term.current', 'sys.payment.term.start'].indexOf(item.configKey) > -1"
                            v-model="item.configValue"
                            value-format="YYYY-MM" format="YYYY-MM"
                            readonly></el-date-picker>
            <el-switch v-else-if="item.configKey === 'sys.initialize.task'"
                       active-value="true"
                       inactive-value="false"
                       disabled
                       v-model="item.configValue"></el-switch>
            <template v-else-if="item.configKey === 'sys.subject.level'">
              <el-input-number readonly :min="sysCfgMap['sys.subject.level'].level" :max="9"
                               v-model="item.configValue" @change="handleLevel(item, $event)"></el-input-number>
              <div style="width: 100%;margin: 10px 0">编辑各级科目编码长度：</div>
              <template v-for="idx in item.configValue" :key="'len'+ idx">
                <el-input-number readonly style="margin-right: 30px"
                                 :min="sysCfgMap['sys.subject.codes.length'].oldLens[idx-1]"
                                 :max="6"
                                 @change="handleLevel(item, item.configValue)"
                                 v-model="sysCfgMap['sys.subject.codes.length'].lens[idx-1]"></el-input-number>
              </template>
            </template>
            <el-input v-else v-model="item.configValue"></el-input>
          </el-form-item>
        </template>
<!--        <el-form-item>-->
<!--          <el-button :loading="buttonLoading" type="primary" @click="submitSaveForm">{{ t('org.confirm') }}</el-button>-->
<!--        </el-form-item>-->
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="SysConfig">
import {listConfig, addConfig, updateConfig} from "@/api/config/sys";
import {useI18n} from "vue-i18n";
import bookStore from "@/store/modules/bookStore";
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {ElForm, FormInstance} from "element-plus";

const {proxy} = getCurrentInstance()!;
const {t} = useI18n()
const currBookStore = bookStore()
const sysCfgList = ref<any>([]);
const sysCfgMap = ref<any>({});
const loading = ref(true);
const buttonLoading = ref(false);
const total = ref(0);

const configSysRef = ref<FormInstance>();

const dialog = reactive<any>({
  visible: false,
  title: ''
});
const initFormData = {
  bookId: currBookStore.bookId,
  configName: undefined,
  configKey: undefined,
  configType: undefined,
  configValue: undefined,
  remark: undefined,
}
const data: any = reactive({
  form: {...initFormData},
  queryParams: {
    bookId: currBookStore.bookId,
    pageNumber: 1,
    pageSize: 100,
    configName: undefined,
    configKey: undefined,
    configType: undefined
  },
  rules: {
    configName: [
      {required: true, message: "参数名称不能为空", trigger: "blur"}
    ],
    configKey: [
      {required: true, message: "参数键名不能为空", trigger: "blur"}
    ],
    configValue: [
      {required: true, message: "参数键值不能为空", trigger: "blur"}
    ]
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询辅助核算列表 */
function getList() {
  loading.value = true;
  listConfig(queryParams.value).then((response: any) => {
    sysCfgList.value = response.data;
    for (let i = 0; i < sysCfgList.value.length; i++) {
      if (sysCfgList.value[i].configKey === 'sys.subject.codes.length') {
        sysCfgList.value[i].lens = sysCfgList.value[i].configValue.split(",").map(Number);
        sysCfgList.value[i].oldLens = sysCfgList.value[i].configValue.split(",").map(Number);
      } else if (sysCfgList.value[i].configKey === 'sys.subject.level') {
        sysCfgList.value[i].configValue = parseInt(sysCfgList.value[i].configValue)
        sysCfgList.value[i].level = sysCfgList.value[i].configValue
      }
      sysCfgMap.value[sysCfgList.value[i].configKey] = sysCfgList.value[i]
    }
    total.value = response.data.total;
    loading.value = false;

    console.log(sysCfgList.value)
  });
}

function handleLevel(item: any, value: any) {
  if (sysCfgMap.value['sys.subject.codes.length'].lens.length !== item.configValue) {
    sysCfgMap.value['sys.subject.codes.length'].lens.push(2)
    sysCfgMap.value['sys.subject.codes.length'].oldLens.push(2)
  }
  sysCfgMap.value['sys.subject.codes.length'].configValue =
      sysCfgMap.value['sys.subject.codes.length'].lens.slice(0, value).join(",")
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  configSysRef.value?.resetFields();
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

const submitSaveForm = async () => {
  buttonLoading.value = true
  await updateConfig(sysCfgMap.value['sys.subject.level'])
  updateConfig(sysCfgMap.value['sys.subject.codes.length'])
      .finally(() => {
        proxy?.$modal.msgSuccess('操作成功');
        buttonLoading.value = false
      });
}

/** 提交按钮 */
const submitForm = () => {
  configSysRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.configId) {
        await updateConfig(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addConfig(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

getList();
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
