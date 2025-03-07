<script setup lang="ts">
import { ref, withDefaults } from "vue";
import { TaskService } from "@/service/TaskService.ts";
import { Task } from "@/types/task.types.ts";

const taskService = new TaskService();

// Define props with TypeScript interface
// interface TaskModel {
//     mode: string;
//     parentTask: string;
//     code?: string;
//     name?: string;
//     description?: string;
//     status?: string;
//     deadline?: string;
// }

interface Props {
    mode: string;
}

let props = withDefaults(defineProps<Props>(), {
    "mode": "create"
});

// Define the props with defaults
let modelObj = defineModel<Task>();

const dropdownStates = ref([
    { name: "New", code: "New" },
    { name: "In Progress", code: "In Progress" },
    { name: "Completed", code: "Completed" }
]);

const onSaveClick = () => {
    if (!modelObj.value) {
        return;
    }
    if (props.mode === "create") {
        let reqBody=  {
            "parentCode": modelObj.value.parentTask,
            "code": modelObj.value.code,
            "name": modelObj.value.name,
            "description": modelObj.value.description,
            "status": modelObj.value.status.code,
            "deadline": modelObj.value.deadline
        };
        taskService.createTask(reqBody);
    } else {
        if (!modelObj.value.code) {
            return;
        }

        taskService.updateTask(modelObj.value.code, modelObj.value);
    }
};
</script>

<template>
    <Fluid>
        <div class="w-full">
            {{ modelObj }}
            <div class=" flex flex-col gap-4 w-full">
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="parent">Parent</label>
                        <InputText id="parent" type="text" v-model="modelObj.parentTask" />
                    </div>
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="taskid">Task ID</label>
                        <InputText id="taskid" type="text" v-model="modelObj.code" />
                    </div>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="name">Name</label>
                        <InputText id="name" type="text" v-model="modelObj.name" />
                    </div>
                </div>
                <div class="flex flex-wrap">
                    <label for="description">Description</label>
                    <Textarea id="description" rows="4" v-model="modelObj.description" />
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="status">Status</label>
                        <Select id="status" v-model="modelObj.status" :options="dropdownStates"
                                optionLabel="name"
                                placeholder="Select One" class="w-full"
                                default-value="New"></Select>
                    </div>

                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="deadline">Deadline</label>
                        <InputText id="deadline" type="text" v-model="modelObj.deadline" />
                    </div>

                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <Button label="Save" icon="pi pi-check" @click="onSaveClick" />
                    <Button label="Save As New" icon="pi pi-check" @click="" />
                </div>
            </div>
        </div>
    </Fluid>
</template>
