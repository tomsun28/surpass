<template>
  <div>
    <el-form-item v-for="item in rows" :label="item.attrName">
      <el-input v-model="item.value"></el-input>
    </el-form-item>
    <div v-if="rows.length === 0" style="padding: 20px">{{ $t('jbx.expandattrs.empty') }}</div>
  </div>
</template>

<script setup lang="ts">
import {findList} from "@/api/system/dept";
import {ref, watch, defineComponent} from "vue";

const props: any = defineProps({
  resources: {
    type: String,
    default: "",
  },
  data: {
    default: undefined,
  },
  expandFlag: {
    type: Boolean,
    default: false
  }
})

watch(
    () => props.expandFlag,
    (val: any) => {
      const params: any = {
        resources: props.resources
      }
      if (val && params) {
        findList(params).then((res: any) => {
          if (res.code === 0) {
            rows.value = transformation(res.data, props.data);
          }
        })
      } else {
        reset();
      }
    },
    {immediate: true}
);

const rows: any = ref<any[]>([]);

function reset(): any {
  rows.value = [];
}

function transformation(rows: any, data: any): any {
  if (undefined === data || null == data || '' == data) {
    for (let row of rows) {
      //如果自定义字段值对象为空，则采用默认值
      row.value = row.defaultValue;
    }
  } else {
    //格式化数据
    var jsonData: any = JSON.parse(data);
    for (let row of rows) {
      for (let json of jsonData) {
        if (row.attr == json.attr) {
          row.value = json.value;
        }
      }
    }
  }
  return rows;
}

function getData(): any {
  let rowsTemp: any = rows.value;
  if (rowsTemp.length === 0) return '';
  //格式化数据
  var jsonArray: any = [];
  for (let {attr, attrName, value} of rowsTemp) {
    const jsonData: any = {
      attr: attr,
      name: attrName,
      value: value
    };
    jsonArray.push(jsonData);
  }
  return JSON.stringify(jsonArray);
}

// 暴露 getData 方法
defineExpose({getData});

defineComponent({
  name: 'exAttrsForm'
})
</script>
