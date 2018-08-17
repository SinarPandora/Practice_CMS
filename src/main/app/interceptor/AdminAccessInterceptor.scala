package app.interceptor

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.stereotype.Component
import org.springframework.web.servlet.{HandlerInterceptor, ModelAndView}

/**
  * 过滤不满足管理员权限的管理请求
  */
@Component
class AdminAccessInterceptor extends HandlerInterceptor {
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    val session = request.getSession
    val permission = session.getAttribute("user_permission").asInstanceOf[Int]
    val isAllowed = permission >= 2
    isAllowed
  }
}
