import request from '@/utils/Request'

export function setGenerateSecretBase(): any {
  return request({
    url: '/apps/generate/secret/base',
    method: 'get'
  })
}

export function setGenerateSecretMethod(me : any, id : any): any {
  return request({
    url: `/apps/generate/secret/${me}?id=${id}`,
    method: 'get'
  })
}

export function listApps(query : any): any {
  return request({
    url: '/apps/fetch',
    method: 'get',
    params: query
  })
}

export function getAppsInit(): any {
  return request({
    url: '/apps/init',
    method: 'get'
  })
}

export function getApps(id : any): any {
  return request({
    url: '/apps/get/' + id,
    method: 'get'
  })
}

export function getAppsOauth2(id : any): any {
  return request({
    url: '/apps/oauth20/get/' + id,
    method: 'get'
  })
}

export function getAppsSaml20(id : any): any {
  return request({
    url: '/apps/saml20/get/' + id,
    method: 'get'
  })
}

export function getAppsOauthInit(): any {
  return request({
    url: '/apps/oauth20/init',
    method: 'get'
  })
}

export function getAppsSaml20Init(): any {
  return request({
    url: '/apps/saml20/init',
    method: 'get'
  })
}

export function addApps(data : any): any {
  return request({
    url: '/apps/add',
    method: 'post',
    data: data
  })
}

export function addAppsOAuth(data : any): any {
  return request({
    url: '/apps/oauth20/add',
    method: 'post',
    data: data
  })
}

export function addAppsSaml20(data : any): any {
  return request({
    url: '/apps/saml20/add',
    method: 'post',
    data: data
  })
}

export function appsUpdateExtendAttr(data : any): any {
  return request({
    url: '/apps/updateExtendAttr',
    method: 'post',
    data: data
  })
}

export function updateApps(data : any): any {
  return request({
    url: '/apps/update',
    method: 'put',
    data: data
  })
}

export function updateOAuthApps(data : any): any {
  return request({
    url: '/apps/oauth20/update',
    method: 'put',
    data: data
  })
}

export function updateSaml20Apps(data : any): any {
  return request({
    url: '/apps/saml20/update',
    method: 'put',
    data: data
  })
}

export function delApps(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/apps/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
