'use strict';

/* Services */

var services = angular.module('take5Services.message', []);

services.factory('MessageService', [
		'$rootScope', '$timeout',
		function($rootScope, $timeout) {
			$rootScope.messages = [];
			return {
				addMessage : function(message){
					$rootScope.messages.push({text : message});
				},
				addMessageCode : function(scope, code, timeout) {
					var message = {code : scope + '.' + code};
					var messagePosition = $rootScope.messages.length;
					$rootScope.messages.push(message);
					if(timeout){
						$timeout(function(){
							$rootScope.messages.splice(messagePosition, 1);
						}, 6000);
					}
				},
				clearMessages : function(){
					$rootScope.messages = [];
				}
			};
		} ]);
