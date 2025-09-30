<template>
  <el-row :gutter="30">
    <el-col :span="24">
      <el-form-item prop="registeredRedirectUris" :label="$t('jbx.apps.oauthRegisteredredirecturis')+':'">
        <el-input type="textarea" v-model="form.registeredRedirectUris"/>
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <el-form-item prop="select_authorizedGrantTypes" :label="$t('jbx.apps.oauthGranttypes')+':'">
        <el-select v-model="form.select_authorizedGrantTypes" placeholder="" clearable
                   @change="handleAuthMethod"
                   allow-create
                   default-first-option
                   multiple
                   filterable
                   style="width: 100%">
          <el-option
              v-for="dict in authorizedGrantTypes"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="subject" :label="$t('jbx.apps.oauthSubject')+':'">
        <el-select v-model="form.subject" placeholder="" clearable style="width: 100%">
          <el-option v-for="dict in apps_ret_account"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="select_scope" :label="$t('jbx.apps.oauthScope')+':'">
        <el-select v-model="form.select_scope" placeholder="" clearable
                   allow-create
                   default-first-option
                   multiple
                   filterable
                   style="width: 100%">
          <el-option
              v-for="dict in scope_types"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="approvalPrompt" :label="$t('jbx.apps.oauthApprovalprompt')+':'">
        <el-radio-group v-model="form.approvalPrompt" size="medium">
          <el-radio-button label="force" value="force">{{ $t('jbx.apps.oauthApprovalpromptForce') }}</el-radio-button>
          <el-radio-button label="auto" value="auto">{{ $t('jbx.apps.oauthApprovalpromptAuto') }}</el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="isExtendAttr" label="PKCE">
        <el-radio-group v-model="form.pkce" size="medium">
          <el-radio-button label="yes" value="yes">{{ $t('jbx.text.yes') }}</el-radio-button>
          <el-radio-button label="no" value="no">{{ $t('jbx.text.no') }}</el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="accessTokenValiditySeconds" :label="$t('jbx.apps.oauthAccesstokenvalidityseconds')+':'">
        <el-input placeholder="" v-model="form.accessTokenValiditySeconds">
          <template slot="append">{{ $t('jbx.text.second') }}</template>
        </el-input>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="refreshTokenValiditySeconds" :label="$t('jbx.apps.oauthRefreshtokenvalidityseconds')+':'">
        <el-input placeholder="" v-model="form.refreshTokenValiditySeconds">
          <template slot="append">{{ $t('jbx.text.second') }}</template>
        </el-input>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {
  protocol_type,
  authorizedGrantTypes,
  apps_ret_account,
  scope_types,
  logoutType,
  purview_types,
  inducer_types,
  sys_yes_no
} =
    proxy.useDict('protocol_type', 'authorizedGrantTypes', 'apps_ret_account', 'scope_types', 'logoutType', 'purview_types', 'inducer_types', 'sys_yes_no')
</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
export default {
  name: "AppsEditOauth",
  dicts: ['protocol_type', 'authorizedGrantTypes', 'apps_ret_account', 'scope_types', 'logoutType', 'purview_types', 'inducer_types', 'sys_yes_no'],
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
    if (this.form.authorizedGrantTypes) {
      this.form.select_authorizedGrantTypes = this.form.authorizedGrantTypes.split(',')
    }

    if (this.form.scope) {
      this.form.select_scope = this.form.scope.split(',')
    }
  },
  methods: {
    handleAuthMethod(val) {
      this.form.authorizedGrantTypes = val.join(',')
    },

    handleScopeMethod(val) {
      this.form.scope = val.join(',')
    }

  },
}
</script>
