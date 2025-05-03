package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import com.laptopstore.ecommerce.dto.user.UpdateAccountDto;
import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.dto.user.UserCriteriaDto;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.RoleRepository;
import com.laptopstore.ecommerce.repository.UserRepository;
import com.laptopstore.ecommerce.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PageableService pageableService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PageableService pageableService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.pageableService = pageableService;
    }

    public boolean handleCheckIfUserExistsByEmail(String email){
        return this.userRepository.existsUserByEmail(email);
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    public void handleUpdateAccountInfo(User user, UpdateAccountDto updateAccountDto) {
        user.setFirstName(updateAccountDto.getFirstName());
        user.setLastName(updateAccountDto.getLastName());
        user.setAddress(updateAccountDto.getAddress());
//        user.setAvatar(accountDto.getAvatar());
        user.setPhone(updateAccountDto.getPhone());

        this.userRepository.save(user);
    }

    public User handleCreateNewUser(RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());

        Role role = this.roleRepository.findByName("USER").orElse(null);
        if (role != null) {
            user.setRole(role);
        }

        return this.userRepository.save(user);
    }

    public Page<User> handleGetAllUsers(UserCriteriaDto userCriteriaDto) {
        Specification<User> specification = Specification.where(null);

        Pageable pageable = pageableService.handleCreatePageable(
                userCriteriaDto.getIntegerPage(),
                userCriteriaDto.getIntegerLimit(),
                userCriteriaDto.getSortBy(),
                userCriteriaDto.getEnumSortDirection());

        if (userCriteriaDto.getEmail() != null &&
                !userCriteriaDto.getEmail().isEmpty()) {
            Specification<User> currentSpecification = UserSpecification
                    .emailLike(userCriteriaDto.getEmail());
            specification = specification.and(currentSpecification);
        }

        return this.userRepository.findAll(specification, pageable);
    }

    public void handleUpdatePassword(ResetPasswordDto resetPasswordDto, User user) {
        user.setPassword(resetPasswordDto.getNewPassword());

        this.userRepository.save(user);
    }

    public User handleGetUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }
}
