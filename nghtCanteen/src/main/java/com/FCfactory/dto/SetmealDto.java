package com.FCfactory.dto;


import com.FCfactory.entity.Setmeal;
import com.FCfactory.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
