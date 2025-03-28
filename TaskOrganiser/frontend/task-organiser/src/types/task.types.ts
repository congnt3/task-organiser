export interface Task {
    parentCode: string;
    code: string;
    type: string;
    name?: string;
    description?: string;
    status?: string;
    deadline?: Date;
    dependsOn?: Task[]
    createdAt?: Date;
    updatedAt?: Date;
}
