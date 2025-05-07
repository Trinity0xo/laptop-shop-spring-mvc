package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.user.UpdateAccountDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.FileService;
import com.laptopstore.ecommerce.service.UploadFoldersService;
import com.laptopstore.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final UploadFoldersService uploadFoldersService;
    private final FileService fileService;

    public AccountController(UserService userService, UploadFoldersService uploadFoldersService, FileService fileService) {
        this.userService = userService;
        this.uploadFoldersService = uploadFoldersService;
        this.fileService = fileService;
    }

    @GetMapping("")
    public String showAccountPage(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setFirstName(user.getFirstName());
        updateAccountDto.setLastName(user.getLastName());
        updateAccountDto.setPhone(user.getPhone());
        updateAccountDto.setAddress(user.getAddress());

        model.addAttribute("updateAccountDto", updateAccountDto);
        model.addAttribute("userAvatar", user.getAvatar());

        return "/client/account";
    }

    @PostMapping("/update")
    public String updateAccountInfo(
            @Valid UpdateAccountDto updateAccountDto,
            BindingResult bindingResult,
            @RequestParam(value = "deleteAvatarName", required = false) String deleteAvatarName,
            HttpSession session
    ){
        if (bindingResult.hasErrors()) {
            return "/client/account";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        String avatarsFolderName = this.uploadFoldersService.handleGetAvatarsFolderName();

        if(deleteAvatarName != null && !deleteAvatarName.isEmpty() && updateAccountDto.getAvatar() != null && !updateAccountDto.getAvatar().isEmpty()) {
            this.fileService.handleDeleteFile(deleteAvatarName, avatarsFolderName);
        }

        if(updateAccountDto.getAvatar() != null && !updateAccountDto.getAvatar().isEmpty()) {
            String updatedAvatar = this.fileService.handleUploadFile(updateAccountDto.getAvatar(), avatarsFolderName);
            user.setAvatar(updatedAvatar);
        }

        user = this.userService.handleUpdateAccountInfo(user, updateAccountDto);

        session.setAttribute("avatar", user.getAvatar());

        String successMessage = "Account updated successfully";

        return "redirect:/account?successMessage=" + successMessage;
    }
}
