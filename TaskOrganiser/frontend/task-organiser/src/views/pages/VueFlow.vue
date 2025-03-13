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

taskService.getAllTasksAsGraph("US123456")
    .then(graph => {
        nodes.value = graph.nodes;
        edges.value = graph.edges;
    });
// const edges = ref<Edge[]>([]);
onConnect((params) => {
    addEdges([params]);


});
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
            :max-zoom="4">
            <Background pattern-color="#aaa" :gap="8" />

            <MiniMap />

            <Controls />

            <template #node-custom="nodeProps">
                <CustomNode v-bind="nodeProps" />
            </template>

            <template #edge-custom="edgeProps">
                <CustomEdge v-bind="edgeProps" />
            </template>
        </VueFlow>
    </div>
</template>
<style>
.layout-main {
    width: 2100px;
}
</style>
