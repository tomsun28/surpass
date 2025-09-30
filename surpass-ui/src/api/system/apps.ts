import request from '@/utils/Request';
/* import apiConfig from '../../apiConfig'; */


export function myApps(): any {
  return request({
    url: '/apps/myApps',
    method: 'get'
  })
}

//分页查询
export function list(query : any): any {
  return request({
    url: '/apps/list',
    method: 'get',
    params: query
  })
}

// 查询全部应用
export function allApps(): any {
  return request({
    url: '/apps/all',
    method: 'get'
  })
}


//新增oidc
export function oidcCreate(data : any): any {
  return request({
    url: `/apps/oidc/create`,
    method: 'post',
    data: data
  })
}

//修改oidc
export function oidcUpdate(data : any): any {
  return request({
    url: `/apps/oidc/update`,
    method: 'put',
    data: data
  })
}

//新增SAML
export function samlCreate(data : any): any {
  return request({
    url: `/apps/saml/create`,
    method: 'post',
    data: data
  })
}

//修改SAML
export function samlUpdate(data : any): any {
  return request({
    url: `/apps/saml/update`,
    method: 'put',
    data: data
  })
}

//新增jwt
export function jwtCreate(data : any): any {
  return request({
    url: `/apps/jwt/create`,
    method: 'post',
    data: data
  })
}

//修改jwt
export function jwtUpdate(data : any): any {
  return request({
    url: `/apps/jwt/update`,
    method: 'put',
    data: data
  })
}

//新增cas
export function casCreate(data : any): any {
  return request({
    url: `/apps/cas/create`,
    method: 'post',
    data: data
  })
}

//修改cas
export function casUpdate(data : any): any {
  return request({
    url: `/apps/cas/update`,
    method: 'put',
    data: data
  })
}

//ID查询IDP扩展信息
export function getAppdetails(id : any): any {
  return request({
    url: `/apps/appdetails/${id}`,
    method: 'get'
  })
}

//启用
export function active(ids : any): any {
  return request({
    url: `/apps/active/${ids}`,
    method: 'put'
  })
}

//禁用
export function disable(ids : any): any {
  return request({
    url: `/apps/disable/${ids}`,
    method: 'put'
  })
}

//删除
export function deleteBatch(ids : any): any {
  return request({
    url: `/apps/delete/${ids}`,
    method: 'delete'
  })
}

//文件解析元数据
export function fileTransform(data : any): any {
  return request({
    url: `/idp/saml20/metadata/fileTransform`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data',
    }
  })
}




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
