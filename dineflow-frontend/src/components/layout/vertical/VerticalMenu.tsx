import { useTheme } from '@mui/material/styles'
import PerfectScrollbar from 'react-perfect-scrollbar'

import type { VerticalMenuContextProps } from '@menu/components/vertical-menu/Menu'

import {
  Menu,
  SubMenu,
  MenuItem
} from '@menu/vertical-menu'

import useVerticalNav from '@menu/hooks/useVerticalNav'

import StyledVerticalNavExpandIcon from '@menu/styles/vertical/StyledVerticalNavExpandIcon'

import menuItemStyles from '@core/styles/vertical/menuItemStyles'
import menuSectionStyles from '@core/styles/vertical/menuSectionStyles'

import { navItems, type NavItem } from '@/configs/navConfig'
import { filterNavItems } from '@/configs/navPermission'
import { useAuth } from '@/hooks/useAuth'

type RenderExpandIconProps = {
  open?: boolean
  transitionDuration?: VerticalMenuContextProps['transitionDuration']
}

const RenderExpandIcon = ({
  open,
  transitionDuration
}: RenderExpandIconProps) => (
  <StyledVerticalNavExpandIcon
    open={open}
    transitionDuration={transitionDuration}
  >
    <i className='ri-arrow-right-s-line' />
  </StyledVerticalNavExpandIcon>
)

const renderNavItems = (items: NavItem[]) => {
  return items.map(item => {
    // Parent menu
    if (item.children?.length) {
      return (
        <SubMenu
          key={item.label}
          label={item.label}
          icon={
            item.icon ? (
              <i className={item.icon} />
            ) : undefined
          }
        >
          {renderNavItems(item.children)}
        </SubMenu>
      )
    }

    // Normal menu item
    return (
      <MenuItem
        key={item.label}
        href={item.href}
        icon={
          item.icon ? (
            <i className={item.icon} />
          ) : undefined
        }
      >
        {item.label}
      </MenuItem>
    )
  })
}

const VerticalMenu = ({
  scrollMenu
}: {
  scrollMenu: (
    container: any,
    isPerfectScrollbar: boolean
  ) => void
}) => {
  const theme = useTheme()

  const {
    isBreakpointReached,
    transitionDuration
  } = useVerticalNav()

  const ScrollWrapper = isBreakpointReached
    ? 'div'
    : PerfectScrollbar

  // Get logged-in user
  const { user } = useAuth()

  const userPermissions = { permissions: user?.permissions ?? [], roles: user?.roles ?? [], superAdmin: user?.superAdmin ?? false }
  const authorizedNavItems = filterNavItems(navItems, userPermissions)

  return (
    <ScrollWrapper
      {...(isBreakpointReached
        ? {
            className:
              'bs-full overflow-y-auto overflow-x-hidden',
            onScroll: (container: any) =>
              scrollMenu(container, false)
          }
        : {
            options: {
              wheelPropagation: false,
              suppressScrollX: true
            },
            onScrollY: (container: any) =>
              scrollMenu(container, true)
          })}
    >
      <Menu
        menuItemStyles={menuItemStyles(theme)}
        renderExpandIcon={({ open }) => (
          <RenderExpandIcon
            open={open}
            transitionDuration={transitionDuration}
          />
        )}
        renderExpandedMenuItemIcon={{
          icon: <i className='ri-circle-line' />
        }}
        menuSectionStyles={menuSectionStyles(theme)}
      >
        {renderNavItems(authorizedNavItems)}
      </Menu>
    </ScrollWrapper>
  )
}

export default VerticalMenu
