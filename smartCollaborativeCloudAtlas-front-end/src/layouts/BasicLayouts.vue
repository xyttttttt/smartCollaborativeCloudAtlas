<template>
  <div id="basicLayouts">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header"><global-header /></a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">智能云图系统</a-layout-footer>
    </a-layout>
  </div>
</template>
<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
</script>

<style scoped>
#basicLayouts .footer {
  background: #efefef;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}

#basicLayouts .content {
  padding: 20px;
  background: linear-gradient(to right, #fefefe, #fff);
  margin-bottom: 28px;
}

#basicLayouts .header {
  background: white;
  color: unset;
  margin-bottom: 16px;
  padding-inline: 20px;
}
</style>
