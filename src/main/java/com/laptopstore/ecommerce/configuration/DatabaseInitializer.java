package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.service.RoleService;
import com.laptopstore.ecommerce.util.constant.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final RoleService roleService;

    public DatabaseInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(">>> START INITIALIZING DATABASE");

        long countRoles = this.roleService.countRole();
        if (countRoles == 0) {
            List<Role> roles = new ArrayList<>();

            for (RoleEnum roleEnum : RoleEnum.values()) {
                roles.add(new Role(
                        roleEnum.getDisplayName(),
                        roleEnum.name()
                ));
            }

            this.roleService.saveAllRoles(roles);
            log.info(">>> DATABASE INITIALIZATION SUCCESS, saved {} roles", roles.size());
        } else {
            log.info(">>> DATABASE ALREADY EXISTS, SKIP INITIALIZATION");
        }
    }
}
