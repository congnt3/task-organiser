import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

import PrimeVue from 'primevue/config';
import AutoComplete from 'primevue/autocomplete';
import Button from 'primevue/button';
import Calendar from 'primevue/calendar';
import Checkbox from 'primevue/checkbox';
import Column from 'primevue/column';
import DataTable from 'primevue/datatable';
import Dialog from 'primevue/dialog';
import Dropdown from 'primevue/dropdown';
import InputText from 'primevue/inputtext';
import Toast from 'primevue/toast';
import ConfirmationService from 'primevue/confirmationservice';
import ToastService from 'primevue/toastservice';
import Aura from '@primevue/themes/aura';
import '@/assets/styles.scss';
import '@/assets/tailwind.css';
import 'primeicons/primeicons.css'

const app = createApp(App);

app.use(router);

app.use(PrimeVue, {
    ripple: true,
    theme: {
        preset: Aura
    }
})
app.use(ToastService);
app.use(ConfirmationService);

app.component('AutoComplete', AutoComplete);
app.component('Button', Button);
app.component('Calendar', Calendar);
app.component('Checkbox', Checkbox);
app.component('Column', Column);
app.component('DataTable', DataTable);
app.component('Dialog', Dialog);
app.component('Dropdown', Dropdown);
app.component('InputText', InputText);
app.component('Toast', Toast);

app.mount('#app');
