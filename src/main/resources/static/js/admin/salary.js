$(document).ready(function(){
    $('#datepicker').datepicker({
        format: "yyyy/mm", // 월이 먼저, 년도가 뒤에 나오도록 설정
        startView: "months", // 달력 초기 화면을 월 선택기로 설정
        minViewMode: "months", // 최소 선택 단위를 월로 설정
        language: "ko", // 한국어로 설정
        autoclose: true, // 선택 후 자동으로 닫기
        container: ".board-container" // datepicker가 표시될 컨테이너 지정

    });

    $('#submitDate').on('click', function(e){
        e.preventDefault();
        let Date = $('#datepicker').val();
        let dateParts = Date.split('/');
        let year = dateParts[0];
        let month = dateParts[1];
        if (Date.length !== 0) {
            let selectedDate = year + '-' + month;
            $('#pickedDate').html(selectedDate);
            window.location.href = `/admin/salary?date=${selectedDate}`;
            // GET 요청을 사용하여 AJAX 호출
        } else {
            alert('날짜를 입력해주세요.');
            window.location.href = `/admin/salary`;
        }
    });
});