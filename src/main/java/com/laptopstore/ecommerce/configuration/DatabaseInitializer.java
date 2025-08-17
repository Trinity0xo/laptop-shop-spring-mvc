package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.service.RoleService;
import com.laptopstore.ecommerce.util.constant.RoleEnum;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleService roleService;

    public DatabaseInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INITIALIZING DATABASE");

        long countRoles = this.roleService.countRole();
        if (countRoles == 0) {
            List<Role> roles = new ArrayList<>();

            for (RoleEnum roleEnum : RoleEnum.values()) {
                roles.add(new Role(
                        roleEnum.getDisplayName(),
                        roleEnum.name(),
                        getRoleDescription(roleEnum)
                ));
            }

            this.roleService.saveAllRoles(roles);
            System.out.println(">>> DATABASE INITIALIZATION SUCCESS");
        } else {
            System.out.println(">>> DATABASE ALREADY EXISTS, SKIP INITIALIZATION");
        }
    }

    private String getRoleDescription(RoleEnum roleEnum) {
        switch (roleEnum) {
            case SUPER_ADMIN:
                return "Có mọi quyền hạn trong hệ thống";
            case OWNER:
                return "Quản lý tài nguyên thuộc sở hữu và giám sát một số nhiệm vụ quản trị";
            case USER:
                return "Người dùng thông thường với quyền truy cập các chức năng cơ bản";
            default:
                return "";
        }
    }
}
