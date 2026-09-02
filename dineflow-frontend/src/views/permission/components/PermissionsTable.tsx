import React, { useMemo } from 'react';
import { Typography, Chip } from '@mui/material';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import TableActions, { TableAction } from '@/common/TableActions';
import DataTable, { DataTableColumn } from '@/common/DataTable';
import { Permission } from '../types/type';

/**
 * PermissionsTable
 * ---------------------------------------------------------------------------
 * Thin, entity-specific wrapper around the generic <DataTable>. Only
 * declares permission-specific columns and row actions — all table
 * mechanics (loading, empty state, pagination) live in DataTable and are
 * shared across every table in the app.
 */

export interface UserAccess {
  view: boolean;
  create: boolean;
  edit: boolean;
  delete: boolean;
}

export interface PermissionsTableProps {
  permissions: Permission[];
  isLoading?: boolean;
  page: number;
  totalPages: number;
  onPageChange: (event: React.ChangeEvent<unknown>, page: number) => void;
  userAccess: UserAccess;
  onView?: (permission: Permission) => void;
  onEdit?: (permission: Permission) => void;
  onDelete?: (permission: Permission) => void;
  deletingId?: string | number | null;
}

const MODULE_COLOR_MAP: Record<string, 'primary' | 'secondary' | 'info' | 'warning'> = {
  users: 'primary',
  roles: 'secondary',
  billing: 'warning',
};

const ACTION_COLOR_MAP: Record<string, 'success' | 'info' | 'warning' | 'error'> = {
  view: 'info',
  create: 'success',
  update: 'warning',
  delete: 'error',
};

const moduleColor = (m: string) => MODULE_COLOR_MAP[m?.toLowerCase()] ?? 'primary';
const actionColor = (a: string) => ACTION_COLOR_MAP[a?.toLowerCase()] ?? 'success';

export function PermissionsTable({
  permissions,
  isLoading = false,
  page,
  totalPages,
  onPageChange,
  userAccess,
  onView,
  onEdit,
  onDelete,
  deletingId = null,
}: PermissionsTableProps) {
  const columns = useMemo<DataTableColumn<Permission>[]>(
    () => [
      {
        key: 'name',
        header: 'Name',
        render: (row) => (
          <Typography variant='subtitle2' sx={{ fontWeight: 600 }}>
            {row.name}
          </Typography>
        ),
      },
      {
        key: 'description',
        header: 'Description',
        render: (row) => (
          <Typography
            variant='body2'
            color='text.secondary'
            sx={{ maxWidth: 360, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}
            title={row.description}
          >
            {row.description || '—'}
          </Typography>
        ),
      },
      {
        key: 'module',
        header: 'Module',
        render: (row) => <Chip label={row.module} color={moduleColor(row.module)} variant='outlined' size='small' />,
      },
      {
        key: 'action',
        header: 'Action',
        render: (row) => <Chip label={row.action} color={actionColor(row.action)} size='small' />,
      },
      {
        key: 'actions',
        header: 'Actions',
        align: 'right',
        width: 140,
        render: (row) => {
          const rowActions: TableAction[] = [
            {
              key: 'edit',
              icon: EditOutlinedIcon,
              label: 'Edit',
              onClick: () => onEdit?.(row),
              hidden: !userAccess.edit || !onEdit,
            },
            {
              key: 'delete',
              icon: DeleteOutlineOutlinedIcon,
              label: 'Delete',
              color: 'error',
              onClick: () => onDelete?.(row),
              isLoading: deletingId === row.id,
              hidden: !userAccess.delete || !onDelete,
            },
          ];
          return <TableActions actions={rowActions} />;
        },
      },
    ],
    [userAccess, onView, onEdit, onDelete, deletingId]
  );

  return (
    <DataTable<Permission>
      columns={columns}
      rows={permissions}
      getRowKey={(row) => row.id}
      isLoading={isLoading}
      page={page}
      totalPages={totalPages}
      onPageChange={onPageChange}
      emptyTitle='No permissions found'
      emptyDescription='Try adjusting your filters, or create a new permission.'
    />
  );
}

export default PermissionsTable;
