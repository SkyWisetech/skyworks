<!--
 * new page
 * @since: 2024-07-23
 * person.vue
-->
<template>
  <el-form
    ref="formRef"
    style="max-width: 600px; padding-top: 11px"
    :model="personInfo"
    label-width="auto"
    class="person-form"
  >
    <el-form-item
      label="昵称"
      prop="nickName"
      :rules="[{ required: true, message: '请输入昵称', trigger: 'blur' }]"
    >
      <el-input size="large" v-model="personInfo.nickName" clearable />
    </el-form-item>
    <el-form-item
      prop="email"
      label="注册邮箱"
      :rules="[
        {
          required: true,
          message: '请输入邮箱',
          trigger: 'blur'
        },
        {
          type: 'email',
          message: '请输入正确的邮箱',
          trigger: ['blur', 'change']
        }
      ]"
    >
      <el-input size="large" v-model="personInfo.email" clearable disabled />
    </el-form-item>
    <el-form-item label="个人简介" prop="desc">
      <el-input
        size="large"
        v-model="personInfo.remark"
        type="textarea"
        resize="none"
        :rows="5"
      />
    </el-form-item>
    <el-form-item>
      <el-button size="large" type="primary" @click="submitForm(formRef)"
        >保存</el-button
      >
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { updateUser } from '../../../api/personal'
import { ElMessage } from 'element-plus'

const { personProps } = defineProps({
  personProps: {
    type: Object,
    default: () => {}
  }
})
const emit = defineEmits('[onFinish]')

const formRef = ref()
let personInfo = reactive({})
onMounted(() => {
  const { email, nickName, remark } = personProps
  personInfo.email = email
  personInfo.nickName = nickName
  personInfo.remark = remark
})
const submitForm = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      console.log('submit!', personInfo)
      //   调用接口
      const res = await updateUser({ ...personInfo, updateType: 1 })
      if (res.data.code == 200) {
        ElMessage.success('成功修改个人信息')
        emit('onFinish')
      } else {
        ElMessage.error(res.data.code)
      }
    } else {
      console.log('error submit!')
    }
  })
}
</script>
<style lang="scss" scoped>
.person-form .el-form-item:last-child {
  float: right;
}
.person-form ::v-deep .el-form-item__label-wrap {
  align-items: center;
}
.person-form ::v-deep .el-form-item__label {
  font-size: 14px;
}
</style>
