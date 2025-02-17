import {createApp} from 'vue'
import App from './App.vue'
import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura';
import ConfirmationService from 'primevue/confirmationservice';
import ToastService from 'primevue/toastservice';
import router from './router';
import '@/assets/styles.scss';
import '@/assets/tailwind.css';
import 'primeicons/primeicons.css'

const app = createApp(App)

app.use(router);

app.use(PrimeVue, {
  ripple: true,
  theme: {
    preset: Aura
  }
})
app.use(ToastService);
app.use(ConfirmationService);
app.mount('#app')
