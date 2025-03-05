/**
 * ApiService provides methods for making RESTful API calls
 */

// Define common types for request and response
export interface ApiResponse<T> {
  data?: T;
  error?: string;
  status: number;
}

export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

export interface RequestOptions {
  headers?: Record<string, string>;
  params?: Record<string, string>;
  timeout?: number;
}

class ApiService {
  private baseUrl: string;
  private defaultHeaders: Record<string, string>;

  constructor(baseUrl: string = '/api') {
    this.baseUrl = baseUrl;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
  }

  /**
   * Creates the final URL with query parameters
   */
  private createUrl(endpoint: string, options?: RequestOptions): string {
    const url = `${this.baseUrl}${endpoint.startsWith('/') ? endpoint : `/${endpoint}`}`;
    
    if (!options?.params) {
      return url;
    }
    
    const queryParams = new URLSearchParams();
    Object.entries(options.params).forEach(([key, value]) => {
      queryParams.append(key, value);
    });
    
    return `${url}?${queryParams.toString()}`;
  }

  /**
   * Performs the HTTP request
   */
  private async request<T>(
    method: HttpMethod,
    endpoint: string,
    data?: unknown,
    options?: RequestOptions
  ): Promise<ApiResponse<T>> {
    try {
      const url = this.createUrl(endpoint, options);
      const headers = { ...this.defaultHeaders, ...options?.headers };
      
      // Create AbortController for timeout handling
      const controller = new AbortController();
      const { signal } = controller;

      // Set timeout if specified
      let timeoutId: NodeJS.Timeout | undefined;
      if (options?.timeout) {
        timeoutId = setTimeout(() => controller.abort(), options.timeout);
      }

      const fetchOptions: RequestInit = {
        method,
        headers,
        signal,
        ...(data && method !== 'GET' ? { body: JSON.stringify(data) } : {})
      };

      const response = await fetch(url, fetchOptions);
      
      // Clear timeout if it was set
      if (timeoutId) {
        clearTimeout(timeoutId);
      }

      // Parse response
      let responseData: T | undefined;
      const contentType = response.headers.get('content-type');
      
      if (contentType && contentType.includes('application/json')) {
        responseData = await response.json();
      }

      if (!response.ok) {
        return {
          status: response.status,
          error: response.statusText || 'Request failed',
          data: responseData
        };
      }

      return {
        status: response.status,
        data: responseData
      };
    } catch (error) {
      if (error instanceof Error) {
        if (error.name === 'AbortError') {
          return {
            status: 0,
            error: 'Request timeout'
          };
        }
        
        return {
          status: 0,
          error: error.message
        };
      }
      
      return {
        status: 0,
        error: 'Unknown error occurred'
      };
    }
  }

  /**
   * Performs a GET request
   */
  public async get<T>(endpoint: string, options?: RequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>('GET', endpoint, undefined, options);
  }

  /**
   * Performs a POST request
   */
  public async post<T>(endpoint: string, data?: unknown, options?: RequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>('POST', endpoint, data, options);
  }

  /**
   * Performs a PUT request
   */
  public async put<T>(endpoint: string, data?: unknown, options?: RequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>('PUT', endpoint, data, options);
  }

  /**
   * Performs a PATCH request
   */
  public async patch<T>(endpoint: string, data?: unknown, options?: RequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>('PATCH', endpoint, data, options);
  }

  /**
   * Performs a DELETE request
   */
  public async delete<T>(endpoint: string, options?: RequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>('DELETE', endpoint, undefined, options);
  }
}

// Create a singleton instance with default configuration
const apiService = new ApiService();

export default apiService;