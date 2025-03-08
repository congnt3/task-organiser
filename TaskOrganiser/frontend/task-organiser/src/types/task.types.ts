export interface Task {
    parentCode: string;
    code?: string;
    name?: string;
    description?: string;
    status?: string;
    deadline?: Date;
    createdAt?: Date;
    updatedAt?: Date;
}
