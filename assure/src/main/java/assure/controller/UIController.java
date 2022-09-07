package assure.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {

    @RequestMapping(value = "/ui")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/ui/bin-skus")
    public String bin() {
        return "binSku.html";
    }

    @RequestMapping(value = "/ui/parties")
    public String party() {
        return "party.html";
    }

    @RequestMapping(value = "/ui/channels")
    public String channel() {
        return "channel.html";
    }

    @RequestMapping(value = "/ui/channel-listings")
    public String channelListing() {
        return "channelListing.html";
    }

    @RequestMapping(value = "/ui/products")
    public String product() {
        return "product.html";
    }

    @RequestMapping(value = "/ui/orders")
    public String order() {
        return "order.html";
    }

}
