$(document).ready(function(){
    const mod = $('#about-modal');
    const modalContact=$('#contact-modal');
    const closeButton = $('.close-btn');


    closeButton.click(() => {
        mod.css('display', 'none');
        modalContact.css('display','none');
    });



    $(window).click((event) => {
        if (event.target === mod[0]) {
            mod.css('display', 'none');
        }
        if(event.target==modalContact[0]){
            modalContact.css('display','none');
        }
    });



});

  function profilemenu(option){

        if(option=='logout'||option =='login' ){
            window.location.href = '/logout';
        }else {
            window.location.href = '/profile'
        }
    }

    function sendMessageToAdmin(){
        alert("Sorry, this feature is not available for now.");
    }

    function menu(menuItem) {
        if(menuItem =='About'){
            $('#about-modal').css('display', 'block');
        }else if(menuItem == 'Contact'){
            $('#contact-modal').css('display','block');
        }
     }
