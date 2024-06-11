const demo = document.getElementById('demo');

const button = demo.querySelector('[rel="button"]');

const checkbox = demo.querySelector('[rel="checkbox"]');

const comments = demo.querySelectorAll('[rel="comment"]');

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
function handleCheckboxChange() {
    for (const comment of comments) {
        // 체크박스가 선택되었는지 확인
        if (checkbox.checked) {
            // 이전 값을 저장
            comment.dataset.previousValue = comment.value;
            // 입력 필드를 비움
            comment.value = '';
            // 입력 필드를 읽기 전용 해제
            comment.readOnly = false;
        } else {
            // 이전 값 복원
            comment.value = comment.dataset.previousValue || '';
            // 입력 필드를 읽기 전용으로 설정
            comment.readOnly = true;
        }
    }
}


// 체크박스의 상태가 변경될 때 handleCheckboxChange 함수 실행
checkbox.addEventListener('change', handleCheckboxChange);


button.onclick = function (e) {
    e.preventDefault();
    const mainList = document.querySelectorAll('.main-list');
    const xhr = new XMLHttpRequest();

    // {index: index , comment : comment , checked : true}
    let sendList = [];
    for (let i = 0; i < mainList.length; i++) {
        const checkInput = mainList[i].querySelector('input[type="checkbox"][rel="checkbox"]');
        if(checkInput.checked === true ){
            const checkIndex = mainList[i].querySelector('[rel="index"]').value;
            const checkComment = mainList[i].querySelector('[rel="comment"]').value;

           const sendObject =  {index : checkIndex , comment : checkComment};
            sendList.push(sendObject);
        }
    }

    let date = '2024-06-11'
    let userEmail = 'lsg9134@gmail.com'
    const formData = {
        sendList : sendList,
        date : date,
        userEmail : userEmail
    }

    fetch('./workList', {
        method: 'POST',
        headers: {'Content-Type' : 'application/json' },
        body : JSON.stringify(formData)
    }).then(response => {
        if (!response.ok) {
            throw new Error("warning")
        }
        return response.json()
    }).then(data => {
        console.log(data)
        // 여기



    }).catch(error => {
        console.log(error)
    });
}


