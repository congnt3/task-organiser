import {createApp} from 'vue'
import App from './App.vue'
import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura';

// Import PrimeVue styles

import 'primeicons/primeicons.css'

const app = createApp(App)

app.use(PrimeVue, {
  ripple: true,
  theme: {
    preset: Aura
  }
})
app.mount('#app')
