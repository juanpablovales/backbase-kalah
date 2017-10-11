package com.backbase.kalah.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jpv on 10/10/2017.
 */
public class KalahRequestDTO {

  @JsonProperty("player_id")
  private Integer playerId;
  @JsonProperty("pit_index")
  private Integer pitIndex;

  public Integer getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Integer playerId) {
    this.playerId = playerId;
  }

  public Integer getPitIndex() {
    return pitIndex;
  }

  public void setPitIndex(Integer pitIndex) {
    this.pitIndex = pitIndex;
  }
}
