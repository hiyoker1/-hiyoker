import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi } from '@/api/auth'
import { getToken, setToken, removeToken, getUsername, setUsername } from '@/utils/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(getToken() || '')
  const username = ref<string>(getUsername() || '')

  // 登录
  async function login(loginForm: { username: string; password: string }) {
    const res = await loginApi(loginForm)
    token.value = res.data.token
    username.value = res.data.username
    setToken(res.data.token)
    setUsername(res.data.username)
    router.push('/')  //跳转
  }

  // 登出
  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      username.value = ''
      removeToken()
      router.push('/login')
    }
  }

  return { token, username, login, logout }
})
