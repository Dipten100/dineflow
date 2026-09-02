import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Typography,
  Button,
  Box,
  Slide,
} from '@mui/material';
import { TransitionProps } from '@mui/material/transitions';
import CloseIcon from '@mui/icons-material/Close';
import { SubmitButton } from './SubmitButton';

/**
 * DynamicModal
 * ---------------------------------------------------------------------------
 * A single, reusable modal shell for the whole app — create/edit forms,
 * confirmations, previews, anything. Content is passed as children, so this
 * component only owns the chrome: title, close button, footer actions,
 * loading state, and transition. Nothing here is tied to "Permission" or
 * any other entity.
 *
 * Usage — create/edit form:
 *   <DynamicModal
 *     open={open}
 *     onClose={handleClose}
 *     title={editingPermission ? 'Edit Permission' : 'Create Permission'}
 *     onSubmit={handleSubmit}
 *     submitLabel={editingPermission ? 'Save changes' : 'Create'}
 *     isSubmitting={createPermission.isPending}
 *   >
 *     <PermissionForm formData={formData} onChange={handleChange} errors={errors} />
 *   </DynamicModal>
 *
 * Usage — confirmation dialog:
 *   <DynamicModal
 *     open={confirmOpen}
 *     onClose={() => setConfirmOpen(false)}
 *     title="Delete permission?"
 *     onSubmit={confirmDelete}
 *     submitLabel="Delete"
 *     submitColor="error"
 *     isSubmitting={deletePermission.isPending}
 *     maxWidth="xs"
 *   >
 *     <Typography variant="body2" color="text.secondary">
 *       This action can't be undone. Are you sure you want to delete
 *       "{target?.name}"?
 *     </Typography>
 *   </DynamicModal>
 *
 * Usage — read-only / no footer (e.g. a details preview):
 *   <DynamicModal open={open} onClose={onClose} title="Permission details" hideActions>
 *     <PermissionDetails permission={selected} />
 *   </DynamicModal>
 */

const SlideUpTransition = React.forwardRef(function SlideUpTransition(
  props: TransitionProps & { children: React.ReactElement },
  ref: React.Ref<unknown>
) {
  return <Slide direction='up' ref={ref} {...props} />;
});

export interface DynamicModalProps {
  open: boolean;
  onClose: () => void;
  title: string;
  /** Optional short description under the title */
  description?: string;
  children: React.ReactNode;

  /** Footer "confirm" action — omit along with hideActions for a read-only modal */
  onSubmit?: () => void;
  submitLabel?: string;
  /** Label shown on the submit button while isSubmitting. Defaults to `${submitLabel}...` */
  submitLoadingLabel?: string;
  /** Optional label shown briefly on success — pair with `submitSuccess` */
  submitSuccessLabel?: string;
  /** Show the brief success state on the submit button */
  submitSuccess?: boolean;
  cancelLabel?: string;
  submitColor?: 'primary' | 'secondary' | 'error' | 'warning' | 'success' | 'info';
  isSubmitting?: boolean;
  submitDisabled?: boolean;

  /** Hide the default footer entirely — use when children render their own actions */
  hideActions?: boolean;

  maxWidth?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  fullWidth?: boolean;
  /** Prevent closing via backdrop click / Escape while a submission is in flight */
  disableCloseOnSubmit?: boolean;
}

export function DynamicModal({
  open,
  onClose,
  title,
  description,
  children,
  onSubmit,
  submitLabel = 'Save',
  submitLoadingLabel,
  submitSuccessLabel,
  submitSuccess = false,
  cancelLabel = 'Cancel',
  submitColor = 'primary',
  isSubmitting = false,
  submitDisabled = false,
  hideActions = false,
  maxWidth = 'sm',
  fullWidth = true,
  disableCloseOnSubmit = true,
}: DynamicModalProps) {
  const handleClose = (_event?: object, reason?: 'backdropClick' | 'escapeKeyDown') => {
    if (disableCloseOnSubmit && isSubmitting) return;
    onClose();
  };

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      maxWidth={maxWidth}
      fullWidth={fullWidth}
      TransitionComponent={SlideUpTransition}
      PaperProps={{
        sx: {
          borderRadius: 3,
        },
      }}
    >
      <DialogTitle sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', pr: 6 }}>
        <Box>
          <Typography variant='h6' sx={{ fontWeight: 600 }}>
            {title}
          </Typography>
          {description && (
            <Typography variant='body2' color='text.secondary' sx={{ mt: 0.5 }}>
              {description}
            </Typography>
          )}
        </Box>
        <IconButton
          onClick={() => handleClose()}
          disabled={disableCloseOnSubmit && isSubmitting}
          sx={{ position: 'absolute', right: 12, top: 12, color: 'text.secondary' }}
          aria-label='Close'
        >
          <CloseIcon fontSize='small' />
        </IconButton>
      </DialogTitle>

      <DialogContent dividers sx={{ py: 3 }}>
        {children}
      </DialogContent>

      {!hideActions && onSubmit && (
        <DialogActions sx={{ px: 3, py: 2 }}>
          <Button onClick={() => handleClose()} disabled={isSubmitting} color='inherit'>
            {cancelLabel}
          </Button>
          <SubmitButton
            onClick={onSubmit}
            variant='contained'
            color={submitColor}
            isLoading={isSubmitting}
            disabled={submitDisabled}
            label={submitLabel}
            loadingLabel={submitLoadingLabel}
            successLabel={submitSuccessLabel}
            success={submitSuccess}
            minWidth={120}
          />
        </DialogActions>
      )}
    </Dialog>
  );
}

export default DynamicModal;
