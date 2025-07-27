import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { getUserInfo } from '@/api/userController.ts'
import UserInfo = API.UserInfo

/**
 * 存储登录用户信息的store
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<UserInfo>({
    userName: '未登录',
    userId: '',
  })

  // todo 远程获取用户登录信息
  async function fetchLoginUser() {
    const res = await getUserInfo()
    if (res.data.code === 'SUCCESS' && res.data.data) {
      loginUser.value = res.data.data
    }
  }

  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
