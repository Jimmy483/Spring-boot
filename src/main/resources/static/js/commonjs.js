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
