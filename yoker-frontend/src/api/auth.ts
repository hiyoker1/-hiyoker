import request from '@/utils/request'

export interface LoginDTO {
  username: string
  password: string
}

export interface TokenVO {
  token: string
  username: string
}

// 登录
export function login(data: LoginDTO) {
  return request.post<any, { data: TokenVO }>('/auth/login', data)
}

// 登出
export function logout() {
  return request.delete('/auth/logout')
}
