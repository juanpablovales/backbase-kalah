package com.backbase.kalah.models;

/**
 * Created by jpv on 11/10/2017.
 */
public class KalahBoard {

  private Integer[] board;
  private Integer playerId;

  public KalahBoard(Integer[] board, Integer playerId) {
    this.board = board;
    this.playerId = playerId;
  }

  public KalahBoard() {
    this.board = new Integer[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    this.playerId = 1;
  }

  public Integer[] getBoard() {
    return board;
  }

  public Integer getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Integer playerId) {
    this.playerId = playerId;
  }
}
