import axios, {AxiosResponse} from 'axios'
import {ElLoading, ElMessage} from 'element-plus'
import {saveAs} from 'file-saver'
import {getToken} from '@/utils/Auth'
import errorCode from '@/utils/ErrorCode'
import {blobValidate} from '@/utils/Jinbooks'

const baseURL = import.meta.env.VITE_APP_BASE_API as string

let downloadLoadingInstance: ReturnType<typeof ElLoading.service> | null = null

async function handleBlobResponse(response: AxiosResponse<Blob>, fallbackName = '文件下载') {
    const isValidBlob = await blobValidate(response.data)
    if (isValidBlob) {
        const fileNameHeader = response.headers['download-filename']
        const fileName = fileNameHeader ? decodeURIComponent(fileNameHeader) : fallbackName
        saveAs(new Blob([response.data]), fileName)
    } else {
        await printErrMsg(response.data)
    }
}

async function printErrMsg(data: Blob) {
    try {
        const text = await data.text()
        const json = JSON.parse(text)
        const msg = errorCode[json.code] || json.msg || errorCode['default']
        ElMessage.error(msg)
    } catch (err) {
        ElMessage.error('下载失败：无法解析错误信息')
    }
}

export default {
    /**
     * 下载文件
     */
    async name(fileName: string, isDelete = true): Promise<void> {
        const url = `${baseURL}/common/download?fileName=${encodeURIComponent(fileName)}&delete=${isDelete}`
        try {
            const response = await axios.get<Blob>(url, {
                responseType: 'blob',
                headers: {Authorization: 'Bearer ' + getToken()}
            })
            await handleBlobResponse(response, fileName)
        } catch (err) {
            ElMessage.error('文件下载失败')
        }
    },

    /**
     * 下载资源路径文件
     */
    async resource(resource: string): Promise<void> {
        const url = `${baseURL}/common/download/resource?resource=${encodeURIComponent(resource)}`
        try {
            const response = await axios.get<Blob>(url, {
                responseType: 'blob',
                headers: {Authorization: 'Bearer ' + getToken()}
            })
            await handleBlobResponse(response)
        } catch (err) {
            ElMessage.error('资源下载失败')
        }
    },

    /**
     * 下载 ZIP 压缩包
     */
    async zip(apiUrl: string, name: string): Promise<void> {
        const url = `${baseURL}${apiUrl}`
        downloadLoadingInstance = ElLoading.service({
            text: '正在下载数据，请稍候',
            background: 'rgba(0, 0, 0, 0.7)'
        })
        try {
            const response = await axios.get<Blob>(url, {
                responseType: 'blob',
                headers: {Authorization: 'Bearer ' + getToken()}
            })
            const isValidBlob = await blobValidate(response.data)
            if (isValidBlob) {
                saveAs(new Blob([response.data], {type: 'application/zip'}), name)
            } else {
                await printErrMsg(response.data)
            }
        } catch (err) {
            console.error(err)
            ElMessage.error('下载文件出现错误，请联系管理员！')
        } finally {
            downloadLoadingInstance?.close()
        }
    },

    /**
     * 直接保存 Blob/File
     */
    saveAs(text: Blob, name: string, opts?: any): void {
        saveAs(text, name, opts)
    }
}
