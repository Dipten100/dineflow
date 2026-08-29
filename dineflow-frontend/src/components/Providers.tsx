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

type Props = ChildrenType & {
  direction: Direction
  mode: Mode
  settingsCookie: Settings
}

const Providers = (props: Props) => {
  // Props
  const { children, direction, mode, settingsCookie } = props

  return (
    <SessionProvider>
      <VerticalNavProvider>
        <SettingsProvider settingsCookie={settingsCookie} mode={mode}>
          <ThemeProvider direction={direction}>
            {children}
          </ThemeProvider>
        </SettingsProvider>
      </VerticalNavProvider>
    </SessionProvider>
  )
}

export default Providers
