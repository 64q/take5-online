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
		'MessageService',
		function($scope, WebSocketManagerService, ACTION, $state, STATUT,
				$rootScope, MessageService) {

			$scope.clickOnCard = function(index) {
				WebSocketManagerService.send({
					action : ACTION.CARD_CHOICE,
					params : {
						card : index
					}
				});
				
				MessageService.clearMessages();
				MessageService.addMessageCode('game', 'WAITING_END_TURN');
			};

			var checkEndTurn = function(data) {
				$rootScope.hand = data.hand;
				$rootScope.gameBoard = data.gameBoard;
				MessageService.clearMessages();
				MessageService.addMessageCode('game', 'PICK_CARD');
			};

			WebSocketManagerService.register(ACTION.END_TURN).then(null, null,
					checkEndTurn);

			var checkRemoveColumn = function(data) {
				if ($rootScope.username === data.user.username) {
					MessageService.clearMessages();
					MessageService.addMessageCode('game', 'REMOVE_LINE');
					console.log('En attente d\'un choix de ligne');
					$scope.mustRemoveColumn = true;
				}
			};

			WebSocketManagerService.register(ACTION.REMOVE_LINE_CHOICE).then(
					null, null, checkRemoveColumn);

			$scope.pickLine = function(index) {
				console.log(index);
				if ($scope.mustRemoveColumn) {
					$scope.mustRemoveColumn = false;
					WebSocketManagerService.send({
						action : ACTION.REMOVE_LINE,
						params : {
							line : index
						}
					});
					MessageService.clearMessages();
					MessageService.addMessageCode('game', 'WAITING_END_TURN');
				}
			};
			
			
			var endGame = function(data) {
				MessageService.clearMessages();
				if($rootScope.username === data.winner.username){
					MessageService.addMessageCode('game', 'WIN');
				}else{
					MessageService.addMessageCode('game', 'LOSE');
				}
				
			};

			WebSocketManagerService.register(ACTION.END_GAME).then(
					null, null, endGame);
		} ]);
