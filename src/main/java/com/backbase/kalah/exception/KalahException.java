package com.backbase.kalah.exception;

/**
 * Created by jpv on 10/10/2017.
 */
public class KalahException extends RuntimeException {

  public KalahException(final String msg) {
    super(msg);
  }

  public KalahException(final String msg, final Throwable throwable) {
    super(msg, throwable);
  }

}
