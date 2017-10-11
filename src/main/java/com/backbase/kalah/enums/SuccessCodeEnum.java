package com.backbase.kalah.enums;

/**
 * Created by jpv on 10/10/2017.
 */
public enum SuccessCodeEnum {
  SUCCESS_CONTINUE(1),
  SUCCESS_WINNER(2),
  ;

  private Integer code;

  SuccessCodeEnum(Integer code) {
    this.code = code;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
