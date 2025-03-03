/**
 * Example of using ApiService for RESTful API calls
 * This file demonstrates how to use the ApiService in a TypeScript application
 */

import apiService from '../ApiService';

// Define your data types for type safety
interface Task {
  id: number;
  title: string;
  completed: boolean;
  dueDate?: string;
}

interface CreateTaskRequest {
  title: string;
  description?: string;
  dueDate?: string;
}

export class TaskApiService {
  private readonly basePath = 'tasks';

  /**
   * Get all tasks
   */
  async getAllTasks(): Promise<Task[]> {
    const response = await apiService.get<Task[]>(this.basePath);
    
    if (response.error) {
      console.error('Failed to fetch tasks:', response.error);
      throw new Error(`Failed to fetch tasks: ${response.error}`);
    }
    
    return response.data || [];
  }

  /**
   * Get a task by ID
   */
  async getTaskById(id: number): Promise<Task | null> {
    const response = await apiService.get<Task>(`${this.basePath}/${id}`);
    
    if (response.error) {
      console.error(`Failed to fetch task ${id}:`, response.error);
      return null;
    }
    
    return response.data || null;
  }

  /**
   * Create a new task
   */
  async createTask(taskData: CreateTaskRequest): Promise<Task | null> {
    const response = await apiService.post<Task>(this.basePath, taskData);
    
    if (response.error) {
      console.error('Failed to create task:', response.error);
      throw new Error(`Failed to create task: ${response.error}`);
    }
    
    return response.data || null;
  }

  /**
   * Update an existing task
   */
  async updateTask(id: number, taskData: Partial<Task>): Promise<Task | null> {
    const response = await apiService.put<Task>(`${this.basePath}/${id}`, taskData);
    
    if (response.error) {
      console.error(`Failed to update task ${id}:`, response.error);
      throw new Error(`Failed to update task: ${response.error}`);
    }
    
    return response.data || null;
  }

  /**
   * Delete a task
   */
  async deleteTask(id: number): Promise<boolean> {
    const response = await apiService.delete<void>(`${this.basePath}/${id}`);
    
    if (response.error) {
      console.error(`Failed to delete task ${id}:`, response.error);
      return false;
    }
    
    return response.status === 200 || response.status === 204;
  }

  /**
   * Search tasks with filters
   */
  async searchTasks(params: { 
    query?: string; 
    completed?: boolean;
    fromDate?: string;
    toDate?: string;
  }): Promise<Task[]> {
    const response = await apiService.get<Task[]>(this.basePath, {
      params: params as Record<string, string>
    });
    
    if (response.error) {
      console.error('Failed to search tasks:', response.error);
      throw new Error(`Failed to search tasks: ${response.error}`);
    }
    
    return response.data || [];
  }
}

// Usage example:
/*
import { TaskApiService } from './service/examples/ApiServiceExample';

// In a Vue component:
const taskService = new TaskApiService();

// Using async/await in a method
async fetchTasks() {
  try {
    const tasks = await taskService.getAllTasks();
    this.tasks = tasks;
  } catch (error) {
    console.error(error);
    // Handle error, perhaps show a notification
  }
}

// Creating a new task
const createNewTask = async () => {
  try {
    const newTask = await taskService.createTask({
      title: 'Learn TypeScript',
      description: 'Study TypeScript fundamentals and advanced topics'
    });
    if (newTask) {
      // Task created successfully
    }
  } catch (error) {
    // Handle error
  }
};
*/