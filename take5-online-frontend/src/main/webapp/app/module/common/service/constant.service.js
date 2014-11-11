'use strict';

/* Services */

var services = angular.module('take5Services.constant', []);

services.constant('APP_NAME', 'Take 5!').constant('APP_VERSION', '0.0.1')
		.constant('ACTION', {
			'LOGIN' : 'LOGIN',
			'LIST_LOBBIES' : 'LIST_LOBBIES',
			'LIST_USERS' : 'LIST_USERS',
			'CREATE_LOBBY' : 'CREATE_LOBBY'
		}).constant('STATUT', {
			'OK' : 'OK',
			'KO' : 'KO'
		}).constant('CODE_RESULTAT', {
			'NOT_LOGGED' : 'NOT_LOGGED'
		});