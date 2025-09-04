package com.ars.userservice.resources;

import com.ars.userservice.dto.request.role.CreateRoleRequestDTO;
import com.ars.userservice.dto.request.role.UpdateRoleRequestDTO;
import com.ars.userservice.service.RoleService;
import com.dct.config.aop.annotation.CheckAuthorize;
import com.dct.model.constants.BaseRoleConstants;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@CheckAuthorize(authorities = BaseRoleConstants.Role.ROLE)
public class RoleResource {
    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public BaseResponseDTO getAllRolesWithPaging(@ModelAttribute BaseRequestDTO request) {
        return roleService.getRolesWithPaging(request);
    }

    @GetMapping("/{roleId}")
    public BaseResponseDTO getRoleDetail(@PathVariable int roleId) {
        return roleService.getRoleDetail(roleId);
    }

    @GetMapping("/authorities")
    public BaseResponseDTO getAuthoritiesTree() {
        return roleService.getAuthoritiesTree();
    }

    @PostMapping
    @CheckAuthorize(authorities = BaseRoleConstants.Role.CREATE)
    public BaseResponseDTO createNewRole(@Valid @RequestBody CreateRoleRequestDTO requestDTO) {
        return roleService.createNewRole(requestDTO);
    }

    @PutMapping
    @CheckAuthorize(authorities = BaseRoleConstants.Role.UPDATE)
    public BaseResponseDTO updateRole(@Valid @RequestBody UpdateRoleRequestDTO requestDTO) {
        return roleService.updateRole(requestDTO);
    }

    @DeleteMapping("/{roleId}")
    @CheckAuthorize(authorities = BaseRoleConstants.Role.DELETE)
    public BaseResponseDTO deleteRole(@PathVariable int roleId) {
        return roleService.deleteRole(roleId);
    }
}
