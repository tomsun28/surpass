<template>
  <div class="user-info-head" @click="editCropper()">
    <img :src="options.img" :title="t('profile.uploadAvatar')" class="img-circle img-lg"/>
    <el-dialog :title="title" v-model="open" width="800px" append-to-body @opened="modalOpened" @close="closeDialog">
      <el-row>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <vue-cropper
              ref="cropper"
              :img="options.img"
              :info="true"
              :autoCrop="options.autoCrop"
              :autoCropWidth="options.autoCropWidth"
              :autoCropHeight="options.autoCropHeight"
              :fixedBox="options.fixedBox"
              :outputType="options.outputType"
              @realTime="realTime"
              v-if="visible"
          />
        </el-col>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="options.previews.url" :style="options.previews.img"/>
          </div>
        </el-col>
      </el-row>
      <br/>
      <el-row>
        <el-col :lg="2" :md="2">
          <el-upload
              action="#"
              :http-request="requestUpload"
              :show-file-list="false"
              :before-upload="beforeUpload"
          >
            <el-button>
              {{ t('profile.select') }}
              <el-icon class="el-icon--right">
                <Upload/>
              </el-icon>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{ span: 1, offset: 2 }" :md="2">
          <el-button icon="Plus" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button icon="Minus" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button icon="RefreshLeft" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button icon="RefreshRight" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{ span: 2, offset: 6 }" :md="2">
          <el-button type="primary" @click="uploadImg()">{{ t('text.submit') }}</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import "vue-cropper/dist/index.css";
import {VueCropper} from "vue-cropper";
import {uploadImage} from "@/api/system/user";
import useUserStore from "@/store/modules/user";
import {useI18n} from "vue-i18n";

const userStore: any = useUserStore();
const {proxy} = getCurrentInstance()!;

const open: any = ref(false);
const visible: any = ref(false);
const title: any = ref("修改头像");

const {t} = useI18n();
//图片裁剪数据
const options: any = reactive({
  img: userStore.avatar,     // 裁剪图片的地址
  autoCrop: true,            // 是否默认生成截图框
  autoCropWidth: 200,        // 默认生成截图框宽度
  autoCropHeight: 200,       // 默认生成截图框高度
  fixedBox: true,            // 固定截图框大小 不允许改变
  outputType: "png",         // 默认生成截图为PNG格式
  filename: 'avatar',        // 文件名称
  previews: {}               //预览数据
});

/** 编辑头像 */
function editCropper(): any {
  open.value = true;
}

/** 打开弹出层结束时的回调 */
function modalOpened(): any {
  visible.value = true;
}

/** 覆盖默认上传行为 */
function requestUpload(): any {
}

/** 向左旋转 */
function rotateLeft(): any {
  proxy.$refs.cropper.rotateLeft();
}

/** 向右旋转 */
function rotateRight(): any {
  proxy.$refs.cropper.rotateRight();
}

/** 图片缩放 */
function changeScale(num: any): any {
  num = num || 1;
  proxy.$refs.cropper.changeScale(num);
}

/** 上传预处理 */
function beforeUpload(file: any): any {
  if (file.type.indexOf("image/") == -1) {
    modal.msgError(t('profile.imgTip'));
  } else {
    const reader: any = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      options.img = reader.result;
      options.filename = file.name;
    };
  }
}

/** 上传图片 */
function uploadImg(): any {
  proxy.$refs.cropper.getCropBlob((data: any) =>  {
    let formData: any = new FormData();
    formData.append("uploadFile", data);
    uploadImage(formData).then((response: any) =>  {
      if (response.code === 200) {
        open.value = false;
        options.img = import.meta.env.VITE_APP_BASE_API + response.data;
        userStore.avatar = options.img;
        modal.msgSuccess(t('org.success.update'));
        visible.value = false;
      } else {
        modal.msgError(response.message);
      }
    });
  });
}

/** 实时预览 */
function realTime(data: any): any {
  options.previews = data;
}

/** 关闭窗口 */
function closeDialog(): any {
  options.img = userStore.avatar;
  options.visible = false;
}
</script>

<style lang='scss' scoped>
.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.user-info-head:hover:after {
  content: "+";
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}
</style>
