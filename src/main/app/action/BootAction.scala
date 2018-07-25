package app.action


import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.{Level, LogManager}
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

// Spring 监管
@Component
class BootAction extends InitializingBean{
  private val logger = LogManager.getLogger("自启动")

  // 设置自动启动
  // TODO 后台入口
  override def afterPropertiesSet(): Unit = {
    Configurator.setLevel("org", Level.OFF)
    logger.info("---------------Spring框架预启动------------------")
    logger.info("------------------已关闭系统日志---------------------")
  }

}
