export interface Task {
    parentCode: string;
    code?: string;
    name?: string;
    description?: string;
    status?: string;
    deadline?: Date;
    dependsOn?: string[]
    createdAt?: Date;
    updatedAt?: Date;
}
