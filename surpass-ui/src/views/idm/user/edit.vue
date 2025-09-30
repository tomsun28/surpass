<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="userRef" label-width="100px" inline-message>
        <el-tabs v-model="activeName" :stretch="true">
          <el-tab-pane :label="$t('jbx.organizations.tabBasic')" name="first">
            <el-row :gutter="gutter">
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.displayName')" :required="true" prop="displayName">
                  <el-input v-model="form.displayName"></el-input>
                </el-form-item>
                <el-form-item :label="$t('jbx.users.username')" :required="true" prop="username">
                  <el-input v-model="form.username"></el-input>
                </el-form-item>
                <el-form-item :label="$t('jbx.users.password')" v-if="isPass" :required="true" prop="password">
                  <el-input v-model="form.password"></el-input>
                </el-form-item>
                <el-form-item :label="$t('jbx.users.gender')">
                  <el-radio-group v-model="form.gender">
                    <el-radio-button value="2">{{ $t('jbx.users.genderMale') }}</el-radio-button>
                    <el-radio-button value="1">{{ $t('jbx.users.genderFemale') }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.picture')">
                  <el-upload
                      action="fakeAction"
                      accept="image/jpg,image/jpeg"
                      class="avatar-uploader"
                      name="uploadFile"
                      :http-request="doUploadAvatar"
                      :before-upload="beforeAvatarUpload"
                      :show-file-list="false">
                    <img v-if="previewImage" :src="previewImage" class="avatar" alt="">
                    <el-icon v-else class="avatar-uploader-icon">
                      <Plus/>
                    </el-icon>
                  </el-upload>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="gutter">
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.mobile')">
                  <el-input v-model="form.mobile"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.email')">
                  <el-input v-model="form.email"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="gutter">
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.userType')" :required="true" prop="userType">
                  <el-select v-model="form.userType" style="width: 100%">
                    <el-option v-for="item in users_type" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.userstate')" :required="true" prop="userState">
                  <el-select v-model="form.userState" style="width: 100%">
                    <el-option v-for="item in users_state" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="gutter">
              <el-col :span="12">
                <el-form-item :label="$t('jbx.text.sortIndex')" :required="true" prop="sortIndex">
                  <el-input-number v-model="form.sortIndex" style="width: 100%" :min="0"
                                   :max="100000"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('jbx.users.status')" :required="true" prop="status">
                  <el-select v-model="form.status" style="width: 100%">
                    <el-option v-for="item in status" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="gutter">
              <el-col :span="span">
                <el-form-item :label="$t('jbx.users.timeZone')">
                  <el-input v-model="form.timeZone"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item :label="$t('jbx.users.preferredLanguage')">
                  <el-input v-model="form.preferredLanguage"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="gutter">
              <el-col :span="span">
                <el-form-item :label="$t('jbx.users.website')">
                  <el-input v-model="form.webSite"/>
                </el-form-item>
              </el-col>
            </el-row>
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
import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import {addUser, getImage, getUser, updateUser, uploadImage} from "@/api/system/user";
import exAttrsForm from "@/components/expandAttrsForm.vue";
import defAva from "@/assets/images/profile.png";
import {ElForm, ElMessage} from 'element-plus';
import modal from "@/plugins/modal"

const {t} = useI18n()
const userRef: any = ref<InstanceType<typeof ElForm> | null>(null);
const childEx: any = ref<InstanceType<typeof exAttrsForm> | null>(null);

defineOptions({
  name: 'UserEdit'
})

const {proxy} = getCurrentInstance()!;

const emit: any = defineEmits(['dialogOfClosedMethods']);

const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: Boolean,
  formId: {
    default: undefined
  },
  users_type: {
    type: Array<any>,
    default: [],
  },
  users_state: {
    type: Array<any>,
    default: [],
  },
  status: {
    type: Array<any>,
    default: [],
  },
  users_idType: {
    type: Array<any>,
    default: [],
  },
  users_married: {
    type: Array<any>,
    default: [],
  }
})

const data: any = reactive({
  form: <any>{
    gender: 1,
    extraAttrs: undefined,
    pictureId: undefined,
    department: undefined
  },
  rules: {
    username: [
      {required: true, message: t('jbx.users.usernameError'), trigger: "blur"},
    ],
    displayName: [
      {required: true, message: t('jbx.users.displayNameError'), trigger: "blur"},
    ],
    password: [
      {required: true, message: t('jbx.users.passwordError'), trigger: "blur"},
    ],
    userState: [
      {required: true, message: t('jbx.users.userStateError'), trigger: ['blur', 'change']},
    ],
    userType: [
      {required: true, message: t('jbx.users.userTypeError'), trigger: ['blur', 'change']},
    ],
    status: [
      {required: true, message: t('jbx.users.statusError'), trigger: ['blur', 'change']},
    ],
    sortIndex: [
      {required: true, message: t('jbx.users.sortIndexError'), trigger: ['blur', 'change']},
    ]
  },
})

const {form, rules} = toRefs(data)
const dialogStatus: any = ref(false);
const activeName: any = ref('first');
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
})
const gutter: any = ref(20);
//密码输入
const isPass: any = ref(true);
const previewImage: any = ref<any>(undefined)
const span: any = ref(12)
const isGetData: any = ref(false)


// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          isPass.value = false;
          getUser(props.formId).then((res: any) => {
            form.value = res.data;
            if (res.data.pictureId === null || res.data.pictureId === '') {
              previewImage.value = defAva;
            } else {
              getImage(res.data.pictureId).then((imageRes: any) => {
                if (imageRes.code === 0) {
                  previewImage.value = imageRes.data;
                } else {
                  previewImage.value = defAva;
                }
              }).catch((error: any) => {
                previewImage.value = defAva;
              });
            }
            isGetData.value = true;
          })
          //修改,查询当前用户
        } else {
          reset();
          isGetData.value = true;
        }
      } else {
        reset();
        isGetData.value = false;
      }
      activeName.value = 'first'
    },
    {immediate: true}
);

function reset(): any {
  isPass.value = true;
  form.value = {
    extraAttrs: undefined,
    gender: 1,
    pictureId: undefined,
    department: undefined
  };
  previewImage.value = undefined;
  userRef?.value?.resetFields();
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

  userRef?.value?.validate((valid: any) => {
    if (valid) {
      form.value.extraAttrs = childEx?.value?.getData();
      const operation: any = props.formId ? updateUser : addUser;
      const successMessage: any = props.formId ? t('org.success.update') : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}

/** 上传头像前检查 */
function beforeAvatarUpload(rawFile: any): any {
  if (rawFile.type !== 'image/jpeg') {
    modal.msgError('Avatar picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 4) {
    modal.msgError('Avatar picture size can not exceed 4MB!')
    return false
  }
  return true
}

function doUploadAvatar(item: any): any {
  let formData: any = new FormData();
  formData.append("uploadFile", item.file);
  uploadImage(formData).then((res: any) => {
    if (res.code === 0) {
      previewImage.value = URL.createObjectURL(item.file);
      form.value.pictureId = res.data;
    }
  })
}

</script>

<style lang="scss" scoped>
.avatar-uploader {
  width: 128px;
  height: 128px;
  border: 3px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 48px;
  color: #8c939d;
  width: 128px;
  height: 128px;
  line-height: 128px;
  text-align: center;
}

.avatar {
  width: 128px;
  height: 128px;
  display: block;
}
</style>



