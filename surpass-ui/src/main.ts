import {createApp} from 'vue'

import Cookies from 'js-cookie'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import i18n from '@/languages'
import '@/assets/styles/index.scss' // global css

import App from './App.vue'
import store from './store'
import router from './router'
import directive from './directive' // directive

// 注册指令
import plugins from './plugins' // plugins
import {download} from '@/utils/Request'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon/index.vue'
import elementIcons from '@/components/SvgIcon/svgicon'

// 引入本地图标文件
import '@/assets/iconfont/iconfont.css'

import './permission' // permission control

import {useDict} from '@/utils/Dict'
import {parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels} from '@/utils/Jinbooks'

// 分页组件
import Pagination from '@/components/Pagination/index.vue'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar/index.vue'
// 富文本组件
import Editor from "@/components/Editor/index.vue"
// 文件上传组件
import FileUpload from "@/components/FileUpload/index.vue"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload/index.vue"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview/index.vue"
// 自定义树选择组件
import TreeSelect from '@/components/TreeSelect/index.vue'
// 字典标签组件
import DictTag from '@/components/DictTag/index.vue'
import DictTagNumber from '@/components/DIctTagNumber/index.vue'

import './index.css'  // 或 tailwind.css

import modal from './plugins/modal';

const app: any = createApp(App)

// 全局方法挂载
app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels
// 挂载到 globalProperties
app.config.globalProperties.$modal = modal;

// 全局组件挂载
app.component('DictTag', DictTag)
app.component('DictTagNumber', DictTagNumber)
app.component('Pagination', Pagination)
app.component('TreeSelect', TreeSelect)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
app.component('RightToolbar', RightToolbar)
app.component('Editor', Editor)

app.use(router)
app.use(store)
app.use(plugins)
app.use(elementIcons)
app.component('svg-icon', SvgIcon)

directive(app)

// 使用element-plus 并且设置全局的大小
app.use(i18n)
    .use(ElementPlus, {
        // locale: locale,
        // 支持 large、default、small
        size: Cookies.get('size') || 'default'
    })
app.mount('#app')
