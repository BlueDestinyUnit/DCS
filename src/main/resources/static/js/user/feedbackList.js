$(document).ready(function () {
    $('#datepicker').datepicker({
        format: "yyyy/mm", // 월이 먼저, 년도가 뒤에 나오도록 설정
        startView: "months", // 달력 초기 화면을 월 선택기로 설정
        minViewMode: "months", // 최소 선택 단위를 월로 설정
        language: "ko", // 한국어로 설정
        autoclose: true, // 선택 후 자동으로 닫기
        container: ".board-container", // datepicker가 표시될 컨테이너 지정
        orientation: 'bottom'

    });

    const demo = document.getElementById('demo');
    const payments = demo.querySelectorAll('[rel="payments"]');


    $('button[rel="dateButton"]').each(function () {
        $(this).on('click', function (e) {
            e.preventDefault();
            let Date = $('#datepicker').val();
            if (!Date) {
                alert('날짜를 입력해주세요.');
            } else {


                let dateParts = Date.split('/');
                let year = dateParts[0];
                let month = dateParts[1];
                let selectedDate = year + '-' + month;
                window.location.href = `/user/feedbackList?date=${selectedDate}`;
            }

        });
    });

    const month = demo.querySelector('.date-value').value;
    const table = demo.querySelector('#table');
    const rows = table.querySelectorAll('tr');
    rows.forEach(row => {
        row.addEventListener('click', function () {
                const day = this.getAttribute('data-index');
                let intDay = parseInt(day);
                intDay++;
                const stringDay = String(intDay).padStart(2, '0');;
                const date = month + '-' + stringDay;
                window.location.href = `/user/feedback?date=${date}`;
        });
    });
});