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
            window.location.href = `/admin/`;
        }
    });


});


// $.ajax({
//     url: `/admin/attendance?date=${selectedDate}`,
//     type: 'GET',
//     success: function (response) {
//         console.log('성공:', response);
//         // 성공 시 필요한 동작을 여기에 추가
//     },
//     error: function (error) {
//         console.log('오류:', error);
//         // 오류 시 필요한 동작을 여기에 추가
//     }
// });

//  onclick 날짜를 파미터로 해서 location 한다 /admin/attendance?date=${date}