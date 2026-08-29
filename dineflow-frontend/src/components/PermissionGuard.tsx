'use client'

import { useAuth } from '@/hooks/useAuth'
import { ReactNode } from 'react'

type PermissionGuardProps = {
  children: ReactNode
  permission?: string
  role?: string
  fallback?: ReactNode
  requireSuperAdmin?: boolean
}

export default function PermissionGuard({
  children,
  permission,
  role,
  fallback = null,
  requireSuperAdmin = false,
}: PermissionGuardProps) {
  const { hasPermission, hasRole, isSuperAdmin, isAuthenticated, isLoading } = useAuth()

  if (isLoading) {
    return fallback
  }

  if (!isAuthenticated) {
    return fallback
  }

  if (requireSuperAdmin && !isSuperAdmin) {
    return fallback
  }

  if (permission && !hasPermission(permission)) {
    return fallback
  }

  if (role && !hasRole(role)) {
    return fallback
  }

  return <>{children}</>
}
