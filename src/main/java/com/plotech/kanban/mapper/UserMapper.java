package com.plotech.kanban.mapper;

import com.plotech.kanban.pojo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author OF00047
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-01-08 16:01:29
* @Entity com.plotech.kanban.pojo.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




