package app.action

import java.util

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
      */
    Maps.mutable.of("state", if (switchTheme(themeName)) 1 else -1)
  }

  def switchTheme(targetThemeName: String): Boolean = {
    val r1 = switchSource(targetThemeName)
    val r2 = renderTheme(targetThemeName)
    val
    if (r1.isRight && r2.isRight) {
      //TODO 写配置文件
      logger.info(s"主题：$targetThemeName 切换成功")

      true
    } else {
      // TODO 异常处理
      if (r1.isLeft) {
      } else {
        r2.left
      }
    }
    false
  }


  // TODO 修改 资源文件
  def switchSource(targetThemeName: String): Either[Exception, Boolean] = {
    val themeFilePath = servletContext.getRealPath("/webapp/WEB_INF/views/templates")
    Right(true)
  }

  // TODO 根据主题内容渲染 Jade 模板
  def renderTheme(targetThemeName: String): Either[Exception, Boolean] = {
    val propertiesFile = servletContext.getRealPath("/config/application.properties")
    Right(true)
  }
}
