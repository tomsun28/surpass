<template>
  <div class="dashboard-container">
    <!-- 基础统计面板 -->
    <div class="section">
      <div class="section-header">
        <h2 class="section-title">数据概览</h2>
        <div class="section-actions">
          <el-button type="primary" size="small" @click="refreshData">
            <svg-icon icon-class="refresh" class="refresh-icon" />
            刷新数据
          </el-button>
          <el-button size="small" @click="exportData">
            <svg-icon icon-class="download" class="download-icon" />
            导出报表
          </el-button>
        </div>
      </div>
      <StatisticPanel :statistics="statistics" />
    </div>
    <!-- 系统状态 -->
    <div class="section">
      <div class="section-header">
        <h2 class="section-title">系统运行状态</h2>
      </div>
      <div class="system-status">
        <div class="status-card">
          <div class="status-header">
            <div class="status-title">
              <svg-icon icon-class="dashboard" class="status-icon" />
              CPU使用率
            </div>
            <div class="status-value">{{ systemStatus.cpuUsage?.toFixed(1) || '0' }}%</div>
          </div>
          <el-progress
              :percentage="systemStatus.cpuUsage || 0"
              :stroke-width="12"
              :color="getStatusColor(systemStatus.cpuUsage || 0)"
          />
        </div>
        <div class="status-card">
          <div class="status-header">
            <div class="status-title">
              <svg-icon icon-class="hdd" class="status-icon" />
              内存使用率
            </div>
            <div class="status-value">{{ systemStatus.memoryUsage?.toFixed(1) || '0' }}%</div>
          </div>
          <el-progress
              :percentage="systemStatus.memoryUsage || 0"
              :stroke-width="12"
              :color="getStatusColor(systemStatus.memoryUsage || 0)"
          />
        </div>
        <div class="status-card">
          <div class="status-header">
            <div class="status-title">
              <svg-icon icon-class="database" class="status-icon" />
              磁盘使用率
            </div>
            <div class="status-value">{{ systemStatus.diskUsage?.toFixed(1) || '0' }}%</div>
          </div>
          <el-progress
              :percentage="systemStatus.diskUsage || 0"
              :stroke-width="12"
              :color="getStatusColor(systemStatus.diskUsage || 0)"
          />
        </div>
        <div class="status-card">
          <div class="status-header">
            <div class="status-title">
              <svg-icon icon-class="user" class="status-icon" />
              活跃会话
            </div>
            <div class="status-value">{{ systemStatus.activeSessions?.toLocaleString() || '0' }}</div>
          </div>
          <div class="status-trend">
            <span class="trend-text">较昨日 +12.5%</span>
          </div>
        </div>
      </div>
    </div>

    <!-- API访问统计 -->
    <div class="section">
      <div class="section-header">
        <h2 class="section-title">API访问统计</h2>
      </div>
      <el-row :gutter="20">
        <el-col :xs="24" :lg="16">
          <ApiAccessChart />
        </el-col>
        <el-col :xs="24" :lg="8">
          <div class="api-summary">
            <div class="summary-card">
              <div class="summary-icon" style="background-color: #1890ff;">
                <svg-icon icon-class="api" class-name="icon" />
              </div>
              <div class="summary-content">
                <div class="summary-value">{{ dashboardData.todayApiCalls?.toLocaleString() || '0' }}</div>
                <div class="summary-label">今日API调用</div>
              </div>
            </div>
            <div class="summary-card">
              <div class="summary-icon" style="background-color: #52c41a;">
                <svg-icon icon-class="check-circle" class-name="icon" />
              </div>
              <div class="summary-content">
                <div class="summary-value">{{ dashboardData.apiSuccessRate?.toFixed(1) || '0' }}%</div>
                <div class="summary-label">API成功率</div>
              </div>
            </div>
            <div class="summary-card">
              <div class="summary-icon" style="background-color: #faad14;">
                <svg-icon icon-class="time" class-name="icon" />
              </div>
              <div class="summary-content">
                <div class="summary-value">{{ dashboardData.weekApiCalls?.toLocaleString() || '0' }}</div>
                <div class="summary-label">本周API调用</div>
              </div>
            </div>
            <div class="summary-card">
              <div class="summary-icon" style="background-color: #722ed1;">
                <svg-icon icon-class="calendar" class-name="icon" />
              </div>
              <div class="summary-content">
                <div class="summary-value">{{ dashboardData.monthApiCalls?.toLocaleString() || '0' }}</div>
                <div class="summary-label">本月API调用</div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 区域分布和热门API -->
    <div class="section">
      <div class="section-header">
        <h2 class="section-title">区域分布与热门API</h2>
      </div>
      <el-row :gutter="20">
        <el-col :xs="24" :lg="12">
          <RegionMap />
        </el-col>
        <el-col :xs="24" :lg="12">
          <ApiTopList />
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useDashboard } from './dashboard/hooks/useDashboard'
import StatisticPanel from './dashboard/components/StatisticPanel.vue'
import ApiAccessChart from './dashboard/components/ApiAccessChart.vue'
import RegionMap from './dashboard/components/RegionMap.vue'
import ApiTopList from './dashboard/components/ApiTopList.vue'

const { dashboardData, fetchDashboardData } = useDashboard()

const systemStatus = computed(() => dashboardData.value.systemStatus)

const statistics = computed(() => [
  {
    title: '应用数量',
    value: dashboardData.value.appCount,
    icon: 'appstore-add',
    color: '#1890ff',
    growth: -12.5
  },
  {
    title: '接口总数',
    value: dashboardData.value.apiCount,
    icon: 'api',
    color: '#74e2ca',
    growth: 12.5
  },
  {
    title: '角色数量',
    value: dashboardData.value.roleCount,
    icon: 'anticon-group',
    color: '#52c41a',
    growth: 8.3
  },
  {
    title: '用户数量',
    value: dashboardData.value.userCount,
    icon: 'user',
    color: '#faad14',
    growth: 15.2
  },
  {
    title: '客户数量',
    value: dashboardData.value.clientCount,
    icon: 'user2',
    color: '#722ed1',
    growth: 5.7
  },
  {
    title: '数据源',
    value: dashboardData.value.datasourceCount,
    icon: 'database',
    color: '#ff4d4f',
    growth: 3.4
  }
])

const getStatusColor = (percentage: number) => {
  if (percentage < 60) return '#52c41a'
  if (percentage < 80) return '#faad14'
  return '#ff4d4f'
}

const refreshData = async () => {
  await fetchDashboardData()
}

const exportData = () => {
  // 导出报表功能
  console.log('导出报表')
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 24px;

    .page-title {
      font-size: 24px;
      font-weight: bold;
      color: #333;
      margin: 0 0 8px 0;
    }

    .page-subtitle {
      font-size: 14px;
      color: #666;
    }
  }

  .section {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .section-title {
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin: 0;
      }

      .section-actions {
        display: flex;
        gap: 8px;

        .refresh-icon,
        .download-icon {
          margin-right: 4px;
        }
      }
    }
  }

  .api-summary {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    height: 400px;

    .summary-card {
      background: #fafafa;
      border-radius: 8px;
      padding: 16px;
      display: flex;
      align-items: center;
      gap: 12px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .summary-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;

        .icon {
          font-size: 24px;
          color: #fff;
        }
      }

      .summary-content {
        flex: 1;

        .summary-value {
          font-size: 24px;
          font-weight: bold;
          color: #333;
          line-height: 1.2;
        }

        .summary-label {
          font-size: 12px;
          color: #666;
          margin-top: 4px;
        }
      }
    }
  }

  .system-status {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;

    .status-card {
      background: #fafafa;
      border-radius: 8px;
      padding: 20px;

      .status-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;

        .status-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 14px;
          font-weight: 500;
          color: #333;

          .status-icon {
            font-size: 16px;
            color: #1890ff;
          }
        }

        .status-value {
          font-size: 24px;
          font-weight: bold;
          color: #333;
        }
      }

      .status-trend {
        margin-top: 16px;

        .trend-text {
          font-size: 12px;
          color: #52c41a;
          font-weight: 500;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 12px;

    .api-summary {
      grid-template-columns: 1fr;
      height: auto;
    }

    .system-status {
      grid-template-columns: 1fr;
    }
  }
}
</style>