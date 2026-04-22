import request from '../utils/request'

export function getCostList() {
  return request({
    url: '/cost',
    method: 'get'
  })
}

export function saveCost(data) {
  return request({
    url: '/cost',
    method: 'post',
    data
  })
}

export function getDailySummary(date) {
  return request({
    url: '/cost/summary/daily',
    method: 'get',
    params: { date }
  })
}

export function getMonthlySummary(year, month) {
  return request({
    url: '/cost/summary/monthly',
    method: 'get',
    params: { year, month }
  })
}

export function getSummaryToDate(date) {
  return request({
    url: '/cost/summary/to-date',
    method: 'get',
    params: { date }
  })
}

export function getTotalCost() {
  return request({
    url: '/cost/total',
    method: 'get'
  })
}
