import request from "../../utils/Request.js";

export const gatewayApi = {
    // 执行API
    execute: (path : any, method = 'GET', params = {}) => {
        const config = {
            method,
            url: `/api-v1${path}`,
            params: method === 'GET' ? params : {},
            data: method !== 'GET' ? params : {}
        }
        return request(config)
    }
}

