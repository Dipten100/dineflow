import { NavItem } from "./navConfig"

type AuthUser = {
  permissions: string[]
  roles: string[]
  superAdmin: boolean
}

export const canAccessNavItem = (
  item: NavItem,
  user: AuthUser
): boolean => {
  // Super admin can access everything
  if (user.superAdmin) {
    return true
  }

  // Check single permission
  if (item.permission) {
    return user.permissions.includes(item.permission)
  }

  // Check multiple permissions
  if (item.permissions?.length) {
    return item.permissions.some(permission =>
      user.permissions.includes(permission)
    )
  }

  // Check role
  if (item.roles?.length) {
    return item.roles.some(role =>
      user.roles.includes(role)
    )
  }

  // Public menu item
  return true
}

export const filterNavItems = (
  items: NavItem[],
  user: AuthUser
): NavItem[] => {
  return items
    .map(item => {
      // Filter children first
      if (item.children?.length) {
        const children = filterNavItems(item.children, user)

        // Parent is visible if it has accessible children
        // OR the parent itself is accessible
        if (children.length > 0) {
          return {
            ...item,
            children
          }
        }

        // No children and parent has no permission
        if (!canAccessNavItem(item, user)) {
          return null
        }

        return {
          ...item,
          children: undefined
        }
      }

      // Normal menu item
      return canAccessNavItem(item, user) ? item : null
    })
    .filter(Boolean) as NavItem[]
}
