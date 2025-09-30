<template>
  <el-row :gutter="30">
    <el-col :span="12">
      <el-form-item prop="issuer" :label="$t('jbx.apps.oauthConnectIssuer')+':'">
        <el-input v-model="form.issuer"/>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="audience" :label="$t('jbx.apps.oauthConnectAudience')+':'">
        <el-input v-model="form.audience"/>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="signature" :label="$t('jbx.apps.oauthConnectSignature')+':'">
        <el-select v-model="form.signature" placeholder="" clearable
                   filterable
                   style="width: 100%" @change="handleSignature">
          <el-option
              v-for="dict in signature_types"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="userInfoResponse" :label="$t('jbx.apps.oauthConnectUserInfoResponse')+':'">
        <el-select v-model="form.userInfoResponse" placeholder="" clearable
                   filterable
                   style="width: 100%">
          <el-option
              v-for="dict in user_api_types"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <el-form-item prop="signatureKey" :label="$t('jbx.apps.oauthConnectSignatureKey')+':'">
        <el-input type="textarea" v-model="form.signatureKey"/>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="algorithm" :label="$t('jbx.apps.oauthConnectAlgorithm')+':'">
        <el-select v-model="form.algorithm" placeholder="" clearable
                   @change="handleAlgorithm"
                   filterable
                   style="width: 100%">
          <el-option
              v-for="dict in encryption_algorithm"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="encryptionMethod" :label="$t('jbx.apps.oauthConnectEncryptionMethod')+':'">
        <el-select v-model="form.encryptionMethod" placeholder="" clearable
                   filterable
                   style="width: 100%">
          <el-option
              v-for="dict in encryption_method"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <el-form-item prop="algorithmKey" :label="$t('jbx.apps.oauthConnectAlgorithmKey')+':'">
        <el-input type="textarea" v-model="form.algorithmKey"/>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {
  signature_types, user_api_types, encryption_method, encryption_algorithm
} = proxy.useDict('signature_types', 'user_api_types', 'encryption_method', 'encryption_algorithm')
</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  setGenerateSecretMethod
} from "@/api/system/apps";

export default {
  name: "AppsEditOpenIDConnect",
  dicts: ['signature_types', 'user_api_types', 'encryption_method', 'encryption_algorithm'],
  props: {
    form: {
      type: Object,
      require: true,
      default: function () {
        return {}
      }
    }
  },
  data() {
    return {
      // 表单校验
      rules: {},
    }
  },
  created() {

  },
  methods: {
    handleSignature(val) {
      setGenerateSecretMethod(val, this.form.id).then((res: any) =>  {
        this.form.signatureKey = res.data
      })
    },
    handleAlgorithm(val) {
      setGenerateSecretMethod(val, this.form.id).then((res: any) =>  {
        this.form.algorithmKey = res.data
      })
    }
  },
}
</script>
