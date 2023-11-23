package com.lijinchao.entity.dto;

import com.lijinchao.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO extends User {
    private Date minDate;
    private Date maxDate;
}
