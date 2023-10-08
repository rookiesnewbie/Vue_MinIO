import request from '@/utils/request'

const baseUrl = '/file'

// 文件列表分页查询
export function getFileList(listQuery) {
  return request({
    url: baseUrl,
    method: 'get',
    params: listQuery
  })
}

// 创建文件夹
export function createFolder(data) {
  return request({
    url: baseUrl + '/createFolder',
    method: 'post',
    data
  })
}

// 重命名文件
export function renameFile(data) {
  return request({
    url: baseUrl + '/rename',
    method: 'put',
    data
  })
}

// 下载文件
export function downloadFile(id) {
  return request({
    url: baseUrl + '/download/' + id,
    method: 'get',
    responseType: 'blob'
  })
}

// 上传文件
export function uploadFiles(data) {
  return request({
    url: baseUrl + '/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

// 删除文件 —— 将文件移入回收站
export function removeFileToRecycleBin(fileId, operatorId) {
  return request({
    url: baseUrl + '?id=' + fileId + (operatorId === undefined ? '' : '&operatorId=' + operatorId),
    method: 'delete'
  })
}

// 获取共享文件夹列表
export function listAllShareFolder() {
  return request({
    url: baseUrl + '/listAllShareFolder',
    method: 'get'
  })
}

// 选择个人文件共享至指定共享文件夹中
export function shareFileTo(data) {
  return request({
    url: baseUrl + '/shareFileTo',
    method: 'post',
    data
  })
}

// 预览文件
export function previewFile(fileId) {
  return request({
    url: baseUrl + '/previewFile/' + fileId,
    method: 'get',
  })
}
