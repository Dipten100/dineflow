'use client'

import { useEffect, useMemo, useRef, useState } from 'react'
import { usePathname, useRouter, useSearchParams } from 'next/navigation'
import { useApiGet } from '@/hooks/useApi'
import {
  Alert,
  Box,
  Card,
  Chip,
  CircularProgress,
  Pagination,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography
} from '@mui/material'
import { PermissionResponse } from './@type/type'
import buildQueryString from '@/utils/buildQueryString'

export default function PermissionPage() {
  const router = useRouter()
  const pathname = usePathname()
  const searchParams = useSearchParams()

  const page = Number(searchParams.get('page') || '1')
  const search = searchParams.get('search') || ''
  const size = Number(searchParams.get('size') || '10')

  // Local input state so typing feels instant, even though the URL/API call is debounced
  const [searchInput, setSearchInput] = useState(search)
  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null)

  // Keep local input in sync if the URL search param changes externally (e.g. back/forward nav)
  useEffect(() => {
    setSearchInput(search)
  }, [search])

  // Cleanup any pending debounce on unmount
  useEffect(() => {
    return () => {
      if (debounceRef.current) clearTimeout(debounceRef.current)
    }
  }, [])

  const updateUrlParams = (nextValues: Record<string, string | number | null | undefined>) => {
    const query = buildQueryString(searchParams, nextValues)
    const targetUrl = query ? `${pathname}?${query}` : pathname

    router.replace(targetUrl, { scroll: false })
  }

  const apiUrl = `/api/permissions?page=${page}&size=${size}${search ? `&search=${encodeURIComponent(search)}` : ''}`

  const { data, isLoading, error } = useApiGet<PermissionResponse>(
    ['permissions', String(page), String(size), search],
    apiUrl
  )

  const permissions = data?.data.permissionDetails ?? []

  const summary = useMemo(() => {
    return {
      total: data?.data.summary.totalPermission || 0,
      modules: data?.data.summary.totalModule || 0,
      actions: data?.data.summary.totalAction || 0
    }
  }, [data])

  const handlePageChange = (_event: React.ChangeEvent<unknown>, newPage: number) => {
    updateUrlParams({ page: newPage, size })
  }

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const nextSearch = event.target.value

    // Update the input immediately for responsive typing
    setSearchInput(nextSearch)

    // Debounce the actual URL update (and therefore the API call) by 500ms
    if (debounceRef.current) clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => {
      updateUrlParams({ search: nextSearch, page: 1, size })
    }, 500)
  }

  return (
    <Box className='p-6'>
      <Stack spacing={3}>
        <Box>
          <Typography variant='h4' sx={{ fontWeight: 700, mb: 0.5 }}>
            Permissions
          </Typography>
          <Typography variant='body2' color='text.secondary'>
            {data?.message || 'Manage access permissions across the application.'}
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
          <TextField
            value={searchInput}
            onChange={handleSearchChange}
            placeholder='Search permissions'
            size='small'
            sx={{ width: { xs: '100%', sm: 280 } }}
          />
        </Box>

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <Card sx={{ flex: 1, p: 2.5 }}>
            <Typography variant='caption' color='text.secondary'>Total permissions</Typography>
            <Typography variant='h4' sx={{ mt: 1, fontWeight: 700 }}>
              {summary.total}
            </Typography>
          </Card>

          <Card sx={{ flex: 1, p: 2.5 }}>
            <Typography variant='caption' color='text.secondary'>Modules</Typography>
            <Typography variant='h4' sx={{ mt: 1, fontWeight: 700 }}>
              {summary.modules}
            </Typography>
          </Card>

          <Card sx={{ flex: 1, p: 2.5 }}>
            <Typography variant='caption' color='text.secondary'>Actions</Typography>
            <Typography variant='h4' sx={{ mt: 1, fontWeight: 700 }}>
              {summary.actions}
            </Typography>
          </Card>
        </Stack>

        {error ? (
          <Alert severity='error'>Unable to load permissions. Please try again later.</Alert>
        ) : isLoading ? (
          <Card sx={{ p: 4, display: 'flex', justifyContent: 'center' }}>
            <CircularProgress />
          </Card>
        ) : permissions.length === 0 ? (
          <Card sx={{ p: 4 }}>
            <Typography variant='body1' color='text.secondary'>
              No permissions found.
            </Typography>
          </Card>
        ) : (
          <Card>
            <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Name</TableCell>
                    <TableCell>Description</TableCell>
                    <TableCell>Module</TableCell>
                    <TableCell>Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {permissions.map(permission => (
                    <TableRow key={permission.id} hover>
                      <TableCell>
                        <Typography variant='subtitle2' sx={{ fontWeight: 600 }}>
                          {permission.name}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography variant='body2' color='text.secondary'>
                          {permission.description}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Chip label={permission.module} color='primary' variant='outlined' size='small' />
                      </TableCell>
                      <TableCell>
                        <Chip label={permission.action} color='success' variant='tonal' size='small' />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
            <Box sx={{ display: 'flex', justifyContent: 'end', my: 2 }}>
              <Pagination
                count={data?.data.pagination.totalPages || 1}
                page={page}
                onChange={handlePageChange}
                variant='outlined'
                color='primary'
                shape='rounded'
              />
            </Box>
          </Card>
        )}
      </Stack>
    </Box>
  )
}
