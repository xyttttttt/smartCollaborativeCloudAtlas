<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <router-link to="/">
          <div class="title-bar">
            <img class="logo" src="../assets/logo.png" alt="logo" />
            <div class="title">智能云图库</div>
          </div>
        </router-link>
      </a-col>
      <a-col flex="auto">
        <a-menu
          id="dddddd"
          v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        ></a-menu>
      </a-col>
      <a-col flex="140px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.userId">
            <a-dropdown>
              <ASpace>
                <a-avatar :src="loginUserStore.loginUser.avatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </ASpace>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>

          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, h, computed } from 'vue'
import {
  HomeOutlined,
  AppstoreOutlined,
  SettingOutlined,
  LogoutOutlined,
} from '@ant-design/icons-vue'
import type { MenuProps, ItemType } from 'ant-design-vue'
import { useRouter } from 'vue-router'
const openKeys = ref<string[]>([' '])
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'
import { logout } from '@/api/authController.ts'
import ACCESS_ENUM from '@/access/asseccEnum.ts'

const loginUserStore = useLoginUserStore()
// 菜单列表
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    icon: () => h(AppstoreOutlined),
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/add_picture',
    label: '创建图片',
    title: '创建图片',
  },
  {
    key: '/admin/pictureManage',
    label: '图片管理',
    title: '图片管理',
  },
  {
    key: 'others',
    label: h('a', { href: 'https://www.codefather.cn', target: '_blank' }, '编程导航'),
    title: '编程导航',
  },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    if (menu.key.toString().startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      console.log(loginUser)
      if (
        !loginUser ||
        (loginUser.userRole !== ACCESS_ENUM.ADMIN && loginUser.userRole !== ACCESS_ENUM.SUPER_ADMIN)
      ) {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const items = computed<MenuProps['items']>(() => filterMenus(originItems))

const router = useRouter()
const doMenuClick: MenuProps['onClick'] = ({ key }) => {
  router.push({
    path: key,
  })
}

const selectedKeys = ref<string[]>(['/'])
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

watch(openKeys, (val) => {
  console.log('openKeys', val)
})

// 用户注销
const doLogout = async () => {
  const res = await logout()
  if (res.data.code === 'SUCCESS') {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    localStorage.removeItem('satoken')
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
.title-bar .logo {
  width: 40px;
  height: 40px;
}
#globalHeader .title-bar {
  display: flex;
  align-items: center;
}
.title-bar .title {
  color: black;
  font-size: 18px;
}
</style>
