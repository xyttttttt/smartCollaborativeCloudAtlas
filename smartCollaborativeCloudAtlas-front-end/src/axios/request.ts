import axios from 'axios'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const myAxios = axios.create({
  baseURL: 'http://localhost:8123',
  timeout: 30000,
  withCredentials: true,
  headers: { 'X-Custom-Header': 'foobar' },
})

export default myAxios

myAxios.interceptors.request.use(
  function (config) {
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
