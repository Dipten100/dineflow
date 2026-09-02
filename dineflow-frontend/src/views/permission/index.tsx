'use client'

import { useEffect, useMemo, useRef, useState } from 'react'
import { usePathname, useRouter, useSearchParams } from 'next/navigation'
import { useApiGet } from '@/hooks/useApi'
import {
  Alert,
  Box,
  Card,
  Paper,
  Skeleton,
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
import { Permission, PermissionResponse } from './types/type'
import buildQueryString from '@/utils/buildQueryString'
import AddButton from '@/common/AddButton'
import { useAuth } from '@/hooks/useAuth'
import PermissionsTable from './components/PermissionsTable'
import DynamicModal from '@/common/DaynamicModal'
import PermissionsEdit from './components/PermissionsEdit'

export interface PermissionFormData {
  name: string
  description: string
  module: string
  action: string
}

export interface PermissionFormErrors {
  name?: string
  description?: string
  module?: string
  action?: string
}

const emptyForm: PermissionFormData = { name: '', description: '', module: '', action: '' }

function validate(form: PermissionFormData): PermissionFormErrors {
  const errors: PermissionFormErrors = {}
  if (!form.name.trim()) errors.name = 'Name is required'
  if (!form.module) errors.module = 'Module is required'
  if (!form.action) errors.action = 'Action is required'
  return errors
}

export default function PermissionPage() {
  const router = useRouter()
  const pathname = usePathname()
  const searchParams = useSearchParams()

  const page = Number(searchParams.get('page') || '1')
  const search = searchParams.get('search') || ''
  const size = Number(searchParams.get('size') || '10')

  const { hasPermission } = useAuth()

  const userAccess = useMemo(
    () => ({
      view: hasPermission('PERMISSION_VIEW'),
      create: hasPermission('PERMISSION_CREATE'),
      edit: hasPermission('PERMISSION_UPDATE'),
      delete: hasPermission('PERMISSION_DELETE'),
    }),
    [hasPermission]
  )

  const canCreate = userAccess.create

  // ---------------------------------------------------------------------
  // Search input (unchanged)
  // ---------------------------------------------------------------------
  const [searchInput, setSearchInput] = useState(search)
  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null)

  useEffect(() => {
    setSearchInput(search)
  }, [search])

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

  const { data, isLoading, error, refetch } = useApiGet<PermissionResponse>(
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
    setSearchInput(nextSearch)
    if (debounceRef.current) clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => {
      updateUrlParams({ search: nextSearch, page: 1, size })
    }, 500)
  }

  const handleAddPermission = () => {
    router.push('/permission/create')
  }

  // ---------------------------------------------------------------------
  // Edit modal (form)
  // ---------------------------------------------------------------------
  const [editTarget, setEditTarget] = useState<Permission | null>(null)
  const [editForm, setEditForm] = useState<PermissionFormData>(emptyForm)
  const [editErrors, setEditErrors] = useState<PermissionFormErrors>({})
  const [isEditSubmitting, setIsEditSubmitting] = useState(false)

  const handleEditPermission = (permission: Permission) => {
    setEditTarget(permission)
    setEditForm({
      name: permission.name,
      description: permission.description,
      module: permission.module,
      action: permission.action
    })
    setEditErrors({})
  }

  const handleEditFieldChange = (field: keyof PermissionFormData, value: string) => {
    setEditForm((prev) => ({ ...prev, [field]: value }))
    setEditErrors((prev) => ({ ...prev, [field]: undefined }))
  }

  const closeEditModal = () => {
    if (isEditSubmitting) return
    setEditTarget(null)
    setEditForm(emptyForm)
    setEditErrors({})
  }

  const handleEditSubmit = async () => {
    const validationErrors = validate(editForm)
    if (Object.keys(validationErrors).length > 0) {
      setEditErrors(validationErrors)
      return
    }
    if (!editTarget) return

    setIsEditSubmitting(true)
    // try {
    //   // TODO: replace with your actual update call, e.g.
    //   // await apiClient.put(`/api/permissions/${editTarget.id}`, editForm)
    //   const res = await fetch(`/api/permissions/${editTarget.id}`, {
    //     method: 'PUT',
    //     headers: { 'Content-Type': 'application/json' },
    //     body: JSON.stringify(editForm)
    //   })
    //   if (!res.ok) throw new Error('Failed to update permission')

    //   await refetch()
    //   closeEditModal()
    // } catch (err) {
    //   console.error('Edit permission failed', err)
    //   setEditErrors((prev) => ({ ...prev, name: prev.name ?? 'Something went wrong. Please try again.' }))
    // } finally {
    //   setIsEditSubmitting(false)
    // }
  }

  // ---------------------------------------------------------------------
  // Delete modal (confirmation)
  // ---------------------------------------------------------------------
  const [deleteTarget, setDeleteTarget] = useState<Permission | null>(null)
  const [isDeleteSubmitting, setIsDeleteSubmitting] = useState(false)

  const handleDeletePermission = (permission: Permission) => {
    setDeleteTarget(permission)
  }

  const closeDeleteModal = () => {
    if (isDeleteSubmitting) return
    setDeleteTarget(null)
  }

  const handleConfirmDelete = async () => {
    if (!deleteTarget) return

    setIsDeleteSubmitting(true)
    // try {
    //   // TODO: replace with your actual delete call, e.g.
    //   // await apiClient.delete(`/api/permissions/${deleteTarget.id}`)
    //   const res = await fetch(`/api/permissions/${deleteTarget.id}`, { method: 'DELETE' })
    //   if (!res.ok) throw new Error('Failed to delete permission')

    //   await refetch()
    //   setDeleteTarget(null)
    // } catch (err) {
    //   console.error('Delete permission failed', err)
    //   // keep the modal open so the user sees it didn't succeed
    // } finally {
    //   setIsDeleteSubmitting(false)
    // }
  }

  // ---------------------------------------------------------------------
  // Skeletons
  // ---------------------------------------------------------------------
  const SummarySkeleton = () => (
    <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
      {[1, 2, 3].map((item) => (
        <Card key={item} sx={{ flex: 1, p: 2.5 }}>
          <Skeleton variant="text" width={120} height={20} />
          <Skeleton variant="text" width={80} height={40} sx={{ mt: 1 }} />
        </Card>
      ))}
    </Stack>
  )

  const TableSkeleton = () => (
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
            {[1, 2, 3, 4, 5].map((item) => (
              <TableRow key={item}>
                <TableCell>
                  <Skeleton variant="text" width={150} height={24} />
                </TableCell>
                <TableCell>
                  <Skeleton variant="text" width={200} height={20} />
                </TableCell>
                <TableCell>
                  <Skeleton variant="rectangular" width={80} height={24} sx={{ borderRadius: 1 }} />
                </TableCell>
                <TableCell>
                  <Skeleton variant="rectangular" width={80} height={24} sx={{ borderRadius: 1 }} />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Box sx={{ display: 'flex', justifyContent: 'end', my: 2 }}>
        <Skeleton variant="rectangular" width={300} height={36} sx={{ borderRadius: 2 }} />
      </Box>
    </Card>
  )

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

        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2 }}>
          <TextField
            value={searchInput}
            onChange={handleSearchChange}
            placeholder='Search permissions'
            size='small'
            sx={{ width: { xs: '100%', sm: 280 } }}
            variant='standard'
            label='Search'
          />
          {canCreate && <AddButton onClick={handleAddPermission} />}
        </Box>

        {isLoading ? <SummarySkeleton /> : (
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
        )}

        {error ? (
          <Alert severity='error'>Unable to load permissions. Please try again later.</Alert>
        ) : isLoading ? (
          <TableSkeleton />
        ) : permissions.length === 0 ? (
          <Card sx={{ p: 4 }}>
            <Typography variant='body1' color='text.secondary'>
              No permissions found.
            </Typography>
          </Card>
        ) : (
          <PermissionsTable
            permissions={permissions}
            isLoading={isLoading}
            page={page}
            totalPages={data?.data.pagination.totalPages || 1}
            onPageChange={handlePageChange}
            userAccess={userAccess}
            onEdit={(permission) => handleEditPermission(permission)}
            onDelete={(permission) => handleDeletePermission(permission)}
          />
        )}
      </Stack>

      {/* Edit permission — form modal */}
      <DynamicModal
        open={Boolean(editTarget)}
        onClose={closeEditModal}
        title='Edit Permission'
        description='Update what this permission controls.'
        onSubmit={handleEditSubmit}
        submitLabel='Save changes'
        isSubmitting={isEditSubmitting}
      >
        <PermissionsEdit 
          editForm={editForm} 
          handleEditFieldChange={handleEditFieldChange} 
          editErrors={editErrors} 
        />
      </DynamicModal>

      {/* Delete permission — confirmation modal */}
      <DynamicModal
        open={Boolean(deleteTarget)}
        onClose={closeDeleteModal}
        title='Delete permission?'
        onSubmit={handleConfirmDelete}
        submitLabel='Delete'
        submitColor='error'
        isSubmitting={isDeleteSubmitting}
        maxWidth='xs'
      >
        <Typography variant='body2' color='text.secondary'>
          This action can&apos;t be undone. Are you sure you want to delete{' '}
          <Typography component='span' variant='body2' sx={{ fontWeight: 600 }}>
            &quot;{deleteTarget?.name}&quot;
          </Typography>
          ?
        </Typography>
      </DynamicModal>
    </Box>
  )
}
