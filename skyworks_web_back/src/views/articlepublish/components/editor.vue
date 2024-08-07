<!--
 * @Author: wangzhiyu <w19165802736@163.com>
 * @version: 1.0.0
 * @Date: 2024-04-7 11:04:25
 * @LastEditTime: 2024-04-08 21:44:20
 * @Descripttion: 富文本编辑器组件
-->
<template>
  <div id="div1">
    <div style="border: 1px solid #ccc; width: 100%">
      <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        mode="default"
      />
      <Editor
        style="height: 400px; overflow-y: hidden"
        v-model="valueHtml"
        :defaultConfig="editorConfig"
        mode="default"
        @blur="getValue"
        @onCreated="handleCreated"
        @customPaste="customPaste"
      />
    </div>
    <el-button style="margin: 0 auto; display: none" @click="getEditorHTML"
      >获取富文本HTML内容</el-button
    >
  </div>
</template>
<script setup>
// 富文本编辑器文档链接: https://www.wangeditor.com/v5/getting-started.html
// 引入富文本编辑器CSS
import '@wangeditor/editor/dist/css/style.css'
import { onBeforeUnmount, ref, shallowRef } from 'vue'
// 导入富文本编辑器的组件
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { uploadImage } from '@/api/articlemanage/articlepublishs'
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref()
const toolbarConfig = {}
const editorConfig = ref({
  placeholder: '请输入内容...',
  MENU_CONF: {}
})
defineExpose({
  valueHtml
})
// 自定义图片上传
editorConfig.value.MENU_CONF['uploadImage'] = {
  async customUpload(file, insertFn) {
    console.log('file---', file)
    let image = new FormData()
    image.append('image', file)
    const res = await uploadImage(image)
    console.log('自定义图片上传res', res)
    // 将上传的file图片转换为base64
    const base64 = URL.createObjectURL(file)

    // 这里的file为上传的图片对象,insertFn传入 base64的值
    insertFn(base64, 'img')
  }
}

// 自定义视频上传
editorConfig.value.MENU_CONF['uploadVideo'] = {
  async customUpload(file) {
    console.log('上传视频', file)
  }
}

// 富文本编辑器生成后触发
const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
  // console.log(editorConfig.value.MENU_CONF, 'editorConfig.value')
}

// 监听富文本编辑器粘贴行为
const customPaste = (editor, event, callback) => {
  // 获取粘贴的纯文本
  const text = event.clipboardData.getData('text/plain')
  if (text) {
    editor.insertText(text)
    event.preventDefault()
    callback(false)
  }
}

// 获取富文本html内容
const getEditorHTML = () => {
  console.log(editorRef.value.getHtml())
}
const getValue = () => {
  console.log(editorRef.value.getHtml())
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>
