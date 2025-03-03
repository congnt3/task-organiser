<template>
  <div class="parameter-component">
    <h2>{{ title }}</h2>
    <p>Message: {{ message }}</p>
    <p>Count: {{ count }}</p>
    <button @click="incrementCount">Increment Count</button>

    <div v-if="items.length > 0">
      <h3>Items:</h3>
      <ul>
        <li v-for="(item, index) in items" :key="index">
          {{ item }}
        </li>
      </ul>
    </div>

    <div class="optional-section" v-if="optionalProp">
      <p>Optional value: {{ optionalProp }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, withDefaults } from 'vue'

// Define props with TypeScript interface
interface Props {
  // Required string prop
  message: string;
  // Optional string prop with default value
  title?: string;
  // Optional array prop with default value
  items?: string[];
  // Optional prop that might not be provided
  optionalProp?: string;
}

// Define the props with defaults
let props = withDefaults(defineProps<Props>(), {
  title: 'Default Title',
  items: () => [],
})

// Component state using ref
const count = ref(0)

// Method to update state
const incrementCount = () => {
  count.value++
}
</script>

<style scoped>
.parameter-component {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin: 16px 0;
  background-color: #f9f9f9;
}

button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin: 8px 0;
}

button:hover {
  background-color: #45a049;
}

ul {
  padding-left: 20px;
}

.optional-section {
  margin-top: 16px;
  padding: 8px;
  background-color: #e9f5e9;
  border-radius: 4px;
}
</style>
