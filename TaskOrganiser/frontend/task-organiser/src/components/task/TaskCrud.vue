<script setup lang="ts">
import {ref, withDefaults} from 'vue'

// Define props with TypeScript interface
interface Props {
    mode: string;
    parentTask: string;
    code?: string;
    name?: string;
    description?: string;
    status?: string;
    deadline?: string;
}

// Define the props with defaults
let props = defineModel<Props>();

let model = ref({
    mode: props.value.mode,
    parentTask: props.value.parentTask,
    code: props.value.code,
    name: props.value.name,
    description: props.value.description,
    status: props.value.status,
    deadline: props.value.deadline
})

const dropdownStates = ref([
    {name: 'New', code: 'New'},
    {name: 'In Progress', code: 'In Progress'},
    {name: 'Completed', code: 'Completed'}
]);
</script>

<template>
    <Fluid>
        <div class="w-full">
            <div class=" flex flex-col gap-4 w-full">
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="parent">Parent</label>
                        <InputText id="parent" type="text" :value="props.parentTask"/>
                    </div>
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="taskid">Task ID</label>
                        <InputText id="taskid" type="text"/>
                    </div>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="name">Name</label>
                        <InputText id="name" type="text" v-model="props.name"/>
                    </div>
                </div>

                <div class="flex flex-wrap">
                    <label for="description">Description</label>
                    <Textarea id="description" rows="4" v-model="props.description"/>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="status">Status</label>
                        <Select id="status" v-model="props.status" :options="dropdownStates"
                                optionLabel="name"
                                placeholder="Select One" class="w-full"
                                default-value="New"></Select>
                    </div>

                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="deadline">Deadline</label>
                        <InputText id="deadline" type="text" v-model="props.deadline"/>
                    </div>

                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <Button label="Save" icon="pi pi-check" @click=""/>
                    <Button label="Save As New" icon="pi pi-check" @click=""/>
                </div>
            </div>
        </div>
    </Fluid>
</template>
