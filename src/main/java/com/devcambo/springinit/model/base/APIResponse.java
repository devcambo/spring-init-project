package com.devcambo.springinit.model.base;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class APIResponse {
    private boolean flag;
    private int code;
    private String message;
    private Object data;

    public APIResponse(boolean flag, int code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }
}
