const logoutButton = document.querySelector('.logout-button');
logoutButton.onclick = () => {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();

    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert("알 수없는 오류가 발생했습니다.")
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const result = responseObject['result'];
        if( result === 'success' ) {
            location.href ='/user/login';
        }
    }
    xhr.open('POST','/logout/');
    xhr.send();
}