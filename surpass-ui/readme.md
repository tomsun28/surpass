# Surpass Web UI

这是 Surpass 项目的前端部分，基于 Vue.js + Vite 构建。
该项目旨在提供一个高性能、模块化、易扩展的现代前端架构。

## 快速开始

### 克隆项目

```bash
git clone https://your.repo.url/surpass-ui.git
cd surpass-ui
````

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

开发服务器将在本地启动，默认地址通常为：

```
http://localhost:3154
```

## 构建与部署

### 构建生产版本

```bash
npm run build
```

### 部署到服务器

将 `dist` 目录中的文件上传至你的服务器，并配置相应的 Web 服务（如 Nginx）。

---

## 常用命令

| 命令              | 说明         |
|-----------------|------------|
| `npm run dev`   | 启动开发服务器    |
| `npm run build` | 构建生产版本     |
| `npm run lint`  | 代码格式检查     |
| `npm run test`  | 运行单元测试     |
| `npm run serve` | 本地预览构建后的文件 |

## 目录结构说明

```
surpass-web-fronted/
├── public/               # 静态资源
├── src/                  # 源代码
│   ├── assets/           # 资源文件
│   ├── components/       # 公共组件
│   ├── views/            # 页面组件
│   ├── store/            # Vuex 状态管理
│   ├── router/           # 路由配置
│   ├── utils/            # 工具函数
│   ├── styles/           # 样式文件
│   ├── App.vue           # 根组件
│   └── main.js           # 应用入口
├── .gitignore            # Git 忽略配置
├── package.json          # 项目信息及依赖
├── README.md             # 项目说明文档
└── vite.config.js        # Vite 配置
```

---

## 贡献指南

欢迎为本项目贡献代码！请遵循以下流程：

1. Fork 本仓库

2. 创建新分支：

   ```bash
   git checkout -b feature/your-feature
   ```

3. 提交更改：

   ```bash
   git commit -m "Add: your feature"
   ```

4. 推送到远程分支：

   ```bash
   git push origin feature/your-feature
   ```

5. 提交 Pull Request（PR）

## 版本管理

本项目采用 [语义化版本控制 (SemVer)](https://semver.org/lang/zh-CN/)，版本格式为：

```
MAJOR.MINOR.PATCH
```

例如：`1.0.0`、`2.1.3` 等。

---

## 依赖说明

本项目主要使用以下依赖：

* **Vue.js**：核心框架
* **Pinia**：状态管理
* **Vue Router**：路由管理
* **Element Plus**：UI 组件库
* **Axios**：HTTP 请求库

## 问题排查

若运行或构建过程中出现问题，请尝试以下操作：

1. 确保已正确安装 Node.js 和 npm

2. 检查网络连接，确保可以正常访问 npm 仓库

3. 清理缓存后重新安装依赖：

   ```bash
   npm cache clean --force
   npm install
   ```

4. 查看控制台错误信息并定位问题

如仍未解决，请提交 Issue 或联系维护者。

## 清理缓存

如果需要清理 npm 缓存，可执行以下命令：

```bash
npm cache clean --force
```

感谢使用 Surpass 前端项目，如有建议或问题，欢迎提交 Issue 或参与贡献！
