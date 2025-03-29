<script setup lang="ts">

import { ref } from "vue";
import { TaskService } from "@/service/TaskService.ts";
import { Task } from "@/types/task.types.ts";
import {
    STATUS_COMPLETED,
    STATUS_IN_PROGRESS,
    STATUS_NEW,
    TYPE_BUSINESS_OUTCOME, TYPE_EPIC, TYPE_FEATURE,
    TYPE_TASK_BW,
    TYPE_TASK_EW
} from "@/config/task.constants.ts";

const taskService = new TaskService();
const message = ref({visible: false, severity: "info", messageText: ""});

// Define the props with defaults
let modelObj = defineModel<Task>();
let mode = defineModel("mode");
let searchBoxModel = ref({searchText: "", result: []});
const dropdownStates = ref([
    {name: STATUS_NEW, code: STATUS_NEW},
    {name: STATUS_IN_PROGRESS, code: STATUS_IN_PROGRESS},
    {name: STATUS_COMPLETED, code: STATUS_COMPLETED}
]);
const dropdownTypes = ref([
    { name: TYPE_BUSINESS_OUTCOME, code: "BO" },
    { name: TYPE_EPIC, code: "E" },
    { name: TYPE_FEATURE, code: "F" },
    { name: TYPE_TASK_BW, code: "TS" },
    { name: TYPE_TASK_EW, code: "ES" }
]);
const onSaveClick = async () => {
    try {
        if (!modelObj.value) {
            showMessage("info", "Nothing saved");
            return;
        }

        if (mode.value === "create") {
            let updateResult = await taskService.createTask(modelObj.value);
            if (updateResult) {
                modelObj.value = updateResult;
                mode.value = "update";
                console.log(`Creating child task for parent: ${modelObj.value.parentCode}`);
                showMessage("success", "Task saved");
            } else throw new Error(`Failed to save record: "${modelObj.value.code || ""}"`);
        } else if (mode.value === "update") {
            if (!modelObj.value.code) {
                console.log("Updating task failed. Task code is empty");
                return;
            }

            let updateResult = await taskService.updateTask(modelObj.value.code, modelObj.value);
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

const onGenerateNewTaskId = () => {
    modelObj.value.code = (modelObj.value?.type ?? "TS") + Date.now();
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
                        <InputText id="parent" type="text" v-model="modelObj.parentCode"/>
                    </div>
                    <div class="flex flex-wrap gap-2 w-2/12">
                        <label for="type">Type</label>
                        <Select id="type" v-model="modelObj.type" :options="dropdownTypes"
                                optionLabel="name"
                                optionValue="code"
                                default-value="TS"
                                placeholder="Select One" class="w-full"></Select>
                    </div>
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="taskid">Task ID</label>
                        <InputGroup >
                            <InputText id="taskid" type="text" v-model="modelObj.code" :readonly="mode.toLowerCase() != 'create'"/>
                            <Button label="Gen" @click="onGenerateNewTaskId" v-if="mode.toLowerCase() == 'create'"/>
                        </InputGroup>
                    </div>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="name">Name</label>
                        <InputText id="name" type="text" v-model="modelObj.name"/>
                    </div>
                </div>
                <div class="flex flex-wrap">
                    <label for="description">Description</label>
                    <Textarea id="description" rows="4" v-model="modelObj.description"/>
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
                        <label for="dueDate">Due Date</label>
                        <div class="flex flex-col gap-1 w-full">
                            <DatePicker input-id="dueDate" v-model="modelObj.dueDate" dateFormat="dd/mm/yy" showIcon
                                        fluid iconDisplay="input"/>
                        </div>
                    </div>

                </div>

                <Fieldset legend="Planning">
                    <div class="flex flex-col md:flex-row gap-4">
                        <div class="flex flex-wrap gap-2 w-full">
                            <label for="estimated">Estimated (days)</label>
                            <div class="flex flex-col gap-1 w-full">
                                <InputNumber v-model="modelObj.estimatedEffortDays" inputId="estimated" :min="0" :max="5"
                                             fluid/>

                            </div>
                        </div>
                        <div class="flex flex-wrap gap-2 w-full">
                            <label for="plannedStartDate">Start Date</label>
                            <div class="flex flex-col gap-1 w-full">
                                <DatePicker input-id="plannedStartDate" v-model="modelObj.plannedStartDate"
                                            dateFormat="dd/mm/yy"
                                            showIcon
                                            fluid iconDisplay="input"/>
                            </div>
                        </div>

                        <div class="flex flex-wrap gap-2 w-full">
                            <label for="plannedCompletionDate">Completion Date</label>
                            <div class="flex flex-col gap-1 w-full">
                                <DatePicker input-id="plannedCompletionDate" v-model="modelObj.plannedCompletionDate"
                                            dateFormat="dd/mm/yy" showIcon
                                            fluid iconDisplay="input"/>
                            </div>
                        </div>
                    </div>
                </Fieldset>
                <br/>
                <div class="flex justify-between" v-if="mode.toLowerCase() != 'create'">
                    <p class="font-bold">Predecessors</p>
                </div>
                <div class="flex flex-col gap-4" style="background-color: #dddddd"
                     v-if="mode.toLowerCase() != 'create'">
                    <InputGroup>
                        <InputText id="searchText" type="text" placeholder="Task to add dependency on ..."
                                   v-model="searchBoxModel.searchText"/>
                        <Button label="Search" @click="startSearch"/>
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
                <br v-if="searchBoxModel.result.length > 0"/>
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
                                        @click="removeDependency(data)"/>
                            </template>
                        </Column>
                    </DataTable>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <Button label="Save" icon="pi pi-check" @click="onSaveClick"/>
                    <Button label="Save As New" v-if="mode!='create'" icon="pi pi-check" @click=""/>
                </div>
            </div>
        </div>

    </Fluid>
</template>
