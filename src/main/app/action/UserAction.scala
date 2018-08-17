package app.action

import app.entity.User
import app.service.UserTableService
import com.google.gson.Gson
import javax.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * 提供用户权限相关业务逻辑实现
  *
  * @param userService         用户数据后台业务
  * @param jsonOrObjectBuilder GSON 工具类，提供 JSON 与对象的互相转换
  */
@Controller
@RequestMapping(Array("/app/user"))
class UserAction(@Autowired val userService: UserTableService, @Autowired val jsonOrObjectBuilder: Gson) {

  private val addLog = LogManager.getLogger("用户操作")

  /**
    * 登录检查
    *
    * @param name              请求参数：用户名
    * @param password          请求参数：密码
    * @param isTelephoneNumber 请求参数：用户名项填入内容是否为电话号码
    * @param request               HTTP 请求
    * @return 指定格式： JSON
    *         成功：isSuccess: True
    *         失败：isSuccess: False
    */
  @RequestMapping(Array("/login"))
  @ResponseBody
  def loginCheck(@RequestParam("name") name: String,
                 @RequestParam("password") password: String,
                 @RequestParam("isTelephoneNumber") isTelephoneNumber: Boolean,
                 request: HttpServletRequest): MutableMap[String, Any] = {
    val result = userService.ifCanLogin(name, password, isTelephoneNumber)
    val canLogin = result.isDefined

    val json: MutableMap[String, Any] = Maps.mutable.empty()
    if (canLogin) {
      json.put("isSuccess", true)
      val user = result.get
      jsonOrObjectBuilder.toJson(user)
      val session = request.getSession
      session.setAttribute("user_name", user.name)
      session.setAttribute("user_id", user.id)
      session.setAttribute("user_permission", user.permission)
    } else {
      json.put("isSuccess", false)
    }
    json
  }

}

