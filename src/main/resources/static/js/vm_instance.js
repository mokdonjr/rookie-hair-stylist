// 메인 페이지 인스턴스 생성
const main = new Vue({
    el: '#pageWrap',
    data: {
        islogin: false
    }
});

// 메인페이지 샵박스 드롭다운 버튼

// 메인페이지 샵 박스
const mainShopBox = new Vue({
    el: '#mainShopBox',
    data: {
       
    }
});

// 디자이너 정보 페이지 샵 박스
const subShopBox = new Vue({
    el: '#sales_tab',
    data: {
       
    }
});

const pfImg = new Vue({
   el: "#pf_tab",
   data: {
       
   }
});

const uploadImg = new Vue({
    el: '#uploadImg',
    data: {
      image: ''
    },
    methods: {
      onDrop: function(e) {
        e.stopPropagation();
        e.preventDefault();
        var files = e.dataTransfer.files;
        this.createFile(files[0]);
      },
      onChange(e) {
        var files = e.target.files;
        this.createFile(files[0]);
      },
      createFile(file) {
        if (!file.type.match('image.*')) {
          alert('Select an image');
          return;
        }
        var img = new Image();
        var reader = new FileReader();
        var vm = this;

        reader.onload = function(e) {
          vm.image = e.target.result;
        };
        reader.readAsDataURL(file);
      },
      removeFile() {
        this.image = '';
      }
    }
  });