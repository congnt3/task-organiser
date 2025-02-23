<script setup>
import {ref} from 'vue';

const dropdownItems = ref([
    {name: 'Option 1', code: 'Option 1'},
    {name: 'Option 2', code: 'Option 2'},
    {name: 'Option 3', code: 'Option 3'}
]);

const dropdownItem = ref(null);
</script>

<template>
    <Fluid>
            <div class="w-full">
                <Accordion value="0">
                    <AccordionPanel value="0">
                        <AccordionHeader>Details</AccordionHeader>
                        <AccordionContent>
                            <div class=" flex flex-col gap-4 w-full">
                                <div class="flex flex-col md:flex-row gap-4">
                                    <div class="flex flex-wrap gap-2 w-full">
                                        <label for="firstname2">Firstname</label>
                                        <InputText id="firstname2" type="text"/>
                                    </div>
                                    <div class="flex flex-wrap gap-2 w-full">
                                        <label for="lastname2">Lastname</label>
                                        <InputText id="lastname2" type="text"/>
                                    </div>
                                </div>

                                <div class="flex flex-wrap">
                                    <label for="address">Address</label>
                                    <Textarea id="address" rows="4"/>
                                </div>

                                <div class="flex flex-col md:flex-row gap-4">
                                    <div class="flex flex-wrap gap-2 w-full">
                                        <label for="state">State</label>
                                        <Select id="state" v-model="dropdownItem" :options="dropdownItems"
                                                optionLabel="name"
                                                placeholder="Select One" class="w-full"></Select>
                                    </div>
                                    <div class="flex flex-wrap gap-2 w-full">
                                        <label for="zip">Zip</label>
                                        <InputText id="zip" type="text"/>
                                    </div>
                                </div>
                            </div>

                        </AccordionContent>
                    </AccordionPanel>
                    <AccordionPanel value="1">
                        <AccordionHeader>Header II</AccordionHeader>
                        <AccordionContent>
                            <p class="m-0">
                                Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium
                                doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore
                                veritatis et quasi architecto beatae vitae dicta sunt
                                explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut
                                fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi
                                nesciunt. Consectetur, adipisci velit, sed quia non
                                numquam eius modi.
                            </p>
                        </AccordionContent>
                    </AccordionPanel>
                </Accordion>
        </div>
    </Fluid>
</template>
