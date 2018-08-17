package app.dao

import app.entity.User
import org.springframework.stereotype.Repository

/**
  * 从用户表中查询相关数据的方法签名列表
  * 方法实现交由 Mybatis 接管
  * 相关 mapper 文件参考：resources/mapper/UserMapper.xml
  */
@Repository
abstract class UserTableDao {
  /**
    * 通过电话号码查询用户
    *
    * @param tel 电话号码
    * @return 查找到的用户
    */
  def getUserByPhoneNumber(tel: String): Option[User]

  /**
    * 通过用户名查找用户
    *
    * @param name 用户名
    * @return 查找到的用户
    */
  def getUserByName(name: String): Option[User]

  /**
    * 通过 ID 查找用户
    *
    * @param id 用户唯一 ID
    * @return 查找到的用户
    */
  def getUserByID(id: Long): Option[User]

  /**
    * 向用户表添加条目
    *
    * @param user 条目信息
    * @return 1 添加成功
    *         0 添加失败
    */
  def addUser(user: User): Int

  /**
    * 修改用户表中对应用户条目
    *
    * @param user 条目信息
    * @return 1 修改成功
    *         0 修改失败
    */
  def updateUser(user: User): Int

  /**
    * 删除用户表中指定用户
    *
    * @param user 条目信息
    * @return 1 删除成功
    *         0 删除失败
    */
  def deleteUser(user: User): Int

  /*TODO 添加用户相关表的检索*/
}
