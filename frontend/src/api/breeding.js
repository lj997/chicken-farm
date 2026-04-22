import request from '../utils/request'

export function getBreedingList() {
  return request({
    url: '/breeding',
    method: 'get'
  })
}

export function getLatestBreeding() {
  return request({
    url: '/breeding/latest',
    method: 'get'
  })
}

export function saveBreeding(data) {
  return request({
    url: '/breeding',
    method: 'post',
    data
  })
}
