$(function () {
    // INITIALIZE DATEPICKER PLUGIN
    $('.datepicker').datepicker({
        clearBtn: true,
        format: 'yyyy-mm-dd',
        todayHighlight: true,
        autoclose: true,
        language: 'ko',
        orientation: 'bottom'
    });
    console.log('.datepicker');

    // FOR DEMO PURPOSE
    $('#saveBtn').click(function (e) {
        e.preventDefault();
        // 수정된 부분: 선택된 날짜를 표시하는 코드
        let selectedDate = $('#reservationDate').val();

        if (selectedDate.length !== 0) {
            $('#pickedDate').html(selectedDate);
            window.location.href = `/admin/attendance?date=${selectedDate}`;
            // GET 요청을 사용하여 AJAX 호출

        } else {
            alert('날짜를 입력해주세요.');
            location.href;
        }
    });


    $(".user-list").each(function() {
        $(this).find("#imageButton").click(function() {
            let date = $("#dateValue").text(); // h2의 텍스트 값을 가져옵니다.
            let email = $(this).closest("tr").data("email"); // 선택한 email 값을 가져옵니다.

            window.location.href = `/admin/workList?date=${date}&email=${email}`;
            // 여기에 email 값을 이용한 추가 동작을 구현할 수 있습니다.
        });
    });
});






