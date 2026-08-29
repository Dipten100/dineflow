import 'next-auth'

declare module 'next-auth' {
  interface User {
    id: string
    email: string
    name: string
    accessToken: string
    permissions: string[]
    roles: string[]
    superAdmin: boolean
  }

  interface Session {
    user: {
      id: string
      email: string
      name: string
      accessToken: string
      permissions: string[]
      roles: string[]
      superAdmin: boolean
    }
  }
}

declare module 'next-auth/jwt' {
  interface JWT {
    id: string
    accessToken: string
    permissions: string[]
    roles: string[]
    superAdmin: boolean
  }
}
