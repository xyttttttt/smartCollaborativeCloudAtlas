import axios from 'axios'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const myAxios = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 30000,
  withCredentials: true,
})

export default myAxios

myAxios.interceptors.request.use(
  function (config) {
    // 在每次请求前动态获取并设置 token
    const token = localStorage.getItem('satoken')
    if (token) {
      config.headers['satoken'] = token
    }
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  },
)

// Add a response interceptor
myAxios.interceptors.response.use(
  function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    const { data } = response
    console.log(data)
    if (data.code === 40100) {
      if (
        !response.request.responseURL.includes('user/get/login') &&
        !window.location.pathname.includes('/user/login')
      ) {
        message.error('请先登录')
        window.location.href = `/user/login?redirect=${window.location.href}`
      }
      return
    }
    if (data.code === 500) {
      return
    }
    return response
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error)
  },
)
