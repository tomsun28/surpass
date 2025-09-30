<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane :label="$t('jbx.apps.tabBasic')" name="1">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="id" :label="$t('jbx.apps.id')">
              <el-input readonly v-model="form.id"/>
            </el-form-item>

            <el-form-item prop="secret" :label="$t('jbx.apps.secret')">
              <el-input v-model="form.secret">
                <template slot="append">
                  <el-button type="primary" @click="handleClientSecret">{{ $t('jbx.text.generate') }}</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.apps.icon')">
              <image-upload v-model="form.iconBase64" useMode="file" @success="handleIcon" :is-show-tip="false"
                            :limit="1"></image-upload>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="appName" :label="$t('jbx.apps.appName')">
              <el-input v-model="form.appName"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="frequently" :label="$t('jbx.apps.frequently')">
              <el-radio-group v-model="form.frequently" size="default">
                <el-radio-button label="yes" value="yes">{{ $t("jbx.text.yes") }}</el-radio-button>
                <el-radio-button label="no" value="no">{{ $t("jbx.text.no") }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="protocol" :label="$t('jbx.apps.protocol')">
              <el-select v-model="form.protocol" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in getProtocolList()"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="category" :label="$t('jbx.apps.category')">
              <el-select v-model="form.category" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in categoryList"
                    :key="dict.id"
                    :label="dict.name"
                    :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item prop="loginUrl" :label="$t('jbx.apps.loginUrl')">
              <el-input v-model="form.loginUrl"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="status" :label="$t('jbx.text.status.status')">
              <el-switch
                  v-model="form.status"
                  :active-value="1"
                  :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="isExtendAttr" :label="$t('jbx.apps.isExtendAttr')">
              <el-radio-group v-model="form.isExtendAttr" size="default">
                <el-radio-button label="0" value="0">{{ $t("jbx.text.no") }}</el-radio-button>
                <el-radio-button label="1" value="0">{{ $t("jbx.text.yes") }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.oauthV20Tab')" name="2"
                   v-if="['OAuth_v2.0', 'OpenID_Connect_v1.0'].indexOf(protocol) > -1">
        <edit-oauth2 :form="form"></edit-oauth2>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.oauthConnectTab')" name="4"
                   v-if="['OpenID_Connect_v1.0'].indexOf(protocol) > -1">
        <edit-open-i-d-connect :form="form"></edit-open-i-d-connect>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.samlTab')" name="5" v-if="['SAML_v2.0'].indexOf(protocol) > -1">
        <edit-saml20 :form="form"></edit-saml20>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.casTab')" name="6" v-if="['CAS'].indexOf(protocol) > -1">
        <edit-cas :form="form"></edit-cas>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.jwtTab')" name="7" v-if="['JWT'].indexOf(protocol) > -1">
        <edit-jwt :form="form"></edit-jwt>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.tokenbasedTab')" name="8" v-if="['Token_Based'].indexOf(protocol) > -1">
        <edit-token :form="form"></edit-token>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.formbasedTab')" name="9" v-if="['Form_Based'].indexOf(protocol) > -1">
        <edit-form :form="form"></edit-form>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.extendapiTab')" name="10" v-if="['Extend_API'].indexOf(protocol) > -1">
        <edit-api :form="form"></edit-api>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.tabExtra')" name="3">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="logoutUrl" :label="$t('jbx.apps.logoutUrl')">
              <el-input v-model="form.logoutUrl"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="logoutType" :label="$t('jbx.apps.logoutType')">
              <el-select v-model="form.logoutType" placeholder="" clearable style="width: 100%">
                <el-option v-for="dict in logoutType"
                           :key="dict.value"
                           :label="dict.label"
                           :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="visible" :label="$t('jbx.apps.visible')">
              <el-select v-model="form.visible" placeholder="" clearable style="width: 100%">
                <el-option v-for="dict in purview_types"
                           :key="dict.value"
                           :label="dict.label"
                           :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="sortIndex" :label="$t('jbx.text.sortIndex')">
              <el-input-number v-model="form.sortIndex"></el-input-number>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="vendor" :label="$t('jbx.apps.vendor')">
              <el-input v-model="form.vendor"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="vendorUrl" :label="$t('jbx.apps.vendorUrl')">
              <el-input v-model="form.vendorUrl"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="isAdapter" :label="$t('jbx.apps.isAdapter')">
              <el-select v-model="form.isAdapter" placeholder="" clearable style="width: 100%">
                <el-option v-for="dict in sys_start_stop"
                           :key="dict.value"
                           :label="dict.label"
                           :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="adapterName" :label="$t('jbx.apps.adapter')">
              <el-input readonly v-model="form.adapterName">
                <template slot="append">
                  <el-button type="primary" @click="handleAdapter">{{ $t("jbx.text.select") }}</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="resourceMgt" :label="$t('jbx.apps.resources')">
              <el-radio-group v-model="form.resourceMgt" size="default">
                <el-radio-button label="false" value="false">{{ $t("jbx.text.no") }}</el-radio-button>
                <el-radio-button label="true" value="true">{{ $t("jbx.text.yes") }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item prop="inducer" :label="$t('jbx.apps.inducer')">
              <el-select v-model="form.inducer" placeholder="" clearable style="width: 100%">
                <el-option v-for="dict in inducer_types"
                           :key="dict.value"
                           :label="dict.label"
                           :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item prop="description" :label="$t('jbx.text.description')">
              <el-input type="textarea" v-model="form.description"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane :label="$t('jbx.apps.tabCustom')" name="11" v-if="form.isExtendAttr === '1'">
        <edit-extend :form="form"></edit-extend>
      </el-tab-pane>
    </el-tabs>


    <el-dialog :title="$t('jbx.text.select')" :visible.sync="openAdapter" width="1000px" append-to-body>
      <select-adapters ref="adapterSelect" v-model="selectAdapter" @submit="handleAdapterSubmit"></select-adapters>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
const {proxy} = getCurrentInstance()
const {protocol_type, authorizedGrantTypes, scope_types, logoutType, purview_types, inducer_types, sys_start_stop} =
    proxy.useDict('protocol_type', 'authorizedGrantTypes', 'scope_types', 'logoutType', 'purview_types', 'inducer_types', 'sys_start_stop')

</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {listCategory} from "@/api/system/category";
import editOauth2 from './edit-oauth2'
import editOpenIDConnect from './edit-openid-connect'
import editSaml20 from './edit-saml20'
import editCas from './edit-cas'
import editJwt from './edit-jwt'
import editToken from './edit-token'
import editForm from './edit-form'
import editApi from './edit-api'
import editExtend from './edit-extend'
import selectAdapters from '@/components/select-adapters.vue'

import {setGenerateSecretBase} from '@/api/system/apps'

export default {
  name: "AppsEdit",
  components: {
    editOauth2,
    editOpenIDConnect,
    editSaml20,
    editCas,
    editJwt,
    editToken,
    editForm,
    editApi,
    selectAdapters,
    editExtend
  },
  props: {
    protocol: {
      type: String,
      default: ''
    },
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
      // 遮罩层
      loading: false,
      openAdapter: false,
      // 表单校验
      rules: {},
      //Tab页
      activeName: '1',
      categoryList: [],
      selectAdapter: {}
    }
  },
  created() {
    listCategory().then((res: any) =>  {
      this.categoryList = res.data
    })
  },
  methods: {
    handleIcon(res) {
      this.form.iconId = res.data
    },

    handleAdapter() {
      this.openAdapter = true
      setTimeout(() => {
        this.$refs.adapterSelect.setProtocol(this.form.protocol)
      }, 200)
    },

    handleAdapterSubmit() {
      this.form.adapterName = this.selectAdapter.name
      this.form.adapterId = this.selectAdapter.id
      this.openAdapter = false
    },

    getCategoryInfo(id) {
      for (let index in this.categoryList) {
        if (this.categoryList[index].id == id) {
          return this.categoryList[index]
        }
      }

      return {}
    },

    handleClientSecret() {
      setGenerateSecretBase().then((res: any) =>  {
        this.form.secret = res.data
        this.form.clientSecret = res.data
      })
    },

    getProtocolList() {
      if (['OAuth_v2.0', 'OAuth_v2.1', 'OpenID_Connect_v1.0'].indexOf(this.form.protocol) > -1) {
        return [
          {
            label: 'OAuth_v2.0',
            value: 'OAuth_v2.0'
          },
          {
            label: 'OAuth_v2.1',
            value: 'OAuth_v2.1'
          },
          {
            label: 'OpenID_Connect_v1.0',
            value: 'OpenID_Connect_v1.0'
          }
        ]
      } else {
        return [
          {
            label: this.form.protocol,
            value: this.form.protocol,
          }
        ]
      }
    }
  },
}
</script>

<style lang="scss" scoped>
::v-deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
  line-height: 110px;
}
</style>
