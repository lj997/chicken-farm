import { createRouter, createWebHistory } from 'vue-router'
import EntryRecord from '../views/EntryRecord.vue'
import BreedingStatus from '../views/BreedingStatus.vue'
import CostRecord from '../views/CostRecord.vue'
import ProfitPage from '../views/ProfitPage.vue'

const routes = [
  {
    path: '/',
    redirect: '/entry'
  },
  {
    path: '/entry',
    name: 'Entry',
    component: EntryRecord
  },
  {
    path: '/breeding',
    name: 'Breeding',
    component: BreedingStatus
  },
  {
    path: '/cost',
    name: 'Cost',
    component: CostRecord
  },
  {
    path: '/profit',
    name: 'Profit',
    component: ProfitPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
