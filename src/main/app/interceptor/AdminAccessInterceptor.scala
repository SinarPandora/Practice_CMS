package app.interceptor

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.stereotype.Component
import org.springframework.web.servlet.{HandlerInterceptor, ModelAndView}

/**
  * 过滤不满足管理员权限的管理请求
  */
@Component
class AdminAccessInterceptor extends HandlerInterceptor {
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = super.preHandle(request, response, handler)

  override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, modelAndView: ModelAndView): Unit = super.postHandle(request, response, handler, modelAndView)

  override def afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, ex: Exception): Unit = super.afterCompletion(request, response, handler, ex)
}
