import request from "../../utils/Request.js";

export const gatewayApi = {
    // 执行API
    execute: (path : any, method = 'GET', contextPath = '/', params = {}) => {
        const config = {
            method,
            url: `/api${contextPath}${path}`,
            params: method === 'GET' ? params : {},
            data: method !== 'GET' ? params : {}
        }
        return request(config)
    }
}

