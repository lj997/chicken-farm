import request from '../utils/request'

export function getAiConfig() {
  return request({
    url: '/ai-config',
    method: 'get'
  })
}

export function saveAiConfig(data) {
  return request({
    url: '/ai-config',
    method: 'post',
    data
  })
}

export function checkAiConfigured() {
  return request({
    url: '/ai-config/check',
    method: 'get'
  })
}

export function getMarketPrice(breed) {
  return request({
    url: '/market-price',
    method: 'get',
    params: { breed }
  })
}
