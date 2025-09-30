<template>
  <div></div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router'

const route: any = useRoute();
const router: any = useRouter();
const {params, query} = route
const {path} = params

router.replace({path: '/' + path, query})
</script>