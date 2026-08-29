// Component Imports
import Providers from '@components/Providers'
import BlankLayout from '@layouts/BlankLayout'
import NotFound from '@views/NotFound'

// Util Imports
import { getMode, getSettingsFromCookie } from '@core/utils/serverHelpers'

const NotFoundPage = () => {
  // Vars
  const direction = 'ltr'
  const mode = getMode()
  const settingsCookie = getSettingsFromCookie()

  return (
    <Providers direction={direction} mode={mode} settingsCookie={settingsCookie}>
      <BlankLayout>
        <NotFound mode={mode} />
      </BlankLayout>
    </Providers>
  )
}

export default NotFoundPage
