package app.interceptor

import app.dao.UserTableDao
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.{HandlerInterceptor, ModelAndView}

/**
  * 过滤伪造权限数据的请求，在处理增删改的时候调用
  *
  * @param fromUserTable 用户表 DAO
  */
@Component
class PermissionFakeInterceptor(@Autowired val fromUserTable: UserTableDao) extends HandlerInterceptor {
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    val session = request.getSession()

    lazy val permissionInSession = session.getAttribute("user_permission").asInstanceOf[Int]

    val permissionInTable = {
      val userIDInSession = session.getAttribute("user_id").asInstanceOf[Long]
      val userInTable = fromUserTable.getUserByID(userIDInSession)
      val userIsExist = userInTable.isDefined
      if (userIsExist) userInTable.get.permission else 0
    }

    val isPermissionConsistent = permissionInSession == permissionInTable
    isPermissionConsistent
  }

  override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, modelAndView: ModelAndView): Unit = super.postHandle(request, response, handler, modelAndView)

  override def afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, ex: Exception): Unit = super.afterCompletion(request, response, handler, ex)
}
