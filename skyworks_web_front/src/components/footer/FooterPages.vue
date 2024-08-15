<template>
  <footer>
    <div class="container-page">
      <div class="footer">
        <div class="footer-box">
          <div class="footer-top">
            <ul>
              <span class="aboutUsTitle">关于我们</span>
              <div class="aboutUs">
                <a href="#"
                  ><img src="@/assets/footer/组 420@2x.png" alt=""
                /></a>
                <a href="#"
                  ><img src="@/assets/footer/蒙版组 27@2x.png" alt=""
                /></a>
                <a href="#"
                  ><img src="@/assets/footer/组 422@2x.png" alt=""
                /></a>
                <a href="#"
                  ><img src="@/assets/footer/蒙版组 32@2x.png" alt=""
                /></a>
                <a href="#"
                  ><img src="@/assets/footer/组 424@2x.png" alt=""
                /></a>
              </div>
            </ul>
            <ul>
              <span class="aboutUsTitle aboutAddress"> 联系我们 </span>
              <div class="aboutUs aboutAddress" style="">
                <img
                  src="@/assets/footer/组 431@2x.png"
                  alt=""
                  style="width: 20px; height: 20px"
                />
                <span>邮箱：sdfsasxdff@fvddcv.com</span>
              </div>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="moreServices" style="display: none">
      <div>
        <a href="#"> 北京火山引擎科技有限公司 </a>
        <a href="#"> 2024版权所有 代理域名注册服务机构：新网数码 商中在线</a>
        <a href="#"> 服务条款</a>
        <a href="#"> 隐私政策</a>
        <a href="#"> 更多协议</a>
      </div>
    </div>
  </footer>
</template>
<style lang="scss" scoped>
.container-page {
  width: 100vw;
  height: 160px;
  display: flex;
  justify-content: center;
  align-items: center;
  // margin-top: 60px;

  .footer {
    display: flex;
    justify-content: space-around;
    width: 1200px;
    height: 100%;

    .footer-box {
      display: flex;
      // flex-direction: column;
      align-items: center;
      border-top: 2px solid #eeeeee;
      /* 上边框 */
      // border-bottom: 2px solid #eeeeee;
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
          align-items: center;
          display: flex;
          flex-direction: column;
          .aboutUsTitle {
            margin-bottom: 20px;
            width: 64px;
            height: 23px;
            font-family:
              Source Han Sans,
              Source Han Sans;
            font-weight: 400;
            font-size: 16px;
            color: #333333;
            line-height: 23px;
            text-align: left;
            font-style: normal;
            text-transform: none;
          }
          .aboutUs {
            display: flex;
            align-items: center;
            img {
              width: 40px;
              height: 40px;
              margin-left: 30px;
            }
            img:first-child() {
              margin-left: 0px;
            }
            span {
              height: 20px;
              font-family:
                Source Han Sans,
                Source Han Sans;
              font-weight: 400;
              font-size: 14px;
              color: #666666;
              line-height: 20px;
              text-align: left;
              font-style: normal;
              text-transform: none;
            }
          }
          .aboutAddress {
            padding-bottom: 15px;
          }
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
}
.moreServices {
  height: 88px;
  font-family:
    Source Han Sans,
    Source Han Sans;
  font-weight: 400;
  font-size: 14px;
  color: #666666;
  line-height: 20px;
  text-align: left;
  font-style: normal;
  text-transform: none;
  width: 1200px;
  margin: 0 auto;
  justify-content: center;
  display: flex;
  align-items: center;
  a {
    text-decoration: none;
    color: #666666;
    margin-right: 10px;
  }
}
/* 移动端 100-500px */
@media screen and (min-width: 100px) and (max-width: 500px) {
  .container-page {
    width: 100vw !important;
    // height: 10rem;
    // height: 16rem;

    .footer {
      width: 100%;
      height: 100%;

      .footer-box {
        padding: 0.625rem 0;
        width: 100vw;

        .footer-top {
          width: 100vw;
          flex-direction: column;
          ul {
            padding: 0;
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            .aboutUsTitle {
              width: 4rem;
              height: 1.4375rem;
              font-size: 1rem;
              margin-bottom: 0px;
              line-height: 1.4375rem;
              padding-bottom: 0.9375rem;
              padding-top: 0.9375rem;
            }
            .aboutUs {
              display: flex;
              // margin-bottom: 0.5rem;
              img {
                width: 2.5rem;
                height: 2.5rem;
                margin-left: 1.875rem;
              }

              span {
                height: 1.25rem;
                font-size: 0.875rem;
                color: #666666;
                line-height: 1.25rem;
              }
            }
            .aboutAddress {
              padding-bottom: 0.9375rem;
              display: flex;
              align-items: center;
            }
            a {
              font-size: 0.8125rem;
            }
          }
        }
      }
    }
  }
}
</style>
