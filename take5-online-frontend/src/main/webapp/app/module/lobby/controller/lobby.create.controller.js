'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.lobby.create', []);

controllers.controller('LobbyCreateCtrl', [
		'$scope',
		'$rootScope',
		'WebSocketManagerService',
		'ACTION',
		'$state',
		'STATUT',
		'MessageService',
		function($scope, $rootScope, WebSocketManagerService, ACTION, $state,
				STATUT, MessageService) {
			$rootScope.lobby = {};

			var checkCreationResultResult = function(data) {
				if (data.state === STATUT.OK) {
					$rootScope.lobby = data.lobby;
					$state.go('lobby');
				} else {
					MessageService.clearMessages();
					MessageService.addMessageCode('lobby', data.code, true);
				}
			};

			$scope.createLobby = function() {
				WebSocketManagerService.register(ACTION.CREATE_LOBBY, true)
						.then(null, null, checkCreationResultResult);

				var data = {
					action : ACTION.CREATE_LOBBY,
					params : $scope.lobby
				};

				WebSocketManagerService.send(data);
			};

		} ]);
