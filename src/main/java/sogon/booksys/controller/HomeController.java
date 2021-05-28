package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {


    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/fragments/common")
    public String common(){
        return "fragments/common";
    }

    @GetMapping("/membership/card")
    public String card(){
        return "membership/card";
    }
    @GetMapping("/membership/event")
    public String event(){
        return "membership/event";
    }
    @GetMapping("/membership/coupon")
    public String coupon(){
        return "membership/coupon";
    }
    @GetMapping("/customercenter/center")
    public String center(){
        return "customercenter/center";
    }
    @GetMapping("/customercenter/onetoone")
    public String onetoone(){
        return "customercenter/onetoone";
    }
    @GetMapping("/restaurant/Hi")
    public String Hi(){
        return "restaurant/Hi";
    }
    @GetMapping("/reservation/statistics")
    public String statistics(){
        return "reservation/statistics";
    }
    @GetMapping("/restaurant/introduce")
    public String introduce(){
        return "restaurant/introduce";
    }
    @GetMapping("/restaurant/location")
    public String location(){
        return "restaurant/location";
    }
    @GetMapping("/menu/lunch")
    public String lunch(){
        return "menu/lunch";
    }
    @GetMapping("/menu/dinner")
    public String dinner(){
        return "menu/dinner";
    }
}
