"use strict";

let hat = document.getElementById("sortingHatImage");

$(document).ready(function() {
   setInterval(() => hat_animation(), 10000);
});

function hat_animation() {

    let rand = Math.floor(Math.random()*4) + 1;

           switch (rand) {
              case 1:
                 animation1.play();
                 break;
              case 2:
                 animation2.play();
                 break;
              case 3:
                 animation3.play();
                 break;
              case 4:
                 animation4.play();
                 break;
           }
}

let animation1 = anime({
   targets: hat,
   rotate: {
      value: 360,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   autoplay: false
});

let animation2 = anime({
   targets: hat,
   rotate: {
      value: 30,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   translateX: {
      value: 100,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   translateY: {
      value: -50,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   direction: 'alternate',
   autoplay: false
});

let animation3 = anime({
   targets: hat,
   rotate: {
      value: -30,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   translateX: {
      value: -100,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   translateY: {
      value: -50,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   direction: 'alternate',
   autoplay: false
});

let animation4 = anime({
   targets: hat,
   translateY: {
      value: -100,
      duration: 1800,
      easing: 'easeInOutSine'
   },
   direction: 'alternate',
   autoplay: false
});
