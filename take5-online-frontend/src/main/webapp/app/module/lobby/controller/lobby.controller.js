'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.lobby', []);

controllers.controller('LobbyCtrl', [
		'$scope',
		'$rootScope',
		'WebSocketManagerService',
		'ACTION',
		'$state',
		'STATUT',
		function($scope, $rootScope, WebSocketManagerService, ACTION, $state,
				STATUT) {
			
			$scope.addUser = function(data){
				$rootScope.lobby.users.push(data.user);
			};
			WebSocketManagerService.register(ACTION.USER_JOIN_LOBBY).then(null, null, 
					$scope.addUser);
			
			$scope.removeUser = function(data){
				$rootScope.lobby.users = $rootScope.lobby.users.filter(function(user){
					return user.username !== data.user.username;
				});
				console.log($rootScope.lobby.users);
			};
			WebSocketManagerService.register(ACTION.USER_QUIT_LOBBY).then(null, null, 
					$scope.removeUser);

			var checkInitGameResult = function(data) {
				if (data.state === STATUT.OK) {
					$rootScope.hand = data.hand;
					$state.go('game');
				} else {
					console.error('error during start');
				}
			};

			$scope.launchGame = function() {
				WebSocketManagerService.register(ACTION.INIT_GAME, true).then(null, null, 
						checkInitGameResult);

				WebSocketManagerService.send({
					action : ACTION.INIT_GAME
				});
			};
			
			var checkQuitLobby = function(data){
				$state.go('home');
			};
			
			$scope.quitLobby = function(){
				WebSocketManagerService.register(ACTION.QUIT_LOBBY, true).then(null, null, 
						checkQuitLobby);

				WebSocketManagerService.send({
					action : ACTION.QUIT_LOBBY,
					params : {
						uid : $rootScope.lobby.uid
					}
				});
			};

		} ]);
