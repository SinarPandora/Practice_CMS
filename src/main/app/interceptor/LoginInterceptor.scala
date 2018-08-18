package app.interceptor


import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor


/**
  * 登录验证，未登录不能查看任何界面
  */
@Component
class LoginInterceptor(@Value("${domain.name}") val domainName: String) extends HandlerInterceptor {

  // 客户端 => 服务器
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    /*TODO 测试*/
    val session = request.getSession
    val currentUserExist = session.getAttribute("user_id") != null
    response.sendRedirect(s"$domainName/login")
    currentUserExist
  }
}
