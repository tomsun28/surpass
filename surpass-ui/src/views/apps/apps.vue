<template>
  <div class="app-container">
    <el-card class="common-card">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item :label="$t('jbx.apps.appName') + '：'" prop="appName">
            <el-input
                v-model="queryParams.appName"
                placeholder=""
                clearable
                style="width: 240px"
                @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item :label="$t('jbx.apps.protocol') + '：'" prop="protocol">
            <el-select v-model="queryParams.protocol" placeholder="ALL" clearable style="width: 220px">
              <el-option
                  v-for="dict in protocol_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('jbx.apps.category') + '：'" prop="category" style="width: 220px">
            <el-select v-model="queryParams.category" placeholder="ALL" clearable>
              <el-option
                  v-for="dict in categoryList"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">{{ $t('jbx.text.query') }}</el-button><!-- type="primary" -->
            <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('org.button.deleteBatch') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.apps.icon')" align="center" prop="appIcon">
          <template #default="scope">
            <el-image :src="scope.row.imageUrl" style="width: 50px"></el-image>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.apps.appName')" align="center" prop="appName"/>
        <el-table-column :label="$t('jbx.apps.protocol')" align="center" prop="protocol"></el-table-column>
        <el-table-column :label="$t('jbx.apps.category')" align="center" prop="createType">
          <template #default="scope">
            <span>{{ getCategoryInfo(scope.row.category).name }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex"></el-table-column>
        <el-table-column :label="$t('jbx.users.status')" align="center" prop="status">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.history.systemlogsMessageaction')" align="center"
                         class-name="small-padding fixed-width" width="200">
          <template #default="scope">
            <el-button
                @click="handleUpdate(scope.row)"
            >{{ $t('jbx.text.edit') }}
            </el-button>
            <el-button
                type="danger"
                @click="handleDelete(scope.row)"
            >{{ $t('jbx.text.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-if="total>0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
          :page-sizes="queryParams.pageSizeOptions"
      />
    </el-card>

    <el-dialog :title="title" v-model="openProtocol" width="1000px" append-to-body>
      <el-tabs type="border-card">
        <el-tab-pane label="标准协议">
          <protocol :data="protocolList.filter((t: any) =>  t.type === 'basic')" @click="handleProtocolAdd"></protocol>
        </el-tab-pane>
        <el-tab-pane label="定制协议">
          <protocol :data="protocolList.filter((t: any) =>  t.type === 'custom')" @click="handleProtocolAdd"></protocol>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="160px">
        <edit-form v-if="open" :protocol="form.protocol" :form="form"></edit-form>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import protocol from "@/views/apps/protocol.vue";

const {proxy} = getCurrentInstance()
const {protocol_type} = proxy.useDict("protocol_type")
</script>
<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import EditForm from './edit/edit'

import {
  getApps,
  listApps,
  addApps,
  updateApps,
  delApps,
  getAppsOauth2,
  getAppsOauthInit,
  updateOAuthApps,
  addAppsOAuth,
  getAppsSaml20Init,
  addAppsSaml20,
  getAppsSaml20,
  updateSaml20Apps, getAppsInit
} from "@/api/system/apps";
import {listCategory} from "@/api/system/category";

import {
  getAppsCasInit,
  getAppsCas,
  addAppsCas,
  updateAppsCas
} from '@/api/system/apps-cas'

import {
  getAppsJwtInit,
  getAppsJwt,
  addAppsJwt,
  updateAppsJwt
} from '@/api/system/apps-jwt'

import {
  getAppsTokenbasedInit,
  getAppsTokenbased,
  addAppsTokenbased,
  updateAppsTokenbased
} from '@/api/system/apps-tokenbase'

import {
  getAppsFormbasedInit,
  getAppsFormbased,
  addAppsFormbased,
  updateAppsFormbased
} from '@/api/system/apps-formbased'

import {
  getAppsExtendapiInit,
  getAppsExtendapi,
  addAppsExtendapi,
  updateAppsExtendapi
} from '@/api/system/apps-extendapi'

export default {
  name: "AppsIndex",
  components: {
    EditForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 参数表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openMethod: 'add',
      openProtocol: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        name: undefined,
        description: undefined,
        resources: undefined,
        pageSizeOptions: [10, 20, 50]
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        secret: [
          {required: true, message: " ", trigger: "blur"}
        ],
        iconId: [
          {required: true, message: " ", trigger: "blur"}
        ],
        appName: [
          {required: true, message: " ", trigger: "blur"}
        ],
        protocol: [
          {required: true, message: " ", trigger: "blur"}
        ],
        loginUrl: [
          {required: true, message: " ", trigger: "blur"}
        ],
        status: [
          {required: true, message: " ", trigger: "blur"}
        ],
        registeredRedirectUris: [
          {required: true, message: " ", trigger: "blur"}
        ],
        select_authorizedGrantTypes: [
          {required: true, message: " ", trigger: "blur"}
        ],
        subject: [
          {required: true, message: " ", trigger: "blur"}
        ],
        select_scope: [
          {required: true, message: " ", trigger: "blur"}
        ],
        approvalPrompt: [
          {required: true, message: " ", trigger: "blur"}
        ],
        pkce: [
          {required: true, message: " ", trigger: "blur"}
        ],
        accessTokenValiditySeconds: [
          {required: true, message: " ", trigger: "blur"}
        ],
        refreshTokenValiditySeconds: [
          {required: true, message: " ", trigger: "blur"}
        ],
        spAcsUrl: [
          {required: true, message: " ", trigger: "blur"}
        ],
        binding: [
          {required: true, message: " ", trigger: "blur"}
        ],
        entityId: [
          {required: true, message: " ", trigger: "blur"}
        ],
        audience: [
          {required: true, message: " ", trigger: "blur"}
        ],
        issuer: [
          {required: true, message: " ", trigger: "blur"}
        ],
        signature: [
          {required: true, message: " ", trigger: "blur"}
        ],
        digestMethod: [
          {required: true, message: " ", trigger: "blur"}
        ],
        encrypted: [
          {required: true, message: " ", trigger: "blur"}
        ],
        validityInterval: [
          {required: true, message: " ", trigger: "blur"}
        ],
        nameidFormat: [
          {required: true, message: " ", trigger: "blur"}
        ],
        nameIdConvert: [
          {required: true, message: " ", trigger: "blur"}
        ],
        fileType: [
          {required: true, message: " ", trigger: "blur"}
        ],
        /* certIssuer: [
           {required: true, message: " ", trigger: "blur"}
         ],
         certExpiration: [
           {required: true, message: " ", trigger: "blur"}
         ],
         certSubject: [
           {required: true, message: " ", trigger: "blur"}
         ],*/
        service: [
          {required: true, message: " ", trigger: "blur"}
        ],
        callbackUrl: [
          {required: true, message: " ", trigger: "blur"}
        ],
        casUser: [
          {required: true, message: " ", trigger: "blur"}
        ],
        expires: [
          {required: true, message: " ", trigger: "blur"}
        ],
        tokenType: [
          {required: true, message: " ", trigger: "blur"}
        ],
        jwtName: [
          {required: true, message: " ", trigger: "blur"}
        ],
        signatureKey: [
          {required: true, message: " ", trigger: "blur"}
        ],
        redirectUri: [
          {required: true, message: " ", trigger: "blur"}
        ],
        cookieName: [
          {required: true, message: " ", trigger: "blur"}
        ],
        algorithm: [
          {required: true, message: " ", trigger: "blur"}
        ],
        select_userPropertys: [
          {required: true, message: " ", trigger: "blur"}
        ],
        usernameMapping: [
          {required: true, message: " ", trigger: "blur"}
        ],
        passwordMapping: [
          {required: true, message: " ", trigger: "blur"}
        ],
        principal: [
          {required: true, message: " ", trigger: "blur"}
        ],
        credentials: [
          {required: true, message: " ", trigger: "blur"}
        ]
      },
      categoryList: [],
      protocolList: [
        
      ],
    };
  },
  created() {
    this.getList();
    listCategory().then((res: any) =>  {
      this.categoryList = res.data
    })
  },
  methods: {
    handleProtocolAdd(row) {
      let api: any = this.getApis(row.value).init
      api().then((res: any) =>  {
        this.openMethod = 'add'
        this.form = res.data
        this.form.protocol = row.value
        this.form.status = 1
        this.form.fileType = 'metadata_file'
        this.form.nameIdConvert = '1'
        this.form.nameidFormat = 'persistent';
        this.form.validityInterval = 3600;
        this.form.accessTokenValiditySeconds = 3600;
        this.form.refreshTokenValiditySeconds = 36000;
        this.form.expires = 1800;
        this.form.pkce = 'no';
        this.form.approvalPrompt = 'auto';
        this.form.subject = 'username'
        this.form.category = '1000'
        this.form.frequently = "no"
        this.open = true
        this.openProtocol = false
        this.title = this.$t('jbx.text.add');
      })
    },

    getCategoryInfo(id) {
      for (let index in this.categoryList) {
        if (this.categoryList[index].id == id) {
          return this.categoryList[index]
        }
      }

      return {}
    },

    /** 查询参数列表 */
    getList() {
      this.loading = true;
      listApps(this.queryParams).then((response: any) =>  {
            this.configList = response.data.rows;
            this.total = response.data.records;
            this.loading = false;
          }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        name: undefined,
        description: undefined,
        sortIndex: undefined,
      };
      this.resetForm(this, "form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams.pageSize = 10;
      this.queryParams.pageNumber = 1;
      this.resetForm(this, "queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.openProtocol = true;
      this.title = this.$t('jbx.text.select')
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item: any) =>  item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const configId: any = row.id || this.ids

      let api: any = this.getApis(row.protocol).get
      api(configId).then((response: any) =>  {
        this.form = response.data;
        this.openMethod = 'edit'
        this.open = true;
        this.title = this.$t('jbx.text.edit');
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid: any) =>  {
        if (valid) {
          if (this.openMethod !== 'add') {
            let api: any = this.getApis(this.form.protocol).update
            api(this.form).then((response: any) =>  {
              this.$modal.msgSuccess(this.$t('jbx.alert.update.success'));
              this.open = false;
              this.getList();
            });
          } else {
            let api: any = this.getApis(this.form.protocol).add
            api(this.form).then((response: any) =>  {
              this.$modal.msgSuccess(this.$t('jbx.alert.add.success'));
              this.open = false;
              this.getList();
            });
          }
        } else {
          this.$message({
            message: '请查看其他页面仍有必填字段',
            type: 'warning'
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm(this.$t('systemNoticeDelete'), this.$t('systemNotice'), {
        confirmButtonText: this.$t('jbx.text.confirm'),
        cancelButtonText: this.$t('jbx.text.close'),
        type: 'warning'
      }).then(() => {
        const configIds: any = row.id || this.ids;
        delApps(configIds).then(() => {
          this.getList();
          this.$modal.msgSuccess(this.$t('jbx.alert.delete.success'));
        }).catch(() => {
        });
      }).catch(() => {
      });
    },

    getApis(protocol) {
      let apis: any = {
        init: getAppsInit,
        get: getApps,
        add: addApps,
        update: updateApps,
        del: delApps,
      }

      if (protocol === 'OAuth_v2.0' || protocol === 'OpenID_Connect_v1.0') {
        return {
          init: getAppsOauthInit,
          add: addAppsOAuth,
          get: getAppsOauth2,
          update: updateOAuthApps,
          del: delApps,
        }
      } else if (protocol === 'SAML_v2.0') {
        return {
          init: getAppsSaml20Init,
          add: addAppsSaml20,
          get: getAppsSaml20,
          update: updateSaml20Apps,
          del: delApps,
        }
      } else if (protocol === 'CAS') {
        return {
          init: getAppsCasInit,
          add: addAppsCas,
          get: getAppsCas,
          update: updateAppsCas,
          del: delApps,
        }
      } else if (protocol === 'JWT') {
        return {
          init: getAppsJwtInit,
          add: addAppsJwt,
          get: getAppsJwt,
          update: updateAppsJwt,
          del: delApps,
        }
      } else if (protocol === 'Token_Based') {
        return {
          init: getAppsTokenbasedInit,
          add: addAppsTokenbased,
          get: getAppsTokenbased,
          update: updateAppsTokenbased,
          del: delApps,
        }
      } else if (protocol === 'Form_Based') {
        return {
          init: getAppsFormbasedInit,
          add: addAppsFormbased,
          get: getAppsFormbased,
          update: updateAppsFormbased,
          del: delApps,
        }
      } else if (protocol === 'Extend_API') {
        return {
          init: getAppsExtendapiInit,
          add: addAppsExtendapi,
          get: getAppsExtendapi,
          update: updateAppsExtendapi,
          del: delApps,
        }
      }

      return apis
    }
  }
};
</script>

<style scoped="scoped" lang="scss">
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

</style>
