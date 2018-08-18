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
  * 需要管理员权限（Permission >= 2）的操作在该类处理
  *
  * @param userService         用户数据后台业务
  * @param jsonOrObjectBuilder GSON 工具类，提供 JSON 与对象的互相转换
  */
@Controller
@RequestMapping(Array("/app/user"))
class AdminAction(@Autowired val userService: UserTableService, @Autowired val jsonOrObjectBuilder: Gson) {

  private val addLog = LogManager.getLogger("管理员用户操作")

  /*TODO 全局的撤销*/

  /**
    * 更新用户信息
    *
    * @param requestBody 请求体（指定格式：JSON）
    * @param request     请求对象
    * @return 指定格式：JSON
    *         成功：isSuccess：True
    *         失败：isSuccess：False
    *         isError：True
    *         errorMessage：当前错误信息
    */
  @PostMapping(Array("/update"))
  @ResponseBody
  def updateUser(@RequestBody requestBody: String, request: HttpServletRequest): MutableMap[String, Any] = {
    val response: MutableMap[String, Any] = Maps.mutable.empty()
    val user = jsonToUser(requestBody)

    val illegal = user.isEmpty
    if (illegal) {
      response.put("isSuccess", true)
      response.put("isError", false)
      response.put("errorMessage", "输入内容不合法")
      return response
    }

    val userToUpdate = user.get
    val result = userService.updateUserInTable(userToUpdate)

    val updateSuccess = result.isRight
    if (updateSuccess) {
      val operatorName: String = request.getSession().getAttribute("user_name").asInstanceOf[String]
      val operatorID: Long = request.getSession().getAttribute("user_id").asInstanceOf[Long]
      val diff: String = result.getOrElse("")
      addLog.info(s"ID为${operatorID}的${operatorName}修改了ID为${userToUpdate.id}的用户信息，变更内容如下：\n$diff")
      response.put("isSuccess", true)
    } else {
      response.put("isSuccess", false)
      response.put("isError", true)
      val errorMessage = result.left.get
      response.put("errorMessage", errorMessage)
    }
    response
  }

  /**
    * 添加用户
    *
    * @param requestBody 请求体（指定格式：JSON）
    * @param request     请求对象
    * @return 指定格式：JSON
    *         成功：isSuccess：True
    *         失败：isSuccess：False
    *         isError：True
    *         errorMessage：当前错误信息
    */
  @RequestMapping(Array("/insert"))
  @ResponseBody
  def insertUser(@RequestBody requestBody: String, request: HttpServletRequest): MutableMap[String, Any] = {
    val response: MutableMap[String, Any] = Maps.mutable.empty()
    val user = jsonToUser(requestBody)

    val illegal = user.isEmpty
    if (illegal) {
      response.put("isSuccess", true)
      response.put("isError", false)
      response.put("errorMessage", "输入内容不合法")
      return response
    }

    val userToInsert = user.get
    val result = userService.insertUserIntoTable(userToInsert)

    val insertSuccess = result.isRight
    if (insertSuccess) {
      val operatorName: String = request.getSession().getAttribute("user_name").asInstanceOf[String]
      val operatorID: Long = request.getSession().getAttribute("user_id").asInstanceOf[Long]
      addLog.info(s"ID为${operatorID}的${operatorName}添加了名为${userToInsert.name}的用户(ID:${result.right.get})")
      response.put("isSuccess", true)
    } else {
      response.put("isSuccess", false)
      response.put("isError", true)
      val errorMessage = result.left.get
      response.put("errorMessage", errorMessage)
    }
    response
  }

  /**
    * 根据指定 ID 删除用户
    *
    * @param id      将删除用户的 ID
    * @param request 请求对象
    * @return 指定格式 ：JSON
    *         成功：isSuccess：True
    *         失败：isSuccess：False
    *         isError：True
    *         errorMessage：当前错误信息
    */
  @RequestMapping(Array("/delete"))
  @ResponseBody
  def deleteUser(@RequestParam("id") id: Long, request: HttpServletRequest): MutableMap[String, Any] = {
    val response: MutableMap[String, Any] = Maps.mutable.empty()

    val illegal = id > 0
    if (illegal) {
      response.put("isSuccess", true)
      response.put("isError", false)
      response.put("errorMessage", "输入内容不合法")
      return response
    }

    val result = userService.deleteUserInTable(id)
    val deleteSuccess = result.isRight
    if (deleteSuccess) {
      val deleteUserName = result.right.get
      val operatorName: String = request.getSession().getAttribute("user_name").asInstanceOf[String]
      val operatorID: Long = request.getSession().getAttribute("user_id").asInstanceOf[Long]
      addLog.info(s"ID为${operatorID}的${operatorName}删除了为${deleteUserName}的用户(ID:$id)")
      response.put("isSuccess", true)
    } else {
      response.put("isSuccess", false)
      response.put("isError", true)
      val errorMessage = result.left.get
      response.put("errorMessage", errorMessage)
    }
    response
  }


  /**
    * 工具方法，将 JSON 转换为 User 对象
    *
    * @param json 待转换的 JSON 字符串
    * @return 成功：返回转换成的 User 对象
    *         失败：返回 None
    */
  def jsonToUser(json: String): Option[User] = {
    try {
      Some(jsonOrObjectBuilder.fromJson(json, User.getClass).asInstanceOf[User])
    } catch {
      case _: Throwable => None
    }
  }


}
