import { ApiService } from "./ApiService";
import { Task } from "../types/task.types";
import {
    API_BASE_URL,
    CREATE_TASK_ENDPOINT,
    UPDATE_TASK_ENDPOINT,
    RETRIEVE_TASK_ENDPOINT,
    DELETE_TASK_ENDPOINT, RETRIEVE_TASKS_BY_PARENT_ENDPOINT
} from "../config/api.config";

export class TaskService {
    private apiService: ApiService;

    constructor() {
        this.apiService = new ApiService(API_BASE_URL);
    }

    /**
     * Create a new task
     * @param task The task to create
     * @returns Promise with the created task
     */
    async createTask(task: Omit<Task, "id">): Promise<Task | null> {
        const response = await this.apiService.post<Task>(CREATE_TASK_ENDPOINT, task);
        return response.data || null;
    }

    /**
     * Retrieve a task by ID
     * @param id The task ID
     * @returns Promise with the task if found
     */
    async getTask(code: string): Promise<Task | null> {
        const response = await this.apiService.get<Task>(RETRIEVE_TASK_ENDPOINT.replace("{{code}}", code));
        return response.data || null;
    }

    /**
     * Retrieve all tasks
     * @returns Promise with array of tasks
     */
    async getAllTasks(parentCode: string): Promise<Task[]> {
        const response = await this.apiService.get<Task[]>(RETRIEVE_TASKS_BY_PARENT_ENDPOINT.replace("{{code}}", parentCode));
        return response.data || [];
    }

    /**
     * Update an existing task
     * @param code The task code
     * @param task The updated task data
     * @returns Promise with the updated task
     */
    async updateTask(code: string, task: Partial<Task>): Promise<Task | null> {
        const response = await this.apiService.put<Task>(UPDATE_TASK_ENDPOINT.replace("{{code}}", code), task);
        return response.data || null;
    }

    /**
     * Delete a task
     * @param code The task code to delete
     * @returns Promise indicating success
     */
    async deleteTask(code: string): Promise<boolean> {
        const response = await this.apiService.delete<void>(DELETE_TASK_ENDPOINT.replace("{{code}}", code));
        return response.status === 200;
    }

}
