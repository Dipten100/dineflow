'use client'

import { useRouter } from 'next/navigation'

/**
 * Stores the current path for redirect after login
 */
export function storeRedirectPath() {
  if (typeof window !== 'undefined') {
    const currentPath = window.location.pathname + window.location.search
    localStorage.setItem('redirectAfterLogin', currentPath)
  }
}

/**
 * Gets the stored redirect path and clears it
 * @returns The stored path or default path
 */
export function getAndClearRedirectPath(defaultPath = '/'): string {
  if (typeof window === 'undefined') return defaultPath
  
  const redirectPath = localStorage.getItem('redirectAfterLogin')
  localStorage.removeItem('redirectAfterLogin')
  
  return redirectPath || defaultPath
}

/**
 * Redirects to the stored path after login
 */
export function useRedirectAfterLogin() {
  const router = useRouter()

  const redirect = (defaultPath = '/') => {
    const targetPath = getAndClearRedirectPath(defaultPath)
    router.push(targetPath)
  }

  return { redirect }
}

/**
 * Clears the redirect path (useful for manual sign out)
 */
export function clearRedirectPath() {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('redirectAfterLogin')
    // Also clear any tokens to ensure clean state
    localStorage.removeItem('accessToken')
    sessionStorage.removeItem('accessToken')
  }
}
