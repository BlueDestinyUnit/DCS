const demo = document.getElementById('demo');

const button = demo.querySelector('[rel="button"]');

const checkbox = demo.querySelector('[rel="checkbox"]');

const comment = demo.querySelector('[rel="comment"]');

function handleCheckboxChange() {
    // 체크박스가 선택되었는지 확인
    if (checkbox.checked) {
        comment.dataset.previousValue = comment.value;
        comment.value = '';

        comment.readOnly = false;
    } else {
        comment.value = comment.dataset.previousValue || '';
        comment.readOnly = true;
    }
}

// 체크박스의 상태가 변경될 때 handleCheckboxChange 함수 실행
checkbox.addEventListener('change', handleCheckboxChange);


    button.onclick = function (e) {
        console.log(comment.value);
        e.preventDefault();
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('comment', comment.value);
        formData.append('check', checkbox.value);
        xhr.onreadystatechange = function () {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                return;
            }
            const responseObject = JSON.parse(xhr.responseText);
        }
        xhr.open('POST', '/admin/workList');
        xhr.send(formData);
    }


