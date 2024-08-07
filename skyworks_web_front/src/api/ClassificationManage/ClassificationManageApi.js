import request from '@/utils/request.js'

//查询标签
export const listLabelApi = () => {
  return request.post('/label/listLabel', {}).then((res) => res.data)
}
