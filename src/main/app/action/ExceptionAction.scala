package app.action

import javax.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
class ExceptionAction {

  private val logger = LogManager.getLogger("错误")

  // TODO 写入日志文件
  @ExceptionHandler(Array(classOf[Exception]))
  def errorHandler(request: HttpServletRequest, e: Exception): ModelAndView = {
    logger.error(s"发生错误，错误信息：${e.getMessage}")
    null
  }
}

//// TODO 独立的日志进程
//class LoggerRecorder extends Runnable{
//  var counter = 0
//
//  def logRecorder(): Option[Boolean] = {
//
//  }
//
//  override def run(): Unit = {
//    logRecorder()
//  }
//}
