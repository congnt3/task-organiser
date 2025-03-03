# TypeScript RESTful API Client

This project contains a TypeScript implementation for making RESTful API calls, located in `src/service/ApiService.ts`. The implementation provides a clean, type-safe way to interact with REST APIs.

## Features

- Type-safe API responses using TypeScript generics
- Support for all common HTTP methods (GET, POST, PUT, PATCH, DELETE)
- Query parameters support
- Request timeout handling
- Error handling with structured responses
- JSON request/response handling

## Basic Usage

```typescript
import apiService from './service/ApiService';

// GET request
const getUserData = async (userId: string) => {
  const response = await apiService.get<UserData>(`users/${userId}`);
  
  if (response.error) {
    console.error('Error fetching user:', response.error);
    return null;
  }
  
  return response.data;
};

// POST request with data
const createUser = async (userData: CreateUserRequest) => {
  const response = await apiService.post<UserData>('users', userData);
  
  if (response.error) {
    throw new Error(`Failed to create user: ${response.error}`);
  }
  
  return response.data;
};
```

## Advanced Usage

### With Query Parameters

```typescript
const searchUsers = async (searchTerm: string, page: number, pageSize: number) => {
  const response = await apiService.get<UserSearchResponse>('users/search', {
    params: {
      q: searchTerm,
      page: page.toString(),
      size: pageSize.toString()
    }
  });
  
  return response.data?.users || [];
};
```

### With Custom Headers

```typescript
const uploadFile = async (file: File) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await apiService.post<UploadResponse>('files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'X-Custom-Header': 'custom-value'
    }
  });
  
  return response;
};
```

### Setting Timeout

```typescript
const getLongRunningOperation = async () => {
  const response = await apiService.get<OperationResult>('operations/long-running', {
    timeout: 30000 // 30 seconds timeout
  });
  
  return response;
};
```

## Creating Service Classes

For better organization, you can create service classes that use the ApiService:

```typescript
// UserService.ts
import apiService from './ApiService';

export class UserService {
  private basePath = 'users';
  
  async getAll() {
    return apiService.get<User[]>(this.basePath);
  }
  
  async getById(id: string) {
    return apiService.get<User>(`${this.basePath}/${id}`);
  }
  
  async create(userData: CreateUserRequest) {
    return apiService.post<User>(this.basePath, userData);
  }
  
  async update(id: string, userData: Partial<User>) {
    return apiService.put<User>(`${this.basePath}/${id}`, userData);
  }
  
  async delete(id: string) {
    return apiService.delete(`${this.basePath}/${id}`);
  }
}
```

## Customizing API Base URL

You can create a custom instance of ApiService with a different base URL:

```typescript
import { ApiService } from './service/ApiService';

// For external APIs
const externalApiService = new ApiService('https://api.external-service.com/v1');

// Usage
const getExternalData = async () => {
  const response = await externalApiService.get<ExternalData>('resource');
  return response;
};
```

## Error Handling Best Practices

```typescript
try {
  const response = await apiService.get<DataType>('endpoint');
  
  if (response.error) {
    // Handle API error (e.g., 404, 500)
    handleApiError(response.error, response.status);
    return;
  }
  
  // Process successful response
  processData(response.data);
} catch (error) {
  // Handle network or other errors
  handleNetworkError(error);
}
```

For a complete example of implementation, see `src/service/examples/ApiServiceExample.ts`.