package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.model.User;
import test.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping (value = "/admin")
public class AdminController {

    private final UserServiceImpl userService ;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    public String allUsers(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("usersList", users);
        return "admin";
    }


    @GetMapping(value = "/edit/{id}")
    public String editPage(@PathVariable("id") int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "editPage";
    }

    @PostMapping(value = "/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam String role,
                           String password) {
        userService.edit(user,role,password);
        return "redirect:/admin";
    }

    @GetMapping(value = "/add")
    public String addPage() {
        return "addPage";
    }

    @PostMapping(value = "/add")
    public String addUserByAdmin(@ModelAttribute("user") User user, @RequestParam String role){
        userService.add(user,role);
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/admin";
    }
}
