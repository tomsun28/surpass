<template>
  <el-dialog title="导入" v-model="importVisible" size="800px" append-to-body
  @close="cancel">
    <div>
      <el-upload
          ref="importUploadRef"
          :auto-upload="props.autoUpload"
          :multiple="false"
          drag
          accept=".xls,.xlsx"
          :before-upload="beforeUpload"
          name="excelFile"
          :http-request="props.httpRequest">
        <el-icon class="el-icon--upload">
          <upload-filled/>
        </el-icon>
        <div class="el-upload__text">
          将文件拖动到这 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            选择文件类型为.xls,.xlsx的文件，且大小不要超过2MB
          </div>
        </template>
      </el-upload>
    </div>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        <el-button type="primary" @click="submit">{{ $t('jbx.text.submit') }}</el-button>
      </div>
    </template>
  </el-dialog>

</template>
<script setup lang="ts">
import {ref, toRefs, defineEmits} from "vue"
import {UploadFilled} from "@element-plus/icons-vue";
import modal from "@/plugins/modal";
import type {UploadInstance} from "element-plus";

const importUploadRef = ref<UploadInstance>()
const emit: any = defineEmits(['cancel', 'submit'])

const props = defineProps({
  httpRequest: {
    type: Function,
    default: null
  },
  autoUpload: {
    type: Boolean,
    default: false
  },
  importVisible: {
    type: Boolean,
    default: false
  }
})
const {importVisible} = toRefs(props);

function beforeUpload(file: any): any {
  let regExp: any = file.name.replace(/.+\./, '');
  let lower: any = regExp.toLowerCase(); //把大写字符串全部转为小写字符串
  let suffix: any = ['xls', 'xlsx'];
  if (suffix.indexOf(lower) === -1) {
    return modal.msgWarning('请上传后缀名为 xls、xlsx 的附件 !');
  }
  let isLt2M: any = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    return modal.msgError('请上传文件大小不能超过 2MB 的附件 !');
  }
}

function submit() {
  importUploadRef.value!.submit()
}

function cancel() {
  emit("cancel")
}

</script>

<style scoped lang="scss">

</style>