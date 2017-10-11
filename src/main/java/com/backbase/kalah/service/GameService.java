package com.backbase.kalah.service;

import com.backbase.kalah.dto.KalahRequestDTO;
import com.backbase.kalah.dto.KalahResponseDTO;
import com.backbase.kalah.enums.SuccessCodeEnum;
import com.backbase.kalah.models.KalahBoardModel;
import com.backbase.kalah.models.KalahPlayerModel;
import com.backbase.kalah.utils.KalahGameValidators;
import com.backbase.kalah.utils.KalahConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by jpv on 10/10/2017.
 */

@Service
public class GameService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
  private KalahPlayerModel PLAYER_ONE;
  private KalahPlayerModel PLAYER_TWO;
  public static KalahBoardModel KALAH_BOARD;

  GameService() {
    KALAH_BOARD = new KalahBoardModel();
    PLAYER_ONE = new KalahPlayerModel(1);
    PLAYER_TWO = new KalahPlayerModel(2);
  }

  /**
   * This method is for restarting the game
   */
  public void restartGame() {
    KALAH_BOARD = new KalahBoardModel();
    PLAYER_ONE = new KalahPlayerModel(1);
    PLAYER_TWO = new KalahPlayerModel(2);
  }

  /**
   * This method is for moving the stone based on the result on the conditions provided.
   * @param request - contains the pit index and current player id.
   * @return response containing updated board details
   */
  public KalahResponseDTO makeMove(final KalahRequestDTO request) {
    LOGGER.info("==== START makeMove method ====");
    KalahGameValidators.executePreconditions(request.getPitIndex(),
        getCurrentPlayer(request.getPlayerId()), KALAH_BOARD);
    Integer pitStones = KALAH_BOARD.getBoard()[request.getPitIndex()];
    Integer otherPlayerKalah = getOtherPlayer(request.getPlayerId()).getKalah();
    int counter = 0;
    KALAH_BOARD.getBoard()[request.getPitIndex()] = 0;
    boolean sameTurnFlag = false;
    boolean emptyPitFlag = false;
    for (int index = request.getPitIndex() + 1; counter < pitStones; index++) {
      if (index > KALAH_BOARD.getBoard().length - 1) {
        index = 0;
      }
      if (index != otherPlayerKalah) {
        counter++;
        if (counter == pitStones) {
          emptyPitFlag = KalahGameValidators.checkIfLastStoneLandsOnOwnEmptyPit(index,
              getCurrentPlayer(request.getPlayerId()), KALAH_BOARD);
          sameTurnFlag = KalahGameValidators.validateIfLastStoneLandsOnOwnKalah(index,
              getCurrentPlayer(request.getPlayerId()));
        }
        addStoneToPitOrKalah(index, getCurrentPlayer(request.getPlayerId()), emptyPitFlag);
      }
    }

    identifyNextPlayer(sameTurnFlag, request.getPlayerId());
    Integer code = checkAndProcessIfGameIsOver(request.getPlayerId());
    LOGGER.info("==== END makeMove method ====");
    return buildResponse(code);
  }


  private KalahResponseDTO buildResponse(final Integer code) {
    KalahResponseDTO response = new KalahResponseDTO();
    response.setBoard(KALAH_BOARD.getBoard());
    response.setPlayerId(KALAH_BOARD.getPlayerId());
    response.setCode(code);
    response.setStatus(true);
    if (SuccessCodeEnum.SUCCESS_CONTINUE.getCode() == code) {
      response.setMessage("Next Player, Player " + KALAH_BOARD.getPlayerId());
    } else if (SuccessCodeEnum.SUCCESS_WINNER.getCode() == code) {
      response.setMessage(buildWinningMessage());
    }
    return response;
  }


  /**
   * This method is used to add stone to pit or kalah.
   * Kalah - if flag is true, stone from opposite pit and own pit
   * will be added to the player's kalah
   * Pit - if flag is false, stone will be added to player's pit
   * @param pitIndex - last index where the stone landed on the board
   * @param playerDTO - current player details
   * @param flag - flag to determine if stone will be added to kalah or pit
   */
  private void addStoneToPitOrKalah(final Integer pitIndex, final KalahPlayerModel playerDTO,
      final boolean flag) {
    LOGGER.info("==== START addStoneToPitOrKalah ====");
    if (flag) {
      addStonesToPlayersKalah(1, playerDTO);
      Integer stonesFromOppositePit = getStonesFromOtherPlayersPit(playerDTO, pitIndex);
      addStonesToPlayersKalah(stonesFromOppositePit, playerDTO);

    } else {
      KALAH_BOARD.getBoard()[pitIndex] += 1;
    }
    LOGGER.info("==== END addStoneToPitOrKalah ====");

  }

  /**
   * This method is for building the winning message based on the most number of stones in Kalah.
   * @return winning message.
   */
  private String buildWinningMessage() {
    LOGGER.info("==== START buildWinningMessage ====");
    String message = "It's a tie!";
    Integer playerOneKalah = KALAH_BOARD.getBoard()[PLAYER_ONE.getKalah()];
    Integer playerTwoKalah = KALAH_BOARD.getBoard()[PLAYER_TWO.getKalah()];
    if (playerOneKalah > playerTwoKalah) {
      message = String.format("Congratulations, Player %s !", KalahConstants.PLAYER_ONE);
    } else if (playerTwoKalah > playerOneKalah) {
      message = String.format("Congratulations, Player %s !", KalahConstants.PLAYER_TWO);
    }
    LOGGER.info("==== END buildWinningMessage {} ====", message);
    return message;

  }

  /**
   * This method is called after every turn to check if there is already a winner.
   * This method will check if there any of the player's pit is already empty and
   * transfer the remaining stones from the opposite pit to the kalah of the opposite player
   * @param currentPlayerId - current player id
   * @return code where 1 is CONTINUE and 2 is WINNER
   */
  private Integer checkAndProcessIfGameIsOver(final Integer currentPlayerId) {
    LOGGER.info("==== START checkAndProcessIfGameIsOver ====");
    boolean ownPitEmptyFlag = validateIfPitsAreEmpty(getCurrentPlayer(currentPlayerId));
    boolean otherPitEmptyFlag = validateIfPitsAreEmpty(getOtherPlayer(currentPlayerId));
    if (ownPitEmptyFlag) {
      Integer totalRemainingStones = emptyAndGetRemainingStonesFromOppositePits(
          getOtherPlayer(currentPlayerId));
      addStonesToPlayersKalah(totalRemainingStones, getOtherPlayer(currentPlayerId));
      return SuccessCodeEnum.SUCCESS_WINNER.getCode();
    } else if (otherPitEmptyFlag) {
      Integer totalRemainingStones = emptyAndGetRemainingStonesFromOppositePits(
          getCurrentPlayer(currentPlayerId));
      addStonesToPlayersKalah(totalRemainingStones, getCurrentPlayer(currentPlayerId));
      return SuccessCodeEnum.SUCCESS_WINNER.getCode();
    }
    LOGGER.info("==== END checkAndProcessIfGameIsOver ====");
    return SuccessCodeEnum.SUCCESS_CONTINUE.getCode();
  }

  /**
   * This method will be called if the game is already over. This method will empty
   * the opposite side of the empty pit and return the sum of all remaining stones to be
   * be added to the kalah.
   * @param playerDTO - details of player opposite the empty pit
   * @return total number of remaining stones from opposite pits.
   */
  private Integer emptyAndGetRemainingStonesFromOppositePits(final KalahPlayerModel playerDTO) {
    LOGGER.info("==== START emptyAndGetRemainingStonesFromOppositePits ====");
    Integer totalRemainingStones = 0;
    for (Integer index : playerDTO.getAllowedPits()) {
      totalRemainingStones += KALAH_BOARD.getBoard()[index];
      KALAH_BOARD.getBoard()[index] = 0;
    }
    LOGGER.info("==== END emptyAndGetRemainingStonesFromOppositePits ====");
    return totalRemainingStones;
  }

  /**
   * Method for checking if the player's pit is empty.
   * @param playerDTO - player details that is currently being checked
   * @return true if empty otherwise false.
   */
  private boolean validateIfPitsAreEmpty(final KalahPlayerModel playerDTO) {
    for (Integer index : playerDTO.getAllowedPits()) {
      if (KALAH_BOARD.getBoard()[index] > 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method will be called if the current player stones landed on an empty.
   * This will get the stones from the opposite pit of the player's empty pit.
   *
   * @param kalahPlayerModel - current player details
   * @param lastPitIndex - index of the last pit
   * @return total number of stones from opposite pit.
   */
  private Integer getStonesFromOtherPlayersPit(final KalahPlayerModel kalahPlayerModel,
      final Integer lastPitIndex) {
    Integer pitIndex = kalahPlayerModel.getOppositePits().get(lastPitIndex);
    Integer stones = KALAH_BOARD.getBoard()[pitIndex];
    KALAH_BOARD.getBoard()[pitIndex] = 0;
    return stones;
  }

  /**
   * This method will add the stone to the player's own kalah.
   * @param stones - number of stones to be added.
   * @param playerDTO - owner of the kalah
   */
  private void addStonesToPlayersKalah(final int stones, final KalahPlayerModel playerDTO) {
    KALAH_BOARD.getBoard()[playerDTO.getKalah()] += stones;
  }

  /**
   * This method will identify the next player to play depending on the flag.
   * If flag is true, the same player will makeMove again otherwise the makeMove is for the other player.
   *
   * @param flag - true if stone landed on own Kalah, otherwise false.
   * @param currenPlayerId - current player id.
   */
  private void identifyNextPlayer(final boolean flag, final Integer currenPlayerId) {
    if (!flag) {
      if (KalahConstants.PLAYER_ONE == currenPlayerId) {
        KALAH_BOARD.setPlayerId(KalahConstants.PLAYER_TWO);
      } else {
        KALAH_BOARD.setPlayerId(KalahConstants.PLAYER_ONE);
      }
    }
  }

  private KalahPlayerModel getCurrentPlayer(int playerId) {
    if (KalahConstants.PLAYER_ONE == playerId) {
      return PLAYER_ONE;
    }
    return PLAYER_TWO;
  }

  private KalahPlayerModel getOtherPlayer(int playerId) {
    if (KalahConstants.PLAYER_ONE == playerId) {
      return PLAYER_TWO;
    }
    return PLAYER_ONE;
  }


}
