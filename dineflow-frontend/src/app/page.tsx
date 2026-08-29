import { redirect } from 'next/navigation'

export default function RootPage() {
  // Redirect to dashboard by default
  // The middleware will handle authentication and redirect to login if not authenticated
  redirect('/dashboard')
}
