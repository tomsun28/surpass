<template>
  <el-row :gutter="30">
    <el-col :span="24">
      <el-form-item prop="redirectUri" :label="$t('jbx.apps.jwtRedirecturi')+':'">
        <el-input v-model="form.redirectUri"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="subject" :label="$t('jbx.apps.jwtSubject')+':'">
        <el-select v-model="form.subject" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in apps_ret_account"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="tokenType" :label="$t('jbx.apps.jwtTokentype')+':'">
        <el-select v-model="form.tokenType" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in apps_jwt_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="jwtName" :label="$t('jbx.apps.jwtJwtname')+':'">
        <el-input v-model="form.jwtName"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="expires" :label="$t('jbx.apps.jwtExpires')+':'">
        <el-input v-model="form.expires">
          <template slot="append">{{ $t('jbx.text.second') }}</template>
        </el-input>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="issuer" :label="$t('jbx.apps.jwtIssuer')+':'">
        <el-input v-model="form.issuer"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="audience" :label="$t('jbx.apps.jwtAudience')+':'">
        <el-input v-model="form.audience"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="signature" :label="$t('jbx.apps.jwtSignature')+':'">
        <el-select v-model="form.signature" placeholder="" clearable
                   @change="handleSignature"
                   style="width: 100%">
          <el-option
              v-for="dict in signature_types"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item prop="signatureKey" :label="$t('jbx.apps.jwtSignaturekey')+':'">
        <el-input type="textarea" v-model="form.signatureKey"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="algorithm" :label="$t('jbx.apps.jwtAlgorithm')+':'">
        <el-select v-model="form.algorithm" placeholder="" clearable
                   style="width: 100%" @change="handleAlgorithm">
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
      <el-form-item prop="encryptionMethod" :label="$t('jbx.apps.jwtEncryptionmethod')+':'">
        <el-select v-model="form.encryptionMethod" placeholder="" clearable
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
      <el-form-item prop="algorithmKey" :label="$t('jbx.apps.jwtAlgorithmkey')+':'">
        <el-input type="textarea" v-model="form.algorithmKey"/>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {apps_ret_account, apps_jwt_type, signature_types, encryption_algorithm, encryption_method} =
    proxy.useDict('apps_ret_account', 'apps_jwt_type', 'signature_types', 'encryption_algorithm', 'encryption_method')
</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  setGenerateSecretMethod
} from "@/api/system/apps";

export default {
  name: "AppsEditOpenIDConnect",
  dicts: ['apps_ret_account', 'apps_jwt_type', 'signature_types', 'encryption_algorithm', 'encryption_method'],
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
