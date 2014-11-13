<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test WebSockets</title>
<script
  src="//cdnjs.cloudflare.com/ajax/libs/json2/20140204/json2.min.js"></script>
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
      } else if (data.action == "END_TURN") {
        console.info("Fin du tour, carte choisie = " + data.card + ", choix automatique = " + data.autoChoice);
        printNotification("Carte choisie = " + data.card);
      } else if (data.action == "QUIT_LOBBY") {
        console.info("Le lobby a été quitté");
      } else if (data.action == "USER_JOIN_LOBBY") {
        printNotification("L'utilisateur " + data.user.username + " a rejoint le lobby");
      } else if (data.action == "USER_QUIT_LOBBY") {
        printNotification("L'utilisateur " + data.user.username + " a quitté le lobby");
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

  <div class="actions">
    Dernière action : <span style="color:red;" id="actions"></span>
  </div> 
  <div class="notifications">
    Dernière notification : <span style="color:blue;" id="notifications"></span>
  </div> 

  <h3>Test connexion pseudo</h3>
  <p>
    Pseudo : <input type="text" id="username" /><input type="button"
      id="connect" value="Connexion" />
  </p>

  <h3>Liste users</h3>
  <p>
    <input type="button" id="list-users" value="Récupérer les utilisateurs" />
  </p>
  <div id="users-list"></div>
  
  <h3>Liste lobbies</h3>
  <p>
    <input type="button" id="list-lobbies" value="Récupérer les lobbies" />
  </p>
  <div id="lobbies-list"></div>

  <h3>Créer lobby</h3>
  <p>
    Nom du lobby : <input type="text" id="lobby-name" /><input type="button" id="create-lobby" value="Créer lobby" />
  </p>
  
  <h3>Démarrer la partie</h3>
  <p>
    <input type="button" id="start-game" value="Démarrer la partie" />
  </p>

  <hr />

  <em>Page de test JSP WebSockets - Quentin Lebourgeois - 2014</em>
</body>
</html>