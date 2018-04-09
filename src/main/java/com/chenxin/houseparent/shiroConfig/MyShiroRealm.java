package com.chenxin.houseparent.shiroConfig;

import com.chenxin.houseparent.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;


public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private AdminService adminService;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String adminName = token.getUsername();
        String password = null;
        try {
            password = this.adminService.getPasswordByAdminName(adminName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SimpleAuthenticationInfo(adminName, password, getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        String adminName = (String) super.getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Map<String, Set<String>> rolesPermissions = null;
        try {
            rolesPermissions = adminService.listRolesPermissions(adminName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<String> roles = rolesPermissions.get("roles");
        Set<String> permissions = rolesPermissions.get("permissions");
        authorizationInfo.setRoles(roles);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }
}
