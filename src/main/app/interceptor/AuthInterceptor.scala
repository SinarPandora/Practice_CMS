package app.interceptor


import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.stereotype.Component
import org.springframework.web.servlet.{HandlerInterceptor, ModelAndView}

// 进入方向 客户端 => 服务器
@Component
class AuthInterceptor extends HandlerInterceptor {


  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    // 备用
    true
  }

  override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, modelAndView: ModelAndView): Unit = {

  }

  override def afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, ex: Exception): Unit = {

  }
}
