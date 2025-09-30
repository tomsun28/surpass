<template>
    <el-row :gutter="30">
      <el-col :span="24">
        <el-form-item prop="service" :label="$t('jbx.apps.casService')+':'">
          <el-input v-model="form.service"/>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item prop="callbackUrl" :label="$t('jbx.apps.casCallbackurl')+':'">
          <el-input v-model="form.callbackUrl"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="casUser" :label="$t('jbx.apps.casCasuser')+':'">
          <el-select v-model="form.casUser" placeholder="" clearable
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
        <el-form-item prop="expires" :label="$t('jbx.apps.casExpires')+':'">
          <el-input v-model="form.expires">
            <template slot="append">{{$t('jbx.text.second')}}</template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {apps_ret_account} =
    proxy.useDict('apps_ret_account')
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
      default: function (){
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

  },
}
</script>
