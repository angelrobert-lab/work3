package com.config;

import cn.dev33.satoken.stp.StpInterface;
import com.domain.po.UserPo;
import com.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserServiceImpl userService;

    /**
     * 返回指定用户拥有的权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissions = new ArrayList<>();

        UserPo user = userService.getById(Long.valueOf(loginId.toString()));
        if (user == null) {
            return permissions;
        }

        // 根据角色分配权限
        String role = user.getRole();
        if ("admin".equals(role)) {
            // 管理员权限
            permissions.add("user.add");
            permissions.add("user.delete");
            permissions.add("user.update");
            permissions.add("user.query");
            permissions.add("user.list");
            permissions.add("system.config");
        } else if ("user".equals(role)) {
            // 普通用户权限
            permissions.add("user.query");
            permissions.add("user.update");
        }

        return permissions;
    }

    /**
     * 返回指定用户拥有的角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roles = new ArrayList<>();

        UserPo user = userService.getById(Long.valueOf(loginId.toString()));
        if (user != null && user.getRole() != null) {
            roles.add(user.getRole());
        }

        return roles;
    }
}
