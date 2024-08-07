<!--
 * new page
 * @since: 2024-07-22
 * search.vue
-->
<template>
  <el-form :inline="true" :model="formInline" class="form-container">
    <el-form-item label="昵称">
      <el-input v-model="formInline.nickName" placeholder="请输入" clearable />
    </el-form-item>
    <el-form-item label="邮箱">
      <el-input v-model="formInline.email" placeholder="请输入" clearable />
    </el-form-item>
    <el-form-item label="注册日期" required>
      <el-date-picker
        v-model="formInline.time"
        type="daterange"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
      />
    </el-form-item>
    <el-form-item>
      <el-button class="right-button" type="primary" @click="onSubmit"
        >搜索</el-button
      >
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive } from 'vue'
const emit = defineEmits(['getSearchInfo'])
const formInline = reactive({
  nickName: '',
  email: '',
  startRegisterDate: '',
  endRegisterDate: ''
})
const onSubmit = () => {
  emit('getSearchInfo', formInline)
  // console.log('submit!', formInline)
}
</script>

<style scoped>
.form-container {
  padding: 17px 27px;
  background: #ffffff;
  border-radius: 11px;
}
.form-container .el-form-item {
  margin-bottom: 0;
}
.form-container .right-button {
  float: right;
}

.form-container >>> .el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 106px;
}
</style>
