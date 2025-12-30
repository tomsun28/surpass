<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="params" ref="queryForm" :inline="true">

          <el-form-item label="请求ID">
            <el-input v-model="params.requestId" clearable />
          </el-form-item>

          <el-form-item label="Client ID">
            <el-input v-model="params.clientId" clearable />
          </el-form-item>

          <el-form-item label="应用名称">
            <el-input v-model="params.appName" clearable />
          </el-form-item>

          <el-form-item label="资源名称">
            <el-input v-model="params.resourceName" clearable />
          </el-form-item>

          <el-form-item label="请求方法">
            <el-select v-model="params.requestMethod" clearable style="width: 120px">
              <el-option label="GET" value="GET"/>
              <el-option label="POST" value="POST"/>
              <el-option label="PUT" value="PUT"/>
              <el-option label="DELETE" value="DELETE"/>
            </el-select>
          </el-form-item>

          <el-form-item label="开始时间">
            <el-date-picker v-model="params.startDatePicker" type="datetime"/>
          </el-form-item>

          <el-form-item label="结束时间">
            <el-date-picker v-model="params.endDatePicker" type="datetime"/>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="common-card">
      <el-table
          v-loading="loading"
          border
          :data="sessions">

        <el-table-column prop="requestId" label="请求ID" width="180" fixed show-overflow-tooltip/>

        <el-table-column prop="requestMethod" label="请求方法" width="80" show-overflow-tooltip/>

        <el-table-column prop="requestUri" label="请求地址" width="220" show-overflow-tooltip/>

        <el-table-column prop="clientId" label="Client ID" width="140" show-overflow-tooltip/>

        <el-table-column prop="appName" label="应用名称" min-width="120" show-overflow-tooltip/>

        <el-table-column prop="resourceName" label="资源名称" width="120" show-overflow-tooltip/>

        <el-table-column prop="ipAddr" label="IP" width="120" show-overflow-tooltip/>

        <el-table-column prop="location" label="位置" width="120" show-overflow-tooltip/>

        <el-table-column prop="authned" label="认证" width="100" show-overflow-tooltip>
          <template #default="scope">
            <el-tag :type="scope.row.authned === 'y' ? 'success' : 'danger'">
              {{ scope.row.authned === 'y' ? '已认证' : '未认证' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="access" label="访问结果" width="90" show-overflow-tooltip>
          <template #default="scope">
            <el-tag :type="scope.row.access === 'y' ? 'success' : 'warning'">
              {{ scope.row.access }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="accessCost" label="耗时(ms)" width="100" show-overflow-tooltip/>

        <el-table-column prop="accessTime" label="访问时间" width="160" fixed="right" show-overflow-tooltip/>

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
import {getOpenApiLogs, loginHistory} from "@/api/audit/audit";

export default {
  name: 'sessions',
  data() {
    return {
      loading: true,
      selectionlist: [],
      ids: [],
      params: {
        requestId: '',
        clientId: '',
        appName: '',
        resourceName: '',
        requestMethod: '',
        ipAddr: '',
        startDate: '',
        endDate: '',
        startDatePicker: this.addDays(new Date(), -7),
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
      if (this.moreCondition) {
        this.params.startDate = this.formatTimestamp(this.params.startDatePicker);
        this.params.endDate = this.formatTimestamp(this.params.endDatePicker);
      } else {
        this.params.endDate = '';
        this.params.startDate = '';
      }
      getOpenApiLogs(this.params).then((res: any) => {
        this.sessions = res.data.rows;
        this.total = res.data.total;
        this.loading = false;
      })
    },
    addDays(date: any, days: any) {
      const newDate: any = new Date(date);
      newDate.setDate(newDate.getDate() + days);
      return newDate.getTime();
    },
    /** 多选操作*/
    handleSelectionChange(selection: any) {
      this.selectionlist = selection;
      this.ids = this.selectionlist.map((item: any) => item.id);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.params.pageNumber = 1;
      this.getList();
    },
    handleReset() {
      this.params = {
            requestId: '',
            clientId: '',
            appName: '',
            resourceName: '',
            requestMethod: '',
            ipAddr: '',
            startDate: '',
            endDate: '',
            startDatePicker: this.addDays(new Date(), -7),
            endDatePicker: Date.now(),
            pageSize: 10,
            pageNumber: 1,
            pageSizeOptions: [10, 20, 50]
      };
      this.handleQuery();
    },
    //时间格式化方法
    formatTimestamp(timestamp: any) {
      const date: any = new Date(timestamp);
      const year: any = date.getFullYear();
      const month: any = String(date.getMonth() + 1).padStart(2, '0');
      const day: any = String(date.getDate()).padStart(2, '0');
      const hours: any = String(date.getHours()).padStart(2, '0');
      const minutes: any = String(date.getMinutes()).padStart(2, '0');
      const seconds: any = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    set2String(set: any) {
      let setValues: any = '';
      set.forEach((value: any) => {
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

