'use client'

import Login from '@views/Login'
import AuthProvider from '@components/AuthProvider'
import type { Mode } from '@core/types'

type Props = {
  mode: Mode
}

export default function LoginWithAuth({ mode }: Props) {
  return (
    <AuthProvider>
      <Login mode={mode} />
    </AuthProvider>
  )
}
