package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.dto.auth.*;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.user.*;
import com.laptopstore.ecommerce.exception.AuthUserNotFoundException;
import com.laptopstore.ecommerce.exception.BadRequestException;
import com.laptopstore.ecommerce.exception.RoleNotFoundException;
import com.laptopstore.ecommerce.exception.UserNotFoundException;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.model.Token;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.CartItemsRepository;
import com.laptopstore.ecommerce.repository.RoleRepository;
import com.laptopstore.ecommerce.repository.UserRepository;
import com.laptopstore.ecommerce.service.*;
import com.laptopstore.ecommerce.specification.UserSpecifications;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.constant.RoleEnum;
import com.laptopstore.ecommerce.util.constant.TokenTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartItemsRepository cartItemsRepository;
    private final FileService fileService;
    private final FolderService folderService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MailService mailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CartItemsRepository cartItemsRepository, FileService fileService, FolderService folderService, PasswordEncoder passwordEncoder, TokenService tokenService, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.fileService = fileService;
        this.folderService = folderService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @Override
    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        User user = this.getUserByEmail(email);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        String newHashedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
        user.setPassword(newHashedPassword);
        userRepository.save(user);
    }

    @Override
    public CreateUserDto getInformationForCreateUser() {
        List<Role> roles = this.roleRepository.findAll();

        return new CreateUserDto(
                roles
        );
    }

    @Override
    public void updateUserRole(String email, UpdateUserRoleDto updateUserRoleDto) {
        User user = this.userRepository.findById(updateUserRoleDto.getId()).orElse(null);
        if(user == null){
            throw new UserNotFoundException(updateUserRoleDto.getId());
        }

        if(user.getEmail().equalsIgnoreCase(email)){
            throw new UserNotFoundException(updateUserRoleDto.getId());
        }

        Role role = this.roleRepository.findById(updateUserRoleDto.getNewRole().getId()).orElse(null);
        if(role == null){
            throw new RoleNotFoundException(updateUserRoleDto.getNewRole().getId());
        }

        user.setRole(role);
        this.userRepository.save(user);
    }

    @Override
    public UpdateUserRoleDto getUserInformationForRoleUpdate(String email,long userId) {
        User user = this.userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new UserNotFoundException(userId);
        }

        if(user.getEmail().equalsIgnoreCase(email)){
            throw new UserNotFoundException(userId);
        }

        Role userRole = user.getRole();
        List<Role> roles = this.roleRepository.findAll();

        return new UpdateUserRoleDto(
                user.getId(),
                user.getEmail(),
                userRole,
                roles
        );
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        Token token = this.tokenService.getToken(resetPasswordDto.getResetPasswordToken(), TokenTypeEnum.RESET_PASSWORD);
        if(token == null || token.isExpired()) {
            throw new BadRequestException("Liên kết đặt lại mật khẩu đã hết hạn hoặc không hợp lệ");
        }

        User user = token.getUser();

        if(user == null){
            throw new BadRequestException("Liên kết đặt lại mật khẩu đã hết hạn hoặc không hợp lệ");
        }

        String newHashedPassword = this.passwordEncoder.encode(resetPasswordDto.getNewPassword());
        resetPasswordDto.setNewPassword(newHashedPassword);

        this.updatePassword(resetPasswordDto, user);
        this.tokenService.deleteTokenById(token.getId());
    }

    @Override
    public ResetPasswordDto getResetPasswordInformation(String tokenValue) {
        Token resetPasswordToken = this.tokenService.getToken(tokenValue, TokenTypeEnum.RESET_PASSWORD);
        if(resetPasswordToken == null || resetPasswordToken.isExpired()) {
            throw new BadRequestException("Liên kết đặt lại mật khẩu đã hết hạn hoặc không hợp lệ");
        }

        return new ResetPasswordDto(resetPasswordToken.getValue());
    }

    @Override
    public String forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        User user = this.getUserByEmail(forgotPasswordDto.getEmail());
        if(user != null){
            String resetPasswordToken = this.tokenService.createToken(user, TokenTypeEnum.RESET_PASSWORD);
            String fullName = user.getFullName();
            this.mailService.sendResetPasswordLink(fullName, user.getEmail(), resetPasswordToken);
        }

        return "Nếu email tồn tại trong hệ thống, bạn sẽ nhận được liên kết đặt lại mật khẩu";
    }

    @Override
    public void registerAccount(RegisterDto registerDto) {
        String hashedPassword = this.passwordEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(hashedPassword);

        this.createNewUser(registerDto);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User createNewUser(RegisterDto registerDto) {
        Role role = this.roleRepository.findBySlug(RoleEnum.USER.name()).orElse(null);
        if(role == null){
            throw new RuntimeException("Không tìm thấy vai trò mặc định cho người dùng");
        }

        User user = new User(
            registerDto.getFirstName(),
            registerDto.getLastName(),
            registerDto.getEmail(),
            registerDto.getPassword(),
            role
        );

        return this.userRepository.save(user);
    }

    @Override
    public void createNewUser(CreateUserDto createUserDto) {
        Role role = this.roleRepository.findById(createUserDto.getRole().getId()).orElse(null);
        if(role == null){
            throw new RoleNotFoundException(createUserDto.getRole().getId());
        }

        String hashedPassword = this.passwordEncoder.encode(createUserDto.getPassword());

        User user = new User(
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                hashedPassword,
                role
        );

        this.userRepository.save(user);
    }

    @Override
    public PageResponse<List<User>> getAllUsers(UserFilterDto userFilterDto, String email) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        Specification<User> specification = Specification.where(UserSpecifications.notEqualId(user.getId()));

        Pageable pageable = PaginationUtils.createPageable(
                userFilterDto.getIntegerPage(),
                userFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(User.class, userFilterDto.getSortBy(), User.DEFAULT_SORT_FIELD),
                userFilterDto.getEnumSortDirection()
        );

        if (userFilterDto.getSearch() != null && !userFilterDto.getSearch().isEmpty()) {
            Specification<User> currentSpecification = UserSpecifications.emailLike(userFilterDto.getSearch());
            specification = specification.and(currentSpecification);
        }

        Page<User> users = this.userRepository.findAll(specification, pageable);

        return new PageResponse<>(
                users.getPageable().getPageNumber() + 1,
                users.getTotalPages(),
                users.getTotalElements(),
                users.getContent()
        );
    }

    @Override
    public void updatePassword(ResetPasswordDto resetPasswordDto, User user) {
        user.setPassword(resetPasswordDto.getNewPassword());

        this.userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        User user = this.userRepository.findById(id).orElse(null);
        if(user == null){
            throw new UserNotFoundException(id);
        }

        return user;
    }

    @Override
    public AuthenticatedInformationDto getAuthenticatedInformation(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        int cartItemCount = this.cartItemsRepository.countByCart(user.getCart());

        return new AuthenticatedInformationDto(
                user.getAvatar(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getSlug(),
                cartItemCount
        );
    }

    @Override
    public UserInformationDto getUserAccountInformation(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        return new UserInformationDto(
                user.getAvatar(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UpdateUserInformationDto getUserAccountInformationForUpdate(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new AuthUserNotFoundException(email);
        }

        return new UpdateUserInformationDto(
                user.getAvatar(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhone()
        );
    }

    @Override
    public void updateUserAccountInformation(String email, UpdateUserInformationDto updateAccountInfoDto){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        if(updateAccountInfoDto.getNewAvatar() != null && !updateAccountInfoDto.getNewAvatar().isEmpty()){
            String avatarsFolderName = this.folderService.getAvatarsFolderName();

            if(updateAccountInfoDto.getCurrentAvatarName() != null && !updateAccountInfoDto.getCurrentAvatarName().isEmpty()){
                this.fileService.deleteFile(updateAccountInfoDto.getCurrentAvatarName(), avatarsFolderName);
            }

            String updatedAvatar = this.fileService.uploadFile(updateAccountInfoDto.getNewAvatar(), avatarsFolderName);
            user.setAvatar(updatedAvatar);
        }

        user.setFirstName(updateAccountInfoDto.getFirstName());
        user.setLastName(updateAccountInfoDto.getLastName());
        user.setAddress(updateAccountInfoDto.getAddress());
        user.setPhone(updateAccountInfoDto.getPhone());

        this.userRepository.save(user);
    }
}