import {ElMessage, ElMessageBox, ElNotification, ElLoading} from 'element-plus'

import i18n from '@/languages'

const {t} = i18n.global;

let loadingInstance: any;


export default {
    // 消息提示
    msg(content: any) {
        ElMessage.info(content)
    },
    // 错误消息
    msgError(content: any) {
        ElMessage.error(content)
    },
    // 成功消息
    msgSuccess(content: any) {
        ElMessage.success(content)
    },
    // 警告消息
    msgWarning(content: any) {
        ElMessage.warning(content)
    },
    // 弹出提示
    alert(content: any) {
        ElMessageBox.alert(content, t('jbx.text.systemprompt'))
    },
    // 错误提示
    alertError(content: any) {
        ElMessageBox.alert(content, t('jbx.text.systemprompt'), {type: 'error'})
    },
    // 成功提示
    alertSuccess(content: any) {
        ElMessageBox.alert(content, t('jbx.text.systemprompt'), {type: 'success'})
    },
    // 警告提示
    alertWarning(content: any) {
        ElMessageBox.alert(content, t('jbx.text.systemprompt'), {type: 'warning'})
    },
    // 通知提示
    notify(content: any) {
        ElNotification.info(content)
    },
    // 错误通知
    notifyError(content: any) {
        ElNotification.error(content);
    },
    // 成功通知
    notifySuccess(content: any) {
        ElNotification.success(content)
    },
    // 警告通知
    notifyWarning(content: any) {
        ElNotification.warning(content)
    },
    // 确认窗体
    confirm(content: any) {
        return ElMessageBox.confirm(content, t('jbx.text.systemprompt'), {
            confirmButtonText: t('jbx.text.confirm'),
            cancelButtonText: t('jbx.text.cancel'),
            type: "warning",
        })
    },
    // 提交内容
    prompt(content: any) {
        return ElMessageBox.prompt(content, t('jbx.text.systemprompt'), {
            confirmButtonText: t('jbx.text.confirm'),
            cancelButtonText: t('jbx.text.cancel'),
            type: "warning",
        })
    },
    // 打开遮罩层
    loading(content: any) {
        loadingInstance = ElLoading.service({
            lock: true,
            text: content,
            background: "rgba(0, 0, 0, 0.7)",
        })
    },
    // 关闭遮罩层
    closeLoading() {
        loadingInstance.close();
    }
}
