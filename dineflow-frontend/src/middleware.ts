import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function middleware(req: NextRequest) {
  const token = req.cookies.get('next-auth.session-token') || 
                req.cookies.get('__Secure-next-auth.session-token')
  
  const isLoggedIn = !!token
  const isAuthPage = req.nextUrl.pathname.startsWith('/login') || 
                     req.nextUrl.pathname.startsWith('/register') ||
                     req.nextUrl.pathname.startsWith('/forgot-password')
  
  // Protected routes are those that require authentication
  // Based on your folder structure, these are routes under (Protected) group
  const isProtectedRoute = req.nextUrl.pathname.startsWith('/dashboard') ||
                           req.nextUrl.pathname.startsWith('/role') ||
                           req.nextUrl.pathname.startsWith('/account-settings')

  // Redirect to login if trying to access protected route while not logged in
  if (isProtectedRoute && !isLoggedIn) {
    return NextResponse.redirect(new URL('/login', req.url))
  }

  // Redirect to dashboard if trying to access auth page while logged in
  if (isAuthPage && isLoggedIn) {
    return NextResponse.redirect(new URL('/dashboard', req.url))
  }

  return NextResponse.next()
}

export const config = {
  matcher: [
    /*
     * Match all request paths except:
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico (favicon file)
     * - public folder
     */
    '/((?!_next/static|_next/image|favicon.ico|.*\\.(?:svg|png|jpg|jpeg|gif|webp)$).*)',
  ],
}
