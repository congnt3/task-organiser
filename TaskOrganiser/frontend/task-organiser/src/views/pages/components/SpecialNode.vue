<script lang="ts" setup>
import { Handle, Position } from "@vue-flow/core";
import { ref } from "vue";
import { Node } from "@vue-flow/core";
import { STATUS_COMPLETED, STATUS_IN_PROGRESS, STATUS_NEW } from "@/config/task.constants.ts";

let node = defineProps<Node>();
function tagValue(task) {
    switch (task.status) {
        case STATUS_COMPLETED:
            return "success";
        case STATUS_IN_PROGRESS:
            return "info";
        case STATUS_NEW:
            return "warn";
    }
}
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
            <Tag :severity="tagValue(node.data)"
                 :value="node.data.status"></Tag>
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
</style>
