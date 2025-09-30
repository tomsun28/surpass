<template>
  <div v-if="props.auxiliary && props.auxiliary.length > 0">
    <el-form :model="form" :rules="rules" ref="auxiliaryFormRef" label-width="68px">
      <!-- 是否禁用 -->
      <el-form-item v-for="(item, index) in auxiliary" :key="item.id"
                    :label="item.label" :prop="item.value">
        <el-select
            style="width: 300px"
            :ref="index === 0 ? setFirstSelectRef : undefined"
            :model-value="itemValue(item).value.map((t: any) => t.value)"
            multiple :multiple-limit="1" clearable placeholder="请选择"
            @remove-tag="handleRemoveTag(item, $event)"
            @clear="handleClear(item)"
            @change="val => handleChange(item, val)">
          <el-option v-for="itemOp in listData[item.value]"
                     :key="itemOp.value"
                     :label="itemOp.label"
                     :value="itemOp.value"
                     :disabled="itemOp.status === 'y'"/>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import {listAssistAcc} from "@/api/config/assistAcc";
import {reactive, ref, toRefs, watch, onMounted, nextTick} from "vue";
import {ElForm, FormInstance} from "element-plus";

const emit: any = defineEmits(['update:modelValue'])
const auxiliaryFormRef = ref<FormInstance>()
const firstSelectRef = ref<any>(null)

const setFirstSelectRef = (el: any) => {
  if (el) {
    firstSelectRef.value = el
  }
}

const props = defineProps({
  show: {type: Boolean, default: false},
  subjectId: {type: String, default: ''},
  auxiliary: {type: Array<any>, default: []},
  modelValue: {type: Array<any>, default: []},
})

const data: any = reactive({
  form: {},
  rules: {}
});
const {rules, form} = toRefs(data);
const {auxiliary, modelValue} = toRefs(props);
const listData = ref<any>({})
const values = ref<any>({})

const itemValue = (aux: any) => {
  if (!values.value[aux.value]) {
    values.value[aux.value] = {id: aux.value, value: [], label: aux.label}
  }
  return values.value[aux.value]
}

/** 查询供应商列表 */
function getList(t: any, id: number) {
  listAssistAcc({
    pageNum: 1,
    pageSize: 1000,
    assistType: id,
    orderByColumn: "status",
    isAsc: "asc"
  }).then((res: any) => {
    listData.value[id] = res.data.records.map((t: any) => {
      return {
        label: t.assistName,
        value: t.id,
        status: t.status
      }
    })
  });
}

function handleValidate(index: number) {
  auxiliaryFormRef.value?.validate((valid, fields) => {
    if (!valid) {
      console.error(fields, rules.value, values.value)
    }
  });
}

function handleClear(item: any) {
  itemValue(item).value = []
  handleUpdate()
}

function handleRemoveTag(item: any, value: any) {
  const idx = itemValue(item).value.findIndex((t: any) => {
    return t.value === value
  })
  if (idx > -1) {
    itemValue(item).value.splice(idx, 1)
  }
  handleUpdate()
}

/** 选中变化 */
function handleChange(item: any, selectedValues: any[]) {
  // 根据选中的 value 数组，找到对应的对象
  itemValue(item).value = listData.value[item.value].filter((op: any) =>
      selectedValues.includes(op.value)
  )
  form.value[item.value] = itemValue(item).value.map((v: any) => v.value).join(",")
  handleUpdate()
}

function handleUpdate() {
  emit('update:modelValue', Object.values(values.value));
  handleValidate(0)
}

function watchUpdate(newVal: any) {
  if (newVal) {
    form.value.length = 0
    newVal.forEach((t: any) => {
      getList(t, t.value)
      const id = t.value
      rules.value[id] = t.must
          ? [{required: true, message: t.label + '不能为空', trigger: 'change'}]
          : []
      form.value[id] = ""
    })
    // 等待DOM更新后聚焦第一个select
    nextTick(() => {
      firstSelectRef.value?.focus()
    })
  }
}

function syncValues(newVal: any[]) {
  watchUpdate(props.auxiliary)
}

watch(
    () => props.auxiliary,
    (newVal: Array<any>) => {
      rules.value.length = 0
      listData.value.length = 0
      watchUpdate(newVal)
    },
    {immediate: true}
)

watch(
    () => props.show,
    (newVal: Boolean) => {
      // 首次挂载后也可以聚焦
      nextTick(() => {
        firstSelectRef.value?.focus()
      })
    },
    {immediate: true, deep: true}
)

watch(
    () => props.modelValue,
    (newVal) => {
      if (newVal && newVal.length) {
        syncValues(newVal);
      }
    },
    {immediate: true, deep: true}
);
onMounted(() => {
  values.value = {}
  props.modelValue.forEach((t: any) => {
    values.value[t.id] = t
  })
  // 首次挂载后也可以聚焦
  nextTick(() => {
    firstSelectRef.value?.focus()
  })
})
</script>
