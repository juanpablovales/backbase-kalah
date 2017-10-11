package com.backbase.kalah.controller;

import com.backbase.kalah.dto.KalahRequestDTO;
import com.backbase.kalah.dto.KalahResponseDTO;
import com.backbase.kalah.models.KalahBoard;
import com.backbase.kalah.models.KalahPlayer;
import com.backbase.kalah.service.GameService;
import com.backbase.kalah.utils.KalahConstants;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jpv on 10/10/2017.
 */
@Controller
@RequestMapping("/api")
public class KalahGameController {

  @Autowired
  private GameService gameService;

  /**
   * API for restarting the game
   * @param model
   * @return
   */
  @GetMapping(value = "/restart")
  public String doRestartGame(Map<String, Object> model) {
    gameService.restartGame();
    model.put("board", new KalahBoard().getBoard());
    model.put("currentPlayer", KalahConstants.PLAYER_ONE);
    model.put("playerOne", new KalahPlayer(1));
    model.put("playerTwo", new KalahPlayer(2));
    return "game";
  }

  /**
   * API for moving stones from selected pit.
   * @param body
   * @return
   */
  @PostMapping(value = "/move")
  public ResponseEntity doMoveStones(@RequestBody KalahRequestDTO body) {
    KalahResponseDTO responseDTO = gameService.makeMove(body);
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  /**
   * API for starting the game.
   * @param model
   * @return
   */
  @GetMapping
  public String doStartGame(Map<String, Object> model) {
    model.put("board", new KalahBoard().getBoard());
    model.put("currentPlayer", KalahConstants.PLAYER_ONE);
    model.put("playerOne", new KalahPlayer(1));
    model.put("playerTwo", new KalahPlayer(2));

    return "game";
  }




}
