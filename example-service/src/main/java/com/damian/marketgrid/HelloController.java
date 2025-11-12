package com.damian.marketgrid;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greetings")
public class HelloController {

    /**
     * authenticated endpoint
     */
    @GetMapping("/hello")
    public String getHello(@RequestParam String name, @AuthenticationPrincipal UserDetails user) {
        var roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return "Hello user " + user.getUsername() + " with roles: " + roles;
    }

    /**
     * public endpoint
     */
    @GetMapping("/hi")
    public String getHi(@RequestParam(required = false) String name) {
        if (name == null) {
            name = "there";
        }
        return "Hi, " + name + "!";
    }
}
