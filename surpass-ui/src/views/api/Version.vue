<template>
  <div class="version-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-info">
          <h1 class="page-title">版本管理</h1>
          <p class="page-description">管理API版本，配置SQL模板和响应模板</p>
        </div>
        <div class="header-actions">

        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- API选择卡片 -->
      <el-card class="api-selector-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div style="display: flex;justify-content: flex-start;align-items: center">
              <span>选择API：</span>
              <div class="selector-content">
                <el-select
                    v-model="selectedApiId"
                    placeholder="请选择要管理的API"
                    style="width: 400px"
                    @change="loadVersions"
                    :loading="apiLoading"
                >
                  <el-option
                      v-for="api in apiList"
                      :key="api.id"
                      :label="api.name"
                      :value="api.id"
                  >
                    <div class="api-option">
                      <span class="api-name">{{ api.name }}</span>
                      <span class="api-path">{{ api.path }}</span>
                    </div>
                  </el-option>
                </el-select>
              </div>
            </div>
            <div>
              <el-button type="primary" @click="showCreateDialog">
                新增版本
              </el-button>
              <el-button type="success" @click="createNextVersion" v-if="hasPublishedVersion">
                新增下一个版本
              </el-button>
              <el-button type="primary" @click="refreshList" :loading="loading">
                刷新
              </el-button>
            </div>
          </div>
        </template>

      </el-card>

      <!-- 版本管理区域 -->
      <div v-if="selectedApiId" class="version-management">
        <!-- 统计信息和操作栏 -->
        <div class="management-header">
          <!-- 统计卡片 -->
          <div class="statistics-cards" v-if="versionStatistics">
            <el-card class="stat-card total" shadow="hover">
              <div class="stat-content">
                <div class="stat-icon">
                  <el-icon>
                    <Collection/>
                  </el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ versionStatistics.total }}</div>
                  <div class="stat-label">总版本数</div>
                </div>
              </div>
            </el-card>
            <el-card class="stat-card draft" shadow="hover">
              <div class="stat-content">
                <div class="stat-icon">
                  <el-icon>
                    <Edit/>
                  </el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ versionStatistics.draft }}</div>
                  <div class="stat-label">草稿</div>
                </div>
              </div>
            </el-card>
            <el-card class="stat-card pending" shadow="hover">
              <div class="stat-content">
                <div class="stat-icon">
                  <el-icon>
                    <Clock/>
                  </el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ versionStatistics.pending }}</div>
                  <div class="stat-label">待发布</div>
                </div>
              </div>
            </el-card>
            <el-card class="stat-card published" shadow="hover">
              <div class="stat-content">
                <div class="stat-icon">
                  <el-icon>
                    <Check/>
                  </el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ versionStatistics.published }}</div>
                  <div class="stat-label">已发布</div>
                </div>
              </div>
            </el-card>
          </div>
        </div>

        <!-- 版本列表 -->
        <el-card class="version-list-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>版本列表</span>
              <div class="list-info">
                <span class="total-count">共 {{ versionList.length }} 个版本</span>
              </div>
            </div>
          </template>

          <el-table
              :data="versionList"
              v-loading="loading"
              border
          >
            <el-table-column header-align="center" align="center" prop="version" label="版本号" width="80">
              <template #default="{ row }">
                <span class="version-number">v{{ row.version }}</span>
              </template>
            </el-table-column>

            <el-table-column header-align="center" prop="sqlTemplate" label="SQL模板" min-width="200">
              <template #default="{ row }">
                <div class="sql-preview">
                  <code>{{ truncateSQL(row.sqlTemplate) }}</code>
                </div>
              </template>
            </el-table-column>

            <el-table-column header-align="center" align="center" label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.status)" size="large" effect="light">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="supportsPaging" label="分页" width="80" align="center">
              <template #default="{ row }">
                {{ row.supportsPaging ? '是' : '否' }}
              </template>
            </el-table-column>

            <el-table-column header-align="center" prop="description" label="描述" min-width="150">
              <template #default="{ row }">
                <span class="description-text">{{ row.description || '-' }}</span>
              </template>
            </el-table-column>


            <el-table-column header-align="center" prop="createdDate" label="创建时间" width="180"/>

            <el-table-column header-align="center" align="center" label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons-group">
                  <el-tooltip content="查看" placement="top">
                    <el-button link type="primary" icon="View" @click="viewVersion(row)">
                    </el-button>
                  </el-tooltip>

                  <el-tooltip content="编辑" placement="top">
                    <el-button icon="Edit" link type="primary" @click="editVersion(row)" v-if="row.status === 0">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="提交审核" placement="top">
                    <el-button icon="Upload" link type="warning" @click="submitForReview(row)" v-if="row.status === 0">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="发布" placement="top">
                    <el-button icon="Check" link type="success" @click="publishVersion(row)" v-if="row.status === 1">
                    </el-button>
                  </el-tooltip>

                  <el-tooltip content="驳回" placement="top">
                    <el-button icon="Close" link type="danger" @click="rejectVersion(row)" v-if="row.status === 1">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="下线" placement="top">
                    <el-button icon="Remove" link type="info" @click="offlineVersion(row)" v-if="row.status === 2">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="上线" placement="top">
                    <el-button icon="CircleCheck" link type="success" @click="onlineVersion(row)"
                               v-if="row.status === 3">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <el-button icon="Delete" link type="danger" @click="deleteVersion(row)" v-if="row.status === 0">
                    </el-button>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <!-- 空状态 -->
          <div v-if="!loading && versionList.length === 0" class="empty-state">
            <el-empty description="暂无版本数据" :image-size="200">
              <el-button type="primary" @click="showCreateDialog">创建第一个版本</el-button>
            </el-empty>
          </div>
        </el-card>
      </div>

      <!-- 未选择API的提示 -->
      <div v-else class="no-api-selected">
        <el-empty description="请选择API开始版本管理" :image-size="200"/>
      </div>
    </div>

    <!-- 新增/编辑抽屉 -->
    <el-drawer
        :title="dialogTitle"
        v-model="drawerVisible"
        direction="rtl"
        size="50%"
        :before-close="handleDrawerClose"
    >
      <template #header>
        <h4>{{ dialogTitle }}</h4>
      </template>
      <div class="drawer-content">
        <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="100px"
        >
          <el-form-item label="版本号" prop="version">
            <el-input-number
                v-model="formData.version"
                :min="1"
                :max="999"
                placeholder="请输入版本号"
            />
          </el-form-item>

          <el-form-item label="SQL模板" prop="sqlTemplate">
            <el-input
                v-model="formData.sqlTemplate"
                type="textarea"
                :rows="4"
                placeholder="请输入SQL模板，支持命名参数如 #{name}"
            />
          </el-form-item>

          <el-form-item label="参数定义" prop="paramDefinition">
            <div class="param-definition-container">
              <div class="param-header">
                <span></span>
                <el-button type="primary" size="small" @click="addParam">
                  添加参数
                </el-button>
              </div>
              <el-table :data="paramList" border style="width: 100%; margin-top: 10px;">
                <el-table-column prop="name" label="参数名" width="120">
                  <template #default="{ row, $index }">
                    <el-input
                        v-model="row.name"
                        placeholder="参数名"
                        :readonly="row.readOnly"
                        :class="{ 'read-only-param': row.readOnly }"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="类型" width="120">
                  <template #default="{ row }">
                    <el-select
                        v-model="row.type"
                        placeholder="选择类型"
                        :disabled="row.readOnly"
                    >
                      <el-option label="字符串" value="string"/>
                      <el-option label="数字" value="number"/>
                      <el-option label="布尔值" value="boolean"/>
                      <el-option label="数组" value="array"/>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="rules" label="输入规则">
                  <template #default="{ row }">
                    <el-button
                        link
                        type="primary"
                        @click="openRuleConfig(row)"
                        :title="JSON.stringify(row.rules, null, 2)"
                        :disabled="row.readOnly"
                    >
                      {{ getRuleDisplayText(row.rules) || '配置规则' }}
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="描述">
                  <template #default="{ row }">
                    <el-input
                        v-model="row.description"
                        placeholder="参数描述"
                        :readonly="row.readOnly"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="60" align="center">
                  <template #default="{ $index }">
                    <el-button
                        icon="Delete"
                        link
                        type="danger"
                        size="small"
                        @click="removeParam($index)"
                        :disabled="paramList[$index]?.readOnly"
                    ></el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>

          <el-form-item label="响应模板" prop="responseTemplate">
            <el-input
                v-model="formData.responseTemplate"
                type="textarea"
                :rows="4"
                placeholder="请输入响应模板，支持 #{data} 占位符代表结果数据"
            />
            <div class="template-tips">
              <p><strong>模板提示：</strong></p>
              <p>• 使用 <code>#{data}</code> 占位符代表查询结果数据</p>
              <p>• 示例：<code>{"code": 0, "message": "success", "data": #{data}}</code></p>
              <p>• 支持JSON格式，系统会自动将查询结果替换到 #{data} 位置</p>
            </div>
          </el-form-item>

          <el-form-item label="描述" prop="description">
            <el-input
                v-model="formData.description"
                type="textarea"
                :rows="2"
                placeholder="请输入版本描述"
            />
          </el-form-item>

          <!-- 分页和限流配置 -->
          <el-divider content-position="left">分页和限流配置</el-divider>

          <el-form-item label="是否分页">
            <el-switch
                v-model="formData.supportsPaging"
                :active-value="1"
                :inactive-value="0"
                active-text="是"
                inactive-text="否"
                @change="handlePagingParams"
            />
          </el-form-item>

          <el-form-item label="最大页面大小" prop="pageSizeMax">
            <el-input-number
                style="width: 200px;"
                v-model="formData.pageSizeMax"
                :min="1"
                :max="10000"
                placeholder=""
                controls-position="right"
            />
            <div class="form-item-tip">限制每页最大记录数</div>
          </el-form-item>

          <el-form-item label="速率限制" prop="rateLimit">
            <el-input-number
                style="width: 200px;"
                v-model="formData.rateLimit"
                :min="0"
                placeholder="次/分钟"
                controls-position="right"
            />
            <div class="form-item-tip">0表示不限制，限制API每分钟调用次数</div>
          </el-form-item>
        </el-form>

        <div class="drawer-footer">
          <el-button @click="handleDrawerClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            保存
          </el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 规则配置对话框 -->
    <el-dialog
        v-model="ruleConfigDialog"
        title="输入规则配置"
        width="500px"
    >
      <el-form label-width="100px">
        <el-form-item label="必填">
          <el-switch v-model="ruleFormData.required"/>
        </el-form-item>
        <div v-if="currentEditingParam?.type === 'string'">
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
        <div v-if="currentEditingParam?.type === 'number'">
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
        <el-button @click="ruleConfigDialog = false">取消</el-button>
        <el-button type="primary" @click="saveRuleConfig">确定</el-button>
      </template>
    </el-dialog>

    <!-- 版本详情对话框 -->
    <el-dialog
        title="版本详情"
        v-model="detailDialogVisible"
        width="900px"
        class="version-detail-dialog"
    >
      <div class="version-detail-content" v-if="currentVersion">
        <!-- 基本信息卡片 -->
        <el-card class="info-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="版本号">
              <span class="version-number">v{{ currentVersion.version }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusTagType(currentVersion.status)" size="small">
                {{ getStatusText(currentVersion.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ currentVersion.createdDate || currentVersion.createTime }}
            </el-descriptions-item>
            <el-descriptions-item label="描述">
              {{ currentVersion.description || '无' }}
            </el-descriptions-item>
            <el-descriptions-item label="是否分页">
              <el-tag :type="currentVersion.supportsPaging ? 'success' : 'info'" size="small">
                {{ currentVersion.supportsPaging ? '是' : '否' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="最大页面大小" v-if="currentVersion.pageSizeMax">
              {{ currentVersion.pageSizeMax }}
            </el-descriptions-item>
            <el-descriptions-item label="速率限制">
              {{ currentVersion.rateLimit || '无' }} 次/分钟
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- SQL模板卡片 -->
        <el-card class="template-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>SQL模板</span>
            </div>
          </template>
          <div class="code-block">
            <pre><code class="sql">{{ currentVersion.sqlTemplate }}</code></pre>
          </div>
        </el-card>

        <!-- 参数定义卡片 -->
        <el-card class="param-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>参数定义</span>
              <el-tag v-if="parsedParams && parsedParams.length > 0" type="info" size="small">
                {{ parsedParams.length }} 个参数
              </el-tag>
            </div>
          </template>
          <div v-if="parsedParams && parsedParams.length > 0">
            <el-table :data="parsedParams" border size="small" class="param-table">
              <el-table-column prop="name" label="参数名" width="120" align="center">
                <template #default="{ row }">
                  <el-tag type="primary" size="small">{{ row.name }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="类型" width="100" align="center">
                <template #default="{ row }">
                  <el-tag :type="getParamTypeTagType(row.type)" size="small">
                    {{ row.type }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="描述">
                <template #default="{ row }">
                  {{ row.description || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="required" label="必填" width="80" align="center">
                <template #default="{ row }">
                  <el-tag v-if="row.rules.required" type="danger" size="small">是</el-tag>
                  <el-tag v-else type="info" size="small">否</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="rules" label="输入规则">
                <template #default="{ row }">
                  {{ getRuleDisplayText(row.rules) || '无' }}
                </template>
              </el-table-column>

            </el-table>
          </div>
          <div v-else class="empty-state">
            <el-empty description="无参数定义" :image-size="80"/>
          </div>
        </el-card>

        <!-- 响应模板卡片 -->
        <el-card class="response-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>响应模板</span>
              <el-tag v-if="currentVersion.responseTemplate" type="success" size="small">
                已配置
              </el-tag>
            </div>
          </template>
          <div v-if="currentVersion.responseTemplate">
            <div class="code-block">
              <pre><code class="json">{{ formatResponseTemplate(currentVersion.responseTemplate) }}</code></pre>
            </div>
            <div class="template-tips">
              <p><strong>模板说明：</strong></p>
              <p>• <code>#{data}</code> 占位符将被实际查询结果替换</p>
              <p>• 支持JSON格式的响应模板</p>
            </div>
          </div>
          <div v-else class="empty-state">
            <el-empty description="无响应模板" :image-size="80"/>
          </div>
        </el-card>
      </div>
    </el-dialog>

  </div>
</template>

<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import * as publishApi from '@/api/api-service/publishApi.ts'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import * as apiVersionApi from '@/api/api-service/apiVersionApi.ts'
import {getById} from "@/api/api-service/apiVersionApi.ts";

const route = useRoute()

// 响应式数据
const loading = ref(false)
const apiLoading = ref(false)
const drawerVisible = ref(false)
const detailDialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref()

const selectedApiId = ref(null)
const apiList = ref([])
const versionList = ref([])
const currentVersion = ref(null)
const versionStatistics = ref(null)

const formData = reactive({
  id: null,
  apiId: null,
  version: 1,
  sqlTemplate: '',
  paramDefinition: '',
  responseTemplate: '',
  description: '',
  supportsPaging: 0,
  pageSizeMax: 20,
  rateLimit: null
})

// 参数列表
const paramList = ref([])

// 规则配置相关
const ruleConfigDialog = ref(false)
const currentEditingParam = ref(null)
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

// 表单验证规则
const formRules = {
  version: [
    {required: true, message: '请输入版本号', trigger: 'blur'}
  ],
  sqlTemplate: [
    {required: true, message: '请输入SQL模板', trigger: 'blur'}
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑版本' : '新增版本')

const hasPublishedVersion = computed(() => {
  return versionList.value.some(version => version.status === 2) // 2 = 已发布
})

// 解析参数定义
const parsedParams = computed(() => {
  if (!currentVersion.value || !currentVersion.value.paramDefinition) {
    return []
  }

  try {
    return JSON.parse(currentVersion.value.paramDefinition)
  } catch (error) {
    console.error('解析参数定义失败:', error)
    return []
  }
})

// 生命周期
onMounted(() => {
  loadApis()
  // 如果有URL参数，自动选择API
  if (route.query.apiId) {
    selectedApiId.value = route.query.apiId
    loadVersions()
  }
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

const loadVersions = async () => {
  if (!selectedApiId.value) return

  try {
    loading.value = true
    const [versionsResponse, statisticsResponse] = await Promise.all([
      apiVersionApi.getByApiId(selectedApiId.value),
      apiVersionApi.getVersionStatistics(selectedApiId.value)
    ])

    versionList.value = versionsResponse.data || []
    versionStatistics.value = statisticsResponse.data || null
  } catch (error) {
    ElMessage.error('加载版本列表失败')
    console.error('加载版本列表失败:', error)
  } finally {
    loading.value = false
  }
}

const refreshList = () => {
  loadVersions()
}

const showCreateDialog = () => {
  if (!selectedApiId.value) {
    ElMessage.warning('请先选择API')
    return
  }

  isEdit.value = false
  resetForm()
  formData.apiId = selectedApiId.value

  // 自动设置下一个版本号
  if (versionList.value.length > 0) {
    const maxVersion = Math.max(...versionList.value.map(v => v.version))
    formData.version = maxVersion + 1
  } else {
    formData.version = 1
  }

  // 处理分页参数
  handlePagingParams()

  drawerVisible.value = true
}

const editVersion = async (row) => {
  if (row.status !== 0) {
    ElMessage.warning('只有草稿状态的版本可以编辑')
    return
  }

  isEdit.value = true
  const apiVersionResponse = await apiVersionApi.getById(row.id)
  Object.assign(formData, {...apiVersionResponse.data})

  // 解析参数定义
  if (row.paramDefinition) {
    try {
      paramList.value = Array.isArray(row.paramDefinition) ? row.paramDefinition : JSON.parse(row.paramDefinition)
    } catch (error) {
      console.error('解析参数定义失败:', error)
      paramList.value = []
    }
  } else {
    paramList.value = []
  }

  // 处理分页参数
  handlePagingParams()

  drawerVisible.value = true
}

const viewVersion = (row) => {
  currentVersion.value = row
  detailDialogVisible.value = true
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    apiId: null,
    version: 1,
    sqlTemplate: '',
    paramDefinition: '',
    responseTemplate: '',
    description: '',
    supportsPaging: 0,
    pageSizeMax: 20,
    rateLimit: null
  })
  paramList.value = []
  formRef.value?.clearValidate()
}

const handleDrawerClose = () => {
  drawerVisible.value = false
  resetForm()
}

// 参数管理方法
const addParam = () => {
  paramList.value.push({
    name: '',
    type: 'string',
    rules: {},
    description: ''
  })
}

const removeParam = (index) => {
  // 不允许删除分页参数
  if (paramList.value[index].readOnly) {
    ElMessage.warning('只读参数不能删除')
    return
  }
  paramList.value.splice(index, 1)
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    // 处理分页参数
    handlePagingParams()

    // 将参数列表转换为JSON字符串
    const validParams = paramList.value.filter(param => param.name.trim() !== '')
    // 将 paramDefinition 转换为 JSON 字符串后提交
    formData.paramDefinition = validParams.length > 0 ? JSON.stringify(validParams, null, 2) : ''

    if (isEdit.value) {
      await apiVersionApi.update(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await apiVersionApi.create(formData)
      ElMessage.success('创建成功')
    }

    drawerVisible.value = false
    loadVersions()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    console.error('保存失败:', error)
  } finally {
    submitting.value = false
  }
}

const submitForReview = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要提交版本 v${row.version} 审核吗？`,
        '确认提交审核',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await publishApi.submitForReview(row.apiId, row.id)
    ElMessage.success('提交审核成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交审核失败')
      console.error('提交审核失败:', error)
    }
  }
}

const publishVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要发布版本 v${row.version} 吗？`,
        '确认发布',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await publishApi.publishVersion(row.apiId, row.id)
    ElMessage.success('发布成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发布失败')
      console.error('发布失败:', error)
    }
  }
}

const rejectVersion = async (row) => {
  try {
    const {value: reason} = await ElMessageBox.prompt(
        '请输入驳回原因',
        '驳回版本',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'textarea',
          inputPlaceholder: '请输入驳回原因...'
        }
    )

    if (reason) {
      await publishApi.rejectVersion(row.apiId, row.id, reason)
      ElMessage.success('版本已驳回')
      loadVersions()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('驳回失败')
      console.error('驳回失败:', error)
    }
  }
}

const offlineVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要下线版本 v${row.version} 吗？`,
        '确认下线',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await publishApi.offlineVersion(row.apiId, row.id)
    ElMessage.success('下线成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('下线失败')
      console.error('下线失败:', error)
    }
  }
}

const onlineVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要重新上线版本 v${row.version} 吗？`,
        '确认上线',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await publishApi.onlineVersion(row.apiId, row.id)
    ElMessage.success('上线成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('上线失败')
      console.error('上线失败:', error)
    }
  }
}

const createNextVersion = async () => {
  if (!selectedApiId.value) {
    ElMessage.warning('请先选择API')
    return
  }

  try {
    // 查找当前已发布的版本
    const publishedVersion = versionList.value.find(v => v.status === 2)
    if (!publishedVersion) {
      ElMessage.warning('没有找到已发布的版本')
      return
    }

    // 计算下一个版本号
    const maxVersion = Math.max(...versionList.value.map(v => v.version))
    const nextVersion = maxVersion + 1

    // 填充表单数据
    isEdit.value = false
    resetForm()
    formData.apiId = selectedApiId.value
    formData.version = nextVersion
    formData.sqlTemplate = publishedVersion.sqlTemplate
    formData.paramDefinition = publishedVersion.paramDefinition
    formData.responseTemplate = publishedVersion.responseTemplate
    formData.description = `基于版本 v${publishedVersion.version} 迭代`
    formData.pageSizeMax = publishedVersion.pageSizeMax
    formData.supportsPaging = publishedVersion.supportsPaging
    formData.rateLimit = publishedVersion.rateLimit

    // 解析参数定义
    if (publishedVersion.paramDefinition) {
      try {
        paramList.value = Array.isArray(publishedVersion.paramDefinition) ? publishedVersion.paramDefinition : JSON.parse(publishedVersion.paramDefinition)
      } catch (error) {
        console.error('解析参数定义失败:', error)
        paramList.value = []
      }
    }

    // 处理分页参数
    handlePagingParams()

    drawerVisible.value = true

  } catch (error) {
    ElMessage.error('创建下一个版本失败')
    console.error('创建下一个版本失败:', error)
  }
}

const deleteVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除版本 v${row.version} 吗？此操作不可恢复。`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await apiVersionApi.deleteData(row.id)
    ElMessage.success('删除成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const getStatusTagType = (status) => {
  const types = {
    0: 'info',    // 草稿
    1: 'warning', // 待发布
    2: 'success', // 已发布
    3: 'danger'   // 下线
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '草稿',
    1: '待发布',
    2: '已发布',
    3: '下线'
  }
  return texts[status] || '未知'
}

// 获取参数类型标签样式
const getParamTypeTagType = (type) => {
  const types = {
    'string': 'success',
    'number': 'warning',
    'boolean': 'danger',
    'array': 'info',
    'object': 'primary'
  }
  return types[type] || 'info'
}

// 格式化响应模板显示
const formatResponseTemplate = (template) => {
  if (!template) return ''

  try {
    // 尝试解析为JSON进行格式化
    const jsonStr = template.replace(/#\{data\}/g, '"#{data}"')
    const parsed = JSON.parse(jsonStr)
    return JSON.stringify(parsed, null, 2).replace(/"#\{data\}"/g, '#{data}')
  } catch (error) {
    // 如果不是有效的JSON，直接返回原内容
    return template
  }
}

// 截断SQL预览
const truncateSQL = (sql) => {
  if (!sql) return ''
  if (sql.length <= 80) return sql
  return sql.substring(0, 80) + '...'
}

// 在 data 部分添加新属性

// 在方法部分添加新方法
const openRuleConfig = (param) => {
  // 不允许编辑只读参数的规则
  if (param.readOnly) {
    ElMessage.warning('不能编辑只读参数的规则')
    return
  }

  currentEditingParam.value = param
  // 解析现有的规则
  parseRulesToForm(param.rules)
  ruleConfigDialog.value = true
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

const saveRuleConfig = () => {
  if (!currentEditingParam.value) return

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

  // 直接存储为对象而不是序列化为字符串
  currentEditingParam.value.rules = Object.keys(rules).length > 0 ? rules : {}
  ruleConfigDialog.value = false
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
    if (currentEditingParam.value?.type === 'number') {
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

const getRuleDisplayText = (rulesObj) => {
  if (!rulesObj || Object.keys(rulesObj).length === 0) return ''

  try {
    const descriptions = []

    if (rulesObj.required) descriptions.push('必填')
    if (rulesObj.minLength !== undefined) descriptions.push(`最短${rulesObj.minLength}字符`)
    if (rulesObj.maxLength !== undefined) descriptions.push(`最长${rulesObj.maxLength}字符`)
    if (rulesObj.pattern) descriptions.push('正则匹配')
    if (rulesObj.minValue !== undefined) descriptions.push(`最小值${rulesObj.minValue}`)
    if (rulesObj.maxValue !== undefined) descriptions.push(`最大值${rulesObj.maxValue}`)
    if (rulesObj.enumValues) descriptions.push(`枚举值(${rulesObj.enumValues.length}个)`)
    if (rulesObj.format) descriptions.push(`${rulesObj.format}格式`)

    return descriptions.join(', ') || '已配置'
  } catch (e) {
    return '规则格式错误'
  }
}

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

const handlePagingParams = () => {
  // 如果启用了分页，则确保分页参数存在
  if (formData.supportsPaging) {
    // 检查是否已经存在分页参数
    const pageNumIndex = paramList.value.findIndex(param => param.name === '_pageNum')
    const pageSizeIndex = paramList.value.findIndex(param => param.name === '_pageSize')

    // 添加或更新 _pageNum 参数
    if (pageNumIndex === -1) {
      paramList.value.unshift({
        name: '_pageNum',
        type: 'number',
        rules: {required: true, minValue: 1},
        description: '页码',
        readOnly: true
      })
    } else {
      // 确保只读属性设置正确
      paramList.value[pageNumIndex].readOnly = true
      paramList.value[pageNumIndex].type = 'number'
      paramList.value[pageNumIndex].rules = {required: true, minValue: 1}
    }

    // 添加或更新 _pageSize 参数
    if (pageSizeIndex === -1) {
      paramList.value.unshift({
        name: '_pageSize',
        type: 'number',
        rules: {required: true, minValue: 1, maxValue: formData.pageSizeMax || 10000},
        description: '每页条数',
        readOnly: true
      })
    } else {
      // 确保只读属性设置正确
      paramList.value[pageSizeIndex].readOnly = true
      paramList.value[pageSizeIndex].type = 'number'
      paramList.value[pageSizeIndex].rules = {required: true, minValue: 1, maxValue: formData.pageSizeMax || 10000}
    }
  } else {
    // 如果禁用了分页，则移除分页参数
    paramList.value = paramList.value.filter(param =>
        param.name !== '_pageNum' && param.name !== '_pageSize'
    )
  }
}

</script>

<style scoped>
.version-page {
  background: #f5f7fa;
  min-height: calc(100vh - 140px);
  padding: 0;
}

/* 页面头部 */
.page-header {
  background: #fff;
  padding: 24px;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin: 0 auto;
}

.header-info {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  line-height: 1.2;
}

.page-description {
  margin: 0;
  color: #909399;
  font-size: 14px;
  line-height: 1.5;
}

.header-actions {
  flex-shrink: 0;
}

/* 主要内容区域 */
.main-content {
  margin: 0 auto;
  padding: 24px;
}

/* API选择卡片 */
.api-selector-card {
  margin-bottom: 24px;
  border-radius: 12px;
  border: 1px solid #e4e7ed;
}

.api-selector-card .card-header {
  padding: 10px 0px;
  border-bottom: 1px solid #f0f2f5;
  font-weight: 600;
  color: #303133;
}

.api-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.api-name {
  font-weight: 500;
  color: #303133;
}

.api-path {
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
}

/* 版本管理区域 */
.version-management {
  margin-top: 24px;
}

/* 管理头部 */
.management-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  gap: 24px;
}

/* 统计卡片 */
.statistics-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  flex: 1;
}

.stat-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
}

.stat-card.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-card.draft {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stat-card.pending {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-card.published {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  margin-right: 16px;
  font-size: 32px;
  opacity: 0.9;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 12px;
  flex-shrink: 0;
}

/* 版本列表卡片 */
.version-list-card {
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  overflow: hidden;
}

.version-list-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0px;
  border-bottom: 1px solid #f0f2f5;
  font-weight: 600;
  color: #303133;
}

.list-info {
  font-size: 14px;
  color: #909399;
}

.total-count {
  background: #f5f7fa;
  padding: 4px 8px;
  border-radius: 4px;
}

/* 表格样式 */
.version-number {
  font-weight: 600;
  color: #409eff;
  font-size: 14px;
}

.sql-preview {
  max-width: 300px;
}

.sql-preview code {
  background: #f5f7fa;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: 'JetBrains Mono', 'Fira Code', monospace;
  font-size: 12px;
  color: #606266;
  line-height: 1.4;
}

.description-text {
  color: #606266;
  line-height: 1.5;
}

.action-buttons-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
}

.action-buttons-group .el-button {
  margin: 0;
}

/* 空状态 */
.empty-state {
  padding: 60px 0;
}

.no-api-selected {
  padding: 80px 0;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .management-header {
    flex-direction: column;
  }

  .statistics-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .action-buttons {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 16px;
  }

  .statistics-cards {
    grid-template-columns: 1fr;
  }

  .header-content {
    flex-direction: column;
    gap: 16px;
  }

  .header-actions {
    width: 100%;
  }
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

.action-bar {
  margin-bottom: 20px;
}

.version-statistics {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.drawer-content {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.read-only-param input {
  color: #909399 !important;
  background-color: #f5f7fa !important;
}

.read-only-param input:focus {
  border-color: #dcdfe6 !important;
}

.drawer-footer {
  margin-top: auto;
  padding-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.param-definition-container {
  width: 100%;
}

.param-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.param-header span {
  font-weight: bold;
  color: #606266;
}

.template-tips {
  margin-top: 8px;
  padding: 12px;
  background: #f0f9ff;
  border: 1px solid #e1f5fe;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}

.template-tips p {
  margin: 4px 0;
}

.template-tips code {
  background: #e8f4fd;
  padding: 2px 4px;
  border-radius: 2px;
  color: #1890ff;
  font-family: 'Courier New', monospace;
}

/* 版本详情对话框样式 */
.version-detail-dialog .el-dialog__body {
  padding: 0;
}

.version-detail-content {
  padding: 20px;
  max-height: 70vh;
  overflow-y: auto;
}

.version-detail-content .el-card {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
}

.version-detail-content .el-card:last-child {
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.version-number {
  font-size: 16px;
  font-weight: bold;
  color: #409eff;
}

.code-block {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  overflow: hidden;
}

.code-block pre {
  margin: 0;
  padding: 16px;
  font-family: 'JetBrains Mono', 'Fira Code', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.5;
  color: #24292e;
  background: transparent;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-all;
}

.code-block code.sql {
  color: #d73a49;
}

.code-block code.json {
  color: #032f62;
}

.code-block code.json .data-placeholder {
  color: #e36209;
  font-weight: bold;
}

.param-table {
  margin-top: 0;
}

.param-table .el-table {
  border-radius: 6px;
}

.param-table .el-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.empty-state {
  padding: 40px 0;
}

/* 响应模板提示样式 */
.response-card .template-tips {
  margin-top: 12px;
  padding: 12px;
  background: #f0f9ff;
  border: 1px solid #e1f5fe;
  border-radius: 6px;
  font-size: 12px;
  color: #666;
}

.response-card .template-tips p {
  margin: 4px 0;
}

.response-card .template-tips code {
  background: #e8f4fd;
  padding: 2px 6px;
  border-radius: 3px;
  color: #1890ff;
  font-family: 'Courier New', monospace;
  font-weight: 500;
}

/* 基本信息卡片样式 */
.info-card .el-descriptions {
  margin-top: 0;
}

.info-card .el-descriptions__label {
  font-weight: 600;
  color: #606266;
}

.info-card .el-descriptions__content {
  color: #303133;
}

pre {
  background: #f5f7fa;
  padding: 8px;
  border-radius: 4px;
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
}

.form-item-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>