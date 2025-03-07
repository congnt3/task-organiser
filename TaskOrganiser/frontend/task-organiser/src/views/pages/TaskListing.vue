<script setup>
import { TaskService } from "@/service/TaskService";
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { onMounted, ref, watch } from "vue";
import TaskCrud from "@/components/task/TaskCrud.vue";
import { useRoute } from "vue-router";

const route = useRoute();

// Create a reactive ref to store the query parameter value
const taskModel = ref({});
const taskCrudMode = ref("create");
const taskService = new TaskService();
// Set the value initially
onMounted(() => {
    taskModel.value.parentTask = route.query.parentTask || undefined;

    taskService.getAllTasks(taskModel.value.parentTask).then((data) => (products.value = data));

    loading.value = true;

    setTimeout(() => {
        loading.value = false;
        nodes.value = loadNodes(0, rows.value);
        totalRecords.value = 100;
    }, 1000);
});

// Watch for URL query parameter changes
watch(
    () => route.query.parentTask,
    (newValue) => {
        taskModel.value.parentTask = newValue || null;
    }
);

const toast = useToast();
const dt = ref();
const products = ref();
const taskCrudDialog = ref(false);
const deleteProductDialog = ref(false);
const deleteProductsDialog = ref(false);
const product = ref({});
const selectedProducts = ref();
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS }
});
const submitted = ref(false);

function openNew() {
    product.value = {};
    submitted.value = false;
    taskCrudDialog.value = true;
}

function hideDialog() {
    taskCrudDialog.value = false;
    submitted.value = false;
}

function saveProduct() {
    submitted.value = true;

    if (product?.value.name?.trim()) {
        if (product.value.id) {
            product.value.inventoryStatus = product.value.inventoryStatus.value ? product.value.inventoryStatus.value : product.value.inventoryStatus;
            products.value[findIndexById(product.value.id)] = product.value;
            toast.add({ severity: "success", summary: "Successful", detail: "Product Updated", life: 3000 });
        } else {
            product.value.id = createId();
            product.value.code = createId();
            product.value.image = "product-placeholder.svg";
            product.value.inventoryStatus = product.value.inventoryStatus ? product.value.inventoryStatus.value : "INSTOCK";
            products.value.push(product.value);
            toast.add({ severity: "success", summary: "Successful", detail: "Product Created", life: 3000 });
        }

        taskCrudDialog.value = false;
        product.value = {};
    }
}

function editProduct(task) {
    product.value = { ...task };
    taskCrudMode.value = "edit";
    taskModel.value = task;
    taskCrudDialog.value = true;

}

function confirmDeleteProduct(prod) {
    product.value = prod;
    deleteProductDialog.value = true;
}

function deleteProduct() {
    products.value = products.value.filter((val) => val.id !== product.value.id);
    deleteProductDialog.value = false;
    product.value = {};
    toast.add({ severity: "success", summary: "Successful", detail: "Product Deleted", life: 3000 });
}

function createChildTask(parentTask) {
    if (!parentTask) {
        return;
    }

    taskModel.value = { parentTask: parentTask.code };
    taskCrudMode.value = "create";
    taskCrudDialog.value = true;

    // Here you can implement the logic to create a child task
    // For example:
    // 1. Open a dialog/modal
    // 2. Get the new task details
    // 3. Make API call to create child task
    // 4. Refresh the tree table
    console.log("Creating child task for parent:", parentTask);
}

function findIndexById(id) {
    let index = -1;
    for (let i = 0; i < products.value.length; i++) {
        if (products.value[i].id === id) {
            index = i;
            break;
        }
    }

    return index;
}

function createId() {
    let id = "";
    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (var i = 0; i < 5; i++) {
        id += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return id;
}

function exportCSV() {
    dt.value.exportCSV();
}

function confirmDeleteSelected() {
    deleteProductsDialog.value = true;
}

function deleteSelectedProducts() {
    products.value = products.value.filter((val) => !selectedProducts.value.includes(val));
    deleteProductsDialog.value = false;
    selectedProducts.value = null;
    toast.add({ severity: "success", summary: "Successful", detail: "Products Deleted", life: 3000 });
}

// TreeTable
const nodes = ref();
const rows = ref(10);
const loading = ref(false);
const totalRecords = ref(0);
const onExpand = async (node) => {
    await doExpandChildren(node);
};

async function doExpandChildren(node, forceReload = false) {
    if (node.children && !forceReload) {
        return;
    }

    loading.value = true;
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

        let newNodes = nodes.value;
        nodes.value = newNodes;
    } catch (error) {
        console.error("Error loading child tasks:", error);
        toast.add({ severity: "error", summary: "Error", detail: "Failed to load child tasks", life: 3000 });
    } finally {
        loading.value = false;
    }
}

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
        .then((data) => (products.value = data))
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

</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="New" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                    <Button label="Delete" icon="pi pi-trash" severity="secondary" @click="confirmDeleteSelected"
                            :disabled="!selectedProducts || !selectedProducts.length" />
                </template>

                <template #end>
                    <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportCSV($event)" />
                </template>
            </Toolbar>
            <TreeTable :value="nodes" :lazy="true" :paginator="true" :rows="rows" :loading="loading"
                       @nodeExpand="onExpand" @page="onPage" :totalRecords="totalRecords" tableStyle="min-width: 50rem">
                <Column field="code" header="Code" :expander="true"></Column>
                <Column field="name" header="Name"></Column>
                <Column field="status" header="Status">
                </Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2"
                                @click="editProduct(slotProps.node.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger"
                                @click="confirmDeleteProduct(slotProps.data)" />
                        <Button icon="pi pi-plus" outlined rounded class="mr-2"
                                @click="createChildTask(slotProps.node.data)"
                                :disabled="!slotProps.node.data"
                                tooltip="Create Child Task" />
                    </template>
                </Column>
            </TreeTable>
        </div>

        <Dialog v-model:visible="taskCrudDialog" :style="{ width: '450px' }" header="Product Details" :modal="true">
            <TaskCrud v-model="taskModel" v-bind:mode="taskCrudMode" />
        </Dialog>

        <Dialog v-model:visible="deleteProductDialog" :style="{ width: '450px' }" header="Confirm" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="product"
                >Are you sure you want to delete <b>{{ product.name }}</b
                >?</span
                >
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteProductDialog = false" />
                <Button label="Yes" icon="pi pi-check" @click="deleteProduct" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProductsDialog" :style="{ width: '450px' }" header="Confirm" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="product">Are you sure you want to delete the selected products?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteProductsDialog = false" />
                <Button label="Yes" icon="pi pi-check" text @click="deleteSelectedProducts" />
            </template>
        </Dialog>
    </div>
</template>
