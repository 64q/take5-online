'use strict';

/* Services */

var services = angular.module('take5Services.constant', []);

services.constant('APP_NAME', 'Take 5!').constant('APP_VERSION', '0.0.1')
		.constant('ACTION', {
			'LOGIN' : 'LOGIN',
			'LIST_LOBBIES' : 'LIST_LOBBIES',
			'LIST_USERS' : 'LIST_USERS',
			'USER_JOIN_SERVER' : 'USER_JOIN_SERVER',
			'USER_QUIT_SERVER' : 'USER_QUIT_SERVER',
			'CREATE_LOBBY' : 'CREATE_LOBBY',
			'JOIN_LOBBY' : 'JOIN_LOBBY',
			'DESTROY_LOBBY': 'DESTROY_LOBBY',
			'INIT_GAME' : 'INIT_GAME',
			'USER_JOIN_LOBBY' : 'USER_JOIN_LOBBY',
			'USER_QUIT_LOBBY' : 'USER_QUIT_LOBBY',
			'QUIT_LOBBY' : 'QUIT_LOBBY',
			'CARD_CHOICE' : 'CARD_CHOICE',
			'END_TURN' : 'END_TURN',
			'REMOVE_LINE' : 'REMOVE_LINE',
			'REMOVE_LINE_CHOICE' : 'REMOVE_LINE_CHOICE',
			'END_GAME': 'END_GAME'
		}).constant('STATUT', {
			'OK' : 'OK',
			'KO' : 'KO'
		}).constant('CODE_RESULTAT', {
			'NOT_LOGGED' : 'NOT_LOGGED'
		}).constant('LOBBY_STATE', {
			'WAITING' : 'WAITING',
			'RUNNING' : 'RUNNING'
		});