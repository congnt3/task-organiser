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

const taskService = new TaskService();
// Set the value initially
onMounted(() => {
    taskModel.value.parentTask = route.query.parentTask || undefined;

    taskService.getAllTasks(taskModel.value.parentTask).then((data) => (products.value = data));

    loading.value = true;

    setTimeout(() => {
        loading.value = false;
        nodes.value = loadNodes(0, rows.value);
        totalRecords.value = 1000;
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
const productDialog = ref(false);
const deleteProductDialog = ref(false);
const deleteProductsDialog = ref(false);
const product = ref({});
const selectedProducts = ref();
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS }
});
const submitted = ref(false);
const statuses = ref([
    { label: "INSTOCK", value: "instock" },
    { label: "LOWSTOCK", value: "lowstock" },
    { label: "OUTOFSTOCK", value: "outofstock" }
]);

function formatCurrency(value) {
    if (value) return value.toLocaleString("en-US", { style: "currency", currency: "USD" });
    return;
}

function openNew() {
    product.value = {};
    submitted.value = false;
    productDialog.value = true;
}

function hideDialog() {
    productDialog.value = false;
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

        productDialog.value = false;
        product.value = {};
    }
}

function editProduct(prod) {
    product.value = { ...prod };
    productDialog.value = true;
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
const onExpand = (node) => {
    if (!node.children) {
        loading.value = true;

        setTimeout(() => {
            let lazyNode = { ...node };

            lazyNode.children = [
                {
                    data: {
                        code: lazyNode.data.code + " - 0",
                        name: lazyNode.data.name + " - 0",
                        status: Math.floor(Math.random() * 1000) + 1 + "kb"
                    }
                },
                {
                    data: {
                        code: lazyNode.data.code + " - 1",
                        name: lazyNode.data.name + " - 1",
                        status: Math.floor(Math.random() * 1000) + 1 + "kb"
                    }
                }
            ];

            let newNodes = nodes.value.map(n => {
                if (n.key === node.key) {
                    n = lazyNode;
                }

                return n;
            });

            loading.value = false;
            nodes.value = newNodes;
        }, 250);
    }
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

    let downloadedTasks = taskService.getAllTasks(taskModel.value.parentTask).then((data) => (products.value = data))
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

        <Dialog v-model:visible="productDialog" :style="{ width: '450px' }" header="Product Details" :modal="true">
            <TaskCrud v-model="taskModel" />
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
