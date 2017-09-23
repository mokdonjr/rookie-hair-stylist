// 페이지 로더
$(function() {
    
    if (location.pathname != "/rookie-hair-stylist/") {
        $('body').css('overflow-y', 'scroll');
    }
    
    setTimeout(function() {
        $('#preLoader').fadeOut(1000, function() {
            $(this).remove();
            $('body').css('overflow-y', 'scroll');
        });
    }, 1000)
});

// anchor click smooth scrolling
(function() {
    $('a[href*=\\#]').on('click', function(event) {
        event.preventDefault();
        $('html,body').animate({ scrollTop: $(this.hash).offset().top }, 500);
    });
}());


// totop 버튼
$(window).scroll(function() {
    if ($(this).scrollTop() > 400) {
        $('.to-top-btn').addClass('showme');
    }
    else {
        $('.to-top-btn').removeClass('showme');
    }
});

$('.to-top-btn').click(function() {
    $('html, body').animate({ scrollTop: '0' }, 600);
});

// 별점 관리
$(function() {
    
    
    // 별점 주기
    $("#starRating").rateYo({
        starWidth: "50px",
        normalFill: "#A0A0A0"
    }).on("rateyo.set", function(e, data) {
        $('#starRatingVal').val(data.rating);
    });


    // 평균 별점 받기
    $("#starRatingReadAverage").rateYo({
        rating: 4.0,
        readOnly: true
    });
    
    $('#starRatingReadOnly').rateYo({
        rating: 4.0,
        starWidth: "20px",
        readOnly: true
    });
    
});
