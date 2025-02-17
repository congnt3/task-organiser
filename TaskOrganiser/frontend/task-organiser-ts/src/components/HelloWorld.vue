<template>
  <div class="card">
    <h2>PrimeVue Demo</h2>

    <div class="field">
      <InputText v-model="name" placeholder="Enter your name" />
    </div>

    <div class="field">
      <Button @click="showMessage" label="Say Hello" />
    </div>

    <div class="greeting">
      Hello {{ name }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'

const name = ref('')
const greeting = ref('')

const showMessage = () => {
  greeting.value = `Hello, ${name.value}!`
}
</script>

<style scoped>
.card {
  padding: 2rem;
  max-width: 500px;
  margin: 0 auto;
}

.field {
  margin-bottom: 1rem;
}

.greeting {
  margin-top: 1rem;
  font-size: 1.2rem;
  color: #4caf50;
}
</style>
