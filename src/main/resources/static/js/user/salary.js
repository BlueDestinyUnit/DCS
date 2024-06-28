$(document).ready(function () {

    $('#datepicker').datepicker({
        format: "yyyy", // 월이 먼저, 년도가 뒤에 나오도록 설정
        startView: "years", // 달력 초기 화면을 월 선택기로 설정
        minViewMode: "years", // 최소 선택 단위를 월로 설정
        language: "ko", // 한국어로 설정
        autoclose: true, // 선택 후 자동으로 닫기
        container: ".board-container", // datepicker가 표시될 컨테이너 지정
        orientation: 'bottom'

    });


    $(document).ready(function () {
        // datepicker 초기화 등 다른 코드는 여기에 포함됩니다.

        // payment 데이터 포맷팅
        $('td[rel="payments"]').each(function () {
            let paymentText = $(this).text(); // 현재 셀의 텍스트 가져오기
            let paymentNumber = parseFloat(paymentText.replace(' ₩', '').replace(/,/g, '')); // 숫자로 변환, ₩와 쉼표 제거
            let formattedPayment = paymentNumber.toLocaleString('ko-KR'); // 한국식 포맷으로 숫자 포맷팅
            $(this).text(formattedPayment + ' ₩'); // 포맷팅된 숫자로 셀 내용 업데이트
        });
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

document.addEventListener("DOMContentLoaded", function () {
    let totalWorkDays = 0;
    let totalWorkCount = 0;
    let totalPayment = 0;

    document.querySelectorAll(".workDays").forEach(function (element) {
        console.log("workDays:", element.textContent); // 디버그 출력
        totalWorkDays += parseInt(element.textContent) || 0;
    });

    document.querySelectorAll(".workCount").forEach(function (element) {
        totalWorkCount += parseInt(element.textContent) || 0;
    });

    document.querySelectorAll(".payment").forEach(function (element) {
        totalPayment += parseInt(element.textContent) || 0;
    });

    document.getElementById("totalWorkDays").textContent = totalWorkDays;
    document.getElementById("totalWorkCount").textContent = totalWorkCount;
    document.getElementById("totalPayment").textContent = totalPayment;
});