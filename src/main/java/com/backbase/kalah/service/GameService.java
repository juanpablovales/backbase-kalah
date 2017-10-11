package com.backbase.kalah.service;

import com.backbase.kalah.dto.KalahBoard;
import com.backbase.kalah.dto.KalahPlayerDTO;
import com.backbase.kalah.dto.KalahRequestDTO;
import com.backbase.kalah.dto.KalahResponseDTO;
import com.backbase.kalah.enums.SuccessCodeEnum;
import com.backbase.kalah.utils.KalahGameUtils;
import com.backbase.kalah.utils.KalahObjects;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by jpv on 10/10/2017.
 */

@Service
public class GameService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
  private static KalahPlayerDTO PLAYER_ONE;
  private static KalahPlayerDTO PLAYER_TWO;

  public static KalahBoard KALAH_BOARD;

  static {
    KALAH_BOARD = new KalahBoard();
    PLAYER_ONE = new KalahPlayerDTO(1);
    PLAYER_TWO = new KalahPlayerDTO(2);
  }

  public void restart() {
    KALAH_BOARD = new KalahBoard();
    PLAYER_ONE = new KalahPlayerDTO(1);
    PLAYER_TWO = new KalahPlayerDTO(2);
  }

  public KalahResponseDTO move(final KalahRequestDTO request) {
    LOGGER.info("==== START move method ====");
    KalahGameUtils.executePreconditions(request.getPitIndex(),
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
          emptyPitFlag = KalahGameUtils.checkIfLastStoneLandsOnOwnEmptyPit(index,
              getCurrentPlayer(request.getPlayerId()), KALAH_BOARD);
          sameTurnFlag = KalahGameUtils.validateIfLastStoneLandsOnOwnKalah(index,
              getCurrentPlayer(request.getPlayerId()));
        }
        addStoneToPitOrKalah(index, getCurrentPlayer(request.getPlayerId()), emptyPitFlag);
      }
    }

    identifyNextPlayer(sameTurnFlag, request.getPlayerId());
    Integer code = checkAndProcessIfGameIsOver(request.getPlayerId());
    LOGGER.info("==== END move method ====");
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
      response.setMessage(identifyGameWinner());
    }
    return response;
  }


  private void addStoneToPitOrKalah(final Integer pitIndex, final KalahPlayerDTO playerDTO,
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

  private String identifyGameWinner() {
    LOGGER.info("==== START identifyGameWinner ====");
    String message = "It's a tie!";
    Integer playerOneKalah = KALAH_BOARD.getBoard()[PLAYER_ONE.getKalah()];
    Integer playerTwoKalah = KALAH_BOARD.getBoard()[PLAYER_TWO.getKalah()];
    if (playerOneKalah > playerTwoKalah) {
      message = String.format("Congratulations, Player %s !", KalahObjects.PLAYER_ONE);
    } else if (playerTwoKalah > playerOneKalah) {
      message = String.format("Congratulations, Player %s !", KalahObjects.PLAYER_TWO);
    }
    LOGGER.info("==== END identifyGameWinner ====");
    return message;

  }

  private Integer checkAndProcessIfGameIsOver(final Integer currentPlayerId) {
    LOGGER.info("==== START checkAndProcessIfGameIsOver ====");
    boolean ownPitFlag = validateIfPitsAreEmpty(getCurrentPlayer(currentPlayerId));
    boolean otherPitFlag = validateIfPitsAreEmpty(getOtherPlayer(currentPlayerId));
    if (ownPitFlag) {
      Integer totalRemainingStones = emptyAndGetRemainingStonesFromOppositePit(
          getOtherPlayer(currentPlayerId));
      addStonesToPlayersKalah(totalRemainingStones, getOtherPlayer(currentPlayerId));
      return SuccessCodeEnum.SUCCESS_WINNER.getCode();
    } else if(otherPitFlag) {
      Integer totalRemainingStones = emptyAndGetRemainingStonesFromOppositePit(
          getCurrentPlayer(currentPlayerId));
      addStonesToPlayersKalah(totalRemainingStones, getCurrentPlayer(currentPlayerId));
      return SuccessCodeEnum.SUCCESS_WINNER.getCode();
    }
    LOGGER.info("==== END checkAndProcessIfGameIsOver ====");
    return SuccessCodeEnum.SUCCESS_CONTINUE.getCode();
  }

  private Integer emptyAndGetRemainingStonesFromOppositePit(final KalahPlayerDTO playerDTO) {
    LOGGER.info("==== START emptyAndGetRemainingStonesFromOppositePit ====");
    Integer totalRemainingStones = 0;
    for (Integer index : playerDTO.getAllowedPits()) {
      totalRemainingStones += KALAH_BOARD.getBoard()[index];
      KALAH_BOARD.getBoard()[index] = 0;
    }
    LOGGER.info("==== END emptyAndGetRemainingStonesFromOppositePit ====");
    return totalRemainingStones;
  }

  private boolean validateIfPitsAreEmpty(final KalahPlayerDTO playerDTO) {
    for (Integer index : playerDTO.getAllowedPits()) {
      if (KALAH_BOARD.getBoard()[index] > 0) {
        return false;
      }
    }
    return true;
  }

  private Integer getStonesFromOtherPlayersPit(final KalahPlayerDTO kalahPlayerDTO,
      final Integer lastPitIndex) {
    Integer pitIndex = kalahPlayerDTO.getOppositePits().get(lastPitIndex);
    Integer stones = KALAH_BOARD.getBoard()[pitIndex];
    KALAH_BOARD.getBoard()[pitIndex] = 0;
    return stones;
  }

  private void addStonesToPlayersKalah(final int stones, final KalahPlayerDTO playerDTO) {
    KALAH_BOARD.getBoard()[playerDTO.getKalah()] += stones;
  }

  private void identifyNextPlayer(final boolean flag, final Integer currenPlayerId) {
    if (!flag) {
      if (KalahObjects.PLAYER_ONE == currenPlayerId) {
        KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_TWO);
      } else {
        KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_ONE);
      }
    }
  }

  private KalahPlayerDTO getCurrentPlayer(int playerId) {
    if (KalahObjects.PLAYER_ONE == playerId) {
      return PLAYER_ONE;
    }
    return PLAYER_TWO;
  }

  private KalahPlayerDTO getOtherPlayer(int playerId) {
    if (KalahObjects.PLAYER_ONE == playerId) {
      return PLAYER_TWO;
    }
    return PLAYER_ONE;
  }


}
