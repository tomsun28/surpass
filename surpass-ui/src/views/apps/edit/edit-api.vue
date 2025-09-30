<template>
  <el-row :gutter="30">
    <el-col :span="12">
      <el-form-item prop="principal" :label="$t('jbx.apps.principal')+':'">
        <el-input v-model="form.principal"/>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item prop="credentials" :label="$t('jbx.apps.credentials')+':'">
        <el-input v-model="form.credentials"/>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item prop="credential" :label="$t('jbx.apps.credential')+':'">
        <el-radio-group v-model="form.credential" size="medium">
          <el-radio-button v-for="(item, index) in apps_form_type" :key="index" :label="item.value" :value="item.value">
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-col>

    <el-col :span="12" v-if="form.type === '2'">
      <el-form-item prop="sharedUsername" :label="$t('jbx.apps.credentialSharedusername')+':'">
        <el-input v-model="form.sharedUsername"/>
      </el-form-item>
    </el-col>

    <el-col :span="12" v-if="form.type === '2'">
      <el-form-item prop="sharedPassword" :label="$t('jbx.apps.credentialSharedpassword')+':'">
        <el-input v-model="form.sharedPassword"/>
      </el-form-item>
    </el-col>

    <el-col :span="12" v-if="form.type === '3'">
      <el-form-item prop="systemUserAttr" :label="$t('jbx.apps.systemUserAttr')+':'">
        <el-select v-model="form.systemUserAttr" placeholder="" clearable
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
  </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {apps_form_algorithm, apps_form_type, apps_ret_account} =
    proxy.useDict('apps_form_algorithm', 'apps_form_type', 'apps_ret_account')
</script>

<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
export default {
  name: "AppsEditOpenIDConnect",
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
