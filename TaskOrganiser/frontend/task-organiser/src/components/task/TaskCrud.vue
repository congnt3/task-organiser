<script setup lang="ts">
import {ref, defineProps, withDefaults} from 'vue'

// Define props with TypeScript interface
interface Props {
    parentTask: string;
    code?: string;
    name?: string;
    description?: string;
    status?: string;
    deadline?: string;
}

// Define the props with defaults
const model = withDefaults(defineProps<Props>(), {
    parentTask: "/"
})

const dropdownStates = ref([
    {name: 'New', code: 'New'},
    {name: 'In Progress', code: 'In Progress'},
    {name: 'Completed', code: 'Completed'}
]);

const dropdownState = ref(null);

</script>

<template>
    <Fluid>
        <div class="w-full">
            <div class=" flex flex-col gap-4 w-full">
                <div class="flex flex-col lg:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="parent">Parent</label>
                        <InputText id="parent" type="text" :value="model.parentTask"/>

                    </div>
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="taskid">Task ID</label>
                        <InputText id="taskid" type="text"/>
                    </div>
                </div>
                <div class="flex flex-col lg:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="name">Name</label>
                        <InputText id="name" type="text"/>
                    </div>
                </div>

                <div class="flex flex-wrap">
                    <label for="description">Description</label>
                    <Textarea id="description" rows="4"/>
                </div>

                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="status">Status</label>
                        <Select id="status" v-model="dropdownState" :options="dropdownStates"
                                optionLabel="name"
                                placeholder="Select One" class="w-full"
                                default-value="New"></Select>
                    </div>
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="deadline">Deadline</label>
                        <InputText id="deadline" type="text"/>
                    </div>
                </div>
            </div>
        </div>
    </Fluid>
</template>
