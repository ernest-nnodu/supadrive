package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (firstname, lastname, username, salt, password) " +
            "VALUES (#{firstname}, #{lastname}, #{username}, #{salt}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);
}
