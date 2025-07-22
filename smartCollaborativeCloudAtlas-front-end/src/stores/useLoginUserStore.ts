import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

/**
 * 存储登录用户信息的store
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<any>({
    userName: '未登录',
    id: '',
  })

  // todo 远程获取用户登录信息
  async function fetchLoginUser() {
    setTimeout(() => {
      loginUser.value.userName = '反物质嘴炮'
      loginUser.value.id = 123
    }, 3000)
  }
  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
