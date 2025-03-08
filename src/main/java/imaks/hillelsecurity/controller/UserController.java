package imaks.hillelsecurity.controller;

import imaks.hillelsecurity.dto.AuthDTO;
import imaks.hillelsecurity.entity.User;
import imaks.hillelsecurity.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        model.addAttribute("loginDTO", new AuthDTO());
        return "auth";
    }

    @PostMapping("/authNgo")
    public String auth(@ModelAttribute("loginDTO") AuthDTO dto, HttpServletResponse response, Model model) {
        String token = userService.authenticate(dto);
        if (token != null) {
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);
            return "redirect:/users";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "auth";
        }
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }
}
