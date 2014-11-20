<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test WebSockets</title>
<style type="text/css">
    table {
        border: 1px solid black;
        margin: 10px 0 10px 0;
    }
    table#gameboard tr td {
        height: 45px;
        width: 45px;
        border: 1px solid black;
    }
    table#hand tr td {
        height: 45px;
        width: 45px;
        border: 1px solid black;
        cursor: pointer;
    }
</style>
<script
  src="/take5-online-backend/json2.min.js"></script>
<script>
  window.addEventListener("load", function() {
    var socket = new WebSocket("ws://localhost:8080/take5-online-backend/game");

    socket.onopen = function(event) {
      console.log("Socket connectée")
    };

    socket.onmessage = function(event) {
      console.log("Réception d'un message");
      console.log(event);
      
      var data = JSON.parse(event.data);
      
      document.querySelector("#json").innerHTML = JSON.stringify(data, undefined, 2);
      
      if (data.state == "KO") {
        printMessage(data.reason); return;
      } else {
        printMessage("Action reçue " + data.action);
      }
      
      if (data.action == "LIST_USERS") {
        console.info("Liste des utilisateurs connectés");
        
        var list = "<ul>";
        
        for (var i = 0; i < data.users.length; i++) {
          list += "<li>" + data.users[i].username + "</li>";
        }
        
        list += "</ul>";
        
        document.querySelector("#users-list").innerHTML = list;
      } else if (data.action == "LOGIN") {
        if (data.state == "OK") {
          console.info("Connecté sous " + data.username);
        }
      } else if (data.action == "LIST_LOBBIES") {
        console.info("Liste des lobbies");
        
        var list = "<ul>";
        
        if (data.lobbies.length == 0) {
          list += "<li>Aucun lobby</li>";
        }
        
        for (var i = 0; i < data.lobbies.length; i++) {
          list += "<li>" + data.lobbies[i].name + " : ";
          
          for (var j = 0; j < data.lobbies[i].users.length; j++) {
            list += data.lobbies[i].users[j].username + " ";
          }
          
          list += " <input type=\"button\" class=\"join-lobby\" value=\"Rejoindre\" name=\"" + data.lobbies[i].uid + "\" /> <input type=\"button\" class=\"quit-lobby\" value=\"Quitter\" name=\"" + data.lobbies[i].uid + "\" /></li>";
        }
        
        list += "</ul>";
        
        document.querySelector("#lobbies-list").innerHTML = list;
        
        defineJoinHandlers();
        defineQuitHandlers();
      } else if (data.action == "CREATE_LOBBY") {
        if (data.state == "OK") {
          console.info("Lobby créé [ uid = " + data.lobby.uid + ", name = " + data.lobby.name + " ]");
        }
      } else if (data.action == "NOTIFICATION") {
        printNotification(data.notification);
      } else if (data.action == "INIT_GAME") {
        console.info("Partie démarrée");
        
        var buttons = document.querySelectorAll(".remove-column");
        
        for (var i = 0; i < buttons.length; i++) {
          (function(i) {
            buttons[i].addEventListener("click", function(event) {
              var obj = {
                  action: "REMOVE_LINE",
                  params: {
                    line: i
                  }
              };
              
              console.info("Suppression de la colonne " + i);
              
              socket.send(JSON.stringify(obj));
            });
          })(i);
        }
        
        drawBoard(data);
      } else if (data.action == "END_TURN") {
        console.info("Fin du tour, carte choisie = " + data.hand.pickedCard.value + ", choisie automatiquement = " + data.hand.pickedAuto);
        console.log(data.gameBoard);
        printNotification("Carte choisie = " + data.hand.pickedCard.value);
        drawBoard(data);
      } else if (data.action == "QUIT_LOBBY") {
        console.info("Le lobby a été quitté");
      } else if (data.action == "USER_JOIN_LOBBY") {
        printNotification("L'utilisateur " + data.user.username + " a rejoint le lobby");
      } else if (data.action == "USER_QUIT_LOBBY") {
        printNotification("L'utilisateur " + data.user.username + " a quitté le lobby");
      } else if (data.action == "USER_JOIN_SERVER") {
        printNotification("L'utilisateur " + data.user.username + " a rejoint le serveur");
      } else if (data.action == "USER_QUIT_SERVER") {
        printNotification("L'utilisateur " + data.user.username + " a quitté le serveur");
      } else if (data.action == "REMOVE_LINE") {
        printNotification("L'utilisateur " + data.user.username + " a retiré la colonne index = " + data.line);
      } else if (data.action == "REMOVE_LINE_CHOICE") {
        printNotification("L'utilisateur " + data.user.username + " doit choisir une colonne à retirer");
      }
    };

    socket.onerror = function(event) {
      console.error("Erreur sur la socket");
      console.error(event);
    };

    socket.onclose = function(event) {
      console.log("Fermeture de la connexion");
      console.log(event);
    };

    document.querySelector("#connect").addEventListener("click", function(event) {
      var sent = {
        action: "LOGIN",
        params: {
          username: document.querySelector("#username").value
        }
      };

      socket.send(JSON.stringify(sent));
    });
    
    document.querySelector("#list-users").addEventListener("click", function() {
      var sent = {
          action: "LIST_USERS",
          params: {}
      };
      
      socket.send(JSON.stringify(sent));
    });
    
    document.querySelector("#list-lobbies").addEventListener("click", function(event) {
      var sent = {
        action: "LIST_LOBBIES",
        params: {}
      };

      socket.send(JSON.stringify(sent));
    });
    
    document.querySelector("#create-lobby").addEventListener("click", function(event) {
      var sent = {
        action: "CREATE_LOBBY",
        params: {
          name: document.querySelector("#lobby-name").value
        }
      };

      socket.send(JSON.stringify(sent));
    });
    
    document.querySelector("#start-game").addEventListener("click", function(event) {
      var sent = {
        action: "INIT_GAME",
        params: {}
      };

      socket.send(JSON.stringify(sent));
    });
    
    function defineJoinHandlers() {
      var joinLobbies = document.querySelectorAll(".join-lobby");
      for (var i = 0; i < joinLobbies.length; i++) {
        joinLobbies[i].addEventListener("click", function() {
          var sent = {
              action: "JOIN_LOBBY",
              params: {
                uid: this.name
              }
          };
          
          socket.send(JSON.stringify(sent));
        });
      }
    }
    
    function defineQuitHandlers() {
      var joinLobbies = document.querySelectorAll(".quit-lobby");
      for (var i = 0; i < joinLobbies.length; i++) {
        joinLobbies[i].addEventListener("click", function() {
          var sent = {
              action: "QUIT_LOBBY",
              params: {
                uid: this.name
              }
          };
          
          socket.send(JSON.stringify(sent));
        });
      }
    }
    
    function drawBoard(data) {
      var cells = document.querySelectorAll("#gameboard tr td");
      
      var k = -1, j = -1;
      
      for (var i = 0; i < cells.length; i++) {
        if (i % 5 == 0) {
          k = k + 1;
          j = 0;
        }
        if (data.gameBoard.board[k][j]) {
          cells[i].innerHTML = data.gameBoard.board[k][j].value;
        }

        j++;
      }
      
      var cards = document.querySelectorAll("#hand tr td");
      
      for (var i = 0; i < data.hand.cards.length; i++) {
        (function(i) {
          cards[i].style.background = "white";
          cards[i].innerHTML = data.hand.cards[i].value;
          cards[i].addEventListener("click", function(event) {
            var obj = {
                action: "CARD_CHOICE",
                params: {
                  card: i,
                }
            };
            
            console.info("Carte " + i + " envoyée");
            
            socket.send(JSON.stringify(obj));
            this.style.background = "yellow";
          });
        })(i);
      }
    }
  });
  
  function printMessage(msg) {
    document.querySelector("#actions").innerHTML = msg; 
  }
  
  function printNotification(msg) {
    document.querySelector("#notifications").innerHTML = msg; 
  }
</script>
</head>
<body>
  <h1>Test WebSockets</h1>

  <pre id="json" style="float: right; border: 1px solid black; width: 800px; height: 500px; overflow: scroll;"></pre>    
    
  <div class="actions">
    Dernière action : <span style="color:red;" id="actions"></span>
  </div> 
  <div class="notifications">
    Dernière notification : <span style="color:blue;" id="notifications"></span>
  </div> 

  <table>
    <tr>
       <td>
         <h3>Test connexion pseudo</h3>
          <p>
            Pseudo : <input type="text" id="username" /><input type="button"
              id="connect" value="Connexion" />
          </p>
      </td>
      <td>
        <h3>Liste users</h3>
        <p>
          <input type="button" id="list-users" value="Récupérer les utilisateurs" />
        </p>
        <div id="users-list"></div>
      </td>
      <td>
        <h3>Liste lobbies</h3>
        <p>
          <input type="button" id="list-lobbies" value="Récupérer les lobbies" />
        </p>
        <div id="lobbies-list"></div>
      </td>
      <td>
        <h3>Créer lobby</h3>
        <p>
          Nom du lobby : <input type="text" id="lobby-name" /><input type="button" id="create-lobby" value="Créer lobby" />
        </p>
      </td>
      <td>
          <h3>Démarrer la partie</h3>
          <p>
            <input type="button" id="start-game" value="Démarrer la partie" />
          </p>
      </td>
    </tr>
  </table>
   
  <table id="gameboard">
    <tr>
        <td></td><td></td><td></td><td></td><td></td><th><input type="button" value="Supprimer" class="remove-column" /></th>
    </tr>
    <tr><td></td><td></td><td></td><td></td><td></td><th><input type="button" value="Supprimer" class="remove-column" /></th></tr>
    <tr><td></td><td></td><td></td><td></td><td></td><th><input type="button" value="Supprimer" class="remove-column" /></th></tr>
    <tr><td></td><td></td><td></td><td></td><td></td><th><input type="button" value="Supprimer" class="remove-column" /></th></tr>
  </table>

  <table id="hand">
    <tr>
        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
    </tr>
  </table>

  <hr />

  <em>Page de test take5 JSP WebSockets</em>
</body>
</html>