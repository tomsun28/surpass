<template>
  <div>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            @click="handleAdd"
        >{{ $t("jbx.text.add") }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="listData">
      <el-table-column :label="$t('jbx.custom.extraAttr.attr')" align="center" prop="attr">
        <template #default="scope">
          <el-input :readonly="rowIndex !== scope.$index" v-model="scope.row.attr"></el-input>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.custom.extraAttr.type')" align="center" prop="type">
        <template #default="scope">
          <el-input :readonly="rowIndex !== scope.$index" v-model="scope.row.type"></el-input>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.custom.extraAttr.value')" align="center" prop="value">
        <template #default="scope">
          <el-input :readonly="rowIndex !== scope.$index" v-model="scope.row.value"></el-input>
        </template>
      </el-table-column>

      <el-table-column :label="$t('jbx.text.action')" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
              plain
              type="primary"
              @click="submit(scope.$index,scope.row)"
              v-if="rowIndex === scope.$index"
          >{{ $t("jbx.text.submit") }}
          </el-button>
          <el-button
              plain
              type="primary"
              @click="handleEdit(scope.$index, scope.row)"
              v-if="rowIndex !== scope.$index"
          >{{ $t("jbx.text.edit") }}
          </el-button>
          <el-button
              plain
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              v-if="rowIndex !== scope.$index"
          >{{ $t("jbx.text.delete") }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {appsUpdateExtendAttr} from '@/api/system/apps'

export default {
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
      listData: [],
      rowIndex: -1,
    }
  },
  created() {

  },
  mounted() {
    setTimeout(() => {
      if (!this.form.extendAttr) {
        return
      }

      this.listData = JSON.parse(this.form.extendAttr)
    }, 500)
  },

  methods: {
    handleAdd() {
      this.listData.push({
        id: new Date().getTime(),
        attr: 'attr' + this.listData.length,
        type: 'string',
        value: ''
      })
      this.rowIndex = this.listData.length - 1
    },

    handleEdit(index, row) {
      this.rowIndex = index
    },

    submit(index, row) {
      appsUpdateExtendAttr({
        id: this.form.id,
        extendAttr: JSON.stringify(this.listData)
      }).then((res: any) =>  {
        this.rowIndex = -1
      })
    },

    handleDelete(index, row) {
      this.listData.splice(index, 1)
      appsUpdateExtendAttr({
        id: this.form.id,
        extendAttr: JSON.stringify(this.listData)
      }).then((res: any) =>  {
        this.rowIndex = -1
      })
    },
  },
}
</script>
