import React from 'react';
import { Button, ButtonProps, CircularProgress, Box, keyframes } from '@mui/material';

/**
 * DynamicSubmitButton
 * ---------------------------------------------------------------------------
 * A reusable submit button that swaps into a custom loading state.
 * Instead of the default MUI CircularProgress-in-a-corner look, this uses a
 * width-animated pill with a soft pulsing ring + fading label swap, so the
 * button visually "settles" into a loading state rather than just disabling.
 *
 * Usage:
 *   <DynamicSubmitButton
 *     isLoading={createPermission.isPending}
 *     label="Create"
 *     loadingLabel="Creating..."
 *     type="submit"
 *   />
 */

// Subtle pulse for the loading ring — draws the eye without being distracting
const pulse = keyframes`
  0% { box-shadow: 0 0 0 0 rgba(25, 118, 210, 0.35); }
  70% { box-shadow: 0 0 0 8px rgba(25, 118, 210, 0); }
  100% { box-shadow: 0 0 0 0 rgba(25, 118, 210, 0); }
`;

// Gentle fade+rise for label swapping (idle <-> loading text)
const fadeIn = keyframes`
  from { opacity: 0; transform: translateY(2px); }
  to { opacity: 1; transform: translateY(0); }
`;

export interface SubmitButtonProps extends Omit<ButtonProps, 'children'> {
  /** Whether the async action (mutation, fetch, etc.) is in flight */
  isLoading: boolean;
  /** Label shown in idle state */
  label: string;
  /** Label shown while loading. Defaults to `${label}...` if omitted */
  loadingLabel?: string;
  /** Optional label shown briefly after success (pass success=true to trigger) */
  successLabel?: string;
  /** Whether the last action succeeded — shows a checkmark + successLabel briefly */
  success?: boolean;
  /** Minimum width so the button doesn't jump in size between states */
  minWidth?: number;
}

export function SubmitButton({
  isLoading,
  label,
  loadingLabel,
  successLabel,
  success = false,
  minWidth = 140,
  disabled,
  sx,
  ...buttonProps
}: SubmitButtonProps) {
  const resolvedLoadingLabel = loadingLabel ?? `${label}...`;

  return (
    <Button
      {...buttonProps}
      disabled={disabled || isLoading}
      sx={{
        minWidth,
        position: 'relative',
        overflow: 'hidden',
        transition: 'background-color 200ms ease, box-shadow 200ms ease',
        ...(isLoading && {
          animation: `${pulse} 1.8s ease-out infinite`,
          pointerEvents: 'none',
        }),
        ...sx,
      }}
    >
      <Box
        component="span"
        sx={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 1,
          animation: `${fadeIn} 180ms ease`,
        }}
      >
        {isLoading && (
          <CircularProgress
            size={16}
            thickness={5}
            sx={{
              color: 'inherit',
              opacity: 0.9,
            }}
          />
        )}
        {!isLoading && success && successLabel ? successLabel : isLoading ? resolvedLoadingLabel : label}
      </Box>
    </Button>
  );
}

export default SubmitButton;
