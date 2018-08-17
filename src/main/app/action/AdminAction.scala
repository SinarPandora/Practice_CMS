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
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, ResponseBody}

/*TODO 过滤器*/
/**
  * 需要管理员权限（Permission >= 2）的操作在该类处理
  *
  * @param userService         用户数据后台业务
  * @param jsonOrObjectBuilder GSON 工具类，提供 JSON 与对象的互相转换
  */
@Controller
@RequestMapping(Array("/app/user"))
class AdminAction(@Autowired val userService: UserTableService, @Autowired val jsonOrObjectBuilder: Gson) {

  private val addLog = LogManager.getLogger("管理员用户操作")

  /**
    * 更新用户信息
    *
    * @param requestBody 请求体（指定格式： JSON）
    * @param request         请求对象
    * @return 指定格式： JSON
    *         成功：isSuccess：True
    *         失败：isSuccess：False
    *         isError：True
    *         errorMessage：当前错误信息
    */
  @PostMapping(Array("/update"))
  @ResponseBody
  def updateUser(@RequestBody requestBody: String, request: HttpServletRequest): MutableMap[String, Any] = {
    val modifiedUser = jsonOrObjectBuilder.fromJson(requestBody, User.getClass).asInstanceOf[User]
    val result = userService.ifUpdateSuccess(modifiedUser)
    val updateSuccess = result.isRight

    val json: MutableMap[String, Any] = Maps.mutable.empty()
    if (updateSuccess) {
      val currentOperatorName: String = request.getSession().getAttribute("user_name").asInstanceOf[String]
      val currentOperatorID: Long = request.getSession().getAttribute("user_id").asInstanceOf[Long]
      val diff: String = result.getOrElse("")
      addLog.info(s"id 为 $currentOperatorID 的$currentOperatorName 修改了 id 为${modifiedUser.id} 用户的信息，变更内容如下：\n$diff")
      json.put("isSuccess", true)
    } else {
      json.put("isSuccess", false)
      val errorMessage = result.left.get
      json.put("isError", true)
      json.put("errorMessage", errorMessage)
    }
    json
  }
}
