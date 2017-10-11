package com.backbase.kalah.utils;

import com.backbase.kalah.exception.EmptyPitException;
import com.backbase.kalah.exception.InvalidPlayerTurnException;
import com.backbase.kalah.exception.WrongPitException;
import com.backbase.kalah.models.KalahBoardModel;
import com.backbase.kalah.models.KalahPlayerModel;

/**
 * Created by jpv on 10/10/2017.
 */
public final class KalahGameValidators {


  /**
   * This method is for executing prevalidation methods before proceeding to moving stones.
   * @param pitIndex selected index of pit
   * @param player current id of player
   * @param kalahBoardModel current kalah board object
   */
  public static void executePreconditions(final int pitIndex, final KalahPlayerModel player,
      final KalahBoardModel kalahBoardModel) {
    validatePlayersTurn(player.getId(), kalahBoardModel.getPlayerId());
    isSelectedPitValid(pitIndex, player);
    isSelectedPitHasStones(pitIndex, kalahBoardModel);
  }

  /**
   * This method is for validating if the stone landed on own Kalah. The response of this method
   * will be used to determine the next player to do the move.
   * @param lastPitIndex last index of pit based on number of stones
   * @param currentPlayer current player
   * @return true if landed on own kalah, otherwise false.
   *
   */
  public static boolean validateIfLastStoneLandsOnOwnKalah(final Integer lastPitIndex,
      final KalahPlayerModel currentPlayer) {
    if (lastPitIndex == currentPlayer.getKalah()) {
      return true;
    }
    return false;
  }

  /**
   * This method is for validating if the last stone landed on empty pit of player.
   * @param lastPitIndex last index of pit based on number of stones
   * @param currentPlayer current player
   * @param kalahBoardModel current kalah board object
   * @return
   */
  public static boolean checkIfLastStoneLandsOnOwnEmptyPit(final Integer lastPitIndex,
      final KalahPlayerModel currentPlayer, final KalahBoardModel kalahBoardModel) {
    if (currentPlayer.getAllowedPits().contains(lastPitIndex)) {
      if (kalahBoardModel.getBoard()[lastPitIndex] == 0) {
        return true;
      }
    }
    return false;
  }


  /**
   * This method is for validating if the selected pit is on the allowed pits of player
   * @param pitIndex selected index of pit
   * @param player  current player
   */
  private static void isSelectedPitValid(final int pitIndex, final KalahPlayerModel player) {
    if (player.getAllowedPits().contains(pitIndex)) {
      return;
    }
    throw new WrongPitException("Selected pit not allowed.");
  }

  /**
   * This method is for validating if the selected pit has stones.
   * @param pitIndex selected index of pit
   * @param kalahBoardModel current kalah board object
   */
  private static void isSelectedPitHasStones(final Integer pitIndex, final KalahBoardModel kalahBoardModel) {
    if (kalahBoardModel.getBoard()[pitIndex] >= 1) {
      return;
    }
    throw new EmptyPitException("Pit has no stones. Select another pit.");
  }

  /**
   * This method is for validating if the player is allowed to do the move.
   * @param currentPlayer current id of player from request
   * @param nextPlayer current player allowed to do the move
   */
  private static void validatePlayersTurn(final Integer currentPlayer, final Integer nextPlayer) {
    if (nextPlayer != currentPlayer) {
      throw new InvalidPlayerTurnException("Current Turn: Player " + nextPlayer);
    }
  }

}
