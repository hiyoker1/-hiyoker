<template>
  <div class="dashboard">
    <el-card>
      <template #header>欢迎使用 yokerAdmin</template>
      <p>当前用户：{{ userStore.username }}</p>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
</style>
