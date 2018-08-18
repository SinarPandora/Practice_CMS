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
    * @return 通过：右值：返回成功登录的用户信息
    *         失败：左值：返回 None
    */
  def checkUserExistAndPasswordCorrect(name: String, password: String, isTelephoneNumber: Boolean): Option[User] = {
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
    * @return 成功：右值：返回修改的内容（以字符串形式）
    *         失败：左值：返回错误信息字符串
    */
  def updateUserInTable(user: User): Either[String, String] = {
    val originUser = fromUserTable.getUserByID(user.id)

    val userNotExist = originUser.isEmpty
    if (userNotExist) {
      return Left("修改失败，指定用户不存在")
    }

    val isSuccess = fromUserTable.updateUser(user) == 1
    if (isSuccess) {
      val diff = User.diff(user, originUser.get)
      Right(diff)
    } else {
      Left("修改失败，重试，若继续失败，请检查数据库是否正常")
    }

  }

  /**
    * 根据给定信息添加用户
    *
    * @param user 要添加的用户
    * @return 成功：右值：返回添加的用户在表中的 id
    *         失败：左值：返回错误信息字符串
    */
  def insertUserIntoTable(user: User): Either[String, Long] = {
    val userNameAlreadyExist = fromUserTable.getUserByName(user.name).isDefined
    if (userNameAlreadyExist) return Left("添加失败，同名用户已存在")

    val userTelAlreadyExist = fromUserTable.getUserByPhoneNumber(user.telephone).isDefined
    if (userTelAlreadyExist) return Left("添加失败，同电话号码用户已存在")

    val isSuccess = fromUserTable.insertUser(user) == 1
    if (isSuccess) {
      val id = fromUserTable.getUserByName(user.name).get.id
      Right(id)
    } else {
      Left("添加失败，请重试，若继续失败，请检查数据库是否正常")
    }
  }


  /**
    * 根据给定 ID 删除用户
    *
    * @param id 将删除用户的 ID
    * @return 成功：右值：返回删除用户的用户名
    *         失败：左值：返回错误信息字符串
    */
  def deleteUserInTable(id: Long): Either[String, String] = {
    val userToDelete = fromUserTable.getUserByID(id)

    val userNotExist = userToDelete.isEmpty
    if (userNotExist) {
      return Left("删除失败，指定用户不存在")
    }

    val isSuccess = fromUserTable.deleteUserByID(id) == 1
    if (isSuccess) {
      Right(userToDelete.get.name)
    } else {
      Left("删除失败，请重试，若继续失败，请检查数据库是否正常")
    }


  }

}
