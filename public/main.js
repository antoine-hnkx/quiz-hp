"use strict";
import {postData} from "./utilsAPI.js";
import {getData} from "./utilsAPI.js";


////////////////////////////////////////////////////////////////////////////////////////////

let questionsCounter = 0;
let userEmail;

$(".login-form").hide();
$(".login").css("background", "#8e9094");

$(".login").click(function () {
    $(".signup-form").hide();
    $(".login-form").show();
    $(".signup").css("background", "#8e9094");
    $(".login").css("background", "#fff");
});

$(".signup").click(function () {
    $(".signup-form").show();
    $(".login-form").hide();
    $(".login").css("background", "#8e9094");
    $(".signup").css("background", "#fff");
});

$(".btn").click(function () {
    $(".input").val("");
});


///////////////////////////////////////////////////////////////////////////////////////


const authenticatedActions = (url, token) => {
    $("#loginSignupForms").hide();
    $("#logOut").show();
    $("#introQuiz").show();
    $("#navHouse").show();
    $('body').css("background-image", "url(\"./images/Hogwarts.jpg\")");

};

const notAuthenticatedActions = (errorMessage = "") => {
    $("#login_board").html(errorMessage);

    if (errorMessage === "") $("#login_board").hide();
    else $("#login_board").show();
    $("#loginSignupForms").show();
    $("#introQuiz").hide();
    $("#question_view").hide();
    $("#logOut").hide();
    $("#navHouse").hide();
    $("#view_members_component").hide();
    $('body').css("background-image", "url(\"./images/Hogwarts.jpg\")");


};


const initialRenderOfComponents = url => {
    let token = localStorage.getItem("token");
    if (token) {
        authenticatedActions(url, token);
        return token;
    } else {
        notAuthenticatedActions();
        return;
    }
};

function getHouseMembers(url, token, house) {
    $("#loginSignupForms").hide();
    $("#logOut").show();
    $("#introQuiz").hide();
    $("#question_view").hide();
    $("#view_members_component").show();
    if (house === "Gryffindor") {
        $('body').css("background-image", "url(\"./images/griffindor-gryffindor-garri.jpg\")");
    } else if (house === "Ravenclaw") {
        $('body').css("background-image", "url(\"./images/kogtevran-ravenclaw-fakultet.jpg\")");
    } else if (house === "Slytherin") {
        $('body').css("background-image", "url(\"./images/slizerin-slytherin-zmeya.jpg\")");
    } else {
        $('body').css("background-image", "url(\"./images/289889.jpg\")");
    }
    getData(url, token).then(response => {
        let resp_json = response;
        if (resp_json.success) {
            $("#message_board").text("");
            let div = document.createElement("div");
            let list = document.createElement("ul");
            let members_array = response.data;
            list.className = "list-group";
            for (let i = 0; i < members_array.length; i++) {
                let item = document.createElement("li");
                item.className = "list-group-item";
                item.innerText = members_array[i].firstName + " " + members_array[i].lastName;
                list.appendChild(item);
            }
            $("#message_board").append(list);
        } else {
            $("#message_board").text(JSON.stringify(response.error));
        }
    }).catch(err => {
    });
}


function getQuestions(url, token) {
    $("#loginSignupForms").hide();
    $("#logOut").show();
    $("#introQuiz").hide();
    $("#view_members_component").hide();
    $("#question_view").show();
    $("#question").show();
    getData(url, token)
        .then(response => {
                if (response.success) {
                    $("#sortingHatImage").attr("src", "./images/sorting_hat.png");
                    $("#question").text("");
                    //Creating question and answers
                    let quizQuestions = response.data;

                    // QUESTION
                    //question div container
                    let questionContainer = document.createElement("div");
                    questionContainer.className = "container col-md-12";
                    //question div style
                    let questionStyle = document.createElement("div");
                    questionStyle.className = "text-center intro";
                    //div question class
                    let questionDiv = document.createElement("div");
                    questionDiv.className = "question";
                    //question text paragraph
                    let questionHTML = document.createElement("p");
                    questionHTML.id = "question-text";
                    displayQuestion(quizQuestions[questionsCounter].question);

                    // APPENDING QUESTION
                    questionDiv.appendChild(questionHTML);
                    questionStyle.appendChild(questionDiv);
                    questionContainer.appendChild(questionStyle);
                    $("#question").append(questionContainer);

                    // ANSWERS BUTTONS
                    // div container all
                    let answersContainer = document.createElement("div");
                    answersContainer.className = "container col-md-12";
                    // main row
                    let mainRow = document.createElement("div");
                    mainRow.className = "row";

                    //LEFT
                    // 2 left buttons
                    let left = document.createElement("div");
                    left.className = "col-md-6";

                    //LEFT-TOP
                    let ltrow = document.createElement("div");
                    ltrow.className = "row";
                    let ltc = document.createElement("div");
                    ltc.className = "col-md-12";
                    let ltb = document.createElement("button");
                    ltb.className = "btn btn-secondary text-center btn-block my-2";
                    ltb.type = "button";
                    ltb.id = "opt1";
                    ltb.innerHTML = quizQuestions[questionsCounter].gryffindor;
                    //LEFT-BOTTOM
                    let lbrow = document.createElement("div");
                    lbrow.className = "row";
                    let lbc = document.createElement("div");
                    lbc.className = "col-md-12";
                    let lbb = document.createElement("button");
                    lbb.className = "btn btn-secondary text-center btn-block my-2";
                    lbb.type = "button";
                    lbb.id = "opt3";
                    lbb.innerHTML = quizQuestions[questionsCounter].slytherin;

                    //RIGHT
                    // 2 right buttons container
                    let right = document.createElement("div");
                    right.className = "col-md-6";
                    //RIGHT-TOP
                    let rtrow = document.createElement("div");
                    rtrow.className = "row";
                    let rtc = document.createElement("div");
                    rtc.className = "col-md-12";
                    let rtb = document.createElement("button");
                    rtb.className = "btn btn-secondary text-center btn-block my-2";
                    rtb.type = "button";
                    rtb.id = "opt2";
                    rtb.innerHTML = quizQuestions[questionsCounter].ravenclaw;
                    //RIGHT-BOTTOM
                    let rbrow = document.createElement("div");
                    rbrow.className = "row";
                    let rbc = document.createElement("div");
                    rbc.className = "col-md-12";
                    let rbb = document.createElement("button");
                    rbb.className = "btn btn-secondary text-center btn-block my-2";
                    rbb.type = "button";
                    rbb.id = "opt4";
                    rbb.innerHTML = quizQuestions[questionsCounter].hufflepuff;

                    // APPENDING BUTTONS
                    ltc.appendChild(ltb);
                    ltrow.appendChild(ltc);
                    lbc.appendChild(lbb);
                    lbrow.appendChild(lbc);

                    left.appendChild(ltrow);
                    left.appendChild(lbrow);

                    rtc.appendChild(rtb);
                    rtrow.appendChild(rtc);
                    rbc.appendChild(rbb);
                    rbrow.appendChild(rbc);

                    right.appendChild(rtrow);
                    right.appendChild(rbrow);

                    mainRow.appendChild(left);
                    mainRow.appendChild(right);
                    answersContainer.appendChild(mainRow);
                    $("#question").append(answersContainer);

                    ////////////////////////////////

                    $("#opt1").on("click", e => {
                        e.preventDefault();
                        updateScore("gryffindor", token);
                    });
                    $("#opt2").on("click", e => {
                        e.preventDefault();
                        updateScore("ravenclaw", token);
                    });
                    $("#opt3").on("click", e => {
                        e.preventDefault();
                        updateScore("slytherin", token);
                    });
                    $("#opt4").on("click", e => {
                        e.preventDefault();
                        updateScore("hufflepuff", token);
                    });

                    let progress = (questionsCounter/11)*100;
                    $('.progress-bar').css('width', progress+'%').attr('aria-valuenow', progress);
                } else {
                    console.log(response.error);
                }
            }
        ).catch(err => {
    });


}

function displayQuestion (question) { // AFFICHE CARACTERE PAR CARACTERE LE TEXTE DE LA QUESTION
        let text = question;
            let i = 0;
            let consoleTyper = setInterval(function () {
                if (i != text.length) {
                    i += 1;
                    if(document.getElementById("question-text") != null)
                        document.getElementById("question-text").innerHTML = text.substr(0, i);
                } else {
                 clearInterval(consoleTyper);
                }
            }, 35);
}



function updateScore(house, token) {
    const data = {email: userEmail};
    postData("/score/" + house, data, token)
        .then(response => {
            if (response.success === "true") {
                if (response.house) {
                    asignHouse(response.house);
                } else {
                    questionsCounter++;
                    getQuestions("/questions", token);
                }
            } else {
                console.error("Error:", response);
            }
        })

}


function asignHouse(house) {
    $("#loginSignupForms").hide();
    $("#logOut").show();
    $("#introQuiz").hide();
    $("#view_members_component").hide();
    $("#question_view").show();
    $("#chosenHouse").show();
    $("#testButton").hide();
    if (house === "gryffindor") {
        $('body').css("background-image", "url(\"./images/griffindor-gryffindor-garri.jpg\")");
    } else if (house === "ravenclaw") {
        $('body').css("background-image", "url(\"./images/kogtevran-ravenclaw-fakultet.jpg\")");
    } else if (house === "slytherin") {
        $('body').css("background-image", "url(\"./images/slizerin-slytherin-zmeya.jpg\")");
    } else {
        $('body').css("background-image", "url(\"./images/289889.jpg\")");
    }

    $("#question").text("");
    let div2 = document.createElement("div");
    div2.className = "col-md-12";
    let div3 = document.createElement("div");
    div3.className = "text-center intro";
    let div4 = document.createElement("div");
    div4.className = "question";
    let questionHTML = document.createElement("p");

    $('.progress-bar').css('width', '100%').attr('aria-valuenow', 11);
    $(".progress-bar").removeClass("bg-secondary");

    if (house === "gryffindor") {
        $("#sortingHatImage").attr("src", "./images/1135719_gryffindor-logo-png.png");
        $(".progress-bar").addClass("bg-danger");
    } else if (house === "ravenclaw") {
        $("#sortingHatImage").attr("src", "./images/4dab0a87210b79bfbb9adbb5432847be.png");
        $(".progress-bar").addClass("bg-primary");
    } else if (house === "slytherin") {
        $("#sortingHatImage").attr("src", "./images/slytherinCrest.png");
        $(".progress-bar").addClass("bg-success");
    } else {
        $("#sortingHatImage").attr("src", "./images/hufflepuff-crest-png-3-removebg-preview.png");
        $(".progress-bar").addClass("bg-warning");
    }


    $("#sortingHatImage").attr("width",450);
    $("#sortingHatImage").css("padding","25px");
    if (house === "gryffindor") {
        questionHTML.innerHTML = "The Sorting Hat has placed you in GRYFFINDOR ! Where dwell the brave at heart, their daring, nerve and chivalry set Gryffindors apart.";
    } else if (house === "ravenclaw") {
        questionHTML.innerHTML = "The Sorting Hat has placed you in RAVENCLAW! Or yet in wise old Ravenclaw.Where those of wit and learning will always find their kind.";
    } else if (house === "slytherin") {
        questionHTML.innerHTML = "The Sorting Hat has placed you in SLYTHERIN ! In Slytherin, you'll make your real friends. Those cunning folk use any means to achieve their ends.";
    } else {
        questionHTML.innerHTML = "The Sorting Hat has placed you in HUFFLEPUFF !Where they are just and loyal. Those patient Hufflepuffs are true and unafraid of toil;";
    }

    div4.appendChild(questionHTML);
    div3.appendChild(div4);
    div2.appendChild(div3);
    $("#question").append(div2);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////

$(document).ready(function () {
    $("#loginSignupForms").show();
    $("#question_view").hide();
    $("#logOut").hide();
    $("#navHouse").hide();
    $("#view_members_component").hide();
    $("#continue").hide();
    localStorage.removeItem("token");
    let url = "/questions";
    let token = initialRenderOfComponents(url);

    $("#login_form").submit(e => {
        e.preventDefault();
        if ($("#emailLogin")[0].checkValidity() && $("#passwordLogin")[0].checkValidity()) {
            const data = {
                email: $("#emailLogin").val(),
                password: $("#passwordLogin").val()
            };

            postData("/login", data)
                .then(response => {
                    userEmail = $("#emailLogin").val();
                    $("#emailLogin").val("");
                    $("#passwordLogin").val("");
                    if (response.success === "true") {
                        localStorage.setItem("token", response.token);
                        token = response.token;
                        authenticatedActions(url, token);
                    } else {
                        console.error("Error:", response);
                        notAuthenticatedActions(response.error);
                    }
                })
        } else {
            alert("Please provide valid credentials.");
        }
    });

    $("#register_form").submit(e => {
        e.preventDefault();
        if ($("#emailSignUp").get(0).checkValidity() && $("#passwordSignUp").get(0).checkValidity()) {
            const data = {
                email: $("#emailSignUp").val(),
                password: $("#passwordSignUp").val(),
                firstName: $("#firstNameSignUp").val(),
                lastName: $("#lastNameSignUp").val()
            };
            postData("/register", data)
                .then(response => {
                    userEmail = $("#emailSignUp").val();
                    $("#firstNameSignUp").val("");
                    $("#lastNameSignUp").val("");
                    $("#emailSignUp").val("");
                    $("#passwordSignUp").val("");
                    token = response.token;
                    localStorage.setItem("token", response.token);
                    authenticatedActions(url, token);
                })
                .catch(err => {
                    console.error("Error :", err);
                });
        }
    });
    $("#logOut").click(e => {
        e.preventDefault();
        userEmail=null;
        localStorage.removeItem("token");
        token = undefined;
        notAuthenticatedActions();
    });
    $("#start").click(e => {
        e.preventDefault();
        $(".progress-bar").removeClass("bg-danger");
        $(".progress-bar").removeClass("bg-primary");
        $(".progress-bar").removeClass("bg-success");
        $(".progress-bar").removeClass("bg-warning");
        $(".progress-bar").addClass("bg-secondary");
        const data = {email: userEmail};
        postData("/score/reset", data, token)
            .then(response => {
                if (response.success === "true") {
                    questionsCounter = 0;
                    getQuestions("/questions", token);
                } else {
                    console.error("Error:", response);
                }
            });
    });
    $("#gryffindorHouse").click(e => {
        e.preventDefault();
        getHouseMembers("/house/gryffindor", token, "Gryffindor");
    });
    $("#slytherinHouse").click(e => {
        e.preventDefault();
        getHouseMembers("/house/slytherin", token, "Slytherin");
    });
    $("#hufflepuffHouse").click(e => {
        e.preventDefault();
        getHouseMembers("/house/hufflepuff", token, "Hufflepuff");
    });
    $("#ravenclawHouse").click(e => {
        e.preventDefault();
        getHouseMembers("/house/ravenclaw", token, "Ravenclaw");
    });

    $("#continue").click(e => {
        e.preventDefault();
        $("#introQuiz").show();
        $("#chosenHouse").hide();
        $("#continue").hide();
        $("#question").hide();
        $("#view_members_component").hide();
        $('body').css("background-image", "url(\"./images/Hogwarts.jpg\")");

    });

    $("#home").click(e=>{
        e.preventDefault();
        if(userEmail!=null) {
            $('body').css("background-image", "url(\"./images/Hogwarts.jpg\")");
            $("#loginSignupForms").hide();
            $("#question").hide();
            $("#chosenHouse").hide();
            $("#logOut").show();
            $("#introQuiz").show();
            $("#question_view").hide();
            $("#view_members_component").hide();

        }
    });


});