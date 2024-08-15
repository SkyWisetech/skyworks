import request from '@/utils/request.js'

//查询标签
export const listLabelApi = () => {
  return request.post('/label/listLabel', {}).then((res) => res.data)
}

//根据标签查询文章
export const listArticleByLabelApi = (data) =>
  request.post('/article/listArticleByLabel', data).then((res) => res.data)

//获取排名前四的标签
export const recommendLabelApi = () =>
  request.post('/caffeine/recommendLabel').then((res) => res.data)
