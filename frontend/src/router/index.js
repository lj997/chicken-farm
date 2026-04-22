import { createRouter, createWebHistory } from 'vue-router'
import EntryRecord from '../views/EntryRecord.vue'
import BreedingStatus from '../views/BreedingStatus.vue'
import CostRecord from '../views/CostRecord.vue'
import ProfitPage from '../views/ProfitPage.vue'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/entry'
  },
  {
    path: '/entry',
    name: 'Entry',
    component: EntryRecord,
    meta: { requiresAuth: true }
  },
  {
    path: '/breeding',
    name: 'Breeding',
    component: BreedingStatus,
    meta: { requiresAuth: true }
  },
  {
    path: '/cost',
    name: 'Cost',
    component: CostRecord,
    meta: { requiresAuth: true }
  },
  {
    path: '/profit',
    name: 'Profit',
    component: ProfitPage,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const isLoggedIn = () => {
  return !!sessionStorage.getItem('token')
}

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth !== false) {
    if (isLoggedIn()) {
      next()
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  } else {
    if (to.path === '/login' && isLoggedIn()) {
      next('/entry')
    } else {
      next()
    }
  }
})

export default router
