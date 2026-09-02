import React from 'react';
import { Box, IconButton, Tooltip, CircularProgress } from '@mui/material';
import { SvgIconComponent } from '@mui/icons-material';

/**
 * TableActions
 * ---------------------------------------------------------------------------
 * Renders a row of action buttons (edit, delete, view, etc.) for a table row,
 * driven entirely by a config array — so each table just declares *what*
 * actions it needs, not how they're laid out or styled.
 *
 * Each action can independently be loading, disabled, or hidden, which makes
 * it easy to wire directly to React Query mutations (isPending per-row).
 *
 * Usage:
 *   <TableActions
 *     actions={[
 *       {
 *         key: 'view',
 *         icon: VisibilityIcon,
 *         label: 'View',
 *         onClick: () => navigate(`/permissions/${row.id}`),
 *       },
 *       {
 *         key: 'edit',
 *         icon: EditIcon,
 *         label: 'Edit',
 *         onClick: () => openEditDialog(row),
 *         disabled: !row.canEdit,
 *       },
 *       {
 *         key: 'delete',
 *         icon: DeleteIcon,
 *         label: 'Delete',
 *         color: 'error',
 *         onClick: () => deletePermission.mutate(row.id),
 *         isLoading: deletePermission.isPending && deletePermission.variables === row.id,
 *       },
 *     ]}
 *   />
 */

export interface TableAction {
  /** Unique key for React list rendering */
  key: string;
  /** Icon component to display, e.g. from @mui/icons-material */
  icon: SvgIconComponent;
  /** Tooltip / accessible label */
  label: string;
  /** Click handler */
  onClick: () => void;
  /** Whether this specific action is mid-flight (shows spinner instead of icon) */
  isLoading?: boolean;
  /** Whether this action is disabled */
  disabled?: boolean;
  /** Whether to hide this action entirely (useful for permission-gated actions) */
  hidden?: boolean;
  /** MUI color token */
  color?: 'default' | 'primary' | 'secondary' | 'error' | 'warning' | 'info' | 'success';
}

export interface TableActionsProps {
  actions: TableAction[];
  /** Size of each icon button */
  size?: 'small' | 'medium' | 'large';
  /** Gap between buttons (theme spacing units) */
  gap?: number;
}

export function TableActions({ actions, size = 'small', gap = 0.5 }: TableActionsProps) {
  const visibleActions = actions.filter((action) => !action.hidden);

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', gap, justifyContent: 'flex-end' }}>
      {visibleActions.map(({ key, icon: Icon, label, onClick, isLoading, disabled, color = 'default' }) => (
        <Tooltip key={key} title={label} arrow>
          {/* span wrapper so Tooltip still works when the button is disabled */}
          <span>
            <IconButton
              size={size}
              color={color}
              disabled={disabled || isLoading}
              onClick={onClick}
              aria-label={label}
              sx={{
                transition: 'transform 150ms ease, background-color 150ms ease',
                '&:hover': {
                  transform: isLoading || disabled ? 'none' : 'scale(1.08)',
                },
              }}
            >
              {isLoading ? (
                <CircularProgress size={18} thickness={5} color="inherit" />
              ) : (
                <Icon fontSize={size === 'large' ? 'medium' : 'small'} />
              )}
            </IconButton>
          </span>
        </Tooltip>
      ))}
    </Box>
  );
}

export default TableActions;
