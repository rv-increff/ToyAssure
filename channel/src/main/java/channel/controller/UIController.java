package channel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {
    @RequestMapping(value = "/ui/orders")
    public String order() {
        return "order.html";
    }
}
