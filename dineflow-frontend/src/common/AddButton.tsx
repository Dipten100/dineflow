'use client'

import { Button } from '@mui/material'

interface AddButtonProps {
    label?: string
    onClick?: () => void
    variant?: 'text' | 'contained' | 'outlined'
    size?: 'small' | 'medium' | 'large'
    startIcon?: React.ReactNode
    disabled?: boolean
    fullWidth?: boolean
    sx?: object
    className?: string
}

const AddButton = ({
    label = 'Add',
    onClick,
    variant = 'contained',
    size = 'small',
    startIcon = <span style={{ fontSize: '1.2rem', fontWeight: 'bold', marginRight: '4px' }}>+</span>,
    disabled = false,
    fullWidth = false,
    sx = {
        minWidth: 100,
        backgroundColor: '#7c3aed', // Purple color
        color: 'white',
        borderRadius: '8px', // Rounded corners
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)', // Subtle shadow
        textTransform: 'none', // Keep text as-is without uppercase
        fontWeight: 500,
        padding: '8px 16px',
        '&:hover': {
            backgroundColor: '#6d28d9', // Darker purple on hover
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.15)', // Enhanced shadow on hover
        },
        '&:active': {
            backgroundColor: '#5b21b6', // Even darker purple when active
            boxShadow: '0 1px 2px rgba(0, 0, 0, 0.1)', // Reduced shadow when active
        },
        '&:disabled': {
            backgroundColor: '#e5e7eb',
            color: '#9ca3af',
            boxShadow: 'none',
        }
    },
    className
}: AddButtonProps) => {
    return (
        <Button
            variant={variant}
            size={size}
            sx={sx}
            startIcon={startIcon}
            onClick={onClick}
            disabled={disabled}
            fullWidth={fullWidth}
            className={className}
        >
            {label}
        </Button>
    )
}

export default AddButton
