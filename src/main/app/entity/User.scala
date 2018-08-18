package app.entity

import java.sql.Timestamp

import app.dao.UserTableDao
import org.springframework.security.crypto.bcrypt.BCrypt

/**
  * 用户实体，对应表：user
  * 参数名遵循数据表列名
  *
  * @param id         唯一值
  * @param name       用户名
  * @param password   密码（hash 后）
  * @param job        职称
  * @param department 所属部门
  * @param entry_time 入职时间
  * @param permission 权限
  * @param gender     性别
  * @param telephone  电话
  * @param reg_date   注册时间
  * @param note       备注
  */
class User(
            var name: String,
            var password: String,
            var job: Int,
            var department: Int,
            var entry_time: Timestamp,
            var permission: Int,
            var gender: Int,
            var telephone: String,
            val reg_date: Timestamp,
            var note: String,
            val id: Long = 0,
            implicit val userTableDao: UserTableDao = null
          ) extends Serializable {

  /**
    * 自动将明文密码转化为 hash 串，并存入对象
    *
    * @param plainPassword 明文密码
    */
  def setPassword(plainPassword: String): Unit = {
    this.password = User.hashPassword(plainPassword)
  }

  /**
    * 从数据库获取该用户的职称
    *
    * @return 职称
    */
  def getJobName = "" /*TODO 实现*/

  /**
    * 从数据库获取该用户的所属部门名称
    *
    * @return 部门名称
    */
  def getDepartmentName = "" /*TODO 实现*/

  /**
    * 从数据库获取该用户的权限名称
    *
    * @return 权限名称
    */
  def getPermissionName = "" /*TODO 实现*/

  /**
    * 获取用户性别
    *
    * @return 性别
    */
  def getGender: String = this.gender match {
    case 0 => "女"
    case 1 => "男"
    case -1 => "未定义"
  }

}

/**
  * 用户类的工具类
  */
object User {

  /**
    * 将明文密码转换为 hash 串
    *
    * @param plainPassword 明文密码
    * @return hash 串
    */
  def hashPassword(plainPassword: String): String = BCrypt.hashpw(plainPassword, BCrypt.gensalt())

  /**
    * 将 user 与 compareTo 作比较，并以排列好的字符串形式返回之间的区别
    * 返回内容将以 user 的内容为主，用于记录 log
    *
    * @param user      用户1
    * @param compareTo 用户2
    * @return 格式化的对比结果
    */
  def diff(user: User, compareTo: User): String = {
    val result = new StringBuffer()
    if              (user.name != compareTo.name)  result.append(s"      name: ${user.name} => ${compareTo.name}\n")
    if      (user.password != compareTo.password)  result.append(s"  password: Password changed")
    if          (user.gender != compareTo.gender)  result.append(s"    gender: ${user.name} => ${compareTo.name}\n")
    if                (user.job != compareTo.job)  result.append(s"       Job: ${user.getJobName} => ${compareTo.getJobName}\n")
    if  (user.department != compareTo.department)  result.append(s"department: ${user.department} => ${compareTo.department}\n")
    if  (user.entry_time != compareTo.entry_time)  result.append(s"entry_time: ${user.entry_time} => ${compareTo.entry_time}\n")
    if  (user.permission != compareTo.permission)  result.append(s"permission: ${user.getPermissionName} => ${compareTo.getPermissionName}\n")
    if    (user.telephone != compareTo.telephone)  result.append(s" telephone: ${user.name} => ${compareTo.name}\n")
    if              (user.note != compareTo.note)  result.append(s"      note: ${user.note.diff(compareTo.note)}\n")
    result.toString
  }
}
