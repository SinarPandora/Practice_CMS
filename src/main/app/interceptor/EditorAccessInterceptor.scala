package app.interceptor

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
  * 过滤不满足编辑权限的请求
  */
@Component
class EditorAccessInterceptor extends HandlerInterceptor {
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    val session = request.getSession
    val permission = session.getAttribute("user_permission").asInstanceOf[Int]
    val isAllowed = permission >= 1
    isAllowed
  }
}
