<script setup>
import { onMounted, ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import {
  uploadImage,
  LabelClassification,
  insertArticle,
  articleDetailList
} from '@/api/articlemanage/articlepublishs.js'
import { updateArticle } from '@/api/articlemanage/articlemanage'
import Editor from './components/editor.vue'
import { isEditorNull } from '@/utils/tools.js'
const route = useRoute()
const router = useRouter()
// 标签数据
const tagsList = ref({
  oneLabelClassList: [], // 一级
  twoLabelClassList: [] // 二级
})
//整个的form数据
const formModel = ref({
  title: '',
  content: '',
  imageName: '',
  label: '',
  oneId: ''
})

// 整个表单的rules
const rules = {
  title: [
    {
      required: true,
      message: '请输入文章标题',
      trigger: 'blur'
    }
  ],
  content: [
    {
      required: true,
      message: '请输入文章内容',
      trigger: 'blur'
    }
  ],
  imageName: [
    {
      required: true,
      message: '文章封面不能为空',
      trigger: 'blur'
    }
  ],
  label: [
    {
      required: true,
      message: '标签不能为空',
      trigger: 'blur'
    }
  ]
}
// 图片上传
const imgUrl = ref(null)
const handleUpload = async (param) => {
  let image = new FormData()
  image.append('image', param.file)
  try {
    console.log('上传图片参数..', param)
    const res = await uploadImage(image) // uploadImage是一个返回Promise的函数
    if (res.data.code == 200) {
      // imgUrl.value = `http://192.168.1.5:8080/skyworks` + res.data.data // 响应的data中有一个imageUrl字段包含图片的URL
      imgUrl.value = `http://207.148.115.202:80/skyworks` + res.data.data // 响应的data中有一个imageUrl字段包含图片的URL
      formModel.value.imageName = res.data.data
      console.log(res.data.data)
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error('图片上传失败')
    }
  } catch (error) {
    ElMessage.error('上传出错')
  }
}

// 分类标签
const LabelClassifications = async () => {
  const {
    data: { data, code, msg }
  } = await LabelClassification()
  if (code == 200) {
    tagsList.value.oneLabelClassList = data
  } else {
    ElMessage({
      showClose: true,
      message: msg || '接口异常',
      type: 'error'
    })
  }
}

const labelName = ref('')
const handleChangeOneLabel = () => {
  // 这里可以添加额外的逻辑，比如清空当前选中的二级标签

  if (!formModel.value.oneId) return []

  let data = tagsList.value.oneLabelClassList.filter(
    (item) => item.id == formModel.value.oneId
  )

  tagsList.value.twoLabelClassList = data[0].childLabelList
  labelName.value = data[0].id //新增的一级标签label取id
  // labelName.value = data[0].labelName //labelName
}
// 编辑/文章详情查询
const articleId = ref({
  articleId: ''
})
articleId.value = route.query.id
const detailList = async () => {
  const res = await articleDetailList({ articleId: articleId.value })
  console.log(res.data.data)
  formModel.value.title = res.data.data.title
  formModel.value.content = res.data.data.content
  editorRef.value.valueHtml = res.data.data.content
  // editorRef.value.valueHtml =
  //   '<p></p><p><strong></strong></p><p><img src="http://207.148.115.202/skyworks/image/1722242761033.jfif" alt="img" data-href="" style=""/></p>'
  // imgUrl.value = `http://192.168.1.5:8080/skyworks` + res.data.data.imageName
  imgUrl.value = `http://207.148.115.202:80/skyworks` + res.data.data.imageName
  formModel.value.imageName = res.data.data.imageName
  formModel.value.label = res.data.data.label
  formModel.value.oneId = res.data.data.labelName
}
// 发布文章
const formRef = ref(null)
const editorRef = ref(null)
const articlepublish = async () => {
  formModel.value.content = editorRef.value.valueHtml
  formModel.value.label = labelName.value
  formRef.value.content = isEditorNull(editorRef.value.valueHtml)
    ? ''
    : editorRef.value.valueHtml
  console.log(editorRef.value.valueHtml, 'valueHtml')
  // editorRef.value.valueHtml =
  //   '<p>文章</p><p><strong>rwerwe666 e</strong></p><p><img src="http://207.148.115.202/skyworks/image/1722243820166.jpg" alt="img" data-href="" style=""/></p>'
  console.log(editorRef.value.valueHtml, 'valueHtml')
  await formRef.value.validate() //登录前的预校验(获取到组件调用方法)
  // return
  // eslint-disable-next-line no-unreachable
  if (route.query.id) {
    console.log('编辑文章')
    // 编辑文章
    formModel.value.id = route.query.id
    console.log(formModel.value)

    const res = await updateArticle(formModel.value)
    if (res.data.code == 200) {
      ElMessage.success('编辑文章成功')
      formModel.value.title = null
      formModel.value.content = null
      formModel.value.imageName = ''
      formModel.value.label = null
      formModel.value.oneId = null
      imgUrl.value = ''
      router.push({
        name: '/articlemanage'
      })
    } else {
      ElMessage.error('编辑文章失败')
    }
  } else {
    // 发布文章
    const res = await insertArticle(formModel.value)
    if (res.data.code == 200) {
      ElMessage.success('文章发布成功')
      formModel.value.title = null
      formModel.value.content = null
      formModel.value.imageName = ''
      formModel.value.label = null
      formModel.value.oneId = null
      imgUrl.value = ''
    } else {
      ElMessage.error('文章发布失败')
    }
  }
}

const pageTitle = computed(() => {
  const id = route.query.id
  return id ? '编辑文章' : '发布文章'
})
onMounted(() => {
  LabelClassifications()
  if (route.query.id) {
    detailList()
  }
})
</script>
<template>
  <PageContainer :title="pageTitle">
    <!-- <img src="http://207.148.115.202/skyworks/image/1722242761033.jfif" /> -->
    <div class="articlecontent">
      <el-form :model="formModel" :rules="rules" ref="formRef">
        <el-form-item label="文章标题:" prop="title" class="title-label">
          <el-input
            autocomplete="off"
            placeholder="请输入文章标题"
            v-model="formModel.title"
          ></el-input>
        </el-form-item>

        <el-form-item label="文章内容:" prop="content">
          <!-- <el-input
            type="textarea"
            placeholder="请输入正文..."
            v-model="formModel.content"
          ></el-input> -->
          <Editor ref="editorRef" />
        </el-form-item>
        <el-form-item prop="imageName" label="上传封面:">
          {{ imgUrl }}
          <el-upload
            v-model="formModel.imageName"
            action
            accept=".jpg, .png"
            :http-request="handleUpload"
            :show-file-list="false"
            class="uploads"
            drag
            multiple
          >
            <img v-if="imgUrl" :src="imgUrl" class="images" />
            <span v-else>
              <el-icon class="el-icon--upload">
                <Plus />
              </el-icon>
              <div class="el-upload__text">添加封面</div>
            </span>
          </el-upload>
        </el-form-item>
        <el-form-item prop="label" label="分类标签选择：">
          <div class="articletags">
            <div class="articletagsone">
              <span>一级标签</span>
              <div class="articletag">
                <el-select
                  v-model="formModel.oneId"
                  placeholder="请选择"
                  class="select"
                  no-data-text="暂无数据"
                  @change="handleChangeOneLabel"
                >
                  <el-option
                    v-for="item in tagsList.oneLabelClassList"
                    :key="item.id"
                    :label="item.labelName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </div>
            </div>
            <div class="articletagstwo">
              <span>二级标签</span>
              <div class="articletag">
                <el-select
                  v-model="formModel.label"
                  placeholder="请选择"
                  class="select"
                  no-data-text="暂无数据"
                >
                  <el-option
                    v-for="item in tagsList.twoLabelClassList"
                    :key="item.id"
                    :label="item.labelName"
                    :value="item.id"
                  />
                </el-select>
              </div>
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <div class="buttons">
            <el-button type="primary" @click="articlepublish">发布</el-button>
            <el-button>取消</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </PageContainer>
</template>
<style lang="scss" scoped>
.page-container {
  height: auto;
}
.articlecontent {
  .title-label {
    display: flex;
    align-items: center;
  }
  ::v-deep(.el-input__inner) {
    border: none;
    height: 40px;
  }

  ::v-deep(.el-textarea__inner) {
    resize: none;
  }

  ::v-deep(.el-textarea__inner) {
    height: 21vw;
    //max-height: 30vw;
    border: none !important;
    overflow-y: auto;
  }

  ::v-deep(.el-form-item__label) {
    font-weight: 700;
  }

  ::v-deep(.el-upload-dragger) {
    height: 120px;
    width: 120px;
  }

  ::v-deep(.el-icon--upload) {
    height: 20px;
    width: 20px;
  }

  .images {
    width: 100%;
    height: 100%;
  }

  .articletags {
    display: flex;
    position: relative;

    .articletagsone {
      display: flex;
    }

    .articletagstwo {
      display: flex;
      margin-left: 80px;
    }

    ::v-deep(.el-select__wrapper) {
      height: 30px;
    }

    .articletag {
      margin-left: 20px;
      width: 130px;
    }
  }

  .buttons {
    position: absolute;
    top: 10px;
    right: 0px;

    ::v-deep(.el-button) {
      height: 40px;
      width: 80px;
      margin-top: -20%;
    }
  }
}
</style>
