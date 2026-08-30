'use client'

import { useQuery, useMutation, useQueryClient, type UseQueryOptions, type UseMutationOptions } from '@tanstack/react-query'
import { apiClient } from '@/lib/api-client-frontend'

// Generic hook for GET requests
export function useApiGet<T>(
  queryKey: string[],
  endpoint: string,
  requestOptions?: RequestInit,
  options?: Omit<UseQueryOptions<T>, 'queryKey' | 'queryFn'>
) {
  return useQuery({
    queryKey,
    queryFn: () => apiClient.get<T>(endpoint, requestOptions),
    ...options,
  })
}

// Generic hook for POST requests
export function useApiPost<T, TVariables = any>(
  endpoint: string,
  requestOptions?: RequestInit,
  options?: Omit<UseMutationOptions<T, Error, TVariables>, 'mutationFn'>
) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: TVariables) => apiClient.post<T>(endpoint, data, requestOptions),
    onSuccess: () => {
      // Invalidate related queries after successful mutation
      queryClient.invalidateQueries()
    },
    ...options,
  })
}

// Generic hook for PUT requests
export function useApiPut<T, TVariables = any>(
  endpoint: string,
  requestOptions?: RequestInit,
  options?: Omit<UseMutationOptions<T, Error, TVariables>, 'mutationFn'>
) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: TVariables) => apiClient.put<T>(endpoint, data, requestOptions),
    onSuccess: () => {
      queryClient.invalidateQueries()
    },
    ...options,
  })
}

// Generic hook for DELETE requests
export function useApiDelete<T>(
  endpoint: string,
  requestOptions?: RequestInit,
  options?: Omit<UseMutationOptions<T, Error, void>, 'mutationFn'>
) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: () => apiClient.delete<T>(endpoint, requestOptions),
    onSuccess: () => {
      queryClient.invalidateQueries()
    },
    ...options,
  })
}
