<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="params" ref="queryForm" :inline="true" v-show="showSearch"><!-- class="search-form" -->
          <div>
            <el-form-item :label="$t('jbx.users.username')">
              <el-input
                  v-model="params.username"
                  clearable
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item :label="$t('jbx.users.displayName')">
              <el-input
                  v-model="params.displayName"
                  clearable
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <span v-show="moreCondition">
          <el-form-item :label="$t('jbx.text.startDate')">
            <el-date-picker v-model="params.startDatePicker" type="datetime">
            </el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('jbx.text.endDate')">
            <el-date-picker v-model="params.endDatePicker" type="datetime">
            </el-date-picker>
          </el-form-item>
        </span>
            <el-divider v-if="!moreCondition">
              <el-button
                  type="text"
                  icon="el-icon-arrow-down"
                  @click="moreCondition = !moreCondition">
                {{ $t('jbx.text.expand') }}
              </el-button>
            </el-divider>
            <el-divider v-else>
              <el-button
                  type="text"
                  icon="el-icon-arrow-up"
                  @click="moreCondition = !moreCondition">
                {{ $t('jbx.text.collapse') }}
              </el-button>
            </el-divider>
          </div>
          <div class="search-form-btns">
            <el-form-item>
              <el-button type="primary" @click="handleQuery">{{ $t('jbx.text.query') }}
              </el-button><!-- type="primary" plain -->
              <el-button @click="handleReset">{{ $t('jbx.text.reset') }}
              </el-button>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </el-card>

    <el-card class="common-card">
      <el-table ref='multipleTable'
                v-loading="loading"
                :data="sessions"
                @selection-change="handleSelectionChange">
        <el-table-column prop="sessionId" :label="$t('jbx.history.loginSessionid')" align="center"
                         min-width="120"/>
        <el-table-column prop="username" :label="$t('jbx.history.loginUsername')" align="center"
                         min-width="90"/>
        <el-table-column prop="displayName" :label="$t('jbx.history.loginDisplayname')" align="center"
                         min-width="90"/>
        <el-table-column :label="$t('jbx.accountsstrategy.appName')" align="center" prop="appName"/>
        <el-table-column prop="loginTime" :label="$t('jbx.history.loginLogintime')" align="center"
                         min-width="120"/>
      </el-table>
      <pagination v-if="total>0" :total="total"
                  :page.sync="params.pageNumber"
                  :limit.sync="params.pageSize"
                  @pagination="getList"
                  :page-sizes="params.pageSizeOptions"/>
    </el-card>
  </div>
</template>

<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {loginAppsHistory} from "@/api/audit/audit";

export default {
  name: 'sessions',
  data() {
    return {
      loading: true,
      selectionlist: [],
      ids: [],
      params: {
        username: '',
        displayName: '',
        employeeNumber: '',
        startDate: '',
        endDate: '',
        startDatePicker: this.addDays(new Date(), -30),
        endDatePicker: Date.now(),
        pageSize: 10,
        pageNumber: 1,
        pageSizeOptions: [10, 20, 50]
      },
      showSearch: true,
      // 更多条件
      moreCondition: false,
      sessions: [],
      total: 0
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.params.startDate = this.formatTimestamp(this.params.startDatePicker, 'yyyy-MM-dd HH:mm:ss');
      this.params.endDate = this.formatTimestamp(this.params.endDatePicker, 'yyyy-MM-dd HH:mm:ss');
      loginAppsHistory(this.params).then((res: any) =>  {
        this.sessions = res.data.records;
        this.total = res.data.records;
        this.loading = false;
      })
    },
    addDays(date, days) {
      const newDate: any = new Date(date);
      newDate.setDate(newDate.getDate() + days);
      return newDate.getTime();
    },
    /** 多选操作*/
    handleSelectionChange(selection) {
      this.selectionlist = selection;
      this.ids = this.selectionlist.map((item: any) =>  item.id);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.params.pageNumber = 1;
      this.getList();
    },
    handleReset() {
      this.params = {
        username: '',
        displayName: '',
        employeeNumber: '',
        startDate: '',
        endDate: '',
        startDatePicker: this.addDays(new Date(), -30),
        endDatePicker: Date.now(),
        pageSize: 10,
        pageNumber: 1,
        pageSizeOptions: [10, 20, 50]
      };
      this.handleQuery();
    },
    //时间格式化方法
    formatTimestamp(timestamp) {
      const date: any = new Date(timestamp);
      const year: any = date.getFullYear();
      const month: any = String(date.getMonth() + 1).padStart(2, '0');
      const day: any = String(date.getDate()).padStart(2, '0');
      const hours: any = String(date.getHours()).padStart(2, '0');
      const minutes: any = String(date.getMinutes()).padStart(2, '0');
      const seconds: any = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    set2String(set) {
      let setValues: any = '';
      set.forEach((value: any) =>  {
        setValues = `${setValues + value},`;
      });
      return setValues;
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog {
  min-height: 100px;
}

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

.el-form-item--small.el-form-item {
  margin-bottom: 10px;
}
</style>
