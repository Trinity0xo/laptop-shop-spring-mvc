package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.role.RoleCriteriaDto;
import com.laptopstore.ecommerce.specification.RoleSpecification;
import com.laptopstore.ecommerce.util.SortFields;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PageableService pageableService;

    public RoleService(RoleRepository roleRepository, PageableService pageableService) {
        this.roleRepository = roleRepository;
        this.pageableService = pageableService;
    }

    public Role handleGetRoleByName(String name) {
        return this.roleRepository.findByName(name).orElse(null);
    }

    public long handleCountRole(){
        return this.roleRepository.count();
    }

    public void handleSaveAll(List<Role> roles){
        this.roleRepository.saveAll(roles);
    }

    public Page<Role> handleGetAllRoles(RoleCriteriaDto roleCriteriaDto){
        Specification<Role> specification = Specification.where(null);

        List<String> validSortBy = SortFields.getValidSortFields(Role.class);

        Pageable pageable = pageableService.handleCreatePageable(
                roleCriteriaDto.getIntegerPage(),
                roleCriteriaDto.getIntegerLimit(),
                roleCriteriaDto.getSortBy(),
                roleCriteriaDto.getEnumSortDirection(),
                validSortBy
        );

        if (roleCriteriaDto.getName() != null &&
                !roleCriteriaDto.getName().isEmpty()) {
            Specification<Role> currentSpecification = RoleSpecification
                    .nameLike(roleCriteriaDto.getName());
            specification = specification.and(currentSpecification);
        }

        return this.roleRepository.findAll(specification, pageable);
    }

    public List<Role> handleGetAllRoles(){
        return this.roleRepository.findAll();
    }

    public Role handleGetRoleById(long id){
        return this.roleRepository.findById(id).orElse(null);
    }

}
