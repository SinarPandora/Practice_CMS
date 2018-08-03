package app.action

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

@Controller
@RequestMapping(value = Array("appx/"))
class JQueryTest {

  @ResponseBody
  @RequestMapping(Array("itWorks"))
  def itJustWorks = "Hello World"

  
}
