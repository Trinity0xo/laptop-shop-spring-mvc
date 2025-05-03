package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.model.Role;
import org.springframework.boot.CommandLineRunner;

import com.laptopstore.ecommerce.service.RoleService;
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
        System.out.println(">>> START INIT DATABASE");
        long countRoles = this.roleService.handleCountRole();

        if(countRoles == 0){
            List<Role> roles = new ArrayList<>();
            roles.add(new Role("ADMIN", "Admin with full access"));
            roles.add(new Role("USER", "User with limited access"));
            this.roleService.handleSaveAll(roles);
            System.out.println(">>> INIT DATABASE SUCCESS");
        }else{
            System.out.println(">>> DATABASE ALREADY EXISTS, SKIP INIT DATABASE");
        }
    }

}
