'use client'

import { useEffect } from 'react'
import { useSession } from 'next-auth/react'

// Component to sync access token from NextAuth session to localStorage
export function AuthTokenSync() {
  const { data: session, status } = useSession()

  useEffect(() => {
    if (typeof window !== 'undefined') {
      console.log('AuthTokenSync - Session status:', status)
      console.log('AuthTokenSync - Has accessToken:', !!session?.user?.accessToken)

      if (session?.user?.accessToken) {
        console.log('AuthTokenSync - Storing token in localStorage')
        localStorage.setItem('accessToken', session.user.accessToken)
      } else if (status === 'unauthenticated') {
        console.log('AuthTokenSync - Clearing tokens from both localStorage and sessionStorage')
        localStorage.removeItem('accessToken')
        sessionStorage.removeItem('accessToken')
        localStorage.removeItem('redirectAfterLogin')
      }
    }
  }, [session, status])

  return null
}
