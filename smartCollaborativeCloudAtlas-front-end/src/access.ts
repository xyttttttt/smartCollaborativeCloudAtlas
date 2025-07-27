import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { message } from 'ant-design-vue'
import router from '@/router'
import ACCESS_ENUM from '@/access/asseccEnum.ts'
import checkAccess from '@/access/checkAccess.ts'

// 是否为首次获取登录用户
let firstFetchLoginUser = true

/**
 * 全局权限校验
 */
router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  // 确保页面刷新，首次加载时，能够等后端返回用户信息后再校验权限
  if (firstFetchLoginUser) {
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
    firstFetchLoginUser = false
    console.log('登录用户', loginUser.userRole)
  }

  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    // 如果没登陆，跳转到登录页面
    if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.NOT_LOGIN) {
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
    // 如果已经登陆了，但是权限不足，那么跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next('/noAuth')
      return
    }
  }

  const toUrl = to.fullPath
  if (toUrl.startsWith('/admin')) {
    if (!checkAccess(loginUser, ACCESS_ENUM.ADMIN)) {
      message.error('没有权限')
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
  }

  next()
})
