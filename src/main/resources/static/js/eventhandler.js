// 페이지 로더
// $('html, body').css('overflow-y', 'hidden');
// $(function() {
//     setTimeout(function() {
//         $('#preLoader').fadeOut(1000, function() {
//             $(this).remove();
//         });
//     }, 1000);
//     $('html, body').css('overflow-y', 'scroll');
// });

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
        rating: 4.0,
        readOnly: true
    });
    
    $('.starrating_read_only').rateYo({
        rating: 4.0,
        starWidth: "20px",
        readOnly: true
    });
    
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
  $(this).datepicker({firstDay: 1});  
});

$("#dateUpload01").click(function(){
  $(this).datepicker({firstDay: 1});  
});

$("#dateUpload02").click(function(){
  $(this).datepicker({firstDay: 1});  
});


// 주소찾기 api controller
//load함수를 이용하여 core스크립트의 로딩이 완료된 후, 우편번호 서비스를 실행합니다.

function daumLocationApi(){
   daum.postcode.load(function(){
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
                // 예제를 참고하여 다양한 활용법을 확인해 보세요.
            }
        }).open();
    }); 
};

function shopCreateSubmit(){
	var date01 = $('#dateUpload01').val().split('/').reverse().join('-');
	var date02 = $('#dateUpload02').val().split('/').reverse().join('-');
	var itr = moment.twix(new Date(date01),new Date(date02)).iterate("days");
	var range=[];
	while(itr.hasNext()){
		range.push(itr.next().toDate());
	}
	range = range + "";
	$('.total_date').val(range);
}


$('.navbar-right li').each(function() {
    if ($(this).find('img').length) {
        $(this).css({
			"top": "-7.5px",
			"position": "relative"
		});
    }
});





