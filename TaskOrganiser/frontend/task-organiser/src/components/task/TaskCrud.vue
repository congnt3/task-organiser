<script setup lang="ts">
import { ref } from "vue";
import { TaskService } from "@/service/TaskService.ts";
import { Task } from "@/types/task.types.ts";
import { STATUS_COMPLETED, STATUS_IN_PROGRESS, STATUS_NEW } from "@/config/task.constants.ts";

const taskService = new TaskService();
const message = ref({ visible: false, severity: "info", messageText: "" });

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

const onSaveClick = async () => {
    try {
        if (!modelObj.value) {
            showMessage("info", "Nothing saved");
            return;
        }

        let reqBody = {
            "parentCode": modelObj.value.parentCode,
            "code": modelObj.value.code,
            "name": modelObj.value.name,
            "description": modelObj.value.description,
            "status": modelObj.value.status,
            "deadline": modelObj.value.deadline
        };

        if (props.mode === "create") {
            let updateResult = await taskService.createTask(reqBody);
            if (updateResult) {
                modelObj.value = { parentCode: modelObj.value.parentCode };
                console.log(`Creating child task for parent: ${reqBody.parentCode}`);
                showMessage("success", "Task saved");
            } else throw new Error(`Failed to save record: "${modelObj.value.code || ""}"`);
        } else if (props.mode === "update") {
            if (!modelObj.value.code) {
                console.log("Updating task failed. Task code is empty");
                return;
            }

            let updateResult = await taskService.updateTask(modelObj.value.code, reqBody);
            if (updateResult) {
                showMessage("success", "Task saved");
            } else throw new Error(`Failed to save record: "${modelObj.value.code || ""}"`);
        }
    } catch (ex) {
        showMessage("warn", `${ex}`);
    }
};

const showMessage = (severity: string, messageText: string) => {
    message.value = {
        visible: true,
        severity: severity,
        messageText: messageText
    };
};
</script>

<template>
    <Fluid>
        <div class="w-full">
            <div class=" flex flex-col gap-4 w-full">
                <Message :severity="message.severity" v-if="message.visible">{{ message.messageText }}</Message>
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
                                optionValue="code"
                                placeholder="Select One" class="w-full"></Select>
                    </div>

                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="deadline">Deadline</label>
                        <InputText id="deadline" type="text" v-model="modelObj.deadline" />
                    </div>

                </div>
                <br/>
                <div class="flex justify-between">
                    <p class="font-bold">Predecessors</p>
                </div>
                <div class="flex flex-col gap-4">
                    <DataTable
                        :value="modelObj?.dependsOn"
                        :rows="10"
                        dataKey="code"
                        :rowHover="true"
                        filterDisplay="menu">

                        <template #empty> No dependency found.</template>
                        <template #loading> Loading customers data. Please wait.</template>
                        <Column field="code" header="Code"></Column>
                        <Column field="name" header="Name"></Column>
                        <Column field="status" header="Status"></Column>
                    </DataTable>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <Button label="Save" icon="pi pi-check" @click="onSaveClick" />
                    <Button label="Save As New" v-if="props.mode!='create'" icon="pi pi-check" @click="" />
                </div>
            </div>
        </div>

    </Fluid>
</template>
