<template>
  <div class="app-container1">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('jbx.apps.adapterName') + '：'" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder=""
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
        <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        <el-button type="primary" size="small" @click="handleAppSubmit">{{ $t('jbx.text.confirm') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="configList" @row-click="handleAppSelectionChange">
      <el-table-column type="selection" width="55" align="center">
        <template slot-scope="scope">
          <el-checkbox :value="scope.row.id === value.id"></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.apps.adapterName')" align="center" prop="name"/>
      <el-table-column :label="$t('jbx.emailsenders.protocol')" align="center" prop="protocol"/>
      <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex"/>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNumber"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />
  </div>
</template>

<script lang="ts">
import {
  listAdapters,
} from "@/api/system/adapters";

export default {
  props: {
    value: {
      type: Object,
      default: function () {
        return {}
      }
    },
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      showSearch: true,
      // 总条数
      total: 0,
      // 参数表格数据
      configList: [],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        name: undefined,
        protocol: undefined
      },
    };
  },
  created() {

  },
  methods: {
    setProtocol(protocol:any) {
      this.queryParams.protocol = protocol
      this.getList()
    },

    /** 查询参数列表 */
    getList() {
      this.loading = true;
      listAdapters(this.queryParams).then((response: any) => {
            this.configList = response.data.rows;
            this.total = response.data.records;
            this.loading = false;
          }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.queryParams.name = undefined;
      this.resetForm(this, "queryForm");
      this.handleQuery();
    },
    handleAppSelectionChange(row:any) {
      this.$emit("input", row);
    },

    handleAppSubmit() {
      this.$emit("submit", this.value);
    },

  }
};
</script>
