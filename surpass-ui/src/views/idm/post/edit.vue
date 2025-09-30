<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form ref="postRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item :label="t('jbx.posts.postCode')" :required="true" prop="postCode">
          <el-input v-model="form.postCode" />
        </el-form-item>
        <el-form-item :label="t('jbx.posts.postName')" :required="true" prop="postName">
          <el-input v-model="form.postName" />
        </el-form-item>
        <el-form-item :label="$t('jbx.users.department')">
          <el-tree-select
              v-model="form.departmentId"
              :data="deptOptions"
              :props="defaultProps"
              check-strictly
              value-key="id"
              :placeholder="t('jbx.users.department')"
              @change="handleTreeChange"
          />
        </el-form-item>
        <el-form-item :label="$t('jbx.text.sortIndex')" :required="true" prop="sortIndex">
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
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{t('org.cancel')}}</el-button>
        <el-button type="primary" @click="submitForm">{{t('org.confirm')}}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { ElForm } from "element-plus";
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {useI18n} from "vue-i18n";
import {addPost, getPost, updatePost} from "@/api/system/post.js";
import {ElMessage} from "element-plus";
const { t } = useI18n()
defineOptions({
  name: 'PostEdit'
})
const {proxy} = getCurrentInstance()!;
const postRef = ref<InstanceType<typeof ElForm> | null>(null);
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
  deptOptions: {
    type: Array,
    default: [],
  },
  currentTreeId: {
    type: String,
    default: null,
  },
});

const emit: any = defineEmits(['dialogOfClosedMethods']);
const data: any = reactive({
  form: {
    sortIndex: 1,
    status: 1
  },
  rules: {
    postName: [
      { required: true, message: t('jbx.posts.postNameError'), trigger: "blur" },
    ],
    postCode: [
      { required: true, message: t('jbx.posts.postCodeError'), trigger: "blur" },
    ]
  }
})
const {form, rules} = toRefs(data)
const dialogStatus: any = ref(false);
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
})
// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          //修改,查询当前岗位
          getPost(props.formId).then((res: any) =>  {
            form.value = res.data;
          })
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
    sortIndex: 1,
    status: 1,
  };
  postRef?.value?.resetFields();;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
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

  postRef?.value?.validate((valid: any) =>  {
    if (valid) {
      const operation: any = props.formId ? updatePost : addPost;
      const successMessage: any = props.formId ? t('org.success.update') : t('org.success.add');

      operation(form.value).then((res: any) =>  handleResponse(res, successMessage));

      dialogOfClosedMethods(true);
    }
  });
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

  form.value.department = findLabel(props.deptOptions, value);
}
</script>
