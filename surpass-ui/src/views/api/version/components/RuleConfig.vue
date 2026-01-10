<template>
  <el-dialog
      :model-value="visible"
      title="输入规则配置"
      width="500px"
      @update:model-value="$emit('update:visible', $event)"
  >
    <el-form label-width="100px">
      <el-form-item label="必填">
        <el-switch v-model="ruleFormData.required"/>
      </el-form-item>
      <div v-if="currentParam?.type === 'String'">
        <el-form-item label="最小长度">
          <el-input-number v-model="ruleFormData.minLength" :min="0" controls-position="right"/>
        </el-form-item>
        <el-form-item label="最大长度">
          <el-input-number v-model="ruleFormData.maxLength" :min="0" controls-position="right"/>
        </el-form-item>
        <el-form-item label="枚举值">
          <el-input v-model="ruleFormData.enumValues" placeholder="多个值用逗号分隔，如: admin,user,guest"/>
        </el-form-item>
      </div>
      <div v-if="currentParam?.type === 'Integer'">
        <el-form-item label="最小值">
          <el-input-number v-model="ruleFormData.minValue" controls-position="right"/>
        </el-form-item>
        <el-form-item label="最大值">
          <el-input-number v-model="ruleFormData.maxValue" controls-position="right"/>
        </el-form-item>
      </div>
      <el-form-item label="格式">
        <el-select v-model="ruleFormData.format" placeholder="选择格式" clearable @change="handleFormatChange">
          <el-option label="邮箱" value="email"/>
          <el-option label="手机号" value="phone"/>
          <el-option label="URL" value="url"/>
          <el-option label="日期" value="date"/>
          <el-option label="时间" value="time"/>
          <el-option label="IPv4" value="ipv4"/>
          <el-option label="IPv6" value="ipv6"/>
        </el-select>
      </el-form-item>

      <el-form-item label="正则表达式">
        <el-input v-model="ruleFormData.pattern" placeholder="例如: ^[a-zA-Z]+$" @input="handlePatternChange"/>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSave">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, defineProps, defineEmits, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  currentParam: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'save'])

const ruleFormData = reactive({
  required: false,
  minLength: '',
  maxLength: '',
  pattern: '',
  minValue: '',
  maxValue: '',
  enumValues: '',
  format: ''
})

const visible = computed({
  get() {
    return props.visible
  },
  set(value) {
    emit('update:visible', value)
  }
})

// 格式选项对应的正则表达式
const formatPatterns = {
  email: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$',
  phone: '^1[3-9]\\d{9}$',
  url: '^https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$',
  date: '^\\d{4}-\\d{2}-\\d{2}$',
  time: '^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$',
  ipv4: '^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$',
  ipv6: '^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$'
}

// 当格式改变时，自动填充对应的正则表达式
const handleFormatChange = (format) => {
  if (format && formatPatterns[format]) {
    // 如果选择了格式并且有对应的正则表达式，则自动填充
    ruleFormData.pattern = formatPatterns[format]
  } else if (!format) {
    // 如果清空了格式，也清空正则表达式（除非用户自己输入的）
    if (Object.values(formatPatterns).includes(ruleFormData.pattern)) {
      ruleFormData.pattern = ''
    }
  }
}

// 当手动输入正则表达式时，清除格式选择（如果有冲突）
const handlePatternChange = (pattern) => {
  ruleFormData.pattern = pattern
  // 检查是否与预定义的格式匹配
  const matchedFormat = Object.keys(formatPatterns).find(key => formatPatterns[key] === pattern)
  if (matchedFormat) {
    ruleFormData.format = matchedFormat
  } else if (ruleFormData.format && formatPatterns[ruleFormData.format] !== pattern) {
    // 如果pattern与当前选择的format不匹配，则清空format
    ruleFormData.format = ''
  }
}

// 检查规则冲突
const checkRuleConflict = () => {
  // 检查枚举值与正则表达式冲突
  if (ruleFormData.enumValues && ruleFormData.pattern) {
    const enumValues = ruleFormData.enumValues.split(',').map(val => val.trim()).filter(val => val)
    if (enumValues.length > 0) {
      return '不能同时设置枚举值和正则表达式'
    }
  }

  // 检查数值范围与长度限制冲突
  if ((ruleFormData.minValue !== '' || ruleFormData.maxValue !== '') &&
      (ruleFormData.minLength !== '' || ruleFormData.maxLength !== '')) {
    if (props.currentParam?.type === 'number') {
      return '数值类型不能设置长度限制'
    }
  }

  // 检查最小值大于最大值
  if (ruleFormData.minValue !== '' && ruleFormData.maxValue !== '' &&
      parseFloat(ruleFormData.minValue) > parseFloat(ruleFormData.maxValue)) {
    return '最小值不能大于最大值'
  }

  // 检查最小长度大于最大长度
  if (ruleFormData.minLength !== '' && ruleFormData.maxLength !== '' &&
      parseInt(ruleFormData.minLength) > parseInt(ruleFormData.maxLength)) {
    return '最小长度不能大于最大长度'
  }

  return null
}

const parseRulesToForm = (rulesObj) => {
  // 重置表单
  Object.assign(ruleFormData, {
    required: false,
    minLength: '',
    maxLength: '',
    pattern: '',
    minValue: '',
    maxValue: '',
    enumValues: '',
    format: ''
  })

  if (!rulesObj || Object.keys(rulesObj).length === 0) return

  try {
    ruleFormData.required = !!rulesObj.required
    if (rulesObj.minLength !== undefined) ruleFormData.minLength = rulesObj.minLength
    if (rulesObj.maxLength !== undefined) ruleFormData.maxLength = rulesObj.maxLength
    if (rulesObj.pattern) ruleFormData.pattern = rulesObj.pattern
    if (rulesObj.minValue !== undefined) ruleFormData.minValue = rulesObj.minValue
    if (rulesObj.maxValue !== undefined) ruleFormData.maxValue = rulesObj.maxValue
    if (rulesObj.enumValues) ruleFormData.enumValues = rulesObj.enumValues.join(',')
    if (rulesObj.format) ruleFormData.format = rulesObj.format

    // 如果有预设的格式，则尝试匹配
    if (rulesObj.pattern && !rulesObj.format) {
      const matchedFormat = Object.keys(formatPatterns).find(key => formatPatterns[key] === rulesObj.pattern)
      if (matchedFormat) {
        ruleFormData.format = matchedFormat
      }
    }
  } catch (e) {
    console.warn('Failed to parse rules:', e)
  }
}

const handleCancel = () => {
  emit('update:visible', false)
}

const handleSave = () => {
  // 检查规则冲突
  const conflict = checkRuleConflict()
  if (conflict) {
    ElMessage.warning(conflict)
    return
  }

  // 构造规则对象
  const rules = {}
  if (ruleFormData.required) rules.required = true
  if (ruleFormData.minLength !== '') rules.minLength = parseInt(ruleFormData.minLength)
  if (ruleFormData.maxLength !== '') rules.maxLength = parseInt(ruleFormData.maxLength)
  if (ruleFormData.pattern) rules.pattern = ruleFormData.pattern
  if (ruleFormData.minValue !== '') rules.minValue = parseFloat(ruleFormData.minValue)
  if (ruleFormData.maxValue !== '') rules.maxValue = parseFloat(ruleFormData.maxValue)
  if (ruleFormData.enumValues) {
    const values = ruleFormData.enumValues.split(',').map(val => val.trim()).filter(val => val)
    if (values.length > 0) rules.enumValues = values
  }
  if (ruleFormData.format) rules.format = ruleFormData.format

  emit('save', rules)
  emit('update:visible', false)
}

// 当currentParam变化时，解析规则
watch(() => props.currentParam, (newParam) => {
  if (newParam) {
    parseRulesToForm(newParam.rules)
  }
}, { immediate: true })
</script>

<style scoped>
/* 规则配置对话框样式 */
</style>