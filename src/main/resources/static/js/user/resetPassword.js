const resetPasswordForm = document.getElementById('resetPasswordForm');

resetPasswordForm.emailLabelObj = new LabelObj(resetPasswordForm.querySelector('[rel="emailLabel"]'));
resetPasswordForm.newPasswordLabelObj = new LabelObj(resetPasswordForm.querySelector('[rel="newPasswordLabel"]'));
resetPasswordForm.nicknameLabelObj = new LabelObj(resetPasswordForm.querySelector('[rel="nicknameLabel"]'));

resetPasswordForm['emailSend'].onclick = () => {
    resetPasswordForm.emailLabelObj.setValid(resetPasswordForm['email'].tests());
    if (!resetPasswordForm.emailLabelObj.isValid()) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', resetPasswordForm['email'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.'],
            success: ['알림', '입력하신 이메일로 인증번호를 전송하였습니다. 인증번호는 5분간만 유효하니 유의해 주세요.', () => {
                resetPasswordForm['emailSalt'].value = responseObject.salt;
                resetPasswordForm['email'].disable();
                resetPasswordForm['emailSend'].disable();
                resetPasswordForm['emailCode'].enable();
                resetPasswordForm['emailCode'].focus();
                resetPasswordForm['emailVerify'].enable();
            }]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './resetPasswordEmail');
    xhr.send(formData);

};

resetPasswordForm['emailVerify'].onclick = () => {
    resetPasswordForm.emailLabelObj.setValid(resetPasswordForm['emailCode'].tests());
    if (!resetPasswordForm.emailLabelObj.isValid()) return;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', resetPasswordForm['email'].value);
    formData.append('code', resetPasswordForm['emailCode'].value);
    formData.append('salt', resetPasswordForm['emailSalt'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '인증번호가 올바르지 않습니다. 다시 확인해 주세요.', () => resetPasswordForm['emailCode'].focus()],
            failure_expired: ['경고', '인증 정보가 만료되었습니다. 다시 인증해 주세요.', () => {
                resetPasswordForm['emailSalt'].value = '';
                resetPasswordForm['email'].enable();
                resetPasswordForm['email'].focus();
                resetPasswordForm['emailSend'].enable();
                resetPasswordForm['emailCode'].disable();
                resetPasswordForm['emailCode'].value = '';
                resetPasswordForm['emailVerify'].disable();
            }],
            success: ['알림', '이메일 인증이 완료되었습니다. 회원가입을 계속해 주세요.', () => {
                resetPasswordForm['emailCode'].disable();
                resetPasswordForm['emailVerify'].disable();
                resetPasswordForm['nickname'].focus();
            }]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('PATCH', './resetPasswordEmail');
    xhr.send(formData);
};

resetPasswordForm.onsubmit = (e) => {
    e.preventDefault();
    resetPasswordForm.newPasswordLabelObj.setValid(resetPasswordForm['newPassword'].tests());
    resetPasswordForm.nicknameLabelObj.setValid(resetPasswordForm['nickname'].tests());
    if (resetPasswordForm['emailSend'].isEnabled() || resetPasswordForm['emailVerify'].isEnabled()) {
        DialogObj.createSimpleOk('경고', '이메일 인증을 완료해 주세요.').show();
        return;
    }
    if (resetPasswordForm['newPasswordCheck'].value === '') {
        DialogObj.createSimpleOk('경고', '비밀번호를 한 번 더 입력해 주세요.', () => resetPasswordForm['newPasswordCheck'].focus()).show();
        return;
    }
    if (resetPasswordForm['newPassword'].value !== resetPasswordForm['newPasswordCheck'].value) {
        DialogObj.createSimpleOk('경고', '재입력한 비밀번호가 일치하지 않습니다.', () => resetPasswordForm['newPasswordCheck'].focus()).show();
        return;
    }
    if (!resetPasswordForm.newPasswordLabelObj.isValid() || !resetPasswordForm.nicknameLabelObj.isValid()) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', resetPasswordForm['email'].value);
    formData.append('code', resetPasswordForm['emailCode'].value);
    formData.append('password', resetPasswordForm['newPassword'].value);
    formData.append('nickname', resetPasswordForm['nickname'].value);
    formData.append('salt', resetPasswordForm['emailSalt'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;

        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '알 수 없는 이유로 비밀번호 재설정에 실패하였습니다. 잠시 후 다시 시도해 주세요.'],
            success: ['알림', '수정이 완료 되었습니다. 확인 버튼을 누르시면 로그인 화면으로 돌아갑니다.', () => moveLogin()]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './resetPassword'); // UserController :: postResetPassword
    xhr.send(formData);

};