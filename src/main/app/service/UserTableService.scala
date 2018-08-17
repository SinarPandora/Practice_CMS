package app.service

import akka.japi.Option.Some
import app.dao.UserTableDao
import app.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service


/**
  * 提供用户数据相关业务逻辑实现
  *
  * @param fromUserTable 用户表 DAO
  */
@Service
class UserTableService(@Autowired fromUserTable: UserTableDao) {

  /**
    * 根据用户输入信息判断用户是否可以登录
    *
    * @param name              用户名或电话号码（默认用户名）
    * @param password          密码
    * @param isTelephoneNumber 输入的登录凭证是否为电话号码
    * @return 验证通过 => 成功登录的用户信息
    *         验证失败 => None
    */
  def ifCanLogin(name: String, password: String, isTelephoneNumber: Boolean): Option[User] = {
    val result =
      if (isTelephoneNumber) {
        fromUserTable.getUserByPhoneNumber(name)
      } else {
        fromUserTable.getUserByName(name)
      }

    if (result.isDefined) {
      val user = result.get
      val passwordIsCorrect: Boolean = BCrypt.checkpw(password, user.password)
      if (passwordIsCorrect) {
        Some(user)
      }
    }
    None
  }

  /**
    * 根据给定的信息修改指定用户
    *
    * @param user 要修改的用户信息
    * @return 成功：返回修改的内容（以字符串形式）
    *         失败：返回错误信息字符串
    */
  def ifUpdateSuccess(user: User): Either[String, String] = {
    val originUser = fromUserTable.getUserByID(user.id)
    if (originUser.isDefined) {
      val isSuccess = fromUserTable.updateUser(user) == 1
      if (isSuccess) {
        val diff = User.diff(user, originUser.get)
        Right(diff)
      } else {
        Left("修改失败，请检查数据库是否正常")
      }
    } else {
      Left("查询失败，可能不存在需要修改的用户")
    }
  }




}
