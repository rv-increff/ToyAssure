package assure.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {

    @RequestMapping(value = "/ui")
    public String index(){
        return "index.html";
    }

    @RequestMapping(value = "/ui/bin-skus")
    public String bins(){
        return "binSku.html";
    }

    @RequestMapping(value = "/ui/parties")
    public String product(){
        return "party.html";
    }

    @RequestMapping(value = "/ui/inventory")
    public String inventory(){
        return "inventory.html";
    }

    @RequestMapping(value = "/ui/orders")
    public String order(){
        return "order.html";
    }

    @RequestMapping(value = "/ui/reports")
    public String reports(){
        return "reports.html";
    }

}
