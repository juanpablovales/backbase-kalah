package com.backbase.kalah.service;

import com.backbase.kalah.dto.KalahBoard;
import com.backbase.kalah.dto.KalahRequestDTO;
import com.backbase.kalah.dto.KalahResponseDTO;
import com.backbase.kalah.exception.EmptyPitException;
import com.backbase.kalah.exception.InvalidPlayerTurnException;
import com.backbase.kalah.exception.WrongPitException;
import com.backbase.kalah.utils.KalahObjects;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jpv on 10/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

  private GameService service = new GameService();

  @Test
  public void testValidMoveForPlayerOne() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0},
            KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(0);
    requestDTO.setPlayerId(1);
    KalahResponseDTO responseDTO = service.move(requestDTO);
    Integer[] expectedResult = {0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0};
    Assert.assertArrayEquals(expectedResult, responseDTO.getBoard());
  }

  @Test
  public void testValidMoveForPlayerTwo() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0},
            KalahObjects.PLAYER_TWO);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(7);
    requestDTO.setPlayerId(2);
    KalahResponseDTO responseDTO = service.move(requestDTO);
    Integer[] expectedResult = {0, 7, 7, 7, 7, 7, 1, 0, 7, 7, 7, 7, 7, 1};
    Assert.assertArrayEquals(expectedResult, responseDTO.getBoard());
  }

  @Test
  public void testValidMoveForPlayerOne_lastStoneOnEmptyPit() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{2, 1, 0, 10, 10, 3, 4, 0, 11, 10, 9, 9, 1, 2},
            KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(1);
    requestDTO.setPlayerId(1);
    KalahResponseDTO responseDTO = service.move(requestDTO);
    Integer[] expectedResult = {2, 0, 0, 10, 10, 3, 14, 0, 11, 10, 0, 9, 1, 2};
    Assert.assertArrayEquals(expectedResult, responseDTO.getBoard());

  }

  @Test
  public void testValidMoveForPlayerTwo_lastStoneOnEmptyPit() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{0, 10, 9, 0, 8, 0, 3, 1, 0, 11, 10, 9, 9, 2},
            KalahObjects.PLAYER_TWO);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(7);
    requestDTO.setPlayerId(2);
    KalahResponseDTO responseDTO = service.move(requestDTO);
    Integer[] expectedResult = {0, 10, 9, 0, 0, 0, 3, 0, 0, 11, 10, 9, 9, 11};
    Assert.assertArrayEquals(expectedResult, responseDTO.getBoard());
  }

  @Test
  public void testValidMove_gameOver() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{0, 0, 0, 0, 0, 1, 11, 10, 10, 5, 5, 10, 10, 10},
            KalahObjects.PLAYER_ONE);
    GameService.KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(5);
    requestDTO.setPlayerId(1);
    KalahResponseDTO responseDTO = service.move(requestDTO);
    Integer[] expectedResult = {0, 0, 0, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 60};
    Assert.assertTrue(2 == responseDTO.getCode());
    Assert.assertArrayEquals(expectedResult, responseDTO.getBoard());

  }

  @Test
  public void testValidMovePlayerOne_gameOver_oppositeEmptyPit() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{10, 1, 0, 0, 0, 0, 33, 0, 0, 0, 1, 0, 0, 27},
            KalahObjects.PLAYER_ONE);
    GameService.KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(1);
    requestDTO.setPlayerId(1);
    KalahResponseDTO responseDTO = service.move(requestDTO);
    Integer[] expectedResult = {0, 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 0, 0, 27};
    Assert.assertTrue(2 == responseDTO.getCode());
    Assert.assertArrayEquals(expectedResult, responseDTO.getBoard());
  }

  @Test(expected = WrongPitException.class)
  public void testInvalidMove_wrongPit() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{10, 1, 0, 0, 0, 0, 33, 0, 0, 0, 1, 0, 0, 27},
            KalahObjects.PLAYER_ONE);
    GameService.KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(8);
    requestDTO.setPlayerId(1);
    service.move(requestDTO);
  }

  @Test(expected = EmptyPitException.class)
  public void testInvalidMove_emptyPit() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{10, 1, 0, 0, 0, 0, 33, 0, 0, 0, 1, 0, 0, 27},
            KalahObjects.PLAYER_ONE);
    GameService.KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(2);
    requestDTO.setPlayerId(1);
    service.move(requestDTO);
  }

  @Test(expected = InvalidPlayerTurnException.class)
  public void testInvalidMove_invalidPlayerTurnt() {
    GameService.KALAH_BOARD =
        new KalahBoard(new Integer[]{10, 1, 0, 0, 0, 0, 33, 0, 0, 0, 1, 0, 0, 27},
            KalahObjects.PLAYER_ONE);
    GameService.KALAH_BOARD.setPlayerId(KalahObjects.PLAYER_ONE);
    KalahRequestDTO requestDTO = new KalahRequestDTO();
    requestDTO.setPitIndex(2);
    requestDTO.setPlayerId(2);
    service.move(requestDTO);
  }






}
