<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  listLabelApi, //查询标签
  insertLabelApi, // 添加标签
  deleteLabelApi, //删除标签
  updateLabelApi //修改标签
} from '@/api/ClassificationManage/ClassificationManageApi.js'

//加载列表
const listData = ref({})
const load = async () => {
  const res = await listLabelApi()
  if (res.data.code == 200) {
    listData.value = res.data.data
  }
}
//删除标签
const deleteLabelClick = async (v) => {
  let params = {
    id: v
  }
  const res = await deleteLabelApi(params)
  if (res.data.code == 200) {
    ElMessage.success(res.data.msg)
    await load()
  }
}

//修改标签
const dialogFormVisible = ref(false)
const labelName = ref('')
const formData = ref({})
const titleName = ref('修改标签')

const updateLabelClick = async (v) => {
  formData.value = v
  dialogFormVisible.value = true
  titleName.value = '修改标签'
  labelName.value = v.labelName
}
const submitClick = async () => {
  let params = {
    id: formData.value.id,
    priority: formData.value.priority,
    color: formData.value.color,
    labelName: labelName.value
  }
  const res = await updateLabelApi(params)
  if (res.data.code == 200) {
    ElMessage.success(res.data.msg)
    dialogFormVisible.value = false
    await load()
  }
}

//添加标签
const parentData = ref({})
const numBtn = ref(false)
const addClick = async (v) => {
  // console.log(v)
  if (!v) {
    numBtn.value = true
  } else {
    numBtn.value = false
  }
  parentData.value = v
  dialogFormVisible.value = true
  titleName.value = '新增标签'
}
const priority = ref()
const addSubmitClick = async () => {
  //新增二级
  if (parentData.value) {
    let params = {
      priority: parentData.value.priority,
      color: parentData.value.color,
      labelName: labelName.value,
      parentId: parentData.value.id
    }
    const res = await insertLabelApi(params)
    if (res.data.code == 200) {
      ElMessage.success(res.data.msg)
      dialogFormVisible.value = false
      await load()
    }
  } else {
    //新增一级
    let params = {
      priority: priority.value,
      color: '',
      labelName: labelName.value,
      parentId: '0'
    }
    const res = await insertLabelApi(params)
    if (res.data.code == 200) {
      ElMessage.success(res.data.msg)
      dialogFormVisible.value = false
      await load()
    }
  }
}
const handleChange = (v) => {
  console.log(v)
}
const destroyClose = () => {
  labelName.value = null
  priority.value = null
}
onMounted(() => {
  load()
})
</script>
<template>
  <div class="classification-management">
    <div class="classification-content">
      <div class="addTop">
        <el-button type="primary" class="publisharticle" @click="addClick()">
          <el-icon class="plus">
            <Plus />
          </el-icon>
          添加
        </el-button>
      </div>
      <div v-for="item in listData" :key="item.id">
        <div style="display: flex">
          <img
            @click="updateLabelClick(item)"
            src="@/assets/classificationmanage/组 154@2x.png"
            class="el-icon-select"
            style="width: 16px; height: 16px"
          />
          <div class="select-label">
            <span @click="updateLabelClick(item)">{{ item.labelName }}</span>
            <img
              @click="deleteLabelClick(item.id)"
              src="@/assets/classificationmanage/组 175@2x.png"
              class="el-icon-select-delete"
              style="
                width: 14px;
                height: 14px;
                cursor: pointer;
                margin-left: 10px;
                margin-top: 6px;
              "
            />
          </div>
        </div>
        <div class="select-list">
          <span
            v-for="label in item.childLabelList"
            :key="label.id"
            style="cursor: pointer"
          >
            <li @click="updateLabelClick(label)">{{ label.labelName }}</li>
            <el-icon
              class="icon"
              @click="deleteLabelClick(label.id)"
              :size="14"
              color="#CACACA"
              ><CircleCloseFilled
            /></el-icon>
          </span>
          <span
            style="width: 44px; height: 38px; cursor: pointer"
            @click="addClick(item)"
          >
            <el-icon
              class="icon"
              :size="14"
              color="#CACACA"
              style="margin-left: 0px"
              ><Plus
            /></el-icon>
          </span>
        </div>
      </div>
    </div>

    <el-dialog
      :destroy-on-close="true"
      v-model="dialogFormVisible"
      :title="titleName"
      width="400"
      @close="destroyClose()"
    >
      <el-form>
        <el-form-item label="新标签" :label-width="50">
          <el-input v-model="labelName" width="100" />
        </el-form-item>
        <el-form-item v-if="numBtn" label="优先级" :label-width="50">
          <el-input-number
            v-model="priority"
            :min="1"
            @change="handleChange(priority)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" v-if="titleName == '修改标签'">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitClick()"> 确定 </el-button>
        </div>
        <div class="dialog-footer" v-else>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="addSubmitClick()"> 确定 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<style lang="scss" scoped>
.classification-management {
  min-height: 100%;
  width: 100%;
  background: #fff;
  border-radius: 20px 20px 20px 20px;

  .classification-content {
    padding: 20px 30px 30px;
    ::v-deep(.el-select__wrapper) {
      border: none;
      box-shadow: none;
      background-color: #ffffff00;
      margin-left: -23px;
      margin-top: -13px;
      .el-select__selection {
        display: none;
      }
      .el-select__caret {
        //小箭头隐藏
        color: #ffffff00;
        /* color: #de1717; */
      }
    }

    .select-list {
      width: 100%;
      display: flex;
      flex-wrap: wrap;
      li {
        list-style: none;
      }
      span {
        width: 180px;
        height: 38px;
        background: #f6f6f6;
        border-radius: 2px 2px 2px 2px;
        padding: 8px 15px;
        margin-right: 30px;
        margin-bottom: 30px;
        box-sizing: border-box;
        height: 38px;
        font-family:
          Source Han Sans,
          Source Han Sans;
        font-weight: 400;
        font-size: 16px;
        color: #333333;
        text-align: left;
        font-style: normal;
        text-transform: none;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .icon {
          margin-left: 40px;
          cursor: pointer;
        }
      }
      .addBtn {
        background: #f6f6f6;
        border-radius: 2px 2px 2px 2px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .select-label {
      display: flex;
      margin-left: 4px;
      margin-top: -4px;
      margin-bottom: 20px;
      /* width: 72px; */
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 500;
      font-size: 18px;
      color: #333333;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }
    .addTop {
      width: 100%;
      display: flex;
      justify-content: end;
      .publisharticle {
        height: 50px;
        width: 80px;
        font-weight: 600;

        .plus {
          font-size: 18px;
          font-weight: 700;
        }
      }
    }
  }
}
</style>
