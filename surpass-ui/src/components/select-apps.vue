<template>
  <div>
    <el-form :model="queryAppParams" ref="queryForm" size="small" :inline="true" label-width="100px">
      <el-form-item :label="$t('jbx.apps.name') + '：'" prop="appName">
        <el-input
            v-model="queryAppParams.appName"
            placeholder=""
            clearable
            style="width: 240px"
            @keyup.enter.native="handleAppQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('jbx.apps.category') + '：'" prop="category">
        <el-select v-model="queryAppParams.category" placeholder="" clearable style="width: 100%">
          <el-option
              v-for="dict in categoryList"
              :key="dict.id"
              :label="dict.name"
              :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" @click="handleAppQuery">
          {{ $t('jbx.text.query') }}
        </el-button>
        <el-button size="small" @click="resetQuery">
          {{ $t('jbx.text.reset') }}
        </el-button>
        <el-button size="small" type="primary" @click="handleAppSubmit">
          {{ $t('jbx.text.confirm') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="appsList" @row-click="handleAppSelectionChange">
      <el-table-column type="selection" width="55" align="center">
        <template slot-scope="scope">
          <el-checkbox :value="scope.row.id === value.id"></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.accountsstrategy.appIcon')" align="center" prop="appIcon">
        <template slot-scope="scope">
          <el-image :src="scope.row.iconBase64" style="width: 50px"></el-image>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.accountsstrategy.appName')" align="center" prop="appName"/>
      <el-table-column :label="$t('jbx.apps.category')" align="center" prop="createType">
        <template slot-scope="scope">
          <span>{{ getCategoryInfo(scope.row.category).name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex"></el-table-column>

      <el-table-column :label="$t('jbx.users.status')" align="center" width="80">
        <template slot-scope="scope">
          <i v-if="scope.row.status == '1'" class="el-icon-success" style="color: green"></i>
          <i v-if="scope.row.status == '0'" class="el-icon-warning" style="color: grey"></i>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="appsTotal>0"
        :total="appsTotal"
        :page.sync="queryAppParams.pageNumber"
        :limit.sync="queryAppParams.pageSize"
        @pagination="getApps"
    />
  </div>
</template>

<script lang="ts">
import {
  listApps
} from '@/api/config/apps'
import {listCategory} from "@/api/config/category";

export default {
  name: "selectApps",
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
      // 显示搜索条件
      showSearch: true,
      queryAppParams: {
        pageNumber: 1,
        pageSize: 5,
        appName: undefined,
        category: undefined,
      },
      appsList: [],
      appsTotal: 0,
      categoryList: new Array<any>([]),
      selectApp: {}
    };
  },
  created() {
    this.getApps();
    listCategory().then((res: any) => {
      this.categoryList = res.data
    })
  },
  methods: {
    getCategoryInfo(id: any) {
      for (let index in this.categoryList) {
        if (this.categoryList[index].id == id) {
          return this.categoryList[index]
        }
      }
      return {}
    },

    getApps() {
      listApps(this.queryAppParams).then((res: any) => {
        this.appsList = res.data.records
        this.appsTotal = res.data.records
      })
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryAppParams.pageNumber = 1;
      this.queryAppParams.pageSize = 10;
      this.queryAppParams.appName = undefined;
      this.queryAppParams.category = undefined;
      this.resetForm(this, "queryForm");
      this.handleAppQuery();
    },

    handleAppQuery() {
      this.queryAppParams.pageNumber = 1;
      this.getApps();
    },

    handleAppSelectionChange(row: any) {
      this.$emit("input", row);
    },

    handleAppSubmit() {
      this.$emit("submit", this.value);
    },
  }
};
</script>

<style scoped>

</style>
