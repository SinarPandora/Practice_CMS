package app.action

import javax.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
class ExceptionAction {

  private val logger = LogManager.getLogger("错误")

  @ExceptionHandler(Array(classOf[Exception]))
  def errorHandler(request: HttpServletRequest, e: Exception): ModelAndView = {
    logger.error(s"错误信息：${e.getStackTrace}")
    null
  }
}
