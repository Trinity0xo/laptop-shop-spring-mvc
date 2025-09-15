package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.user.CreateUserDto;
import com.laptopstore.ecommerce.dto.user.UpdateUserRoleDto;
import com.laptopstore.ecommerce.dto.user.UserFilterDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUserPage(
            Model model,
            UserFilterDto userFilterDto
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        PageResponse<List<User>> response = this.userService.getAllUsers(userFilterDto, email);
        model.addAttribute("response",response);
        model.addAttribute("userFilterDto", userFilterDto);

        return "/admin/user/index";
    }

    @GetMapping("/details/{userId}")
    public String showUserDetailsPage(
            Model model,
            @PathVariable long userId
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        User user = this.userService.getUserById(userId);
        model.addAttribute("user", user);

        return "/admin/user/details";
    }

    @GetMapping("/create")
    public String showCreateUserPage(
            Model model
    ){
        model.addAttribute("createUserDto", new CreateUserDto());

        return "/admin/user/create";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid CreateUserDto createUserDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            return "/admin/user/create";
        }

        this.userService.createNewUser(createUserDto);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo người dùng thành công");

        return "redirect:/dashboard/user";
    }

    @GetMapping("/update-role/{userId}")
    public String showUpdateRolePage(
            Model model,
            @PathVariable long userId
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        UpdateUserRoleDto updateUserRoleDto = this.userService.getUserInformationForRoleUpdate(userId);
        model.addAttribute("updateUserRoleDto", updateUserRoleDto);

        return "/admin/user/update_role";
    }

    @PostMapping("/update-role")
    public String updateRole(
            @Valid UpdateUserRoleDto updateUserRoleDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ){
        String email = AuthenticationUtils.getAuthenticatedName();

        if(bindingResult.hasErrors()){
            UpdateUserRoleDto newUpdateUserRoleDto = this.userService.getUserInformationForRoleUpdate(updateUserRoleDto.getId());
            updateUserRoleDto.setEmail(newUpdateUserRoleDto.getEmail());
            return "/admin/user/update_role";
        }

        this.userService.updateUserRole(updateUserRoleDto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật vai trò người dùng thành công");

        return "redirect:/dashboard/user/details/" + updateUserRoleDto.getId();
    }
}
