package com.backbase.kalah.controller;

import com.backbase.kalah.dto.BaseResponseDTO;
import com.backbase.kalah.exception.KalahException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by jpv on 10/10/2017.
 */
@ControllerAdvice
public class GlobalExceptionController {
  private static final Logger _LOG = LoggerFactory
      .getLogger(GlobalExceptionController.class);


  /**
   * Exception Handler for KalahException
   */
  @ExceptionHandler(KalahException.class)
  public ResponseEntity<BaseResponseDTO> handleKalahException(KalahException ex) {
    _LOG.warn(ex.getMessage(), ex);
    BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
    baseResponseDTO.setStatus(false);
    baseResponseDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);
  }

  /**
   * Exceotion handler for Exception
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponseDTO> handleException(Exception ex) {
    _LOG.warn(ex.getMessage(), ex);
    BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
    baseResponseDTO.setStatus(false);
    baseResponseDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
  }

}
