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
import better.files._
import better.files.File._, Dsl._

@Controller
@RequestMapping(Array("/app"))
class ThemeChangeAction(@Value("${theme.name}") val currentThemeName: String) {

  @Autowired val servletContext: ServletContext = null
  private val logger = LogManager.getLogger("主题切换")

  @ResponseBody
  @RequestMapping(value = Array("/getThemeName"))
  def getThemeName: String = currentThemeName

  // 更改主题接口
  @ResponseBody
  @RequestMapping(value = Array("/switch/{name}"))
  def compare(@PathVariable("name") targetThemeName: String, req: HttpServletRequest): util.Map[String, Any] = {
    /**
      * JSON
      * state: 1 代表更换成功，-1 代表更换失败
      * err_message: 如果更换失败就提示错误
      */
    val result = switchTheme(targetThemeName)
    if (result.isRight) {
      Maps.mutable.of("state", 1)
    } else {
      Maps.mutable.of("state", -1, "err_message", result.left.get)
    }
  }

  // 更改主题逻辑实现
  def switchTheme(targetThemeName: String): Either[String, Boolean] = {
    val finishReplace = copyFiles(targetThemeName)
    val propertiesFile = servletContext.getRealPath("config/application.properties")
    // 判断文件替换操作与渲染操作是否成功
    if (finishReplace.isRight) {
      // 写入配置文件
      val prop = new Properties()
      prop.load(new FileReader(propertiesFile))
      prop.setProperty("theme.name", targetThemeName)
      prop.store(new FileWriter(propertiesFile), s"Switch theme to $targetThemeName")
      logger.info(s"主题：$targetThemeName 切换成功")
      Right(true)
    } else {
      logger.error(s"主题：$targetThemeName 切换失败，原因为：${finishReplace.left.get.getMessage}")
      Left(s"主题：$targetThemeName 切换失败，原因为：${finishReplace.left.get.getMessage}")
    }
  }

  // 复制文件
  def copyFiles(targetThemeName: String): Either[Throwable, Boolean] = {
    val themeFiles = servletContext.getRealPath(s"/config/themes/$targetThemeName/").toFile
    val websiteDir = servletContext.getRealPath("/").toFile
    val viewsDir = servletContext.getRealPath("/WEB-INF/views").toFile
    val backupDir = servletContext.getRealPath("/backup").toFile

    if (backupDir.exists) {
      backupDir.clear()
    } else {
      mkdir(backupDir)
    }
    // 备份原始文件
    val packageOldSources = (backupDir/"Source.zip").zipIn(Array(websiteDir/"css", websiteDir/"js", websiteDir/"statics").filter(_.exists).toIterator)
    val packageOldViews = zip(viewsDir)(destination = backupDir/"Views.zip")

    try {
      // 删除旧文件
      Array[File](websiteDir/"css", websiteDir/"js", websiteDir/"statics").filter(_.exists).foreach(rm)
      viewsDir.clear()
      // 移动新文件
      (themeFiles/"toCopy").copyTo(websiteDir)
      (themeFiles/"toRender").copyTo(viewsDir)
      // 返回成功
      Right(true)
    } catch {
      case e:Throwable =>
        // 遇到错误，首先删除可能移动过的文件
        Array[File](websiteDir/"css", websiteDir/"js", websiteDir/"statics").filter(_.exists).foreach(rm)
        viewsDir.clear()
        // 然后恢复旧文件
        packageOldSources.unzipTo(websiteDir)
        packageOldViews.unzipTo(viewsDir)
        // 返回错误
        Left(e)
    }

  }

  // TODO 后期可以尝试做检测主题损坏的功能
}
