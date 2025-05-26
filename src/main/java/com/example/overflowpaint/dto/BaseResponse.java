package com.example.overflowpaint.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
/*TODO：
 * 按照华为规范进行控制层的封装，待办
 * */
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
