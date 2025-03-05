<script setup lang="ts">
import { ref, computed } from 'vue';
import { useLayout } from '@/layout/composables/layout';
import { useRouter } from 'vue-router';

const { layoutConfig, contextPath } = useLayout();

const router = useRouter();
const outsideClick = ref(false);

const logoUrl = computed(() => {
    return `${contextPath}layout/images/${layoutConfig.darkTheme.value ? 'logo-white' : 'logo-dark'}.svg`;
});
</script>

<template>
    <router-view></router-view>
</template>

<style lang="scss">
@import '@/assets/styles.scss';
</style>