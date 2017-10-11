package com.backbase.kalah.dto;

/**
 * Created by jpv on 10/10/2017.
 */
public class KalahResponseDTO extends BaseResponseDTO {

  private int playerId;
  private Integer[] board;

  public int getPlayerId() {
    return playerId;
  }

  public void setPlayerId(int playerId) {
    this.playerId = playerId;
  }

  public Integer[] getBoard() {
    return board;
  }

  public void setBoard(Integer[] board) {
    this.board = board;
  }
}
