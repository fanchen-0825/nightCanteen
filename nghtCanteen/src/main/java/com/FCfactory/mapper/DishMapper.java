package com.FCfactory.mapper;

import com.FCfactory.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    //屎山代码，推翻重写
    //白给，没推成功
    //终于成功
//    @Update("update dish set status =#{status} where id=#{id}")
//    public int editStatus(int status,Long id);
}
