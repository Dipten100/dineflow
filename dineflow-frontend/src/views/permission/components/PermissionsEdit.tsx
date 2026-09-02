import { Stack, TextField } from "@mui/material";
import SearchableSelect from "@/common/SearchableSelect";
import { ACTION_OPTIONS, MODULE_OPTIONS } from "../data/Options";
import { PermissionFormData, PermissionFormErrors } from "..";

export default function PermissionsEdit({editForm, handleEditFieldChange, editErrors}: {editForm: PermissionFormData, handleEditFieldChange: any, editErrors: PermissionFormErrors}) {
    return (
        <Stack spacing={2.5} sx={{ mt: 0.5 }}>
          <TextField
            label='Name'
            value={editForm.name}
            onChange={(e) => handleEditFieldChange('name', e.target.value)}
            error={Boolean(editErrors.name)}
            helperText={editErrors.name}
            fullWidth
            required
          />
          <TextField
            label='Description'
            value={editForm.description}
            onChange={(e) => handleEditFieldChange('description', e.target.value)}
            error={Boolean(editErrors.description)}
            helperText={editErrors.description}
            fullWidth
            multiline
            minRows={2}
          />
          <SearchableSelect
            label='Module'
            options={MODULE_OPTIONS}
            value={editForm.module}
            onChange={(value) => handleEditFieldChange('module', value)}
            error={editErrors.module}
            required
          />
          <SearchableSelect
            label='Action'
            options={ACTION_OPTIONS}
            value={editForm.action}
            onChange={(value) => handleEditFieldChange('action', value)}
            error={editErrors.action}
            required
          />
        </Stack>
    )
}
