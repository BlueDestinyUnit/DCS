const chatCreateForm = document.getElementById('chatCreateForm');
const joinButtons = document.querySelectorAll('[rel="joinButton"]')
const deleteButtons = document.querySelectorAll('[rel="deleteButton"]')

console.log(joinButtons);
console.log(deleteButtons)

chatCreateForm.onsubmit = function(e) {
    e.preventDefault();
    const roomName = chatCreateForm.roomName.value;
    const xhr = new XMLHttpRequest();
    const formData = new FormData(chatCreateForm);

    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert("알 수없는 오류가 발생했습니다.")
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        location.href = `/chat/chatRoom?index=${responseObject.index}`;
    }
    xhr.open('POST','./createRoom');
    xhr.send(formData);
}

joinButtons.forEach(el => {
   el.addEventListener('click', (event) => {
       event.preventDefault();
       const roomIndex = el.value;
       location.href = `/chat/chatRoom?index=${roomIndex}`;
   })
});

deleteButtons.forEach(el => {
    el.addEventListener('click', (event) => {
        event.preventDefault();
        const roomIndex = el.value;
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('index',roomIndex);
        xhr.onreadystatechange = function () {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert("알 수없는 오류가 발생했습니다.")
                return;
            }
            const responseObject = JSON.parse(xhr.responseText);
            switch (responseObject.result) {
                case 'success': {
                    alert('삭제에 성공하였습니다.')
                    location.reload();
                }
                case 'failure': {
                    alert('삭제에 실패하였습니다.')
                }
            }

        }
        xhr.open('DELETE','./deleteRoom');
        xhr.send(formData);
    })
})

