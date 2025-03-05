export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
export const RETRIEVE_TASK_ENDPOINT = "/api/tasks/code/{{code}}";
export const CREATE_TASK_ENDPOINT = "/api/tasks";
export const UPDATE_TASK_ENDPOINT = "/api/tasks/code/{{code}}";
export const DELETE_TASK_ENDPOINT = "/api/tasks/code/{{code}}";
export const RETRIEVE_TASKS_BY_PARENT_ENDPOINT = "/api/tasks/parent/{{code}}";
