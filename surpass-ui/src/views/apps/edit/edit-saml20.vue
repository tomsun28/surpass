<template>
  <el-row :gutter="30">
    <el-col :span="24">
      <el-form-item prop="spAcsUrl" :label="$t('jbx.apps.samlSpacsurl')+':'">
        <el-input v-model="form.spAcsUrl"/>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="binding" :label="$t('jbx.apps.samlBinding')+':'">
        <el-select v-model="form.binding" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in binding_types"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="entityId" :label="$t('jbx.apps.samlEntityid')+':'">
        <el-input v-model="form.entityId"/>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="audience" :label="$t('jbx.apps.samlAudience')+':'">
        <el-input v-model="form.audience"/>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item prop="issuer" :label="$t('jbx.apps.samlIssuer')+':'">
        <el-input v-model="form.issuer"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="signature" :label="$t('jbx.apps.samlSignature')+':'">
        <el-select v-model="form.signature" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in encryption_algorithm_SAML"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="digestMethod" :label="$t('jbx.apps.samlDigestmethod')+':'">
        <el-select v-model="form.digestMethod" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in apps_abstracting"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="encrypted" :label="$t('jbx.apps.samlEncrypted')+':'">
        <el-select v-model="form.encrypted" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in app_encryption_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="validityInterval" :label="$t('jbx.apps.samlValidityinterval')+':'">
        <el-input v-model="form.validityInterval">
          <template slot="append">{{ $t('jbx.text.second') }}</template>
        </el-input>
      </el-form-item>
    </el-col>


    <el-col :span="12">
      <el-form-item prop="nameidFormat" :label="$t('jbx.apps.samlNameidformat')+':'">
        <el-select v-model="form.nameidFormat" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in app_nameid_format"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="nameIdConvert" :label="$t('jbx.apps.samlNameidconvert')+':'">
        <el-select v-model="form.nameIdConvert" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in apps_nameid_convert"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="fileType" :label="$t('jbx.apps.samlFiletype')+':'">
        <el-select v-model="form.fileType" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in apps_cert_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="binding_types" :label="$t('jbx.apps.samlMetadata')+':'">
        <el-input v-if="form.fileType === 'metadata_url'" v-model="form.metaUrl"></el-input>
        <file-upload v-else v-model="form.metaFileId" :limit="1" :isShowTip="false">
          <el-button size="small" type="primary" plain icon="el-icon-upload2">{{ $t('jbx.text.upload') }}</el-button>
        </file-upload>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="certIssuer" :label="$t('jbx.apps.samlCertissuer')+':'">
        <el-input disabled v-model="form.certIssuer"></el-input>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="certExpiration" :label="$t('jbx.apps.samlCertexpiration')+':'">
        <el-input disabled v-model="form.certExpiration"></el-input>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item prop="certSubject" :label="$t('jbx.apps.samlCertsubject')+':'">
        <el-input disabled v-model="form.certSubject"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {
  encryption_algorithm_SAML, binding_types,
  apps_cert_type, apps_nameid_convert, app_nameid_format, app_encryption_status, apps_abstracting
} = proxy.useDict('encryption_algorithm_SAML', 'binding_types',
    'apps_cert_type', 'apps_nameid_convert', 'app_nameid_format', 'app_encryption_status', 'apps_abstracting')
</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
export default {
  name: "AppsEditOpenIDConnect",
  dicts: ['encryption_algorithm_SAML', 'binding_types',
    'apps_cert_type', 'apps_nameid_convert', 'app_nameid_format', 'app_encryption_status', 'apps_abstracting'],
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
  methods: {},
}
</script>
