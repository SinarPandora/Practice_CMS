package app.action

import java.io.{FileReader, FileWriter}
import java.util
import java.util.Properties

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.eclipse.collections.impl.factory.Maps
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

@Controller
@RequestMapping(Array("/app"))
class ThemeChangeAction(@Value("${theme.name}") val currentThemeName: String) {

  @Autowired val servletContext: ServletContext = null
  private val logger = LogManager.getLogger("主题切换")
  //  private val themeFilePath = servletContext.getRealPath("/webapp/WEB_INF/views/templates")
  //  private val propertiesFile = servletContext.getRealPath("/config/application.properties")


  @ResponseBody
  @RequestMapping(value = Array("/getThemeName"))
  def getThemeName: String = currentThemeName

  @ResponseBody
  @RequestMapping(value = Array("/switch/{name}"))
  def compare(@PathVariable("name") themeName: String, req: HttpServletRequest): util.Map[String, Any] = {
    /**
      * JSON
      * state: 1 代表更换成功，-1 代表更换失败
      * err_message: 如果更换失败就提示错误 xinxi1
      */
    Maps.mutable.of("state", if (switchTheme(themeName)) 1 else -1)
  }

  def switchTheme(targetThemeName: String): Boolean = {
    val finishRender = renderTheme(targetThemeName)
    val propertiesFile = servletContext.getRealPath("config/application.properties")
    // 判断文件替换操作与渲染操作是否成功
    if (finishRender.isRight) {
      // 写入配置文件
      val prop = new Properties()
      prop.load(new FileReader(propertiesFile))
      prop.setProperty("theme.name", targetThemeName)
      prop.store(new FileWriter(propertiesFile), s"Switch theme to $targetThemeName")
      logger.info(s"主题：$targetThemeName 切换成功")
      true
    } else {
      // TODO 异常处理
      false
    }
  }

  // TODO 修改 资源文件
  def changeSource(targetThemeName: String): Either[Exception, Boolean] = {
    Right(true)
  }

  // TODO 根据主题内容渲染 Jade 模板
  def renderTheme(targetThemeName: String): Either[Exception, Boolean] = {
    val themeFilePath = servletContext.getRealPath("/webapp/WEB_INF/views/templates")
    val themePropDir = servletContext.getRealPath("/config/themes/")
    val finishChange = changeSource(targetThemeName)
    // 检测资源文件是否修改成功如不成功，将错误递交给上一级
    if (finishChange.isLeft) {
      logger.error(s"主题：$targetThemeName 切换失败，错误为：${finishChange.left.get.getMessage}")
      return finishChange
    }

    // 正式的逻辑处理
    Right(true)
  }
}
