'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.lobby.create', []);

controllers.controller('LobbyCreateCtrl', [
		'$scope',
		'WebSocketManagerService',
		'ACTION',
		'$state',
		'STATUT',
		function($scope, WebSocketManagerService, ACTION, $state, STATUT) {
			$scope.lobby = {};
			
			var checkCreationResultResult = function(data){
				if(data.state === STATUT.OK){
					$state.go('lobby');
				}else{
					console.error('error during creation');
				}
			};
			
			$scope.createLobby = function(){
				WebSocketManagerService.register(ACTION.CREATE_LOBBY, true).then(checkCreationResultResult);
				
				var data = {
					action : ACTION.CREATE_LOBBY,
					params : $scope.lobby
				};
				
				WebSocketManagerService.send(data);
			};

		} ]);
