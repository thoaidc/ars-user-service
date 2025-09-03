package com.ars.userservice.service;

import com.ars.userservice.dto.mapping.IRoleDTO;
import com.ars.userservice.dto.request.role.CreateRoleRequestDTO;
import com.ars.userservice.dto.request.role.UpdateRoleRequestDTO;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;

import java.util.List;

public interface RoleService {
    BaseResponseDTO getRolesWithPaging(BaseRequestDTO request);
    BaseResponseDTO getRoleDetail(Integer roleId);
    BaseResponseDTO getAuthoritiesTree();
    BaseResponseDTO createNewRole(CreateRoleRequestDTO request);
    BaseResponseDTO updateRole(UpdateRoleRequestDTO request);
    BaseResponseDTO deleteRole(Integer roleId);
    List<IRoleDTO> getUserRoles(Integer accountId);
}
