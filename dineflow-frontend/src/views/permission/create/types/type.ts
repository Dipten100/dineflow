import { ApiResponse } from "@/types/ApiResponse";

export interface CreatePermission {
    name: string;
    description: string;
    module: string;
    action: string;
}

export interface CreatePermissionApiResponse {
    name: string;
    description: string;
    module: string;
    action: string;
}

export interface CreatePermissionResponse extends ApiResponse<CreatePermissionApiResponse> {
}
