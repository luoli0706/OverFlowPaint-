package com.example.overflowpaint.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseRequest<T> {
    private T data;

}
