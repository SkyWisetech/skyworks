<script setup>
defineProps({
  breadcrumbItems: {
    type: Array,
    default: () => []
  }
})
</script>

<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item
        v-for="(item, index) in breadcrumbItems"
        :key="index"
        :to="item.to || null"
      >
        <template v-if="!item.to">
          <a :href="item.href || '#'">{{ item.text }}</a>
        </template>
        <template v-else>
          {{ item.text }}
        </template>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<style lang="scss" scoped>
// .page-container {
//     min-height: 100%;
//     box-sizing: border-box;

//     .header {
//         display: flex;
//         align-items: center;
//         justify-content: space-between;
//     }
// }
</style>
