// 페이지 로더
$('html, body').css('overflow-y', 'hidden');
$(function() {
    setTimeout(function() {
        $('#preLoader').fadeOut(1000, function() {
            $(this).remove();
        });
    }, 1000);
    $('html, body').css('overflow-y', 'scroll');
});

// anchor click smooth scrolling
(function() {
    $('.sales_navigator a[href*=\\#]').on('click', function(event) {
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
        rating: $("#average").val(),
        readOnly: true
    });
    var count = $("#count").text();
    for(var i=1;i<=count;i++){
	    $('.'+i).rateYo({
	    	rating: $("#"+i).val(),
	        starWidth: "20px",
	        readOnly: true
	    });
    }
    
});


// 에약하기 상자 스크롤링 
$(window).on('scroll', function(){
    if ($(this).scrollTop() > 300) {
        $("#reserveBox").stop().animate({"marginTop": ($(window).scrollTop()) + "px"}, 1000);    
    } else {
        $("#reserveBox").stop().animate({"marginTop": 205 + "px"}, 1000);    
    }
});


// 예약날짜 선택
$("#datepicker").click(function(){
  $(this).datepicker({ firstDay: 1});  
})