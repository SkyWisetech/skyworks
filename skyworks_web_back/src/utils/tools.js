export const getText = (str) => {
  return str
    .replace(/<[^<>]+>/g, '') // 是将所有<>的内容 replace成 ''
    .replace(/&nbsp;/gi, '') // gi是全局搜索，将所有的 &nbsp 都replace成 ''
}
export const isNull = (str) => {
  if (str == '') return true
  var regu = '^[ ]+$'
  var re = new RegExp(regu)
  return re.test(str)
}
export const isEditorNull = (str) => {
  return isNull(getText(str))
}
