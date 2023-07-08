"use strict";

const axios = require('axios');

axios.defaults.baseURL = 'http://localhost:8080'

axios.defaults.headers.common['Accept'] = 'application/hal+json, text/uri-list';

//Adding request interceptor to get rid of uri templates
axios.interceptors.request.use(function(request) {
	if(request.url.indexOf('{') === -1){
		return request
	} else {
		request.url = request.url.split('{')[0];
		return request;
	}
});