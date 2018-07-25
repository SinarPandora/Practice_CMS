package app.action

import java.util

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.apache.logging.log4j.LogManager
import org.json.JSONArray
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.ModelAndView


@Controller
@RequestMapping(Array("/app"))
// TODO 成功获取到配置参数了！！！
// TODO 下一步根据主题内容渲染 HTML 模板
class MainAction(@Value("${theme.name}") val themeName: String, @Value("${dark.css}") val css: Array[String]) {

  private val logger = LogManager.getLogger("前端")


  @RequestMapping(Array("/index"))
  def sayHello(req: HttpServletRequest, resp: HttpServletResponse): String = {
    println(themeName)
    css.foreach(println)
    logger.info("-------------------------Success------------------------")
    "/pages/app"
  }

  @RequestMapping(Array("/form"))
  def form(): ModelAndView = {
    logger.info("> 进入表单管理页面")
    val fakeInfo =
      """[
  {
    "id": 1,
    "name": "招聘信息",
    "submits": 120,
    "isInProgress": true,
    "templatePath": "./forms/01.html"
  },
  {
    "id": 2,
    "name": "社会调查",
    "submits": 502,
    "isInProgress": false,
    "templatePath": "./forms/02.html"

  },
  {
    "id": 3,
    "name": "测试1",
    "submits": 23,
    "isInProgress": false,
    "templatePath": "./forms/03.html"
  },
  {
    "id": 4,
    "name": "测试2",
    "submits": 3,
    "isInProgress": true,
    "templatePath": "./forms/04.html"
  }
]""".stripMargin
    val jsonArray = new JSONArray(fakeInfo)
    val model = new util.HashMap[String, Any]()
    model.put("type", 1)
    model.put("forms", jsonArray)
    render(model)
  }

  // 返回 JSON
  @ResponseBody
  @PostMapping(Array("/getMessage"))
  def messageSender(): util.HashMap[String, String] = {
    val json = new util.HashMap[String, String]()
    json.put("name", "zhangsan")
    json.put("age", "aha")
    json
  }

  @RequestMapping(Array("/jq"))
  def jq(): String = {
    "/pages/JQueryTest"
  }

  @GetMapping(Array("/error"))
  def testError(): Unit = {
    logger.error("测试错误")
  }

  def render(request: HttpServletRequest, url: String): String = {
    request.setAttribute("themeName", "dark")
    url
  }

  def render(model: util.HashMap[String, Any]): ModelAndView = {
    model.put("themeName", "dark")
    new ModelAndView("/pages/app", model)
  }




}
