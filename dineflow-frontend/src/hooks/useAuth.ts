'use client'

import { useSession } from 'next-auth/react'
import { useRouter } from 'next/navigation'

export function useAuth() {
  const { data: session, status } = useSession()
  const router = useRouter()

  const user = session?.user
  const isAuthenticated = status === 'authenticated'
  const isLoading = status === 'loading'

  const hasPermission = (permission: string) => {
    return user?.permissions?.includes(permission) || false
  }

  const hasRole = (role: string) => {
    return user?.roles?.includes(role) || false
  }

  const isSuperAdmin = user?.superAdmin || false

  const logout = async () => {
    await router.push('/login')
  }

  return {
    user,
    isAuthenticated,
    isLoading,
    hasPermission,
    hasRole,
    isSuperAdmin,
    logout,
  }
}
