import React, { ReactNode } from 'react';
import {
  Card,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
  Pagination,
  Skeleton,
  Stack,
} from '@mui/material';
import InboxOutlinedIcon from '@mui/icons-material/InboxOutlined';

/**
 * DataTable
 * ---------------------------------------------------------------------------
 * A fully generic, reusable table shell — not tied to permissions, users, or
 * any specific entity. Pass it a row type `T`, a `columns` config, and data;
 * it handles layout, loading skeletons, empty state, and pagination for you.
 *
 * Each screen (permissions, users, roles, invoices, ...) just supplies its
 * own column definitions and data — no table markup is duplicated anywhere.
 *
 * Usage:
 *   <DataTable<Permission>
 *     columns={[
 *       { key: 'name', header: 'Name', render: (row) => row.name },
 *       { key: 'module', header: 'Module', render: (row) => <Chip label={row.module} /> },
 *       {
 *         key: 'actions',
 *         header: 'Actions',
 *         align: 'right',
 *         render: (row) => <TableActions actions={buildActionsFor(row)} />,
 *       },
 *     ]}
 *     rows={permissions}
 *     getRowKey={(row) => row.id}
 *     isLoading={isLoading}
 *     page={page}
 *     totalPages={data?.data.pagination.totalPages || 1}
 *     onPageChange={handlePageChange}
 *     emptyTitle="No permissions found"
 *     emptyDescription="Try adjusting your filters, or create a new permission."
 *   />
 */

export interface DataTableColumn<T> {
  key: string;
  header: ReactNode;
  render: (row: T) => ReactNode;
  align?: 'left' | 'right' | 'center';
  width?: string | number;
  /** Hide this column entirely (e.g. based on permissions), without removing it from the config */
  hidden?: boolean;
}

export interface DataTableProps<T> {
  columns: DataTableColumn<T>[];
  rows: T[];
  /** Unique key extractor for each row, e.g. (row) => row.id */
  getRowKey: (row: T) => string | number;
  isLoading?: boolean;
  skeletonRows?: number;

  /** Pagination — omit page/totalPages/onPageChange to render without pagination */
  page?: number;
  totalPages?: number;
  onPageChange?: (event: React.ChangeEvent<unknown>, page: number) => void;

  /** Empty state customization */
  emptyTitle?: string;
  emptyDescription?: string;
  emptyIcon?: ReactNode;

  /** Optional row click handler (e.g. navigate to detail view) */
  onRowClick?: (row: T) => void;

  /** Optional dense mode for compact tables */
  dense?: boolean;
}

export function DataTable<T>({
  columns,
  rows,
  getRowKey,
  isLoading = false,
  skeletonRows = 5,
  page,
  totalPages,
  onPageChange,
  emptyTitle = 'No results found',
  emptyDescription = 'Try adjusting your filters.',
  emptyIcon,
  onRowClick,
  dense = false,
}: DataTableProps<T>) {
  const visibleColumns = columns.filter((col) => !col.hidden);
  const showPagination = Boolean(onPageChange) && (totalPages ?? 0) > 1;

  return (
    <Card
      elevation={0}
      sx={{
        border: (theme) => `1px solid ${theme.palette.divider}`,
        borderRadius: 2,
      }}
    >
      <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
        <Table size={dense ? 'small' : 'medium'}>
          <TableHead>
            <TableRow
              sx={{
                '& th': {
                  backgroundColor: (theme) =>
                    theme.palette.mode === 'light' ? theme.palette.grey[50] : theme.palette.grey[900],
                  fontWeight: 600,
                  color: 'text.secondary',
                  fontSize: '0.75rem',
                  letterSpacing: 0.2,
                  borderBottom: (theme) => `1px solid ${theme.palette.divider}`,
                },
              }}
            >
              {visibleColumns.map((col) => (
                <TableCell key={col.key} align={col.align ?? 'left'} width={col.width}>
                  {col.header}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>

          <TableBody>
            {isLoading &&
              Array.from({ length: skeletonRows }).map((_, i) => (
                <TableRow key={`skeleton-${i}`}>
                  {visibleColumns.map((col) => (
                    <TableCell key={col.key}>
                      <Skeleton variant='text' width={col.width ? '60%' : '80%'} />
                    </TableCell>
                  ))}
                </TableRow>
              ))}

            {!isLoading && rows.length === 0 && (
              <TableRow>
                <TableCell colSpan={visibleColumns.length} sx={{ border: 0, py: 8 }}>
                  <Stack alignItems='center' spacing={1.5}>
                    {emptyIcon ?? <InboxOutlinedIcon sx={{ fontSize: 40, color: 'text.disabled' }} />}
                    <Typography variant='subtitle2' color='text.secondary'>
                      {emptyTitle}
                    </Typography>
                    <Typography variant='body2' color='text.disabled'>
                      {emptyDescription}
                    </Typography>
                  </Stack>
                </TableCell>
              </TableRow>
            )}

            {!isLoading &&
              rows.map((row) => (
                <TableRow
                  key={getRowKey(row)}
                  hover
                  onClick={onRowClick ? () => onRowClick(row) : undefined}
                  sx={{
                    transition: 'background-color 150ms ease',
                    cursor: onRowClick ? 'pointer' : 'default',
                    '&:last-child td': { borderBottom: 0 },
                  }}
                >
                  {visibleColumns.map((col) => (
                    <TableCell key={col.key} align={col.align ?? 'left'}>
                      {col.render(row)}
                    </TableCell>
                  ))}
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>

      {showPagination && (
        <Box sx={{ display: 'flex', justifyContent: 'end', px: 2, py: 2 }}>
          <Pagination
            count={totalPages || 1}
            page={page || 1}
            onChange={onPageChange}
            variant='outlined'
            color='primary'
            shape='rounded'
          />
        </Box>
      )}
    </Card>
  );
}

export default DataTable;
