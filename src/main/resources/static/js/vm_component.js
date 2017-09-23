//// 글로벌 컴포넌트 설정 ////

// 메인 슬라이더
Vue.component('main-slider', {
    template: `
    <div id="carouselExampleIndicators" class="carousel slide main_slide" data-ride="carousel">
        <div class="container">
            <ol class="carousel-indicators">
                <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
            </ol>
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <img class="d-block w-100" src="http://placehold.it/1520x485" alt="First slide">
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="http://placehold.it/1520x485" alt="Second slide">
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="http://placehold.it/1520x485" alt="Third slide">
                </div>
            </div>
            <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>
  `
})

Vue.component('shop-select-btn', {
    template: `
        <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle ml-auto mr-0" type="button" id="showFilterMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="btn_title">평점순</span><span class="btn_icon"><i class="xi-angle-down"></i></span></button>
            <div class="dropdown-menu" aria-labelledby="showFilterMenu">
                <a class="dropdown-item" href="#">시간순</a>
                <a class="dropdown-item" href="#">날짜순</a>
            </div>
        </div>
    `
})


Vue.component('shop-box', {
    template: `
        <div id="shopCard" class="shop_card">
            <div id="mcLocation" class="sc_location">
                <span>혜화/대학로</span>
            </div>
            <div class="sc_imgarea">
                
                <div class="profile_img">
                    
                </div>
            </div>
            <div class="sc_bottom">
                <h4 class="sc_title">남자 투블럭/염색/스타일링</h4>  
                <span class="sc_des">#투블럭 #남자 #머리 #염색 #컷</span>
                <div class="sc_info clearnext">
                    <span class="designer_name"><span>Designer</span> 은진</span>
                    <span class="stars">
                        <i class="xi-star"></i><i class="xi-star"></i><i class="xi-star"></i><i class="xi-star"></i><i class="xi-star"></i>
                        <span class="reply_num">(15)</span>
                    </span>
                </div>
            </div>
        </div>
    `,
    
});

Vue.component('pf-img', {
    template: `
        <div class="col-xl-4 col-sm-12">
            <img src="https://scontent-ams3-1.cdninstagram.com/t51.2885-15/e35/10570041_220776811620145_85779000_n.jpg" class="center-block img-responsive" />
        </div>
    `
});