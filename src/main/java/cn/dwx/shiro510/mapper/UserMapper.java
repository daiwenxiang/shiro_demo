package cn.dwx.shiro510.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    //根据用户名(name)获得用户
    @Select("select * from user_info where name = #{name}")
    Map getByName(String name);

    //根据用户名获得所有的角色集合
    @Select("select r.id, r.role from sys_role r join sys_user_role ur on ur.role_id = r.id where uid = #{id}")
    List<Map> getRoleByUid(@Param("id") int id);

    //根据角色id获得所有的权限集合
    @Select("select p.permission from sys_permission p join sys_role_permission rp on rp.permission_id = p.id where role_id = #{rid}")
    List<Map> getPermissionByRid(@Param("rid") int id);

    //注册
    @Insert("insert into user_info values(default, #{userName}, #{name}, #{password}, #{salt}, 0)")
    int add(Map map);
}
