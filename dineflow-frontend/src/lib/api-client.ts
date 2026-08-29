import { getServerSession } from 'next-auth'

import { authOptions } from '@/lib/auth'

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

async function getAccessToken() {
  const session = await getServerSession(authOptions)
  return session?.user?.accessToken
}

export async function apiRequest(
  endpoint: string,
  options: RequestInit = {}
) {
  const token = await getAccessToken()

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const response = await fetch(`${API_URL}${endpoint}`, {
    ...options,
    headers,
  })

  if (!response.ok) {
    throw new Error(`API request failed: ${response.statusText}`)
  }

  return response.json()
}

export async function apiGet(endpoint: string) {
  return apiRequest(endpoint, { method: 'GET' })
}

export async function apiPost(endpoint: string, data: any) {
  return apiRequest(endpoint, {
    method: 'POST',
    body: JSON.stringify(data),
  })
}

export async function apiPut(endpoint: string, data: any) {
  return apiRequest(endpoint, {
    method: 'PUT',
    body: JSON.stringify(data),
  })
}

export async function apiDelete(endpoint: string) {
  return apiRequest(endpoint, { method: 'DELETE' })
}
