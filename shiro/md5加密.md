# shiro 加密

## 一：MD5加密

- 在spring-shiro.xml中修改配置文件的Realm 的默认的credentialsMetcher 为

```xml
<!-- 注册shiro安全管理器 -->
<bean id="hgRealm" class="com.ybkj.happyGo.service.realm.HgRealm">
  <property name="credentialsMatcher">
    <bean class="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
      <!--加密算法的名称-->
      <property name="hashAlgorithmName" value="MD5"></property> 
       <!--配置加密的次数-->
      <property name="hashIterations" value="1024"></property> 
    </bean>
  </property>
</bean>
```

## 二:盐值

- 修改Realm使用盐值加密 完整的ShiroRealm.java

  ```java
  package com.ybkj.happyGo.service.realm;

  import java.util.ArrayList;
  import java.util.List;

  import org.apache.commons.lang3.StringUtils;
  import org.apache.shiro.SecurityUtils;
  import org.apache.shiro.authc.AuthenticationException;
  import org.apache.shiro.authc.AuthenticationInfo;
  import org.apache.shiro.authc.AuthenticationToken;
  import org.apache.shiro.authc.SimpleAuthenticationInfo;
  import org.apache.shiro.authc.UsernamePasswordToken;
  import org.apache.shiro.authc.credential.Md2CredentialsMatcher;
  import org.apache.shiro.authz.AuthorizationInfo;
  import org.apache.shiro.authz.SimpleAuthorizationInfo;
  import org.apache.shiro.realm.AuthorizingRealm;
  import org.apache.shiro.subject.PrincipalCollection;
  import org.apache.shiro.subject.Subject;
  import org.apache.shiro.util.ByteSource;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;

  import com.ybkj.happyGo.bean.AdminResourceBean;
  import com.ybkj.happyGo.bean.AdminRoleBean;
  import com.ybkj.happyGo.bean.AdminUserBean;
  import com.ybkj.happyGo.dao.AdminResourceMapper;
  import com.ybkj.happyGo.dao.AdminRoleMapper;
  import com.ybkj.happyGo.dao.AdminUserMapper;
  import com.ybkj.happyGo.service.AdminResourceService;

  /**
   * 权限控制
   * 
   * @author guozi 2018年1月29日
   */
  public class HgRealm extends AuthorizingRealm {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AdminResourceMapper adminResourceMapper;

    @Autowired
    private AdminResourceService adminResourceService;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
      // 为用户授权,只需把用户需要的权限添加到info中就可以了
      SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      // 获取用户
      Subject subject = SecurityUtils.getSubject();
      // 获取用户
      AdminUserBean user = (AdminUserBean) subject.getPrincipal();
      AdminRoleBean adminRole = null;
      String authoritys = null;
      String[] split = null;
      List<String> list = new ArrayList<>();

      if (null != user)
        // 根据用户获取对应的角色
        adminRole = adminRoleMapper.selectByPrimaryKey(user.getRoleid());

      // 根据角色获取对应的权限id字符串
      if (null != adminRole)
        authoritys = adminRole.getAuthority();

      if (StringUtils.isNotEmpty(authoritys))
        split = authoritys.split(",");

      if (null != split) {
        for (String authorityId : split) {
          AdminResourceBean adminResource = adminResourceMapper.selectByPrimaryKey(Integer.parseInt(authorityId));
          if (null != adminResource) {
            list.add(adminResource.getPermissions());
          }
        }
      }
      info.addStringPermissions(list);
      return info;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken paramAuthenticationToken)
      throws AuthenticationException {
      UsernamePasswordToken token = (UsernamePasswordToken) paramAuthenticationToken;
      String username = token.getUsername();
      AdminUserBean user = adminUserMapper.findByUsername(username);
      if (user == null) {
        return null;
      }
      // 根据用户获取对应的角色
      AdminRoleBean adminRole = adminRoleMapper.selectByPrimaryKey(user.getRoleid());
      // 根据角色获取对应的权限中的url即菜单
      List<AdminResourceBean> list = adminResourceService.selectResourceByStringId(adminRole.getAuthority());
      user.setList(list);
      
  //============盐值加密==========================================================
  //ByteSource credentialsSalt = ByteSource.Util.bytes(user.getId());// 这里的参数要给个唯一的;
  //AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, getName());
  //===================================================================================
      
      AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
      return info;
    }
  }

  ```

  ​