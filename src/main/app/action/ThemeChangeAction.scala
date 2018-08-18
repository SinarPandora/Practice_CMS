package app.action

import java.io.{FileReader, FileWriter}
import java.util.Properties

import better.files.Dsl._
import better.files._
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * 主题更换相关逻辑实现
  *
  * @param currentThemeName 当前主题名称（从配置文件读取）
  */
/*TODO 给每个用户单独记录配置*/
@Controller
@RequestMapping(Array("/app/config"))
class ThemeChangeAction(@Value("${theme.name}") val currentThemeName: String) {

  @Autowired val servletContext: ServletContext = null
  private val addLog = LogManager.getLogger("主题切换")

  /**
    * 获取当前主题名称
    *
    * @return 当前主题名称
    */
  @ResponseBody
  @RequestMapping(value = Array("/getThemeName"))
  def getThemeName: String = currentThemeName

  /**
    * 更改当前主题（对外 API）
    *
    * @param targetThemeName 目标主题名称
    * @param request         请求对象
    * @return 指定格式：JSON
    *         成功：isSuccess：True
    *         失败：isSuccess：False
    *         isError：True
    *         errorMessage：当前错误信息
    */
  @ResponseBody
  @RequestMapping(value = Array("/switch/{name}"))
  def switchTheme(@PathVariable("name") targetThemeName: String, request: HttpServletRequest): MutableMap[String, Any] = {
    val response: MutableMap[String, Any] = Maps.mutable.empty()
    val result = switchTheme(targetThemeName)

    val isSuccess = result.isRight
    if (isSuccess) {
      response.put("isSuccess", true)
    } else {
      val errorMessage = result.left.get
      response.put("isSuccess", false)
      response.put("isError", true)
      response.put("errorMessage", errorMessage)
    }
    response
  }

  /**
    * 更改主题逻辑实现
    *
    * @param targetThemeName 目标主题名称
    * @return 成功：返回 True
    *         并写入配置文件
    *         失败：返回错误信息
    */
   private def switchTheme(targetThemeName: String): Either[String, Boolean] = {
    val result = copyFiles(targetThemeName)
    val propertiesFile = servletContext.getRealPath("config/application.properties")

    val finishReplace = result.isRight
    if (finishReplace) {
      val prop = new Properties()
      prop.load(new FileReader(propertiesFile))
      prop.setProperty("theme.name", targetThemeName)
      prop.store(new FileWriter(propertiesFile), s"Switch theme to $targetThemeName")
      addLog.info(s"主题：${targetThemeName}切换成功")
      Right(true)
    } else {
      val errorInfo = result.left.get.getStackTrace
      addLog.error(s"主题：${targetThemeName}切换失败，原因为：$errorInfo")
      Left(s"主题切换失败，请重试，若再次失败，请联系技术人员")
    }
  }

  /**
    * 主题相关复制文件逻辑
    *
    * @param targetThemeName 目标主题名称
    * @return 成功：返回 True
    *         错误：回滚，并返回 Throwable 对象
    */
  private def copyFiles(targetThemeName: String): Either[Throwable, Boolean] = {
    val themeFiles = servletContext.getRealPath(s"/config/themes/$targetThemeName/").toFile
    val websiteDir = servletContext.getRealPath("/").toFile
    val viewsDir = servletContext.getRealPath("/WEB-INF/views").toFile
    val backupDir = servletContext.getRealPath("/backup").toFile

    if (backupDir.exists) {
      backupDir.clear()
    } else {
      mkdir(backupDir)
    }

    val packageOldSources = (backupDir / "Source.zip").zipIn(Array(websiteDir / "css", websiteDir / "js", websiteDir / "statics").filter(_.exists).toIterator)
    val packageOldViews = zip(viewsDir)(destination = backupDir / "Views.zip")

    try {
      Array[File](websiteDir / "css", websiteDir / "js", websiteDir / "statics").filter(_.exists).foreach(rm)
      viewsDir.clear()
      (themeFiles / "toCopy").copyTo(websiteDir)
      (themeFiles / "toRender").copyTo(viewsDir)
      Right(true)
    } catch {
      case e: Throwable =>
        Array[File](websiteDir / "css", websiteDir / "js", websiteDir / "statics").filter(_.exists).foreach(rm)
        viewsDir.clear()
        packageOldSources.unzipTo(websiteDir)
        packageOldViews.unzipTo(viewsDir)
        Left(e)
    }

  }

  // TODO 后期可以尝试做检测主题损坏的功能
}
