package com.autolog.controller;

import com.autolog.model.User;
import com.autolog.service.DashboardService;
import com.autolog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

@Controller
@SessionAttributes({ "user" })
public class IndexController {

    private final DashboardService dashboardService;
    private final UserService userService;

    public IndexController(DashboardService dashboardService,
                           UserService userService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
    }
    
    @GetMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }


    @GetMapping("/index")
    public String index(Model model, Principal principal) {

    	User user = (User) model.getAttribute("user");
        if (user == null) {
            user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }
    	
        var dashboard = dashboardService.getDashboard(user.getId());
        model.addAttribute("dashboard", dashboard);

        return "index";
    }
}
