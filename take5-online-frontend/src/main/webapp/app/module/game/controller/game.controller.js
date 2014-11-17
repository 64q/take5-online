'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.game', []);

controllers.controller('GameCtrl', [
		'$scope',
		'WebSocketManagerService',
		'ACTION',
		'$state',
		'STATUT',
		'$rootScope',
		function($scope, WebSocketManagerService, ACTION, $state, STATUT,
				$rootScope) {

			$scope.clickOnCard = function(index) {
				WebSocketManagerService.send({
					action : ACTION.CARD_CHOICE,
					params : {
						card : index
					}
				});
			};
			
			var checkEndTurn = function(data){
				$rootScope.hand = data.hand;
				$rootScope.gameBoard = data.gameBoard;
			};
			
			WebSocketManagerService.register(ACTION.END_TURN).then(null, null, checkEndTurn);
			
			
			var checkRemoveColumn = function(data) {
				if($rootScope.username === data.user.username){
					console.log('En attente d\'un choix de ligne');
					$scope.mustRemoveColumn = true;
				}
			};
			
			WebSocketManagerService.register(ACTION.REMOVE_COLUMN).then(null, null, checkRemoveColumn);
			
			$scope.pickLine= function(index) {
				if($scope.mustRemoveColumn){
					$scope.mustRemoveColumn = false;
					WebSocketManagerService.send({
						action : ACTION.REMOVE_COLUMN,
						params : {
							column : index
						}
					});
				}
			};
			// $rootScope.gameBoard = {board : [[{value: 15, oxHeads: 1},
			// {value: 1, oxHeads: 5}], [{value: 15, oxHeads: 1}, {value: 1,
			// oxHeads: 5}]]};
			// $rootScope.hand = {cards : [{value: 15, oxHeads: 1}, {value: 1,
			// oxHeads: 5}, {value: 15, oxHeads: 7}, {value: 15, oxHeads: 3},
			// {value: 15, oxHeads: 1}, {value: 1, oxHeads: 2}, {value: 15,
			// oxHeads: 7}, {value: 15, oxHeads: 3}, {value: 15, oxHeads: 1},
			// {value: 1, oxHeads: 5}]};
		} ]);
