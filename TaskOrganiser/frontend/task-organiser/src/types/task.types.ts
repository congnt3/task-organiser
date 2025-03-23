export interface Task {
    parentCode: string;
    code: string;
    name?: string;
    description?: string;
    status?: string;
    dueDate?: Date;
    estimatedEffortDays?: number
    plannedStartDate?: Date;
    plannedCompletionDate?: Date;
    dependsOn?: Task[]
    createdAt?: Date;
    updatedAt?: Date;
}
