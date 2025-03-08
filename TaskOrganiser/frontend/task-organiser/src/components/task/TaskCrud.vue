<script setup lang="ts">
import {ref} from "vue";
import {TaskService} from "@/service/TaskService.ts";
import {Task} from "@/types/task.types.ts";
import { STATUS_COMPLETED, STATUS_IN_PROGRESS, STATUS_NEW } from "@/config/task.constants.ts";

const taskService = new TaskService();

interface Props {
    mode: string;
}

let props = withDefaults(defineProps<Props>(), {
    "mode": "create"
});

// Define the props with defaults
let modelObj = defineModel<Task>();

const dropdownStates = ref([
    { name: STATUS_NEW, code: STATUS_NEW },
    { name: STATUS_IN_PROGRESS, code: STATUS_IN_PROGRESS },
    { name: STATUS_COMPLETED, code: STATUS_COMPLETED }
]);

const onSaveClick = () => {
    if (!modelObj.value) {
        return;
    }

    let reqBody = {
        "parentCode": modelObj.value.parentCode,
        "code": modelObj.value.code,
        "name": modelObj.value.name,
        "description": modelObj.value.description,
        "status": modelObj.value.status.code,
        "deadline": modelObj.value.deadline
    };

    if (props.mode === "create") {
        taskService.createTask(reqBody);
        console.log(`Creating child task for parent: ${reqBody.parentCode}`);
    } else if (props.mode === "update") {
        if (!modelObj.value.code) {
            console.log("Updating task failed. Task code is empty");
            return;
        }
        console.log("Updating task:", reqBody.code);
        taskService.updateTask(modelObj.value.code, reqBody);
    }
};
</script>

<template>
    <Fluid>
        <div class="w-full">
            <div class=" flex flex-col gap-4 w-full">
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="parent">Parent</label>
                        <InputText id="parent" type="text" v-model="modelObj.parentCode" />
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
                                default-value="1"></Select>
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
