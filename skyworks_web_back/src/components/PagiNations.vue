<script setup>
const emit = defineEmits(['getPage'])
let props = defineProps({
  totalProp: {
    type: Number,
    default: 0
  },
  pageNum: {
    type: Number,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  }
})
const onChange = (e) => {
  console.log('e--', e)
  emit('getPage', e)
}

console.log('*****', props.pageNum)
</script>

<template>
  <div class="example-pagination-block">
    <!-- <el-pagination
      layout="->,prev, pager, next"
      :total="totalProp"
      @change="onChange"
      :currentPage="pageNum"
      :page-size="pageSize"
      @size-change="$emit('update:pageSize', $event)"
    /> -->
    <el-pagination
      :currentPage="pageNum"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="totalProp"
      @current-change="onChange"
    />
  </div>
</template>
<style lang="scss" scoped>
.example-pagination-block {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  ::v-deep(.is-active) {
    border-radius: 50px 50px 50px 50px;
    background-color: #7554e6;
    color: #fff;
  }
}
</style>
