package cn.ant0n.gbm.types.response;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @create 2023-07-29 10:47
 */
@Data
public class BaseEvent<T> {

    private String id;
    private Date timestamp;
    private T data;

}
