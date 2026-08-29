import { getServerSession } from 'next-auth'
import { redirect } from 'next/navigation'

import { authOptions } from '@/lib/auth'

export async function requireAuth() {
  const session = await getServerSession(authOptions)

  if (!session) {
    redirect('/login')
  }

  return session
}

export async function getCurrentUser() {
  const session = await getServerSession(authOptions)

  return session?.user
}
