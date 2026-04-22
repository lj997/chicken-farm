import request from '../utils/request'

export function getEntryRecord() {
  return request({
    url: '/entry',
    method: 'get'
  })
}

export function saveEntryRecord(data) {
  return request({
    url: '/entry',
    method: 'post',
    data
  })
}
