<script setup>
import {onMounted, ref, watch} from 'vue';
import TaskCrud from "@/components/task/TaskCrud.vue";
import { useRoute } from 'vue-router';

const route = useRoute()

// Create a reactive ref to store the query parameter value
const parentTask = ref('')

// Set the value initially
onMounted(() => {
    parentTask.value = route.query.parentTask || null
})

// Watch for URL query parameter changes
watch(
    () => route.query.parentTask,
    (newValue) => {
        parentTask.value = newValue || null
    }
)

const dropdownItems = ref([
    {name: 'Option 1', code: 'Option 1'},
    {name: 'Option 2', code: 'Option 2'},
    {name: 'Option 3', code: 'Option 3'}
]);

const dropdownItem = ref(null);
</script>

<template>
    <TaskCrud :parentTask="parentTask"/>
</template>
