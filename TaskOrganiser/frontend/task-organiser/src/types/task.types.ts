export interface Task {
    parentTask: string;
    code: string;
    name: string;
    description?: string;
    status: string;
    deadline: Date;
    createdAt?: Date;
    updatedAt?: Date;
}
