package com.example.overflowpaint.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
/*TODO：
* 按照华为规范进行控制层的封装，待办
* */
public class BaseRequest<T> {
    private T data;

}
