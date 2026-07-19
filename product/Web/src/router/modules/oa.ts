import { Layout } from '@/utils/routerHelper'
import type { AppRouteRecordRaw } from '@/router/types'

const { t } = useI18n()

const oaRoutes: AppRouteRecordRaw[] = [
  {
    path: '/oa',
    component: Layout,
    name: 'Oa',
    meta: {
      hidden: true
    },
    children: [
      {
        path: 'leave',
        component: () => import('@/views/oa/leave/index.vue'),
        name: 'OaLeave',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaLeave'),
          activeMenu: '/oa/leave'
        }
      },
      {
        path: 'businessTrip',
        component: () => import('@/views/oa/businessTrip/index.vue'),
        name: 'OaBusinessTrip',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaBusinessTrip'),
          activeMenu: '/oa/businessTrip'
        }
      },
      {
        path: 'loan',
        component: () => import('@/views/oa/loan/index.vue'),
        name: 'OaLoan',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaLoan'),
          activeMenu: '/oa/loan'
        }
      },
      {
        path: 'visit',
        component: () => import('@/views/oa/visit/index.vue'),
        name: 'OaVisit',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaVisit'),
          activeMenu: '/oa/visit'
        }
      },
      {
        path: 'request',
        component: () => import('@/views/oa/request/index.vue'),
        name: 'OaRequest',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaRequest'),
          activeMenu: '/oa/request'
        }
      },
      {
        path: 'schedule',
        component: () => import('@/views/oa/schedule/index.vue'),
        name: 'OaSchedule',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaSchedule'),
          activeMenu: '/oa/schedule'
        }
      },
      {
        path: 'task',
        component: () => import('@/views/oa/task/index.vue'),
        name: 'OaTask',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaTask'),
          activeMenu: '/oa/task'
        }
      },
      {
        path: 'message',
        component: () => import('@/views/oa/message/index.vue'),
        name: 'OaMessage',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaMessage'),
          activeMenu: '/oa/message'
        }
      },
      {
        path: 'workReport',
        component: () => import('@/views/oa/workReport/index.vue'),
        name: 'OaWorkReport',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaWorkReport'),
          activeMenu: '/oa/workReport'
        }
      },
      {
        path: 'documentDir',
        component: () => import('@/views/oa/documentDir/index.vue'),
        name: 'OaDocumentDir',
        meta: {
          noCache: true,
          hidden: true,
          canTo: true,
          title: t('router.oaDocumentDir'),
          activeMenu: '/oa/documentDir'
        }
      }
    ]
  }
]

export default oaRoutes
