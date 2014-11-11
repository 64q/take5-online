'use strict';

/* Services */

var services = angular.module('take5Services.websocket', []);

services.factory('WebSocketManagerService', [
		'$state',
		'$rootScope',
		'$q', 'CODE_RESULTAT', 'STATUT',
		function($state, $rootScope, $q, CODE_RESULTAT, STATUT) {
			var ws = new WebSocket(
					"ws://localhost:8080/take5-online-backend/game");

			var registration = {};
			var wsReady = false;

			ws.onopen = function() {
				wsReady = true;
			};

			ws.onmessage = function(message) {
				var result = JSON.parse(message.data);

				if (result.state === STATUT.KO
						&& result.code === CODE_RESULTAT.NOT_LOGGED) {
					$state.go('login');
				} else if (registration[result.action]) {
					//dispatch message
					$rootScope.$apply(registration[result.action].defer
							.resolve(result));
					if (registration[result.action].unique) {
						delete registration[result.action];
					}
				}

			};

			console.log('there');
			return {
				send : function(data) {
					if (wsReady) {
						console.log(data);
						ws.send(JSON.stringify(data));
					} else {
						//websocket not ready
						$state.go('login');
					}
				},
				register : function(idAction, unique) {
					var defer = $q.defer();
					registration[idAction] = {
						defer : defer,
						unique : unique
					};
					return defer.promise;
				}
			};
		} ]);
