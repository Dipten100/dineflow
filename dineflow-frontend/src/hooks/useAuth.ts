'use client'

import { useSession, signOut } from 'next-auth/react'
import { useRouter } from 'next/navigation'
import { clearRedirectPath } from '@/lib/redirect-utils'

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
    // Clear any stored redirect path
    clearRedirectPath()
    
    // Clear tokens from both storage locations
    if (typeof window !== 'undefined') {
      localStorage.removeItem('accessToken')
      sessionStorage.removeItem('accessToken')
    }
    
    // Sign out from NextAuth
    await signOut({ redirect: false })
    
    // Redirect to login
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
