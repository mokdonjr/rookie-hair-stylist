//// 1. homeApp 모듈 생성
//var homeApp = angular.module('homeApp', []);
//
//// 2. homeApp Configuration
//homeApp.config(function($httpProvider){
//	$httpProvider.defaults.headers.common['X-Requested-With']='XMLHttpRequest';
//});
//
//// 3. homeApp 모듈에 Controller 생성
//homeApp.controller("home", function($http, $location){
//	
//	var self = this;
//	
//	$http.get("/api/users")
//		.success(function(data){
//			if(data.name){
//				self.user = data.name;
//				self.authenticated = true;
//			}
//			else{
//				self.user = "N/A";
//				self.authenticated = false;
//			}
//		})
//		.error(function(){
//			self.user = "N/A";
//			self.authenticatied = false;
//		});
//	
//	self.logout = function(){
//		$http.get('logout', {})
//			.success(function(){
//				self.authenticated = false;
//				$location.path("/");
//			})
//			.error(function(data){
//				console.log("logout failed")
//				self.authenticated = false;
//			});
//	};
//});