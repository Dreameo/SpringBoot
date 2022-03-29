package com.yfh.boot.mapper;

import com.yfh.boot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserById(Integer id);
}
