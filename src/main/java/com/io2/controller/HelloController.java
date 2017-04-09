package com.io2.controller;

import com.io2.model.Sneaker;
import com.io2.model.User;
import com.io2.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Niki on 2017-03-22.
 */

@org.springframework.stereotype.Controller
public class HelloController {

    @Autowired
    private MyUserDetailsService userDetailsService;
    private static final Logger LOGGER = Logger.getLogger(HelloController.class.getName());

    @RequestMapping("/")
    public String main() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        LOGGER.log(Level.INFO, "!!!!!!!!!!!!" + authorities);
        return "index";
    }

    @RequestMapping("/welcome")
    public String hello() {
        return "index";
    }

    @RequestMapping("/loginSuccess")
    public String afterLogin(@ModelAttribute("user") User user) {
        return "index";
    }

    @RequestMapping(value = "/collection", method = RequestMethod.GET)
    public String collection() {
        return "collection";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String form() {
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String formSupport(@ModelAttribute("form") @Valid Sneaker form, BindingResult result) {
        if (result.hasErrors()) {
            return "add";
        } else {
            //formularz wypełniony prawidłowo
            return "add";
        }
    }
}
