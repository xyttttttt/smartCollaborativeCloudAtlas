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
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            {{ loginUserStore.loginUser.userName ?? '无名' }}
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
import { reactive, ref, watch, VueElement, h } from 'vue'
import { HomeOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue'
import type { MenuProps, ItemType } from 'ant-design-vue'
import { useRouter } from 'vue-router'
const openKeys = ref<string[]>(['sub1'])
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()
const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/home',
    icon: () => h(AppstoreOutlined),
    label: '关于',
    title: '关于',
  },
  {
    key: '/sub1',
    icon: () => h(SettingOutlined),
    label: 'Navigation Three - Submenu',
    title: 'Navigation Three - Submenu',
  },
])

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
