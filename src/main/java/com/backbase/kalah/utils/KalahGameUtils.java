package com.backbase.kalah.utils;

import com.backbase.kalah.dto.KalahBoard;
import com.backbase.kalah.dto.KalahPlayerDTO;
import com.backbase.kalah.dto.KalahRequestDTO;
import com.backbase.kalah.exception.EmptyPitException;
import com.backbase.kalah.exception.InvalidPlayerTurnException;
import com.backbase.kalah.exception.KalahException;
import com.backbase.kalah.exception.WrongPitException;
import java.util.List;

/**
 * Created by jpv on 10/10/2017.
 */
public final class KalahGameUtils {


  public static void executePreconditions(final int pitIndex, final KalahPlayerDTO player,
      final KalahBoard kalahBoard) {
    validatePlayersTurn(player.getId(), kalahBoard.getPlayerId());
    isSelectedPitValid(pitIndex, player);
    isSelectedPitHasStones(pitIndex, kalahBoard);
  }

  public static boolean validateIfLastStoneLandsOnOwnKalah(final Integer lastPitIndex,
      final KalahPlayerDTO currentPlayer) {
    if (lastPitIndex == currentPlayer.getKalah()) {
      return true;
    }
    return false;
  }

  public static boolean checkIfLastStoneLandsOnOwnEmptyPit(final Integer lastPitIndex,
      final KalahPlayerDTO currentPlayer, final KalahBoard kalahBoard) {
    if (currentPlayer.getAllowedPits().contains(lastPitIndex)) {
      if (kalahBoard.getBoard()[lastPitIndex] == 0) {
        return true;
      }
    }
    return false;
  }


  private static void isSelectedPitValid(final int pitIndex, final KalahPlayerDTO player) {
    if (player.getAllowedPits().contains(pitIndex)) {
      return;
    }
    throw new WrongPitException("Selected pit not allowed.");
  }

  private static void isSelectedPitHasStones(final Integer pitIndex, final KalahBoard kalahBoard) {
    if (kalahBoard.getBoard()[pitIndex] >= 1) {
      return;
    }
    throw new EmptyPitException("Pit has no stones. Select another pit.");
  }

  private static void validatePlayersTurn(final Integer currentPlayer, final Integer nextPlayer) {
    if (nextPlayer != currentPlayer) {
      throw new InvalidPlayerTurnException("Current Turn: Player " + nextPlayer);
    }
  }

}
