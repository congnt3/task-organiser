# Binding Vue Component Properties from URL Query Parameters

When building Vue applications, you often need to access URL query parameters and bind them to component properties. This guide explains how to accomplish this.

## Accessing URL Query Parameters in Vue

In Vue, you can access URL query parameters using the Vue Router's `$route` object.

### Method 1: Using Vue Router in Setup Script

```vue
<script setup>
import { useRoute } from 'vue-router'
import { ref, onMounted, watch } from 'vue'

// Access the route object
const route = useRoute()

// Create a reactive ref to store the query parameter value
const parameterValue = ref('')

// Set the value initially
onMounted(() => {
  parameterValue.value = route.query.paramName || 'default value'
})

// Watch for URL query parameter changes
watch(
  () => route.query.paramName,
  (newValue) => {
    parameterValue.value = newValue || 'default value'
  }
)
</script>

<template>
  <div>
    <p>Parameter value: {{ parameterValue }}</p>
  </div>
</template>
```

### Method 2: Using Computed Properties

```vue
<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()

// Create a computed property that updates when the query parameter changes
const parameterValue = computed(() => route.query.paramName || 'default value')
</script>

<template>
  <div>
    <p>Parameter value: {{ parameterValue }}</p>
  </div>
</template>
```

## Example: Binding URL Query Parameter to a Component Prop

Assuming you want to pass a URL query parameter to a child component:

### ParentComponent.vue
```vue
<template>
  <div>
    <!-- Pass the URL query parameter as a prop to the ParameterComponent -->
    <ParameterComponent :message="messageFromUrl" />
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import ParameterComponent from '@/components/ParameterComponent.vue'

const route = useRoute()

// Get the 'message' query parameter from URL
const messageFromUrl = computed(() => route.query.message || 'No message in URL')
</script>
```

## Using with ParameterComponent

Based on the `ParameterComponent.vue` in your project, you can bind URL parameters to any of its props:

```vue
<template>
  <div>
    <ParameterComponent 
      :message="messageFromUrl"
      :title="titleFromUrl"
      :items="itemsFromUrl"
      :optionalProp="optionalFromUrl"
    />
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import ParameterComponent from '@/components/ParameterComponent.vue'

const route = useRoute()

const messageFromUrl = computed(() => route.query.message || 'Default message')
const titleFromUrl = computed(() => route.query.title || undefined)
const optionalFromUrl = computed(() => route.query.optional || undefined)

// For array props, you might need to parse the query parameter
// Assuming items are comma-separated in the URL: ?items=item1,item2,item3
const itemsFromUrl = computed(() => {
  const itemsParam = route.query.items
  return itemsParam ? String(itemsParam).split(',') : []
})
</script>
```

## Handling Navigation to Pages with Query Parameters

To navigate to pages with query parameters programmatically:

```vue
<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function navigateWithParams() {
  router.push({
    path: '/your-path',
    query: {
      message: 'Hello from query',
      title: 'My Title'
    }
  })
}
</script>

<template>
  <button @click="navigateWithParams">Navigate with Parameters</button>
</template>
```

## URL Example

When your application is running, you can access a component with query parameters like:
```
https://your-app.com/your-page?message=Hello&title=Custom%20Title&items=item1,item2,item3
```

The component will then receive these values as props.