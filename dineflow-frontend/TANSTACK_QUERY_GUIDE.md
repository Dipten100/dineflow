# TanStack Query Integration Guide

This project now uses TanStack Query for efficient API calls with automatic caching and deduplication. This prevents duplicate API calls for the same data.

## What's Been Added

1. **@tanstack/react-query** - Installed and configured
2. **Client-side API client** (`src/lib/api-client-frontend.ts`) - Handles API calls with authentication
3. **Custom hooks** (`src/hooks/useApi.ts`) - Reusable hooks for GET, POST, PUT, DELETE operations
4. **Auth token sync** (`src/components/AuthTokenSync.tsx`) - Syncs NextAuth tokens to localStorage for API calls
5. **Query provider** - Configured in `src/components/Providers.tsx`

## How It Works

- **Caching**: Identical API calls are cached and deduplicated automatically
- **Stale time**: Data is considered fresh for 1 minute by default
- **Auto-refresh**: Data can be automatically refetched when needed
- **Auth integration**: Access tokens from NextAuth are automatically included in API requests

## Usage Examples

### 1. GET Request (Fetch Data)

```tsx
'use client'

import { useApiGet } from '@/hooks/useApi'

function UsersList() {
  // This will cache the data and prevent duplicate calls
  const { data, isLoading, error } = useApiGet<User[]>(
    ['users'], // Query key - must be unique
    '/api/users' // API endpoint
  )

  if (isLoading) return <div>Loading...</div>
  if (error) return <div>Error: {error.message}</div>

  return (
    <ul>
      {data?.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  )
}
```

#### GET with Custom Headers

```tsx
const { data } = useApiGet<User[]>(
  ['users'],
  '/api/users',
  {
    headers: {
      'X-Custom-Header': 'custom-value',
      'Accept': 'application/vnd.api+json',
    }
  }
)
```

### 2. POST Request (Create Data)

```tsx
'use client'

import { useApiPost } from '@/hooks/useApi'

function CreateUserForm() {
  const createUser = useApiPost<User, CreateUserInput>(
    '/api/users'
  )

  const handleSubmit = async (userData: CreateUserInput) => {
    try {
      const result = await createUser.mutateAsync(userData)
      console.log('User created:', result)
      // Related queries are automatically invalidated
    } catch (error) {
      console.error('Failed to create user:', error)
    }
  }

  return (
    <form onSubmit={(e) => handleSubmit(userData)}>
      {/* form fields */}
      <button disabled={createUser.isPending}>
        {createUser.isPending ? 'Creating...' : 'Create User'}
      </button>
    </form>
  )
}
```

#### POST with Custom Headers

```tsx
const createUser = useApiPost<User, CreateUserInput>(
  '/api/users',
  {
    headers: {
      'X-Custom-Header': 'custom-value',
    }
  }
)
```

### 3. PUT Request (Update Data)

```tsx
'use client'

import { useApiPut } from '@/hooks/useApi'

function UpdateUserForm({ userId }: { userId: string }) {
  const updateUser = useApiPut<User, UpdateUserInput>(
    `/api/users/${userId}`
  )

  const handleSubmit = async (userData: UpdateUserInput) => {
    try {
      const result = await updateUser.mutateAsync(userData)
      console.log('User updated:', result)
    } catch (error) {
      console.error('Failed to update user:', error)
    }
  }

  return (
    <form onSubmit={(e) => handleSubmit(userData)}>
      {/* form fields */}
      <button disabled={updateUser.isPending}>
        {updateUser.isPending ? 'Updating...' : 'Update User'}
      </button>
    </form>
  )
}
```

#### PUT with Custom Headers

```tsx
const updateUser = useApiPut<User, UpdateUserInput>(
  `/api/users/${userId}`,
  {
    headers: {
      'X-Custom-Header': 'custom-value',
    }
  }
)
```

### 4. DELETE Request

```tsx
'use client'

import { useApiDelete } from '@/hooks/useApi'

function DeleteUserButton({ userId }: { userId: string }) {
  const deleteUser = useApiDelete(
    `/api/users/${userId}`
  )

  const handleDelete = async () => {
    if (confirm('Are you sure?')) {
      try {
        await deleteUser.mutateAsync()
        console.log('User deleted')
      } catch (error) {
        console.error('Failed to delete user:', error)
      }
    }
  }

  return (
    <button 
      onClick={handleDelete}
      disabled={deleteUser.isPending}
    >
      {deleteUser.isPending ? 'Deleting...' : 'Delete User'}
    </button>
  )
}
```

#### DELETE with Custom Headers

```tsx
const deleteUser = useApiDelete(
  `/api/users/${userId}`,
  {
    headers: {
      'X-Custom-Header': 'custom-value',
    }
  }
)
```

## Advanced Options

### Custom Headers

You can pass custom headers for any request:

```tsx
// GET with custom headers
const { data } = useApiGet(
  ['users'],
  '/api/users',
  {
    headers: {
      'X-Custom-Header': 'value',
      'Accept': 'application/vnd.api+json',
    }
  }
)

// POST with custom headers
const createUser = useApiPost('/api/users', {
  headers: {
    'X-Request-ID': '12345',
  }
})
```

### Skipping Default Headers

If you need to skip the default `Content-Type: application/json` header:

```tsx
const { data } = useApiGet(
  ['image'],
  '/api/image',
  {
    headers: {
      'Content-Type': undefined, // Skip default
      'Accept': 'image/png',
    }
  }
)
```

**Note**: The `Authorization` header with the access token is automatically included if the user is authenticated. You can override this by providing your own `Authorization` header or setting it to `null` to skip it.

### Custom Query Options

```tsx
const { data } = useApiGet(
  ['users'],
  '/api/users',
  {
    staleTime: 5 * 60 * 1000, // 5 minutes
    refetchInterval: 30 * 1000, // Refetch every 30 seconds
    enabled: !!userId, // Only fetch when userId exists
  }
)
```

### Select/Transform Data

```tsx
const { data } = useApiGet(
  ['users'],
  '/api/users',
  {
    select: (users) => users.filter(u => u.active),
  }
)
```

### Custom Mutation Options

```tsx
const createUser = useApiPost('/api/users', {
  onSuccess: (data) => {
    // Custom success handler
    console.log('User created:', data)
  },
  onError: (error) => {
    // Custom error handler
    console.error('Error:', error)
  },
  onSettled: () => {
    // Runs regardless of success/failure
    console.log('Mutation settled')
  },
})
```

## Query Invalidation

By default, mutations automatically invalidate all queries. You can be more specific:

```tsx
const updateUser = useApiPut(`/api/users/${userId}`, {
  onSuccess: () => {
    // Only invalidate specific queries
    queryClient.invalidateQueries({ queryKey: ['users'] })
    queryClient.invalidateQueries({ queryKey: ['user', userId] })
  },
})
```

## Server-Side vs Client-Side

- **Server-side**: Continue using `src/lib/api-client.ts` for server components and API routes
- **Client-side**: Use the new `useApi` hooks for client components

## Benefits

1. **No duplicate calls**: Same request made multiple times = single API call
2. **Automatic caching**: Data is cached and reused
3. **Loading states**: Built-in loading, error, and success states
4. **Optimistic updates**: Can show UI updates before API completes
5. **Background refetching**: Keep data fresh in the background
6. **Type safety**: Full TypeScript support
7. **Automatic 401 handling**: Redirects to login and back to original page after authentication

## 401 Error Handling

The API client automatically handles 401 (Unauthorized) responses:

1. **Automatic redirect**: When a 401 is received, the user is redirected to the login page
2. **Path preservation**: The current page path is stored for redirect after login
3. **Auto-redirect**: After successful login, users are returned to their original page
4. **Token cleanup**: Access tokens are cleared on 401 or manual logout

### Manual Logout

The `useAuth` hook includes an improved logout function:

```tsx
const { logout } = useAuth()

const handleLogout = async () => {
  await logout() // Clears tokens, signs out, and redirects to login
}
```

### Custom Redirect

You can manually store redirect paths if needed:

```tsx
import { storeRedirectPath } from '@/lib/redirect-utils'

// Store current path before navigation
storeRedirectPath()
router.push('/some-other-page')
```

## Migration Tips

To migrate existing client-side API calls:

1. Replace direct `fetch` calls with `useApiGet` hooks
2. Replace async functions with `useApiPost`, `useApiPut`, `useApiDelete` mutations
3. Use the built-in loading/error states instead of manual state management
4. Remove duplicate API calls - TanStack Query handles this automatically

## Troubleshooting

**Issue**: Data not updating after mutation
- **Solution**: Check that your query keys match the invalidation pattern

**Issue**: Authentication errors
- **Solution**: Ensure the user is logged in and the token is being synced properly

**Issue**: Stale data
- **Solution**: Adjust `staleTime` or manually invalidate queries when needed
