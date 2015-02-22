'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.login', []);

controllers.controller('LoginCtrl', [ '$scope', 'WebSocketManagerService', 'ACTION', '$state', 'STATUT', '$rootScope', 'MessageService',
		function($scope, WebSocketManagerService, ACTION, $state, STATUT, $rootScope, MessageService) {
			$scope.user = {};
			/**
			 * Callback function which check the result sent by the server.
			 * 
			 * @param data
			 *            result sent via websocket
			 */
			var checkLoginResult = function(data) {
				if(data.state === STATUT.OK){
					$rootScope.username = data.username;
					MessageService.clearMessages();
					$state.go('home');
				}else{
					MessageService.clearMessages();
					MessageService.addMessageCode('login', data.code, true);
				}
			};
			
			/**
			 * Send the username to the server.
			 */
			$scope.login = function() {
				WebSocketManagerService.register(ACTION.LOGIN, true).then(null, null, checkLoginResult);
				
				var data = {
					action : ACTION.LOGIN,
					params : {
						username : $scope.user.name
					}
				};
				
				WebSocketManagerService.send(data);
			};
		} ]);
