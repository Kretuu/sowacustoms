'use strict';

const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

function register(registrations) {
    const socket = SockJS('/payroll');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        registrations.forEach(function (registration) {
            stompClient.subscribe(registration.route, registration.callback);
        });
    });
}

module.exports.register = register;