const demo = document.getElementById('demo');
const backButton = demo.querySelector('[rel="backButton"]');


backButton.onclick = () => {
    const date = demo.querySelector('.date-value').value;
    const month = String(date).substring(0, 7);
    window.location.href = `/user/feedbackList?date=${month}`;
}

const prevButtons = demo.querySelectorAll('.arrow-prev');
prevButtons.forEach(button => {
    button.onclick = () => {
        const date = demo.querySelector('.date-value').value;
        const parts = date.split('-');
        const year = parts[0];
        const month = parts[1];
        const day = parts[2];
        const prevDay = String(parseInt(day) - 1).padStart(2, '0');
        if (prevDay > 0) {
            const prevDate = year + '-' + month + '-' + prevDay;
            window.location.href = `/user/feedback?date=${prevDate}`;
        } else {
            const intPrevMonth = parseInt(month) - 1;
            const daysInMonth = new Date(year, intPrevMonth, 0).getDate();
            const prevMonth = String(intPrevMonth).padStart(2, '0');
            const prevDate = year + '-' + prevMonth + '-' + daysInMonth;
            window.location.href = `/user/feedback?date=${prevDate}`;

            if (prevMonth > 0) {
                return;
            } else {
                const prevYear = String( parseInt(year) - 1);
                const prevDate = prevYear + '-' + 12 + '-' + 31;
                window.location.href = `/user/feedback?date=${prevDate}`;
            }
        }
    };
});

//              위에 prev 그대로 밑에 next 로 바꾸기

const nextButtons = demo.querySelectorAll('.arrow-next');
nextButtons.forEach(button => {
    button.onclick = () => {
        const date = demo.querySelector('.date-value').value;
        const parts = date.split('-');
        const year = parts[0];
        const month = parts[1];
        const day = parts[2];
        const nextDay = String(parseInt(day) + 1).padStart(2, '0');
        if (nextDay > 0) {  // 그 달 최대일보다 작다면으로 수정
            const nextDate = year + '-' + month + '-' + nextDay;
            window.location.href = `/user/feedback?date=${nextDate}`;
        } else {
            const intNextMonth = parseInt(month) - 1;
            const daysInMonth = new Date(year, intNextMonth, 0).getDate();
            const nextMonth = String(intNextMonth).padStart(2, '0');
            const nextDate = year + '-' + nextMonth + '-' + daysInMonth;
            window.location.href = `/user/feedback?date=${nextDate}`;

            if (nextMonth > 0) {
                return;
            } else {
                const nextYear = String( parseInt(year) - 1);
                const nextDate = nextYear + '-' + 12 + '-' + 31;
                window.location.href = `/user/feedback?date=${nextDate}`;
            }
        }
    };
});

function openModal(imageUrl) {
    let modal = document.getElementById('myModal');
    let modalImg = document.getElementById('modalImage');
    modal.style.display = 'block';
    modalImg.src = imageUrl;
}

function closeModal() {
    let modal = document.getElementById('myModal');
    modal.style.display = 'none';
}

// 모달 바깥 영역 클릭 시 모달 닫기
window.onclick = function (event) {
    let modal = document.getElementById('myModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}
document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        // ESC 키가 눌렸을 때 실행할 동작을 여기에 추가합니다.
        console.log('ESC 키가 눌렸습니다.');

        // 예시: 모달을 닫는 함수 호출
        closeModal();
    }
});