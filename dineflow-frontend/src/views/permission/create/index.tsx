'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import {
    Box,
    Card,
    Typography,
    TextField,
    MenuItem,
    Button,
    Alert,
    Stack,
    FormControl,
    InputLabel,
    Select
} from '@mui/material'
import { useApiPost } from '@/hooks/useApi'
import SubmitButton from '@/common/SubmitButton'
import { CreatePermission, CreatePermissionResponse } from './types/type'
import { ACTION_OPTIONS, MODULE_OPTIONS } from '../data/Options'

export default function PermissionCreatePage() {
    const router = useRouter()
    const [formData, setFormData] = useState<CreatePermission>({
        name: '',
        description: '',
        module: '',
        action: ''
    })
    const [errors, setErrors] = useState<Partial<Record<keyof CreatePermission, string>>>({})

    const createPermission = useApiPost<CreatePermissionResponse, CreatePermission>('/api/permissions')

    const validateForm = (): boolean => {
        const newErrors: Partial<Record<keyof CreatePermission, string>> = {}

        if (!formData.name.trim()) {
            newErrors.name = 'Name is required'
        } else if (!/^[A-Z_]+$/.test(formData.name)) {
            newErrors.name = 'Name must be uppercase letters and underscores only (e.g., USER_OUTLET_VIEW)'
        }

        // convert space to "_"
        formData.name = formData.name.replace(/\s/g, '_')

        if (!formData.description.trim()) {
            newErrors.description = 'Description is required'
        }

        if (!formData.module) {
            newErrors.module = 'Module is required'
        }

        if (!formData.action) {
            newErrors.action = 'Action is required'
        }

        setErrors(newErrors)
        return Object.keys(newErrors).length === 0
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (!validateForm()) {
            return
        }

        try {
            await createPermission.mutateAsync(formData)
            router.push('/permission')
        } catch (error) {
            console.error('Failed to create permission:', error)
        }
    }

    const handleChange = (field: keyof CreatePermission, value: string) => {
        setFormData(prev => ({ ...prev, [field]: value }))
        // Clear error for this field when user starts typing
        if (errors[field]) {
            setErrors(prev => ({ ...prev, [field]: value }))
        }
    }

    const handleCancel = () => {
        router.push('/permission')
    }

    return (
        <Box className='p-6'>
            <Card sx={{ maxWidth: 800, mx: 'auto', p: 4 }}>
                <Typography variant='h4' sx={{ mb: 3, fontWeight: 700 }}>
                    Create Permission
                </Typography>

                {createPermission.error && (
                    <Alert severity='error' sx={{ mb: 3 }}>
                        Failed to create permission. Please try again.
                    </Alert>
                )}

                <form onSubmit={handleSubmit}>
                    <Stack spacing={3}>
                        <TextField
                            fullWidth
                            label='Name'
                            value={formData.name}
                            onChange={(e) => handleChange('name', e.target.value)}
                            error={!!errors.name}
                            helperText={errors.name || 'e.g., USER_OUTLET_VIEW'}
                            placeholder='Enter permission name'
                            required
                        />

                        <TextField
                            fullWidth
                            label='Description'
                            value={formData.description}
                            onChange={(e) => handleChange('description', e.target.value)}
                            error={!!errors.description}
                            helperText={errors.description}
                            placeholder='Enter permission description'
                            multiline
                            rows={3}
                            required
                        />

                        <FormControl fullWidth error={!!errors.module}>
                            <InputLabel>Module</InputLabel>
                            <Select
                                label='Module'
                                value={formData.module}
                                onChange={(e)=>handleChange('module', e.target.value as string)}
                                required
                            >
                                {MODULE_OPTIONS.map((module) => (
                                    <MenuItem key={module} value={module}>
                                        {module}
                                    </MenuItem>
                                ))}
                            </Select>
                            {errors.module && (
                                <Typography variant='caption' color='error' sx={{ mt: 1 }}>
                                    {errors.module}
                                </Typography>
                            )}
                        </FormControl>

                        <FormControl fullWidth error={!!errors.action}>
                            <InputLabel>Action</InputLabel>
                            <Select
                                label='Action'
                                value={formData.action}
                                onChange={(e)=>handleChange('action', e.target.value as string)}
                                required
                            >
                                {ACTION_OPTIONS.map((action) => (
                                    <MenuItem key={action} value={action}>
                                        {action}
                                    </MenuItem>
                                ))}
                            </Select>
                            {errors.action && (
                                <Typography variant='caption' color='error' sx={{ mt: 1 }}>
                                    {errors.action}
                                </Typography>
                            )}
                        </FormControl>

                        <Stack direction='row' spacing={2} sx={{ mt: 2 }}>
                            <SubmitButton
                                type='submit'
                                variant='contained'
                                isLoading={createPermission.isPending}
                                label='Create'
                                loadingLabel='Creating...'
                                disabled={createPermission.isPending}
                                sx={{ minWidth: 120 }}
                            />
                            <Button
                                variant='outlined'
                                onClick={handleCancel}
                                disabled={createPermission.isPending}
                                sx={{ minWidth: 120 }}
                            >
                                Cancel
                            </Button>
                        </Stack>
                    </Stack>
                </form>
            </Card>
        </Box>
    )
}
