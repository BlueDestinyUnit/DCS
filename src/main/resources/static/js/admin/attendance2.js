$(document).ready(function() {
    $('#datepicker').datepicker({
        format: "yyyy/mm/dd", // 연도만 표시
        startView: "months",
        minViewMode: "days",
        language: "ko",
        autoclose: true,
        container: ".board-container", // datepicker가 표시될 컨테이너 지정
        orientation: 'bottom'
    });



    // FOR DEMO PURPOSE
    $('#saveBtn').click(function (e) {
        e.preventDefault();
        // 수정된 부분: 선택된 날짜를 표시하는 코드
        let Date = $('#datepicker').val();

        if (Date.length !== 0) {
            $('#pickedDate').html(Date);
            let dateParts = Date.split('/');
            let year = dateParts[0];
            let month = dateParts[1];
            let day = dateParts[2];
            let selectedDate = year + '-' + month + '-' + day;
            window.location.href = `/admin/attendance?date=${selectedDate}`;
            // GET 요청을 사용하여 AJAX 호출

        } else {
            alert('날짜를 입력해주세요.');
            location.href;
        }
    });


});






