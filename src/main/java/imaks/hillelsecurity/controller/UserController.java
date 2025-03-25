package imaks.hillelsecurity.controller;

import imaks.hillelsecurity.dto.AuthDTO;
import imaks.hillelsecurity.entity.User;
import imaks.hillelsecurity.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("userId")Long userId) {
        userService.delete(userId);
        return "redirect:/users";
    }

    @GetMapping("/users/update")
    public String showUpdateUserForm(@RequestParam("userId") Long userId, Model model) {
        User user = userService.get(userId);
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/users/update")
    public String updateUser(@Valid@ModelAttribute("user") User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "update-user";
        }
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String validateAndRegister(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.create(user);
        return "redirect:/authNgo";
    }

    @GetMapping("/authNgo")
    public String authForm(Model model) {
        model.addAttribute("authDTO", new AuthDTO());
        return "auth";
    }

    @PostMapping("/authNgo")
    public String auth(@ModelAttribute("authDTO") AuthDTO dto, HttpServletResponse response, Model model) {
        String token;
        try {
            token = userService.authenticate(dto);
        } catch (BadCredentialsException e) {
            model.addAttribute("authError", "Invalid username or password");
            return "auth";
        }
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return "redirect:/users";

    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }
}
