# How to Use the ParameterComponent

The `ParameterComponent.vue` demonstrates how to create a Vue component that accepts parameters (props) with TypeScript support.

## Usage Example

```vue
<template>
  <div>
    <!-- Basic usage with required prop -->
    <ParameterComponent message="Hello world!" />
    
    <!-- With optional props -->
    <ParameterComponent 
      message="Custom message"
      title="Custom Title"
      :items="['Item 1', 'Item 2', 'Item 3']"
      optionalProp="This is optional"
    />
  </div>
</template>

<script setup lang="ts">
import ParameterComponent from '@/components/ParameterComponent.vue'
</script>
```

## Component Features

1. **Props with TypeScript Interface**:
   - `message` (required string)
   - `title` (optional string with default 'Default Title')
   - `items` (optional string array, defaults to empty array)
   - `optionalProp` (optional string with no default)

2. **Reactive State**:
   - Counter with increment functionality

3. **Conditional Rendering**:
   - Items list only shows when items are present
   - Optional section only appears when optionalProp is provided