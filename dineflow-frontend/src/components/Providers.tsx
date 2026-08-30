'use client'

// Type Imports
import type { ChildrenType, Direction, Mode } from '@core/types'
import type { Settings } from '@core/contexts/settingsContext'

// Context Imports
import { VerticalNavProvider } from '@menu/contexts/verticalNavContext'
import { SettingsProvider } from '@core/contexts/settingsContext'
import ThemeProvider from '@components/theme'

// NextAuth Imports
import { SessionProvider } from 'next-auth/react'

// TanStack Query Imports
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

// Custom Components
import { AuthTokenSync } from '@/components/AuthTokenSync'

type Props = ChildrenType & {
  direction: Direction
  mode: Mode
  settingsCookie: Settings
}

const Providers = (props: Props) => {
  // Props
  const { children, direction, mode, settingsCookie } = props

  // TanStack Query Client
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 60 * 1000, // 1 minute
        retry: 1,
        refetchOnWindowFocus: false,
      },
    },
  })

  return (
    <SessionProvider>
      <QueryClientProvider client={queryClient}>
        <AuthTokenSync />
        <VerticalNavProvider>
          <SettingsProvider settingsCookie={settingsCookie} mode={mode}>
            <ThemeProvider direction={direction}>
              {children}
            </ThemeProvider>
          </SettingsProvider>
        </VerticalNavProvider>
      </QueryClientProvider>
    </SessionProvider>
  )
}

export default Providers
