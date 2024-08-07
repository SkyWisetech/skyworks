<template>
  <footer>
    <div class="container-page">
      <div class="footer">
        <div class="footer-box">
          <div class="footer-top">
            <ul>
              <span>关于我们</span>
              <li><a href="#">为什么选择</a></li>
              <li><a href="#">文档中心</a></li>
              <li><a href="#">联系我们</a></li>
              <li><a href="#">人才招聘</a></li>
            </ul>
            <ul>
              <span> 解决方案 </span>
              <li><a href="#">汽车行业</a></li>
              <li><a href="#">文娱行业</a></li>
              <li><a href="#">医疗建行行业</a></li>
              <li><a href="#">智慧文旅</a></li>
            </ul>
            <ul>
              <span>服务与支持</span>
              <li><a href="#">备案副业</a></li>
              <li><a href="#">服务咨询</a></li>
              <li><a href="#">建议与反馈</a></li>
              <li><a href="#">投诉举报</a></li>
            </ul>
            <ul>
              <span>联系我们</span>

              <li><a href="#">业务咨询：sdfsasxdff@fvd</a></li>
              <li><a href="#">市场合作：sdfsghfgf@fgdasss.com</a></li>
              <li><a href="#">电话：333-525-3345</a></li>
              <li>
                <a href="#"
                  >地址：北京市海淀区北三环西路甲18号院大钟寺广场一号</a
                >
              </li>
            </ul>
            <div class="erweima"></div>
          </div>
        </div>
        <div class="footer-bottom">
          <ul class="bottoms">
            <li>
              <a href="#"
                >北京火山引擎科技有限公司 2024版权所有
                代理域名注册服务机构：新网数码 商中在线</a
              >
            </li>
            <li><a href="#">服务条款</a></li>
            <li><a href="#">隐私政策</a></li>
            <li><a href="#">更多协议</a></li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>
<style lang="scss" scoped>
.container-page {
  width: 100vw;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 60px;

  .footer {
    // background-color: pink;
    width: 1200px;
    height: 100%;

    .footer-box {
      display: flex;
      // flex-direction: column;
      align-items: center;
      border-top: 2px solid #eeeeee;
      /* 上边框 */
      border-bottom: 2px solid #eeeeee;
      /* 下边框 */
      border-left: none;
      /* 左边框，设置为无 */
      border-right: none;
      /* 右边框，设置为无 */
      padding: 10px 0;

      .footer-top {
        display: flex;
        justify-content: center;
        align-items: center;

        ul {
          margin-right: 100px;

          span {
            font-family:
              Source Han Sans,
              Source Han Sans;
            font-weight: 400;
            font-size: 15px;
            color: #333333;
            line-height: 23px;
            text-align: left;
            font-style: normal;
            text-transform: none;
          }

          li {
            list-style-type: none;

            a {
              text-decoration: none;
              color: #666666;
              font-size: 13px;
              font-family:
                Source Han Sans,
                Source Han Sans;
            }
          }
        }

        .erweima {
          width: 92px;
          height: 92px;
          background-color: #5a5a5a;
          margin-top: 20px;
        }
      }
    }
  }

  .footer-bottom {
    ul {
      display: flex;

      li {
        list-style-type: none;
        margin-right: 22px;

        a {
          text-decoration: none;
          color: #666666;
          font-size: 13px;
          font-family:
            Source Han Sans,
            Source Han Sans;
        }
      }
    }
  }
}
</style>
