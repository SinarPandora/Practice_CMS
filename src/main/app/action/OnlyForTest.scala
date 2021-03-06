package app.action

import java.io.{FileOutputStream, FileReader, FileWriter}
import java.util
import java.util.Properties
import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.autermann.yaml.{YamlNodeFactory, Yaml => GYaml}
import io.reactivex.{Observable, Single}
import monix.eval.Task
import org.yaml.snakeyaml.Yaml
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.beans.BeanProperty


object OnlyForTest {

  val text =
    """
  accountName: Ymail Account
  username: USERNAME
  password: PASSWORD
  mailbox: INBOX
  imapServerUrl: imap.mail.yahoo.com
  protocol: imaps
  minutesBetweenChecks: 1
  usersOfInterest: [barney, betty, wilma]
  """

  class EmailAccount {
    @BeanProperty var accountName: String = null
    @BeanProperty var username: String = null
    @BeanProperty var password: String = null
    @BeanProperty var mailbox: String = null
    @BeanProperty var imapServerUrl: String = null
    @BeanProperty var minutesBetweenChecks: Int = 0
    @BeanProperty var protocol: String = null
    @BeanProperty var usersOfInterest = new java.util.ArrayList[String]()

    override def toString: String = {
      accountName
    }
  }

  class EmailAccountJK {
    @JsonProperty("accountName") var accountName: String = null
    @JsonProperty("username") var username: String = null
    @JsonProperty("password") var password: String = null
    @JsonProperty("mailbox") var mailbox: String = null
    @JsonProperty("imapServerUrl") var imapServerUrl: String = null
    @JsonProperty("minutesBetweenChecks") var minutesBetweenChecks: Int = 0
    @JsonProperty("protocol") var protocol: String = null
    @JsonProperty("usersOfInterest") var usersOfInterest = new java.util.ArrayList[String]()

    override def toString: String = {
      accountName
    }
  }

  class A(val name: String, val id: Int, val note: String)

  def main(args: Array[String]): Unit = {
    val a: Int = null.asInstanceOf[Int]
    println(a)
  }

  def towKindOfRx(): Unit = {
    // 'Rx' scala
    val task = Task(1 + 1)
    val future = task.runAsync
    println(Await.result(future, 5.seconds))

    // RxJava
    val task2 = Single.just(1 + 1)
    val future2 = task2.toFuture
    println(future2.get(5, TimeUnit.MINUTES))
  }

  def themeChanger(): Unit = {
    val configFilePath = "/Users/sinar/IdeaProjects/Ark/src/resources/config/themes/theme.dark.properties"
    val properties = new Properties()
    properties.load(new FileReader(configFilePath))
    properties.setProperty("theme.name", "dark")
    properties.store(new FileOutputStream(configFilePath), "SwitchTheme")
  }

  def yamlReader(): Unit = {
    val yamlFile = "/Users/sinar/IdeaProjects/Ark/src/resources/config/themes/test.yaml"

    // 读
    val yaml = new GYaml().load(new FileReader(yamlFile))
    if (yaml.isMap) {
      val node = yaml.asMap()
      println(node.path("accountName").textValue())
    }

    // 写
    val factory = YamlNodeFactory.createDefault()
    val mnode = factory.orderedMapNode()
    mnode.put("Hello", "world")
    mnode.dump(new FileWriter(yamlFile, true))

  }

  def yamlReaderSnake(): Unit = {
    val yamlFile = "/Users/sinar/IdeaProjects/Ark/src/resources/config/themes/test.yaml"

    // 读
    val yaml = new Yaml().load(new FileReader(yamlFile)).asInstanceOf[util.HashMap[String, Object]]
    println(yaml.get("accountName"))
  }

}
