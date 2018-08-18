package app.action

import org.eclipse.collections.impl.factory.Maps
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.ModelAndView

/**
  * 用户操作界面路由配置
  */
@Controller
class GUIRouters {

  @GetMapping(Array("/app/user/gui")) def userManagerGUI: ModelAndView =
    new ModelAndView("/pages/app", Maps.mutable.of("panel", "user"))

  @GetMapping(Array("/login")) def login = "/pages/login"

  @GetMapping(Array("app/form/gui")) def formManagerGUI:  ModelAndView =
    new ModelAndView("/pages/app", Maps.mutable.of("panel", "form"))

}
