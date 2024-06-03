const loginForm = document.getElementById('loginForm');

loginForm.emailLabelObj = new LabelObj(loginForm.querySelector('[rel="emailLabel"]'));
loginForm.passwordLabelObj = new LabelObj(loginForm.querySelector('[rel="passwordLabel"]'));
loginForm.onsubmit = (e) => {
    e.preventDefault();
    loginForm.emailLabelObj.setValid(loginForm['username'].tests());
    loginForm.passwordLabelObj.setValid(loginForm['password'].tests());
    if (!loginForm.emailLabelObj.isValid() || !loginForm.passwordLabelObj.isValid()) return;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', loginForm['email'].value);
    formData.append('password', loginForm['password'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        if (responseObject.result === 'success') {
            location.reload();
            return;
        }
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '이메일 혹은 비밀번호가 올바르지 않습니다. 다시 확인해 주세요.', () => loginForm['email'].focus()]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './login');
    xhr.send(formData);
}