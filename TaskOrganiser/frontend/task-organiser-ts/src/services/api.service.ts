import { API_BASE_URL } from '../config/api.config';
import type { ApiResponse, ApiError } from '../types/api.types';

export class ApiService {
    private static async handleResponse<T>(response: Response): Promise<ApiResponse<T>> {
        if (!response.ok) {
            const error: ApiError = {
                message: response.statusText,
                status: response.status
            };
            throw error;
        }
        
        const data = await response.json();
        return {
            data,
            status: response.status
        };
    }

    static async get<T>(endpoint: string): Promise<ApiResponse<T>> {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return this.handleResponse<T>(response);
    }

    static async post<T>(endpoint: string, data: any): Promise<ApiResponse<T>> {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        return this.handleResponse<T>(response);
    }

    static async put<T>(endpoint: string, data: any): Promise<ApiResponse<T>> {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        return this.handleResponse<T>(response);
    }

    static async delete<T>(endpoint: string): Promise<ApiResponse<T>> {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return this.handleResponse<T>(response);
    }
}