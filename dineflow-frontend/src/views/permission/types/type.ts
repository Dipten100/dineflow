import { ApiResponse } from "@/types/ApiResponse";

export interface Permission {
    id: number;
    name: string;
    description: string;
    module: string;
    action: string;
}

export interface PermissionApiResponse {
    summary: {
        totalModule: number;
        totalPermission: number;
        totalAction: number;
    },
    pagination: {
        page: number;
        size: number;
        totalElements: number;
        totalPages: number;
        last: boolean;
    },
    permissionDetails: Permission[]
}

export interface PermissionResponse extends ApiResponse<PermissionApiResponse> {
}


export interface EditPermission {
    id: number;
    name: string;
    description: string;
    module: string;
    action: string;
}

export interface EditPermissionApiResponse {
    name: string;
    description: string;
    module: string;
    action: string;
}

export interface EditPermissionResponse extends ApiResponse<EditPermissionApiResponse> {
}
