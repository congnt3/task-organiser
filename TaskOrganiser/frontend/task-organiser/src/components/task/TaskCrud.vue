<script setup lang="ts">
import { ref } from "vue";
import { TaskService } from "@/service/TaskService.ts";
import { Task } from "@/types/task.types.ts";
import { STATUS_COMPLETED, STATUS_IN_PROGRESS, STATUS_NEW } from "@/config/task.constants.ts";

const taskService = new TaskService();
const message = ref({ visible: false, severity: "info", messageText: "" });

// Define the props with defaults
let modelObj = defineModel<Task>();
let mode = defineModel("mode");
let searchBoxModel = ref({ searchText: "", result: [] });
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

        if (mode.value === "create") {
            let updateResult = await taskService.createTask(reqBody);
            if (updateResult) {
                modelObj.value = updateResult;
                mode.value = "update";
                console.log(`Creating child task for parent: ${reqBody.parentCode}`);
                showMessage("success", "Task saved");
            } else throw new Error(`Failed to save record: "${modelObj.value.code || ""}"`);
        } else if (mode.value === "update") {
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

const removeDependency = async (data) => {
    console.log(data);
    if (!(modelObj.value?.code && data.code)) {
        showMessage("warn", "No data available.");
        return;
    }

    await taskService.removeDependency(modelObj.value?.code, data.code);
    let taskResult = await taskService.getTask(modelObj.value.code);
    modelObj.value = taskResult || undefined;
};

const startSearch = async () => {
    let searchResult = await taskService.getTask(searchBoxModel.value.searchText);
    searchBoxModel.value.result = [];

    if (searchResult) {
        searchBoxModel.value.result = [searchResult];
        searchBoxModel.value.searchText = "";

    } else {
        showMessage("warn", "No search result found.");
    }
};
const addToDependency = async (data: Task) => {
    if (!data.code) {
        showMessage("warn", "No data available.");
        return;
    }

    let taskResult = await taskService.addDependencies(modelObj.value?.code, data.code);
    if (taskResult) {
        searchBoxModel.value.result = [];
        modelObj.value = taskResult || undefined;

    } else {
        showMessage("warn", "Failed to add dependency.");
    }
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
                <br />
                <div class="flex justify-between" v-if="mode.toLowerCase() != 'create'">
                    <p class="font-bold">Predecessors</p>
                </div>
                <div class="flex flex-col gap-4" style="background-color: #dddddd"
                     v-if="mode.toLowerCase() != 'create'">
                    <InputGroup>
                        <InputText id="searchText" type="text" placeholder="Task to add dependency on ..." v-model="searchBoxModel.searchText" />
                        <Button label="Search" @click="startSearch" />
                    </InputGroup>
                </div>
                <div class="flex flex-col gap-4" v-if="mode.toLowerCase() != 'create'">
                    <DataTable
                        :value="searchBoxModel.result"
                        v-if="searchBoxModel.result.length > 0"
                        :rows="10"
                        dataKey="code">
                        <template #empty> Nothing here.</template>
                        <Column field="code" header="Code"></Column>
                        <Column field="name" header="Name"></Column>
                        <Column field="status" header="Status"></Column>
                        <Column :exportable="false" style="min-width: 12rem">
                            <template #body="{ data }">
                                <Button icon="pi pi-arrow-right" outlined rounded severity="success"
                                        :disabled="(modelObj?.dependsOn?.map(n => n.code).includes(data.code) || data.code == modelObj?.code)"
                                        @click="addToDependency(data)" label="Add">
                                </Button>

                            </template>
                        </Column>
                    </DataTable>
                </div>
                <br  v-if="searchBoxModel.result.length > 0"/>
                <div class="flex flex-col gap-4" v-if="mode.toLowerCase() != 'create'">
                    <DataTable
                        :value="modelObj?.dependsOn"
                        :rows="10"
                        dataKey="code"
                        :rowHover="true"
                        filterDisplay="menu">
                        <template #empty> Nothing found.</template>
                        <template #loading> Loading customers data. Please wait.</template>
                        <Column field="code" header="Code"></Column>
                        <Column field="name" header="Name"></Column>
                        <Column field="status" header="Status"></Column>
                        <Column :exportable="false" style="min-width: 12rem">
                            <template #body="{ data }">
                                <Button icon="pi pi-trash" outlined rounded severity="danger"
                                        @click="removeDependency(data)" />
                            </template>
                        </Column>
                    </DataTable>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <Button label="Save" icon="pi pi-check" @click="onSaveClick" />
                    <Button label="Save As New" v-if="mode!='create'" icon="pi pi-check" @click="" />
                </div>
            </div>
        </div>

    </Fluid>
</template>
