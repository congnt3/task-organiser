<script lang="ts" setup>
import { Handle, Position } from "@vue-flow/core";
import { ref } from "vue";
import { Node } from "@vue-flow/core";

let node = defineProps<Node>();
const counter = ref(0);
</script>

<template>
    <Handle type="target" :position="Position.Top" />
    <Handle type="source" :position="Position.Bottom" />
    <Card style="width: 100%; overflow: hidden">
        <template #header>
            <p></p>
        </template>
        <template #title>{{ node.data.code }}</template>
        <template #subtitle>{{ node.data.name }}</template>
        <template #content>
            {{ node.data.status }}
        </template>
        <template #footer></template>
    </Card>
</template>

<style>
.vue-flow__node {
    border: none !important;
    background-color: transparent !important;
}
.vue-flow__node-default {
    border: none !important;
    background-color: transparent;
}

.custom-node {
    min-width: 100px;
    gap: 4px;
    padding: 8px;
    background: white;
    border: 1px solid black;
    border-radius: 4px;
}

.increment {
    border-radius: 4px;
    background: #42b983;
    font-size: 10px;
    color: #fff;
    cursor: pointer;
    border: none;
}

.increment:hover {
    box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
}

.counter {
    margin-top: 8px;
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 4px;
}

.count {
    font-size: 6px;
    color: #ff0072;
    border: 1px solid rgba(0, 0, 0, 0.3);
    border-radius: 8px;
}
</style>
