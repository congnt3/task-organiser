<script setup>
import { TaskService } from "@/service/TaskService";
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { onMounted, ref, watch } from "vue";
import TaskCrud from "@/components/task/TaskCrud.vue";
import { useRoute } from "vue-router";
import { STATUS_COMPLETED, STATUS_IN_PROGRESS, STATUS_NEW } from "@/config/task.constants";
import { color } from "chart.js/helpers";

const route = useRoute();

// Create a reactive ref to store the query parameter value
const taskModel = ref({});
const taskDeleting = ref({});
const taskCrudMode = ref("create");
const taskService = new TaskService();
// Set the value initially
onMounted(() => {
    taskModel.value.parentTask = route.query.root || undefined;

    loading.value = true;

    setTimeout(() => {
        loading.value = false;
        nodes.value = loadNodes(0, rows.value);
        totalRecords.value = 100;
    }, 1000);
});

// Watch for URL query parameter changes
watch(
    () => route.query.root,
    (newValue) => {
        taskModel.value.parentTask = newValue || null;
    }
);

const toast = useToast();
const taskCrudDialog = ref(false);
const deleteTaskDialog = ref(false);

// const filters = ref({
//     global: {value: null, matchMode: FilterMatchMode.CONTAINS}
// });
const submitted = ref(false);

function openNew() {
    taskCrudMode.value = "create";
    taskModel.value = { parentCode: route.query.root };
    taskCrudDialog.value = true;
}

function hideDialog() {
    taskCrudDialog.value = false;
    submitted.value = false;
}

async function editTask(task) {
    taskCrudMode.value = "update";
    let theTask = await taskService.getTask(task.code);
    taskModel.value = {
        ...theTask
    };
    taskModel.value.status = { Name: theTask.status, Code: theTask.status };

    taskCrudDialog.value = true;
}

function confirmDeleteTask(task) {
    taskDeleting.value = task;
    deleteTaskDialog.value = true;
}

function deleteTask() {
    try {
        taskService.deleteTask(taskDeleting.value.code);
    } catch (error) {
        console.error("Error deleting task:", error);
        toast.add({ severity: "error", summary: "Error", detail: "Failed to delete task", life: 3000 });
    }
    taskDeleting.value = {};
    deleteTaskDialog.value = false;
    toast.add({ severity: "success", summary: "Successful", detail: "Product Deleted", life: 3000 });
}

function createChildTask(parentTask) {
    if (!parentTask) {
        return;
    }

    taskModel.value = { parentCode: parentTask.code };
    taskCrudMode.value = "create";
    taskCrudDialog.value = true;
}

function createId() {
    let id = "";
    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (var i = 0; i < 5; i++) {
        id += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return id;
}

// TreeTable
const nodes = ref();
const rows = ref(10);
const loading = ref(false);
const totalRecords = ref(0);
const onExpand = async (node) => {
    // loading.value = true;
    await doLoadChildren(node);
    redrawTree();
    // loading.value = false;
};

const updateTaskStatus = async (node, status) => {
    try {
        await taskService.updateTaskStatus(node.data.code, status);
    } catch (error) {
        console.error("Error updating task status:", error);
        toast.add({ severity: "error", summary: "Error", detail: "Failed to update task status", life: 3000 });
    }
    await refreshNode(node);
};

const refreshNode = async (node) => {
    await reloadANode(node);
    if (node.data.children) {
        await doLoadChildren(node, true);
    }

    redrawTree();
};

const onPage = (event) => {
    loading.value = true;

    //imitate delay of a backend call
    setTimeout(() => {
        loading.value = false;
        loadNodes(event.first, rows.value);
    }, 1000);
};

const loadNodes = (first, rows) => {
    let loadingNodes = [];

    taskService.getAllTasks(taskModel.value.parentTask)
        .then((tasks) => {
            for (let i = 0; i < tasks.length; i++) {
                let node = tasks[i];
                loadingNodes.push({
                    key: node.code,
                    data: {
                        code: node.code,
                        name: node.name,
                        status: node.status
                    },
                    leaf: false
                });
            }
            nodes.value = loadingNodes;
        });
};

/*
Reload the node and its children
 */
async function reloadANode(node) {
    try {
        // Fetch child tasks using the TaskService
        const task = await taskService.getTask(node.data.code);

        node.data = task;

    } catch (error) {
        console.error("Error loading task:", error);
        toast.add({ severity: "error", summary: "Error", detail: "Failed to load task", life: 3000 });
    }
}

async function doLoadChildren(node, forceReload = false) {
    if (node.children && !forceReload) {
        return;
    }

    try {
        // Fetch child tasks using the TaskService
        const childTasks = await taskService.getAllTasks(node.data.code);

        let lazyNode = { ...node };
        lazyNode.children = childTasks.map(task => ({
            key: task.code,
            data: {
                code: task.code,
                name: task.name,
                status: task.status
            },
            leaf: false
        }));

        node.children = lazyNode.children;

    } catch (error) {
        console.error("Error loading child tasks:", error);
        toast.add({ severity: "error", summary: "Error", detail: "Failed to load child tasks", life: 3000 });
    }
}

function redrawTree() {
    let newNodes = nodes.value;
    nodes.value = newNodes;
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="New" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                </template>

                <template #end>
                    <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportCSV($event)" />
                </template>
            </Toolbar>
            <TreeTable :value="nodes" :lazy="true" :paginator="false" :rows="rows" :loading="loading"
                       @nodeExpand="onExpand" tableStyle="min-width: 50rem">
                <Column field="code" header="Code" :expander="true"></Column>
                <Column field="name" header="Name"></Column>
                <Column field="status" header="Status">
                    <template #body="slotProps">
                        <p v-if="slotProps.node.data.status == STATUS_COMPLETED" style="color: green">
                            {{ slotProps.node.data.status }}</p>
                        <p v-if="slotProps.node.data.status == STATUS_IN_PROGRESS" style="color: orange">
                            {{ slotProps.node.data.status }}</p>
                        <p v-if="slotProps.node.data.status == STATUS_NEW" style="color: blue;">
                            {{ slotProps.node.data.status }}</p>
                    </template>
                </Column>
                <Column :exportable="false" style="min-width: 12rem" header="Set Status">
                    <template #body="slotProps">
                        <Button icon="pi pi-play" outlined rounded class="mr-2"
                                @click="updateTaskStatus(slotProps.node, STATUS_IN_PROGRESS)"
                                :disabled="!slotProps.node.data"
                                tooltip="Mark as In Progress" />
                        <Button icon="pi pi-check" outlined rounded class="mr-2"
                                @click="updateTaskStatus(slotProps.node, STATUS_COMPLETED)"
                                :disabled="!slotProps.node.data"
                                tooltip="Mark as Completed" />
                    </template>

                </Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-refresh" outlined rounded class="mr-2"
                                @click="refreshNode(slotProps.node)"
                                :disabled="!slotProps.node.data"
                                tooltip="Reload the node data" />
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2"
                                @click="editTask(slotProps.node.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger"
                                @click="confirmDeleteTask(slotProps.node.data)" />
                        <Button icon="pi pi-plus" outlined rounded class="mr-2"
                                @click="createChildTask(slotProps.node.data)"
                                :disabled="!slotProps.node.data"
                                tooltip="Create Child Task" />
                    </template>
                </Column>
            </TreeTable>
        </div>

        <Dialog v-model:visible="taskCrudDialog" :style="{ width: '450px', 'text-transform': 'capitalize' }"
                v-bind:header="taskCrudMode.concat(' Task Details')" :modal="true">
            <TaskCrud v-model="taskModel" v-bind:mode="taskCrudMode" />
        </Dialog>

        <Dialog v-model:visible="deleteTaskDialog" :style="{ width: '450px' }" header="Confirm deletion" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="taskDeleting">
                    Are you sure you want to delete <b>{{ taskDeleting.code }}</b> - {{ taskDeleting.name }}?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteTaskDialog = false" />
                <Button label="Yes" icon="pi pi-check" @click="deleteTask" />
            </template>
        </Dialog>
    </div>
</template>
