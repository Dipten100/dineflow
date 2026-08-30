'use client'

import { useEffect } from 'react'
import { useSession } from 'next-auth/react'

// Hook to sync access token from NextAuth session to localStorage
export function useAuthSession() {
  const { data: session, status } = useSession()

  useEffect(() => {
    if (typeof window !== 'undefined') {
      if (session?.user?.accessToken) {
        localStorage.setItem('accessToken', session.user.accessToken)
      } else if (status === 'unauthenticated') {
        localStorage.removeItem('accessToken')
      }
    }
  }, [session, status])

  return { session, status }
}
