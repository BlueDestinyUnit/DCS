const demo = document.getElementById('demo');

const backButton = demo.querySelector('[rel="backButton"]');

const submitButton = demo.querySelector('[rel="submitButton"]');


// function handleCheckboxChange() {
//     // 체크박스가 선택되었는지 확인
//     if (checkbox.checked) {
//         comment.dataset.previousValue = comment.value;
//         comment.value = '';
//
//         comment.readOnly = false;
//     } else {
//         comment.value = comment.dataset.previousValue || '';
//         comment.readOnly = true;
//     }
// }


// 메인 리스트들을 다 가져와서 바로 2분할


function handleCheckboxChange() {

    // 요소 찾는법 해당 체크박스로 커멘트를 추적 경로가 본인에서 부모 -> 부모 -> 자식 -> 커맨트
    const mainList = demo.querySelectorAll('.main-list');
    for (const main of mainList) {
        const checkbox = main.querySelector('.checkbox');
        const comment = main.querySelector('.comment');


        checkbox.onclick = (event) => {
            if (checkbox.checked) {
                comment.setAttribute('class', 'focus');
                comment.setAttribute('placeholder', '');
                comment.focus();
                // 체크박스가 선택된 경우
                // 이전 값을 저장
                comment.dataset.previousValue = comment.value;
                // 입력 필드를 비움
                comment.value = '';
                // 입력 필드를 읽기 전용 해제
                comment.readOnly = false;
            } else {
                comment.setAttribute('class', 'comment');
                comment.setAttribute('placeholder', '선택 후 작성 가능합니다.');
                if (comment.value.length !== 0) {
                    if (!confirm(`작성한 내용이 삭제됩니다.
계속하시겠습니까?`)) {

                        // 이벤트 기본 동작 막기
                        event.preventDefault();
                        // 체크박스를 다시 선택된 상태로 되돌림
                        checkbox.checked = true;
                        return;
                    }
                }
                // 체크박스가 선택 해제된 경우
                // 이전 값 복원


                comment.value = comment.dataset.previousValue || '';
                // 입력 필드를 읽기 전용으로 설정
                comment.readOnly = true;
            }

        };
    }
}

handleCheckboxChange();


backButton.onclick = () => {
    history.back();
}


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
window.onclick = function(event) {
    let modal = document.getElementById('myModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        // ESC 키가 눌렸을 때 실행할 동작을 여기에 추가합니다.
        console.log('ESC 키가 눌렸습니다.');

        // 예시: 모달을 닫는 함수 호출
        closeModal();
    }
});

submitButton.onclick = function (e) {
    e.preventDefault();
    const mainList = document.querySelectorAll('.main-list');
    const xhr = new XMLHttpRequest();

    // {index: index , comment : comment , checked : true}
    let sendList = [];
    for (let i = 0; i < mainList.length; i++) {
        const checkInput = mainList[i].querySelector('input[type="checkbox"][rel="checkbox"]');
        if (checkInput.checked === true) {
            const checkIndex = mainList[i].querySelector('[rel="index"]').value;
            const checkComment = mainList[i].querySelector('[rel="comment"]').value;

            const sendObject = {index: checkIndex, comment: checkComment};
            sendList.push(sendObject);
        }
    }

    let date = '2024-06-11'
    let userEmail = 'lsg9134@gmail.com'
    const formData = {
        sendList: sendList,
        date: date,
        userEmail: userEmail
    }

    fetch('./workList', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    }).then(response => {
        if (!response.ok) {
            throw new Error("warning")
        }
        return response.json()
    }).then(data => {
        console.log(data)
        if (data['result'] === 'success') {
            alert('정상');
        } else {
            alert('비정상');
        }
    }).catch(error => {
        console.log(error)
    });
}


