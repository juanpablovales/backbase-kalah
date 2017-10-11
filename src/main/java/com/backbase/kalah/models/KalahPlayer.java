package com.backbase.kalah.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jpv on 11/10/2017.
 */
public class KalahPlayer {

  private int id;
  private List<Integer> allowedPits;
  private Map<Integer, Integer> oppositePits;
  private int kalah;

  public KalahPlayer(int id) {
    if (1 == id) {
      this.allowedPits = Arrays.asList(0, 1, 2, 3, 4, 5);
      this.kalah = 6;
      Map oppositePits = new HashMap();
      oppositePits.put(0, 12);
      oppositePits.put(1, 11);
      oppositePits.put(2, 10);
      oppositePits.put(3, 9);
      oppositePits.put(4, 8);
      oppositePits.put(5, 7);
      this.oppositePits = oppositePits;
    } else {
      this.allowedPits = Arrays.asList(12, 11, 10, 9, 8, 7);
      this.kalah = 13;
      Map oppositePits = new HashMap();
      oppositePits.put(7, 5);
      oppositePits.put(8, 4);
      oppositePits.put(9, 3);
      oppositePits.put(10, 2);
      oppositePits.put(11, 1);
      oppositePits.put(12, 0);
      this.oppositePits = oppositePits;
    }
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public List<Integer> getAllowedPits() {
    return allowedPits;
  }

  public int getKalah() {
    return kalah;
  }

  public void setKalah(int kalah) {
    this.kalah = kalah;
  }

  public Map<Integer, Integer> getOppositePits() {
    return oppositePits;
  }
}
