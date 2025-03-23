<script lang="ts" setup>
import { ref } from "vue";
import { Background } from "@vue-flow/background";
import { Controls } from "@vue-flow/controls";
import { MiniMap } from "@vue-flow/minimap";
import { VueFlow, useVueFlow, type Node, type Edge } from "@vue-flow/core";
import CustomNode from "./components/SpecialNode.vue";
import CustomEdge from "./components/SpecialEdge.vue";
/* these are necessary styles for vue flow */
import "@vue-flow/core/dist/style.css";

/* this contains the default theme, these are optional styles */
import "@vue-flow/core/dist/theme-default.css";
import { TaskService } from "@/service/TaskService.ts";

const { onConnect, addEdges } = useVueFlow();
const taskService = new TaskService();

const nodes = ref<Node[]>([]);
// const nodes = ref<Node[]>([]);
const edges = ref<Edge[]>([]);
const drawerModel = ref({ visible: false, data: {} });

taskService.getAllTasksAsGraph("root")
    .then(graph => {
        nodes.value = graph.nodes;
        edges.value = graph.edges;
    });
// const edges = ref<Edge[]>([]);
onConnect((params) => {
    addEdges([params]);
});

// Node click event handler
function onNodeClick({ event, node }) {
    drawerModel.value = { visible: true, data: node.data };
}
</script>

<template>
    <div class="flex items-center justify-center min-h-screen overflow-hidden" style="height: 100vh">
        <VueFlow
            v-model:nodes="nodes"
            v-model:edges="edges"
            fit-view-on-init
            class="vue-flow-basic-example"
            :default-zoom="0.2"
            :min-zoom="0.2"
            :max-zoom="4"
            @node-click="onNodeClick">
            <Background pattern-color="#aaa" :gap="8" />
            <Controls />
            <template #node-default="nodeProps">
                <CustomNode v-bind="nodeProps" />
            </template>
            <template #edge-custom="edgeProps">
                <CustomEdge v-bind="edgeProps" />
            </template>
        </VueFlow>
        <Drawer v-model:visible="drawerModel.visible" header="Task Details" position="right" class="!w-full md:!w-80 lg:!w-[30rem]">
            <div>
                <DataView :value="Object.keys(drawerModel.data)">
                    <template #list="slotProps">
                        <div class="flex flex-col">
                            <div v-for="(item, index) in slotProps.items" :key="index">
                                <div class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                                     :class="{ 'border-t border-surface-200 dark:border-surface-700': index !== 0 }">
                                    <div>
                                        <span
                                            class="font-medium text-surface-500 dark:text-surface-400 text-sm capitalize"> {{ item
                                            }}</span>
                                        <div class="text-lg font-medium mt-2">{{ drawerModel.data[item] }}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                </DataView>
            </div>
        </Drawer>
    </div>
</template>
<style>
.layout-main {
    width: 2100px;
}
</style>
