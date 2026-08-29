import type { ReactNode } from 'react'

export type NavItem = {
  label: string
  href?: string
  icon?: string
  permission?: string
  permissions?: string[]
  roles?: string[]
  children?: NavItem[]
}

export const navItems: NavItem[] = [
  {
    label: 'Dashboard',
    href: '/dashboard',
    icon: 'ri-home-smile-line'
  },

  {
    label: 'Restaurant',
    icon: 'ri-restaurant-line',
    children: [
      {
        label: 'Restaurants',
        href: '/restaurant',
        icon: 'ri-store-2-line',
        permission: 'RESTAURANT_VIEW'
      },
      {
        label: 'Create Restaurant',
        href: '/restaurant/create',
        icon: 'ri-add-line',
        permission: 'RESTAURANT_CREATE'
      }
    ]
  },

  {
    label: 'Outlets',
    icon: 'ri-store-line',
    children: [
      {
        label: 'View Outlets',
        href: '/outlet',
        icon: 'ri-list-check',
        permission: 'OUTLET_VIEW'
      },
      {
        label: 'Create Outlet',
        href: '/outlet/create',
        icon: 'ri-add-line',
        permission: 'OUTLET_CREATE'
      }
    ]
  },

  {
    label: 'Menu',
    icon: 'ri-restaurant-2-line',
    children: [
      {
        label: 'View Menu',
        href: '/menu',
        icon: 'ri-list-unordered',
        permission: 'MENU_VIEW'
      },
      {
        label: 'Create Menu',
        href: '/menu/create',
        icon: 'ri-add-line',
        permission: 'MENU_CREATE'
      }
    ]
  },

  {
    label: 'Orders',
    icon: 'ri-shopping-bag-line',
    children: [
      {
        label: 'View Orders',
        href: '/orders',
        icon: 'ri-list-check',
        permission: 'ORDER_VIEW'
      },
      {
        label: 'Create Order',
        href: '/orders/create',
        icon: 'ri-add-line',
        permission: 'ORDER_CREATE'
      }
    ]
  },

  {
    label: 'Users',
    icon: 'ri-user-line',
    children: [
      {
        label: 'View Users',
        href: '/users',
        icon: 'ri-user-search-line',
        permission: 'USER_VIEW'
      },
      {
        label: 'Create User',
        href: '/users/create',
        icon: 'ri-user-add-line',
        permission: 'USER_CREATE'
      }
    ]
  },

  {
    label: 'Roles',
    icon: 'ri-shield-user-line',
    children: [
      {
        label: 'View Roles',
        href: '/role',
        icon: 'ri-shield-line',
        permission: 'ROLE_VIEW'
      },
      {
        label: 'Create Role',
        href: '/role/create',
        icon: 'ri-add-line',
        permission: 'ROLE_CREATE'
      }
    ]
  },

  {
    label: 'Permissions',
    icon: 'ri-key-2-line',
    children: [
      {
        label: 'View Permissions',
        href: '/permission',
        icon: 'ri-key-line',
        permission: 'PERMISSION_VIEW'
      },
      {
        label: 'Create Permission',
        href: '/permission/create',
        icon: 'ri-add-line',
        permission: 'PERMISSION_CREATE'
      }
    ]
  },

  {
    label: 'Payments',
    icon: 'ri-bank-card-line',
    children: [
      {
        label: 'View Payments',
        href: '/payment',
        icon: 'ri-money-rupee-circle-line',
        permission: 'PAYMENT_VIEW'
      },
      {
        label: 'Create Payment',
        href: '/payment/create',
        icon: 'ri-add-line',
        permission: 'PAYMENT_CREATE'
      }
    ]
  },

  {
    label: 'Reports',
    href: '/reports',
    icon: 'ri-bar-chart-box-line',
    permission: 'REPORT_VIEW'
  }
]
