import request from '@/utils/Request'

//上传头像
export function upload(data : any): any {
  return request({
    url: '/file/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data',
    }
  })
}
