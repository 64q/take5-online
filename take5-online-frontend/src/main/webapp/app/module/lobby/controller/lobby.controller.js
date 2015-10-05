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
		'MessageService',
		function($scope, $rootScope, WebSocketManagerService, ACTION, $state,
				STATUT, MessageService) {

			/**
			 * Add user
			 */
			$scope.addUser = function(data) {
				$rootScope.lobby.users.push(data.user);
			};
			WebSocketManagerService.register(ACTION.USER_JOIN_LOBBY).then(null,
					null, $scope.addUser);

			/**
			 * Remove user
			 */
			$scope.removeUser = function(data) {
				$rootScope.lobby.users = $rootScope.lobby.users
						.filter(function(user) {
							return user.username !== data.user.username;
						});
				console.log($rootScope.lobby.users);
			};
			WebSocketManagerService.register(ACTION.USER_QUIT_LOBBY).then(null,
					null, $scope.removeUser);

			/**
			 * Launch game
			 */
			var checkInitGameResult = function(data) {
				if (data.state === STATUT.OK) {
					$rootScope.hand = data.hand;
					$rootScope.gameBoard = data.gameBoard;
					MessageService.clearMessages();
					MessageService.addMessageCode('game', 'PICK_CARD');
					$state.go('game');
				} else {
					MessageService.clearMessages();
					MessageService.addMessageCode('lobby', data.code, true);
				}
			};

			WebSocketManagerService.register(ACTION.INIT_GAME, true).then(null,
					null, checkInitGameResult);

			$scope.launchGame = function() {
				WebSocketManagerService.send({
					action : ACTION.INIT_GAME
				});
			};

			/**
			 * Quit lobby
			 */
			var checkQuitLobby = function(data) {
				$state.go('home');
			};

			$scope.quitLobby = function() {
				WebSocketManagerService.register(ACTION.QUIT_LOBBY, true).then(
						null, null, checkQuitLobby);

				WebSocketManagerService.send({
					action : ACTION.QUIT_LOBBY,
					params : {
						uid : $rootScope.lobby.uid
					}
				});
			};

			var checkDestroyLobby = function(data) {
				$state.go('home');
				MessageService.addMessageCode('lobby', 'LOBBY_DESTROYED', true);
			};
			WebSocketManagerService.register(ACTION.DESTROY_LOBBY, true).then(
						null, null, checkDestroyLobby);

			/**
			 * Remove user
			 */
			$scope.removeUser = function(data) {
				$rootScope.lobby.users = $rootScope.lobby.users
						.filter(function(user) {
							return user.username !== data.user.username;
						});
				console.log($rootScope.lobby.users);
			};
			WebSocketManagerService.register(ACTION.USER_QUIT_LOBBY).then(null,
					null, $scope.removeUser);

		} ]);
