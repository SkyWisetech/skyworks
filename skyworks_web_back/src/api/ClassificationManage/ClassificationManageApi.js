import request from '@/utils/request.js'

//查询标签
export const listLabelApi = () => request.post('/label/listLabel', {})

// 添加标签
export const insertLabelApi = ({ priority, color, labelName, parentId }) =>
  request.post('/label/insertLabel', { priority, color, labelName, parentId })

//删除标签
export const deleteLabelApi = (id) => request.post('/label/deleteLabel', id)

//修改标签
export const updateLabelApi = ({ id, priority, color, labelName }) =>
  request.post('/label/updateLabel', { id, priority, color, labelName })
