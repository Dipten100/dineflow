import React from 'react';
import { Autocomplete, TextField, Typography, Box, Chip } from '@mui/material';

/**
 * SearchableSelect
 * ---------------------------------------------------------------------------
 * A dynamic, searchable dropdown built on MUI's Autocomplete, styled to drop
 * in wherever a plain <Select> is used today. Works with a simple string[]
 * of options, or objects via `getOptionLabel` / `getOptionValue` — so it's
 * reusable for Module, Role, Status, or any other option list in the app.
 *
 * Usage (simple string options, drop-in replacement for your Select):
 *   <SearchableSelect
 *     label="Module"
 *     options={MODULE_OPTIONS}
 *     value={formData.module}
 *     onChange={(value) => handleChange('module', value)}
 *     error={errors.module}
 *     required
 *   />
 *
 * Usage (object options):
 *   <SearchableSelect
 *     label="Assignee"
 *     options={users}
 *     value={formData.assigneeId}
 *     onChange={(value) => handleChange('assigneeId', value)}
 *     getOptionLabel={(u) => u.name}
 *     getOptionValue={(u) => u.id}
 *   />
 *
 * Usage (multi-select):
 *   <SearchableSelect
 *     label="Modules"
 *     options={MODULE_OPTIONS}
 *     value={formData.modules}
 *     onChange={(values) => handleChange('modules', values)}
 *     multiple
 *   />
 */

export interface SearchableSelectProps<T> {
  label: string;
  /** Full list of selectable options — strings or objects */
  options: T[];
  /** Currently selected value (or array of values, if `multiple`) */
  value: string | T | (string | T)[] | null;
  /** Called with the new value (single) or array of values (multiple) */
  onChange: (value: any) => void;
  /** Extract a display label from an option — defaults to the option itself if it's a string */
  getOptionLabel?: (option: T) => string;
  /** Extract the underlying value from an option — defaults to the option itself if it's a string */
  getOptionValue?: (option: T) => string | number;
  error?: string;
  required?: boolean;
  disabled?: boolean;
  placeholder?: string;
  multiple?: boolean;
  /** Show a small helper hint below the field when there's no error */
  helperText?: string;
  fullWidth?: boolean;
}

export function SearchableSelect<T = string>({
  label,
  options,
  value,
  onChange,
  getOptionLabel,
  getOptionValue,
  error,
  required,
  disabled,
  placeholder = 'Search...',
  multiple = false,
  helperText,
  fullWidth = true,
}: SearchableSelectProps<T>) {
  const resolveLabel = (option: T): string =>
    getOptionLabel ? getOptionLabel(option) : (option as unknown as string);

  const resolveValue = (option: T): string | number =>
    getOptionValue ? getOptionValue(option) : (option as unknown as string);

  // Map raw value(s) back to full option object(s) so Autocomplete can match them
  const resolveSelected = (): T | T[] | null => {
    if (multiple) {
      const values = (Array.isArray(value) ? value : []) as (string | T)[];
      return options.filter((opt) => values.some((v: any) => resolveValue(opt) === (typeof v === 'object' ? resolveValue(v) : v)));
    }
    if (value === null || value === undefined || value === '') return null;
    return options.find((opt) => resolveValue(opt) === (value as any)) ?? null;
  };

  return (
    <Autocomplete
      multiple={multiple}
      fullWidth={fullWidth}
      disabled={disabled}
      options={options}
      value={resolveSelected() as any}
      getOptionLabel={resolveLabel}
      isOptionEqualToValue={(opt, val) => resolveValue(opt) === resolveValue(val)}
      onChange={(_event, newValue) => {
        if (multiple) {
          onChange((newValue as T[]).map((opt) => resolveValue(opt)));
        } else {
          onChange(newValue ? resolveValue(newValue as T) : '');
        }
      }}
      renderOption={(props, option) => (
        <Box component='li' {...props} key={resolveValue(option)}>
          <Typography variant='body2'>{resolveLabel(option)}</Typography>
        </Box>
      )}
      renderTags={(tagValue, getTagProps) =>
        tagValue.map((option, index) => (
          <Chip
            {...getTagProps({ index })}
            key={resolveValue(option)}
            label={resolveLabel(option)}
            size='small'
            sx={{ borderRadius: 1 }}
          />
        ))
      }
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          required={required}
          placeholder={placeholder}
          error={Boolean(error)}
          helperText={
            error ? (
              <Typography component='span' variant='caption' color='error'>
                {error}
              </Typography>
            ) : (
              helperText
            )
          }
          sx={{
            '& .MuiOutlinedInput-root': {
              transition: 'box-shadow 150ms ease, border-color 150ms ease',
            },
          }}
        />
      )}
      sx={{
        '& .MuiAutocomplete-tag': {
          borderRadius: 1,
        },
      }}
    />
  );
}

export default SearchableSelect;
