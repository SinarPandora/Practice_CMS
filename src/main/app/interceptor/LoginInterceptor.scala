package app.interceptor


import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.stereotype.Component
import org.springframework.web.servlet.{HandlerInterceptor, ModelAndView}


/**
  * 登录验证，未登录不能查看任何界面
  */
@Component
class LoginInterceptor extends HandlerInterceptor {

  // 客户端 => 服务器
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    /*TODO 测试*/
    val session = request.getSession
    val currentUserExist = session.getAttribute("user_id") != null
    currentUserExist
  }

  // 服务器 => 客户端
  // override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, modelAndView: ModelAndView): Unit = {}

  // 后处理
  // override def afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, ex: Exception): Unit = {}
}
