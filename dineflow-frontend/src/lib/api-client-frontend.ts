'use client'

import { signOut } from 'next-auth/react'
import { storeRedirectPath } from '@/lib/redirect-utils'

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

// Client-side API client that works with NextAuth session
export class ApiClient {
  private getAccessToken(): string | null {
    if (typeof window === 'undefined') return null

    const localToken = localStorage.getItem('accessToken')
    const sessionToken = sessionStorage.getItem('accessToken')

    return localToken || sessionToken
  }

  private async getHeaders(customHeaders?: HeadersInit): Promise<Record<string, string>> {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
    }

    if (customHeaders) {
      if (customHeaders instanceof Headers) {
        customHeaders.forEach((value, key) => {
          if (value !== undefined) {
            headers[key] = value
          }
        })
      } else if (Array.isArray(customHeaders)) {
        customHeaders.forEach(([key, value]) => {
          if (value !== undefined) {
            headers[key] = value
          }
        })
      } else {
        Object.assign(headers, customHeaders)
      }
    }

    const token = this.getAccessToken()
    if (token && !headers['Authorization']) {
      headers['Authorization'] = `Bearer ${token}`
    }

    return headers
  }

  private handle401Error() {
    if (typeof window !== 'undefined') {
      storeRedirectPath()
      localStorage.removeItem('accessToken')
      sessionStorage.removeItem('accessToken')

      signOut({ callbackUrl: '/login', redirect: true }).catch(() => {
        window.location.href = '/login'
      })
    }
  }

  async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const headers = await this.getHeaders(options.headers as HeadersInit)

    const response = await fetch(`${API_URL}${endpoint}`, {
      ...options,
      headers,
    })

    if (response.status === 401) {
      this.handle401Error()
      throw new Error('Unauthorized - redirecting to login')
    }

    if (!response.ok) {
      throw new Error(`API request failed: ${response.statusText}`)
    }

    return response.json()
  }

  async get<T>(endpoint: string, options?: RequestInit): Promise<T> {
    return this.request<T>(endpoint, { method: 'GET', ...options })
  }

  async post<T>(endpoint: string, data: any, options?: RequestInit): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
      ...options,
    })
  }

  async put<T>(endpoint: string, data: any, options?: RequestInit): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
      ...options,
    })
  }

  async delete<T>(endpoint: string, options?: RequestInit): Promise<T> {
    return this.request<T>(endpoint, { method: 'DELETE', ...options })
  }
}

export const apiClient = new ApiClient()

