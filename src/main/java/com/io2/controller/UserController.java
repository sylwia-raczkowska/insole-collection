package com.io2.controller;

import com.io2.exception.EmailExistsException;
import com.io2.model.User;
import com.io2.model.UserDTO;
import com.io2.service.UserServiceImpl;
import com.io2.validator.UserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * Created by Niki on 2017-04-06.
 */
@Controller
public class UserController {

    private final UserServiceImpl userService;
    private final UserFormValidator userFormValidator;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Autowired
    public UserController(UserServiceImpl userService, UserFormValidator userFormValidator) {
        this.userService = userService;
        this.userFormValidator = userFormValidator;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "sign-up";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        User registeredUser;
        userFormValidator.validate(userDTO, result);
        if (result.hasErrors()) {
            modelAndView.setViewName("sign-up");
            return modelAndView;
        }
        registeredUser = createUserAccount(userDTO);

        if (registeredUser == null) {
            modelAndView.addObject("regError", "messages.regError");
            modelAndView.setViewName("sign-up");
            return modelAndView;
        }

        if (!userService.isPasswordTheSame(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            modelAndView.addObject("passwordError", "messages.passwordError");
            modelAndView.setViewName("sign-up");
            return modelAndView;
        }

        modelAndView.addObject("regSucc", "messages.regSucc");
        modelAndView.setViewName("index");
        return modelAndView;

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/welcome?logout";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginError() {
        return "index";
    }


    private User createUserAccount(UserDTO userDTO) {
        User registeredUser = null;
        try {
            registeredUser = userService.registerNewUser(userDTO);
        } catch (EmailExistsException ex) {
            return null;
        }
        return registeredUser;
    }

}
