<!--
 * new page
 * @since: 2024-07-23
 * pagination.vue
-->
<template>
  <div class="page-container">
    <el-pagination
      @change="changePage"
      size="large"
      layout="prev, pager, next"
      :total="totalProp"
    />
  </div>
</template>

<script setup lang="ts">
defineProps({
  totalProp: {
    type: Number,
    default: 10
  }
})
const emit = defineEmits(['getPageInfo'])
const changePage = (e) => {
  emit('getPageInfo', e)
}
</script>

<style scoped lang="scss">
.page-container {
  display: flex;
  justify-content: center;
}
</style>
