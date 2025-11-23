<template>
  <div class="debug-page">
    <div class="page-header">
      <h2>API调试</h2>
      <p>测试和调试已发布的API</p>
    </div>

    <div class="page-content">
      <!-- API选择 -->
      <div class="api-selector">
        <el-select
            v-model="selectedApiId"
            placeholder="请选择API"
            style="width: 300px"
            @change="loadApiDetail"
            :loading="apiLoading"
        >
          <el-option
              v-for="api in apiList"
              :key="api.id"
              :label="api.name"
              :value="api.id"
          />
        </el-select>
      </div>

      <!-- API信息 -->
      <div class="api-info" v-if="selectedApi && currentVersion">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>API信息</span>
            </div>
          </template>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">API名称：</span>
              <span>{{ selectedApi.name }}</span>
            </div>
            <div class="info-item">
              <span class="label">路径：</span>
              <el-tag>{{ selectedApi.path }}</el-tag>
            </div>
            <div class="info-item">
              <span class="label">方法：</span>
              <el-tag :type="getMethodTagType(selectedApi.method)">
                {{ selectedApi.method }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="label">当前版本：</span>
              <el-tag type="success">v{{ currentVersion.version }}</el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- SQL模板预览 -->
      <div class="sql-preview" v-if="selectedApi && currentVersion">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>SQL模板预览</span>
            </div>
          </template>
          <div class="sql-content">
            <pre>{{ currentVersion.sqlTemplate }}</pre>
          </div>
        </el-card>
      </div>

      <!-- 请求配置 -->
      <div class="request-config" v-if="selectedApi && currentVersion">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>请求配置</span>
            </div>
          </template>

          <!-- 参数配置 -->
          <div class="params-section">
            <h4>请求参数</h4>
            <div class="params-list">
              <div
                  v-for="(param, index) in requestParams"
                  :key="index"
                  class="param-item"
              >
                <el-input
                    v-model="param.name"
                    placeholder="参数名"
                    style="width: 150px"
                    :class="{ 'required-param': param.required }"
                />
                <el-select
                    v-model="param.type"
                    placeholder="类型"
                    style="width: 100px"
                >
                  <el-option label="字符串" value="string"/>
                  <el-option label="数字" value="number"/>
                  <el-option label="布尔" value="boolean"/>
                </el-select>
                <el-input
                    v-model="param.value"
                    placeholder="参数值"
                    style="flex: 1"
                />
                <el-tooltip
                    :content="param.required ? '必填参数' : '可选参数'"
                    placement="top"
                >
                  <el-icon :color="param.required ? '#f56c6c' : '#909399'">
                    <InfoFilled/>
                  </el-icon>
                </el-tooltip>
                <el-button
                    type="danger"
                    @click="removeParam(index)"
                    v-if="!param.required"
                >
                  <el-icon>
                    <Delete/>
                  </el-icon>
                </el-button>
              </div>
            </div>
            <el-button @click="addParam">
              <el-icon>
                <Plus/>
              </el-icon>
              添加参数
            </el-button>
          </div>

          <!-- 执行按钮 -->
          <div class="execute-section">
            <el-button
                type="primary"
                @click="executeApi"
                :loading="executing"
                :disabled="!selectedApi"
            >
              <el-icon>
                <Promotion/>
              </el-icon>
              执行API
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 响应结果 -->
      <div class="response-result" v-if="responseData">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>响应结果</span>
              <el-tag :type="responseData.code === 0 ? 'success' : 'danger'">
                {{ responseData.code === 0 ? '成功' : '失败' }}
              </el-tag>
            </div>
          </template>

          <div class="response-info">
            <div class="info-item">
              <span class="label">状态码：</span>
              <span>{{ responseData.code }}</span>
            </div>
            <div class="info-item">
              <span class="label">执行时间：</span>
              <span>{{ executionTime }}ms</span>
            </div>
            <div class="info-item">
              <span class="label">消息：</span>
              <span>{{ responseData.message }}</span>
            </div>
          </div>

          <div class="response-data">
            <h4>响应数据：</h4>
            <pre>{{ formatResponseData(responseData) }}</pre>
          </div>
        </el-card>
      </div>

      <!-- 空状态 -->
      <el-empty
          v-if="!selectedApiId"
          description="请选择API"
      />
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {gatewayApi} from '@/api/api-service/gatewayApi.ts'
import * as apiVersionApi from '@/api/api-service/apiVersionApi.ts'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import {Delete, InfoFilled, Plus, Promotion} from '@element-plus/icons-vue'

// 响应式数据
const apiLoading = ref(false)
const executing = ref(false)

const selectedApiId = ref(null)
const selectedApi = ref(null)
const apiList = ref([])
const currentVersion = ref(null)
const responseData = ref(null)
const executionTime = ref(0)

const requestParams = ref([
  {name: '', value: ''}
])

// 生命周期
onMounted(() => {
  loadApis()
})

// 方法
const loadApis = async () => {
  try {
    apiLoading.value = true
    const response = await apiDefinitionApi.list()
    apiList.value = response || []
  } catch (error) {
    ElMessage.error('加载API列表失败')
    console.error('加载API列表失败:', error)
  } finally {
    apiLoading.value = false
  }
}

const loadApiDetail = async () => {
  if (!selectedApiId.value) return

  try {
    // 加载API详情
    selectedApi.value = (await apiDefinitionApi.getById(selectedApiId.value)).data;

    // 加载当前版本
    const versionResponse = await apiVersionApi.getPublishedVersion(selectedApiId.value)
    if (versionResponse.data) {
      currentVersion.value = versionResponse.data
      // 解析参数定义并初始化请求参数
      initRequestParams(versionResponse.data.paramDefinition)
    } else {
      currentVersion.value = null
      ElMessage.warning('该API没有已发布的版本')
    }

    // 重置响应数据
    responseData.value = null

  } catch (error) {
    ElMessage.error('加载API详情失败')
    console.error('加载API详情失败:', error)
  }
}

const addParam = () => {
  requestParams.value.push({name: '', value: ''})
}

const removeParam = (index) => {
  if (requestParams.value.length > 1) {
    requestParams.value.splice(index, 1)
  }
}

const initRequestParams = (paramDefinition) => {
  if (!paramDefinition) {
    requestParams.value = [{name: '', value: ''}]
    return
  }

  try {
    const params = JSON.parse(paramDefinition)
    if (Array.isArray(params)) {
      requestParams.value = params.map(param => ({
        name: param.name || '',
        value: param.defaultValue || '',
        type: param.type || 'string',
        required: param.required || false
      }))
    } else if (typeof params === 'object') {
      requestParams.value = Object.keys(params).map(key => ({
        name: key,
        value: params[key].defaultValue || '',
        type: params[key].type || 'string',
        required: params[key].required || false
      }))
    }

    // 确保至少有一个参数项
    if (requestParams.value.length === 0) {
      requestParams.value = [{name: '', value: ''}]
    }
  } catch (error) {
    console.error('解析参数定义失败:', error)
    requestParams.value = [{name: '', value: ''}]
  }
}

const executeApi = async () => {
  if (!selectedApi.value) {
    ElMessage.warning('请先选择API')
    return
  }

  // 验证必填参数
  const missingParams = requestParams.value
      .filter(param => param.required && (!param.name || !param.value))
      .map(param => param.name || '未命名参数')

  if (missingParams.length > 0) {
    ElMessage.warning(`请填写必填参数: ${missingParams.join(', ')}`)
    return
  }

  let startTime = 0

  try {
    executing.value = true
    executionTime.value = 0

    // 构建请求参数
    const params = {}
    requestParams.value.forEach(param => {
      if (param.name && param.value !== '') {
        // 根据类型转换参数值
        let value = param.value
        if (param.type === 'number') {
          value = Number(value)
        } else if (param.type === 'boolean') {
          value = value.toLowerCase() === 'true'
        }
        params[param.name] = value
      }
    })

    // 记录开始时间
    startTime = Date.now()

    // 执行API
    const response = await gatewayApi.execute(
        selectedApi.value.path,
        selectedApi.value.method,
        params
    )

    // 计算执行时间
    executionTime.value = Date.now() - startTime

    responseData.value = response

    if (response.code === 0) {
      ElMessage.success(`API执行成功 (${executionTime.value}ms)`)
    } else {
      ElMessage.error(`API执行失败: ${response.message}`)
    }

  } catch (error) {
    executionTime.value = startTime ? Date.now() - startTime : 0
    ElMessage.error('API执行失败')
    console.error('API执行失败:', error)
    responseData.value = {
      code: 1,
      message: error.message || 'API执行失败',
      data: null
    }
  } finally {
    executing.value = false
  }
}

const formatResponseData = (data) => {
  if (typeof data === 'string') {
    try {
      return JSON.stringify(JSON.parse(data), null, 2)
    } catch {
      return data
    }
  }
  return JSON.stringify(data, null, 2)
}

const getMethodTagType = (method) => {
  const types = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  }
  return types[method] || 'info'
}
</script>

<style scoped>
.debug-page {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  min-height: calc(100vh - 140px);
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e6e6e6;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.api-selector {
  margin-bottom: 20px;
}

.api-info,
.sql-preview,
.request-config,
.response-result {
  margin-bottom: 20px;
}

.sql-content pre {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 4px;
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 200px;
  overflow-y: auto;
}

.required-param .el-input__inner {
  border-color: #f56c6c;
}

.required-param .el-input__inner:focus {
  border-color: #f56c6c;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item .label {
  font-weight: bold;
  min-width: 80px;
  color: #606266;
}

.params-section {
  margin-bottom: 20px;
}

.params-section h4 {
  margin: 0 0 12px 0;
  color: #303133;
}

.params-list {
  margin-bottom: 12px;
}

.param-item {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
  align-items: center;
}

.execute-section {
  text-align: center;
}

.response-info {
  margin-bottom: 16px;
}

.response-data h4 {
  margin: 0 0 8px 0;
  color: #303133;
}

pre {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 400px;
  overflow-y: auto;
}
</style>
