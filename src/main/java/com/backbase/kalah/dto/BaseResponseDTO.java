package com.backbase.kalah.dto;

/**
 * Created by jpv on 10/10/2017.
 */
public class BaseResponseDTO {

  private boolean status;
  private Integer code;
  private String message;


  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
