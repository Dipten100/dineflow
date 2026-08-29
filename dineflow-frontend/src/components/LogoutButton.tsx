'use client'

import { signOut } from 'next-auth/react'
import { Button } from '@mui/material'

export default function LogoutButton() {
  const handleLogout = async () => {
    try {
      await signOut({ callbackUrl: '/login' })
    } catch (error) {
      console.error('Logout error:', error)
      // Fallback redirect if signOut fails
      window.location.href = '/login'
    }
  }

  return (
    <Button
      fullWidth
      variant='contained'
      color='error'
      size='small'
      endIcon={<i className='ri-logout-box-r-line' />}
      sx={{ '& .MuiButton-endIcon': { marginInlineStart: 1.5 } }}
      onClick={handleLogout}
    >
      Logout
    </Button>
  )
}
