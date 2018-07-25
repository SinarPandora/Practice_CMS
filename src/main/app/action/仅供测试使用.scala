package app.action

import java.io.{File, FileReader, FileWriter}
import java.util.Properties

import de.neuland.jade4j.Jade4J

import scala.collection.JavaConverters._


object 仅供测试使用 {

  def main(args: Array[String]): Unit = {

  }

  def themeChanger(): Unit = {
    val configFilePath = "/Users/sinar/IdeaProjects/Ark/src/resources/config/themes/theme.dark.properties"
    val configFile = new Properties()
    configFile.load(new FileReader(new File(configFilePath)))


    val css = List("dark").asJava
    val model = collection.mutable.Map[String, Object]()
    model("css") = css
    model("name") = "zhangsan"

    val html = Jade4J.render("/Users/sinar/IdeaProjects/Ark/src/main/app/action/theme-header.jade", model.asJava)
    println(html)
    val page = new File("/Users/sinar/IdeaProjects/Ark/src/main/app/action/theme-header.html")
    if (page.exists()) {
      page.delete()
    }
    val writer = new FileWriter(page)
    writer.write(html)
  }

  def yamlReader(): Unit = {


  }

}
