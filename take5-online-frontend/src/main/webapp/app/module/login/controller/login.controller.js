'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.login', []);

controllers.controller('LoginCtrl', [ '$scope', 'WebSocketManagerService', 'ACTION', '$state', 'STATUT', '$rootScope',
		function($scope, WebSocketManagerService, ACTION, $state, STATUT, $rootScope) {
			$scope.user = {};
			/**
			 * Callback function which check the result sent by the server.
			 * 
			 * @param data
			 *            result sent via websocket
			 */
			var checkLoginResult = function(data) {
				console.log(data);
				if(data.state === STATUT.OK){
					$rootScope.username = data.username;
					$state.go('home');
				}else{
					$scope.alertes = [{
						statut: data.statut,
						message: data.reason
					}];
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
