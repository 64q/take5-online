'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.home', []);

controllers.controller('HomeCtrl', [
		'$scope',
		'$rootScope',
		'WebSocketManagerService',
		'ACTION',
		'$state',
		'STATUT',
		function($scope, $rootScope, WebSocketManagerService, ACTION, $state, STATUT) {
			$scope.game = {};

			var checkListLobbiesResult = function(data) {
				$scope.game.lobbies = data.lobbies;
			};
			var checkListUsersResult = function(data) {
				$scope.game.users = data.users;
			};
			var addUsersToListResult = function(data) {
				$scope.game.users.push(data.user);
			};
			var removeUsersFromListResult = function(data) {
				$scope.game.users = $scope.game.users.filter(function(user){return user.username !== data.user.username});
			};
			var createLobbyResultResult = function(data) {
				$scope.game.lobbies.push(data.lobby);
			};

			WebSocketManagerService.register(ACTION.LIST_LOBBIES).then(null, null,
					checkListLobbiesResult);
			WebSocketManagerService.register(ACTION.LIST_USERS).then(null, null,
					checkListUsersResult);
			WebSocketManagerService.register(ACTION.USER_JOIN_SERVER).then(null, null,
					addUsersToListResult);
			WebSocketManagerService.register(ACTION.USER_QUIT_SERVER).then(null, null,
					removeUsersFromListResult);
			WebSocketManagerService.register(ACTION.CREATE_LOBBY).then(null, null,
					createLobbyResultResult);

			WebSocketManagerService.send({
				action : ACTION.LIST_LOBBIES
			});

			WebSocketManagerService.send({
				action : ACTION.LIST_USERS
			});

			var checkJoinLobbyResult = function(result){
				if(result.state === STATUT.OK){
					$rootScope.lobby = result.lobby;
					$state.go('lobby');
					console.log('Join success');
				}else{
					console.error('can\'t join');
				}
			};
			WebSocketManagerService.register(ACTION.JOIN_LOBBY).then(null, null,
					checkJoinLobbyResult);

			$scope.joinLobby = function(uid){
				WebSocketManagerService.send({
					action : ACTION.JOIN_LOBBY,
					params : {
						uid : uid
					}
				});
			};
		} ]);
