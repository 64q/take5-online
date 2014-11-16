'use strict';

/* Controllers */

var controllers = angular.module('take5Controllers.game', []);

controllers.controller('GameCtrl', [ '$scope', 'WebSocketManagerService', 'ACTION', '$state', 'STATUT', '$rootScope',
		function($scope, WebSocketManagerService, ACTION, $state, STATUT, $rootScope) {

//	$rootScope.gameBoard = {board : [[{value: 15, oxHeads: 1}, {value: 1, oxHeads: 5}], [{value: 15, oxHeads: 1}, {value: 1, oxHeads: 5}]]};
//	$rootScope.hand = {cards : [{value: 15, oxHeads: 1}, {value: 1, oxHeads: 5}, {value: 15, oxHeads: 7}, {value: 15, oxHeads: 3}, {value: 15, oxHeads: 1}, {value: 1, oxHeads: 2}, {value: 15, oxHeads: 7}, {value: 15, oxHeads: 3}, {value: 15, oxHeads: 1}, {value: 1, oxHeads: 5}]};
} ]);
