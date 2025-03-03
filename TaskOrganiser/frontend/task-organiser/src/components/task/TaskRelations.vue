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
<!--        <OrganizationChart :value="data" collapsible>-->
<!--            <template #person="slotProps">-->
<!--                <div class="flex flex-col">-->
<!--                    <div class="flex flex-col items-center">-->
<!--                        <img :alt="slotProps.node.data.name" :src="slotProps.node.data.image" class="mb-4 w-12 h-12" />-->
<!--                        <span class="font-bold mb-2">{{ slotProps.node.data.name }}</span>-->
<!--                        <span>{{ slotProps.node.data.title }}</span>-->
<!--                    </div>-->
<!--                </div>-->
<!--            </template>-->
<!--            <template #default="slotProps">-->
<!--                <span>{{ slotProps.node.label }}</span>-->
<!--            </template>-->
<!--        </OrganizationChart>-->
        <div class="w-full">
            <Accordion value="0">
                <AccordionPanel value="0">
                    <AccordionHeader>Details</AccordionHeader>
                    <AccordionContent>
                        <div class=" flex flex-col gap-4 w-full">

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
