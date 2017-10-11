<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<script
        src="https://code.jquery.com/jquery-3.2.1.js"
        integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE="
        crossorigin="anonymous"></script>


<head>
    <meta charset="UTF-8">
    <title>Kalah</title>
</head>

<script type="text/javascript">

  $(document).ready(function () {

    var currentPlayer = $('#currentPlayer').val();
    showCurrentPlayer(currentPlayer);
    $(".clickable").click(function () {
      doMove($(this).attr("index"));
    });

  });

  function doMove(index) {
    var currentPlayer = $('#currentPlayer').val();
    var body = {
      "player_id": currentPlayer,
      "pit_index": index

    }

    $.ajax({
      url: "/api/move",
      type: "POST",
      data: JSON.stringify(body),
      contentType: "application/json; charset=utf-8",
      dataType: "json",
      success: function (data, status) {
        if (data.status) {
          $.each(data.board, function (index, value) {
            $("#pit_" + index).html(value);
          });
          showCurrentPlayer(data.playerId);
          $("#message").html(data.message);
        } else {
          $("#message").html(data.message);
        }
      }
    })
  }

  function showCurrentPlayer(playerId) {
    if (playerId == 1) {
      $('.playerOne').attr("style", "background:green");
      $('.playerTwo').attr("style", "background:gold");
    } else {
      $('.playerOne').attr("style", "background:gold");
      $('.playerTwo').attr("style", "background:green");
    }
    $('#currentPlayer').val(playerId);
  }

</script>
<style type="text/css">

    table {
        width: 90%;
    }

    td {
        width: 12.5%;
        position: relative;
    }

    td:after {
        content: '';
        display: block;
        margin-top: 100%;
    }

    td .content {
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: gold;

    }

    td .points {
        background: palevioletred;

    }

    span {
        width: 100%;
        display: block;
        text-align: center;
        font-size: 300%;
    }

    #kalah-board {
        position: absolute;
        margin: auto;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        width: 600px;
        height: 300px;
    }


</style>
<body>
<div id="kalah-board">

    <form action="/api/restart" method="get">
        <input type="submit" value="RESTART GAME"/>
    </form>
    <br/>
    <div><span id="message" class="text">First move, Player ${currentPlayer}</span></div>
    <table border="1">
        <input type="hidden" value="${currentPlayer}" id="currentPlayer"/>

        <tr>
            <td class="">
                <div class="content points">
                    <span id="pit_${playerTwo.kalah}" class="text">
                    <c:out value="${board[playerTwo.kalah]}"/> </span>
                </div>
            </td>
            <c:forEach items="${playerTwo.allowedPits}" var="item">
                <td index="${item}" class="clickable">
                    <div class="content">
                        <span id="pit_${item}" class="text"><c:out value="${board[item]}"/></span>
                    </div>
                </td>
            </c:forEach>

            <td>
                <div class="content playerTwo"><span>P2</span></div>
            </td>
        </tr>

        <tr>
            <td>
                <div class="content playerOne"><span>P1</span></div>
            </td>
            <c:forEach items="${playerOne.allowedPits}" var="item">
                <td index="${item}" class="clickable">
                    <div class="content">
                        <span id="pit_${item}" class="text"><c:out value="${board[item]}"/></span>
                    </div>
                </td>
            </c:forEach>
            <td>
                <div class="content points">
                    <span id="pit_${playerOne.kalah}" class="text">
                    <c:out value="${board[playerOne.kalah]}"/>
                    </span>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>