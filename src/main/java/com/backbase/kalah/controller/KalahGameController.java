package com.backbase.kalah.controller;

import com.backbase.kalah.dto.KalahBoard;
import com.backbase.kalah.dto.KalahPlayerDTO;
import com.backbase.kalah.dto.KalahRequestDTO;
import com.backbase.kalah.dto.KalahResponseDTO;
import com.backbase.kalah.service.GameService;
import com.backbase.kalah.utils.KalahObjects;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jpv on 10/10/2017.
 */
@Controller
public class KalahGameController {

  @Autowired
  private GameService gameService;

  @RequestMapping(value = "/api/restart", method = RequestMethod.GET)
  public String restart(final HttpServletRequest request,
      Map<String, Object> model) {
    gameService.restart();
    model.put("board", new KalahBoard().getBoard());
    model.put("currentPlayer", KalahObjects.PLAYER_ONE);
    model.put("playerOne", new KalahPlayerDTO(1));
    model.put("playerTwo", new KalahPlayerDTO(2));
    return "game";
  }

  @RequestMapping(value = "/api/move", method = RequestMethod.POST)
  public ResponseEntity move(final HttpServletRequest request,
      @RequestBody KalahRequestDTO body) {
    KalahResponseDTO responseDTO = gameService.move(body);
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }


  @RequestMapping(value = "/api", method = RequestMethod.GET)
  public String game(final HttpServletRequest request,
      Map<String, Object> model) {
    model.put("board", new KalahBoard().getBoard());
    model.put("currentPlayer", KalahObjects.PLAYER_ONE);
    model.put("playerOne", new KalahPlayerDTO(1));
    model.put("playerTwo", new KalahPlayerDTO(2));

    return "game";
  }




}
