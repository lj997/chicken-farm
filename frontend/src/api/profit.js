import request from '../utils/request'

export function calculateProfit(data) {
  return request({
    url: '/profit/calculate',
    method: 'post',
    data
  })
}
