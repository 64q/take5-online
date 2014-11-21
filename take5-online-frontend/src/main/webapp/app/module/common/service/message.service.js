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
					var tt = $rootScope.messages.length;
					$rootScope.messages.push(message);
					if(timeout){
						$timeout(function(){
							console.log(tt);
							$rootScope.messages.splice(tt, 1);
						}, 2000);
					}
				},
				clearMessages : function(){
					$rootScope.messages = [];
				}
			};
		} ]);
