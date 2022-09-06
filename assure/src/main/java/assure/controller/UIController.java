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

    @RequestMapping(value = "/ui/channels")
    public String inventory(){
        return "channel.html";
    }

    @RequestMapping(value = "/ui/channel-listings")
    public String order(){
        return "channelListing.html";
    }

    @RequestMapping(value = "/ui/reports")
    public String reports(){
        return "reports.html";
    }

}
