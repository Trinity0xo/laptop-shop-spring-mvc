package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.dto.auth.AuthenticatedInformationDto;
import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.user.*;
import com.laptopstore.ecommerce.dto.auth.RegisterDto;
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
import com.laptopstore.ecommerce.util.error.BadRequestException;
import com.laptopstore.ecommerce.util.error.NotFoundException;
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
    public void updateUserRole(UpdateUserRoleDto updateUserRoleDto) {
        User user = this.userRepository.findById(updateUserRoleDto.getId()).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        Role role = this.roleRepository.findBySlug(updateUserRoleDto.getRole().name()).orElse(null);
        if(role == null){
            throw new NotFoundException("Không tìm thấy vai trò");
        }

        user.setRole(role);
        this.userRepository.save(user);
    }

    @Override
    public UpdateUserRoleDto getUserInformationForRoleUpdate(long userId) {
        User user = this.userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        RoleEnum role = RoleEnum.valueOf(user.getRole().getSlug());

        return new UpdateUserRoleDto(
                user.getId(), user.getEmail(), role
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
            throw new BadRequestException("User not found");
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

        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng");
        }

        String resetPasswordToken = this.tokenService.createToken(user, TokenTypeEnum.RESET_PASSWORD);
        String fullName = user.getFullName();

        this.mailService.sendResetPasswordLink(fullName, user.getEmail(), resetPasswordToken);

        return user.getEmail();
    }

    @Override
    public void registerAccount(RegisterDto registerDto) {
        String hashedPassword = this.passwordEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(hashedPassword);

        this.createNewUser(registerDto);
    }

    @Override
    public boolean checkIfUserExistsByEmail(String email){
        return this.userRepository.existsUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User createNewUser(RegisterDto registerDto) {
        Role role = this.roleRepository.findBySlug(RoleEnum.USER.name()).orElse(null);
        if(role == null){
            throw new NotFoundException("không tìm thấy vai trò cho người dùng");
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
        Role role = this.roleRepository.findBySlug(createUserDto.getRole().name()).orElse(null);
        if(role == null){
            throw new NotFoundException("Không tìm thấy vai trò");
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
            throw new NotFoundException("Không tìm thấy người dùng");
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
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        return user;
    }

    @Override
    public AuthenticatedInformationDto getAuthenticatedInformation(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        int cartItemCount = this.cartItemsRepository.countByCart(user.getCart());

        return new AuthenticatedInformationDto(
                user.getAvatar(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getName(),
                user.getRole().getSlug(),
                cartItemCount
        );
    }

    @Override
    public UserInformationDto getUserAccountInformation(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
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
            throw new NotFoundException("Không tìm thấy người dùng");
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
            throw new NotFoundException("Không tìm thấy người dùng");
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