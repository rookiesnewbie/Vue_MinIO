import request from '@/utils/request'

/**
 * 回收站
 */

const baseUrl = '/recycleFile'

// 文件列表分页查询
export function getRecycleFileList(listQuery) {
  return request({
    url: baseUrl,
    method: 'get',
    params: listQuery
  })
}

// 彻底删除 文件/文件夹
export function deleteRecycleBinFile(id) {
  return request({
    url: baseUrl + '/' + id,
    method: 'delete'
  })
}

// 恢复文件（夹）
export function recoveryFile(id) {
  return request({
    url: baseUrl + '/' + id,
    method: 'put'
  })
}

// 清空回收站
export function clearAllRecycleBin() {
  return request({
    url: baseUrl + '/'  + 'deleteAll',
    method: 'delete'
  })
}
