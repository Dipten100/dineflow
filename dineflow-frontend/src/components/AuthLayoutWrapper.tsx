'use client'

import { ReactNode } from 'react'
import AuthProvider from '@components/AuthProvider'

type Props = {
  children: ReactNode
}

export default function AuthLayoutWrapper({ children }: Props) {
  return <AuthProvider>{children}</AuthProvider>
}
