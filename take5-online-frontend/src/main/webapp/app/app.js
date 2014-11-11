'use strict';

/* App Module */

var app = angular.module('take5App', [ 'ui.router', 'take5Controllers.login',
		'take5Controllers.home', 'take5Controllers.lobby.create',
		'take5Services.websocket', 'take5Services.constant' ]);

app.config([ '$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$urlRouterProvider.otherwise("/login");

			$stateProvider.state('login', {
				url : "/login",
				templateUrl : "app/module/login/view/login.view.html",
				controller : 'LoginCtrl'
			}).state('home', {
				url : "/home",
				templateUrl : "app/module/home/view/home.view.html",
				controller : 'HomeCtrl'
			}).state('lobbyCreate', {
				url : "/lobby/create",
				templateUrl : "app/module/lobby/view/lobby.create.view.html",
				controller : 'LobbyCreateCtrl'
			});

		} ]);
