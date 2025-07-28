<template>
  <div id="userManagerPage">
    <div class="search-header">
      <a-form layout="inline" :model="searchParams" @finish="doSearch">
        <a-form-item label="账号">
          <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">搜索</a-button>
        </a-form-item>
      </a-form>
    </div>
    <div>
      <a-table :columns="columns" :data-source="dataList">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userAvatar'">
            <a-image :src="record.avatar" :width="60" />
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            <div v-if="record.status === 1">
              <a-tag color="green">正常</a-tag>
            </div>
            <div v-else-if="record.status === 2">
              <a-tag color="green">已冻结</a-tag>
            </div>
            <div v-else>
              <a-tag color="blue">已注销</a-tag>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'userRole'">
            <div v-if="record.userRole === 'ADMIN'">
              <a-tag color="green">管理员</a-tag>
            </div>
            <div v-else>
              <a-tag color="blue">普通用户</a-tag>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'lastLoginTime'">
            {{ dayjs(record.lastLoginTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.dataIndex === 'createTime'">
            {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button v-if="record.status === 1" danger @click="doFreeze(record.id)">冻结</a-button>
            <a-button v-else-if="record.status === 2" type="primary" @click="doUnfreeze(record.id)"
              >解冻</a-button
            >
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>
<script setup lang="ts">
// 数据
import { computed, onMounted, reactive, ref } from 'vue'
import { freeze, unfreeze, userVoList } from '@/api/adminUserController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'

const dataList = ref([])
const total = ref(0)
// 获取数据
const doSearch = () => {
  // 重置页码
  searchParams.currentPage = 1
  fetchData()
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.currentPage ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total) => `共 ${total} 条`,
  }
})

// 表格变化处理
const doTableChange = (page: any) => {
  searchParams.currentPage = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索条件
const searchParams = reactive<API.UserQueryParams>({
  currentPage: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await userVoList({
    ...searchParams,
  })
  if (res.data.data) {
    dataList.value = res.data.data ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

// 删除数据
const doFreeze = async (id: string) => {
  if (!id) {
    return
  }
  const res = await freeze({ id })
  if (res.data.code === 'SUCCESS') {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 删除数据
const doUnfreeze = async (id: string) => {
  if (!id) {
    return
  }
  const res = await unfreeze({ id })
  if (res.data.code === 'SUCCESS') {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '状态',
    dataIndex: 'status',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '邀请码',
    dataIndex: 'inviteCode',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '上次登录时间',
    dataIndex: 'lastLoginTime',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]
</script>

<style scoped></style>
