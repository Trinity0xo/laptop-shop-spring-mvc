package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.role.RoleFilterDto;
import com.laptopstore.ecommerce.service.RoleService;
import com.laptopstore.ecommerce.specification.RoleSpecifications;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.error.RoleNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public long countRole(){
        return this.roleRepository.count();
    }

    @Override
    public void saveAllRoles(List<Role> roles){
        this.roleRepository.saveAll(roles);
    }

    @Override
    public PageResponse<List<Role>> getAllRoles(RoleFilterDto roleFilterDto){
        Specification<Role> specification = Specification.where(null);

        Pageable pageable = PaginationUtils.createPageable(
                roleFilterDto.getIntegerPage(),
                roleFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Role.class, roleFilterDto.getSortBy(), Role.DEFAULT_SORT_FIELD),
                roleFilterDto.getEnumSortDirection()
        );

        if(roleFilterDto.getSearch() != null && !roleFilterDto.getSearch().isEmpty()){
            Specification<Role> currentSpecification = RoleSpecifications.nameLike(roleFilterDto.getSearch());
            specification = specification.and(currentSpecification);
        }

        Page<Role> roles = this.roleRepository.findAll(specification, pageable);

        return new PageResponse<>(
                roles.getPageable().getPageNumber() + 1,
                roles.getTotalPages(),
                roles.getTotalElements(),
                roles.getContent()
        );
    }

    @Override
    public Role getRoleById(long roleId){
        Role role = this.roleRepository.findById(roleId).orElse(null);
        if(role == null){
            throw new RoleNotFoundException("/dashboard/role");
        }

        return role;
    }
}
