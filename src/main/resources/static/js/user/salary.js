$(document).ready(function () {
    // $('.yearpicker').datepicker({
    //
    //     minViewMode: 'years',
    //
    //     format: 'yyyy'
    //
    // });
    $('#datepicker').datepicker({
        format: "yyyy", // 월이 먼저, 년도가 뒤에 나오도록 설정
        startView: "years", // 달력 초기 화면을 월 선택기로 설정
        minViewMode: "years", // 최소 선택 단위를 월로 설정
        language: "ko", // 한국어로 설정
        autoclose: true, // 선택 후 자동으로 닫기
        container: ".board-container", // datepicker가 표시될 컨테이너 지정
        orientation: 'bottom'

    });

    $('button[rel="dateButton"]').each(function () {
        $(this).on('click', function (e) {
            e.preventDefault();
            let Date = $('#datepicker').val();
            if (!Date) {
                alert('날짜를 입력해주세요.');
            } else {
                window.location.href = `/user/salary?date=${Date}`

            }

        });
    });
});

