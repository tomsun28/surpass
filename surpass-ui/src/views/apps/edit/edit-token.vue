<template>
  <el-row :gutter="30">
    <el-col :span="24">
      <el-form-item prop="redirectUri" :label="$t('jbx.apps.tokenbasedRedirecturi')+':'">
        <el-input v-model="form.redirectUri"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="tokenType" :label="$t('jbx.apps.tokenbasedTokentype')+':'">
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
      <el-form-item prop="cookieName" :label="$t('jbx.apps.tokenbasedCookiename')+':'">
        <el-input v-model="form.cookieName"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="algorithm" :label="$t('jbx.apps.tokenbasedAlgorithm')+':'">
        <el-select v-model="form.algorithm" placeholder="" clearable
                   style="width: 100%">
          <el-option
              v-for="dict in apps_token_encryption"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="expires" :label="$t('jbx.apps.tokenbasedExpires')+':'">
        <el-input v-model="form.expires">
          <template slot="append">{{ $t('jbx.text.second') }}</template>
        </el-input>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item prop="select_userPropertys" :label="$t('jbx.apps.tokenbasedTokenContent')+':'">
        <el-select v-model="form.select_userPropertys" placeholder="" clearable
                   multiple
                   @change="handlePropertys"
                   style="width: 100%">
          <el-option
              v-for="dict in apps_token_content"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {
  apps_jwt_type, apps_token_encryption, apps_token_content
} = proxy.useDict('apps_jwt_type', 'apps_token_encryption', 'apps_token_content')
</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
export default {
  name: "AppsEditOpenIDConnect",
  dicts: ['apps_jwt_type', 'apps_token_encryption', 'apps_token_content'],
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
  mounted() {
    setTimeout(() => {
      if (this.form.userPropertys) {
        this.form.select_userPropertys = this.form.userPropertys.split(',')
      }
    }, 500)
  },
  methods: {
    handlePropertys(val) {
      this.form.userPropertys = val.join(',')
    }
  },
}
</script>
