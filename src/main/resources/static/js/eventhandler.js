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

$(function() {
	
	//datepicker
	// 예약날짜 선택
//	$("#datepicker").focus(function(){
//		$(this).datepicker({firstDay: 1});  
//	});
//	
//	$("#datepicker2").focus(function(){
//		$(this).datepicker({firstDay: 1});  
//	});
//	$("#bookDatepicker").focus(function(){
//		$(this).datepicker({firstDay: 1});  
//	});
	
	
	$("#dateUpload").click(function(){
	  $(this).datepicker({
		  dateFormat: "yy-mm-dd",
		  dayNamesMin: ['일','월', '화', '수', '목', '금','토'],
		  minDate: 0,
		  maxDate: '+1m'
	  }).focus(function() {
		  $("#datepicker").datepicker("show");
	  	}).focus();
	});
	
	$("#dayUpload").click(function(){
		  $(this).datepicker({
			  dateFormat: "yy-mm-dd",
			  dayNamesMin: ['일','월', '화', '수', '목', '금','토'],
			  minDate: 0,
			  maxDate: '+1m'
		  }).focus(function() {
			  $("#datepicker2").datepicker("show");
		  	}).focus();
		});
	

	$("#bookUpload").click(function(){
		  $(this).datepicker({
			  dateFormat: "yy-mm-dd",
			  minDate: new Date($("#startDate").val()),
			  maxDate: new Date($("#endDate").val()),
			  dayNamesMin: ['일','월', '화', '수', '목', '금','토'],
			  onClose: function(date) {
				 $.ajax({
					 type:"get",
					 url:"/book/enableTime",
					 data:{'selectedDate' : date},
					 dataType:'text',
					 success: function(result){
						 booked = eval(result);
					 },
					 error: function(request,status,error){
						 alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
					 }
				 });
			  }
			  //beforeShowDay: disableBookDays
		  }).focus(function() {
			  $("#datepicker2").datepicker("show");
		  	}).focus();
	});
	
	//timepicker
	$("#timeUpload").timepicker({
		dynamic: false
	});
	$("#endUpload").timepicker({
		dynamic: false
	});
	$("#bookTimeUpload").timepicker({
		minTime: $("#startTime").val(),
		maxTime: $("#endTime").val(),
		dynamic: false
	});
	
	var booked;
	$("#bookTimeUpload").click(function(){
		//$(".ui-timepicker-viewport").children().eq(0).children().css('display','none');
		var length = $(".ui-timepicker-viewport > li").length;
		for(i=0;i<length;i++){
			$(".ui-timepicker-viewport").children().eq(i).children().css('display','block');
			var str = $(".ui-timepicker-viewport").children().eq(i).text();
			for(j=0;j<booked.length;j++){
				if(str==booked[j]){
					$(".ui-timepicker-viewport").children().eq(i).children().css('display','none');
				} 
			}
		}
	})
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

//상품&옵션 추가
var index=1;
$("#pdAddBtn").click(function(){
	$("#productName").append("<br><input name='products["+index+"].productName' " +
			"value='' class='form-control inline_input' type='text' placeholder='상품명을 입력해 주세요'/>");
	$("#productPrice").append("<br><input name='products["+index+"].productPrice' " +
			"class='form-control inline_input' type='text' placeholder='0'/>");
	index++;
});

var opIndex=1;
$("#optionAddBtn").click(function(){
	$("#optionName").append("<br><input name='options["+opIndex+"].optionName' " +
			"class='form-control inline_input' type='text' placeholder='옵션명을 입력해 주세요'>");
	$("#optionPrice").append("<br><input name='options["+opIndex+"].optionPrice' " +
			"class='form-control inline_input' type='text' placeholder='0'>");
	opIndex++;
});

//포트폴리오 추가 버튼
$("#portfolioAddBtn").click(function(){
	$("#portfolioFile").append("<input id='photoUpload' name='portfolio' class='form-control' type='file' placeholder='등록'>");
});


//상품 select박스 이벤트
$("#productSelect").change(function(){
	//선택된 상품 가격 추출
	var str = $.trim($("#productSelect option:selected").text());
	strArr = str.split(" ");
	var productAmount = parseInt(strArr[1].substring(0,strArr[1].length-1).replace(/,/g,''));
	//선택된 옵션 가격 추출
	var opStr = $.trim($("#optionSelect option:selected").text());
	strArr2 = opStr.split(" ");
	var optionAmount = parseInt(strArr2[1].substring(0,strArr2[1].length-1).replace(/,/g,''));
	$("#price").text(productAmount+optionAmount);
});

//option select박스 이벤트
$("#optionSelect").change(function(){
	//선택된 상품 가격 추출
	var str = $.trim($("#productSelect option:selected").text());
	strArr = str.split(" ");
	var productAmount = parseInt(strArr[1].substring(0,strArr[1].length-1).replace(/,/g,''));
	
	//선택된 옵션 가격 추출
	var opStr = $.trim($("#optionSelect option:selected").text());
	strArr2 = opStr.split(" ");
	var optionAmount = parseInt(strArr2[1].substring(0,strArr2[1].length-1).replace(/,/g,''));
	
	$("#price").text(productAmount+optionAmount);
});

//취소하기 버튼 이벤트
$(".tb_btn").click(function(){
	location.href='/book/cancel/'+$(this).attr("id");
});

$(".designer_tb_btn").click(function(){
	location.href='/book/designer/cancel/'+$(this).attr("id");
});

$(".designer_confirm_btn").click(function(){
	location.href='/book/confirm/'+$(this).attr("id");
})

$(".designer_complete_btn").click(function(){
	location.href='/book/complete/'+$(this).attr("id");
})



