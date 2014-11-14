'use strict';

/* App Module */

var app = angular.module('take5App', [ 'ui.router', 'take5Controllers.login',
		'take5Controllers.home', 'take5Controllers.lobby.create', 'take5Controllers.lobby', 'take5Controllers.game',
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
			}).state('lobby', {
				url : "/lobby",
				templateUrl : "app/module/lobby/view/lobby.view.html",
				controller : 'LobbyCtrl'
			}).state('game', {
				url : "/game",
				templateUrl : "app/module/game/view/game.view.html",
				controller : 'GameCtrl'
			});
			
		} ]);

app.run(['$location', function($location){
	$location.path('/login');
}]);

app.run(['$rootScope', 'LOBBY_STATE', function ($rootScope, LOBBY_STATE) {
    $rootScope.LOBBY_STATE = LOBBY_STATE;
 }]);