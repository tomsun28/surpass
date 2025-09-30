<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
       <span
           v-if="(item.elTagType == 'default' || item.elTagType == '') && (item.elTagClass == '' || item.elTagClass == null)"
           :key="item.value"
           :index="index"
           :class="item.elTagClass"
       >{{ item.label + " " }}</span>
        <el-tag
            v-else
            :disable-transitions="true"
            :key="item.value + ''"
            :index="index"
            :type="item.elTagType === 'primary' ? '' : item.elTagType"
            :class="item.elTagClass"
        >{{ item.label + " " }}</el-tag>
      </template>
    </template>
  </div>
</template>

<script setup lang="ts">
  const props: any = defineProps({
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
  });

  const values: any = computed(() => {
      if (props.value !== null && typeof props.value !== 'undefined') {
        return Array.isArray(props.value) ? props.value : [props.value];
      } else {
        return [];
      }
  });

</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
