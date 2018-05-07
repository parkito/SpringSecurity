package ru.siksmfp.spring.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.siksmfp.spring.security.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
@Controller
public class MainController {

    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("result", "");
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "choice";
        }
    }

    @RequestMapping(value = "/choice", method = RequestMethod.GET)
    public String getUserByEmail(String page) {
        switch (page) {
            case "find":
                return "redirect:/find";
            case "delete":
                return "redirect:/delete";
            case "save":
                return "redirect:/save";
            default:
                return "redirect:/error";
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public void getUserByEmail(Model model, String email) {
        try {
            model.addAttribute("result", usersService.findUserByEmail(email));
        } catch (Exception ex) {
            model.addAttribute("result", ex);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void deleteUserByEmail(Model model, String email) {
        try {
            usersService.deleteUser(email);
        } catch (Exception ex) {
            model.addAttribute("result", ex);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public void createUser(Model model, String email, String firstName, String secondName, String password) {
        try {
            usersService.createUser(email, firstName, secondName, password);
        } catch (Exception ex) {
            model.addAttribute("result", ex);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "login";
    }

    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }
}
