const modifyPasswordForm = document.getElementById('modifyPasswordForm');

modifyPasswordForm.emailLabelObj = new LabelObj(modifyPasswordForm.querySelector('[rel="emailLabel"]'));
modifyPasswordForm.newPasswordLabelObj = new LabelObj(modifyPasswordForm.querySelector('[rel="newPasswordLabel"]'));
modifyPasswordForm.nicknameLabelObj = new LabelObj(modifyPasswordForm.querySelector('[rel="nicknameLabel"]'));

modifyPasswordForm['emailSend'].onclick = () => {
    modifyPasswordForm.emailLabelObj.setValid(modifyPasswordForm['email'].tests());
    if (!modifyPasswordForm.emailLabelObj.isValid()) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', modifyPasswordForm['email'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;
        loading.hide();
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.'],
            success: ['알림', '입력하신 이메일로 인증번호를 전송하였습니다. 인증번호는 5분간만 유효하니 유의해 주세요.', () => {
                modifyPasswordForm['emailSalt'].value = responseObject.salt;
                modifyPasswordForm['email'].disable();
                modifyPasswordForm['emailSend'].disable();
                modifyPasswordForm['emailCode'].enable();
                modifyPasswordForm['emailCode'].focus();
                modifyPasswordForm['emailVerify'].enable();
            }]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './modifyPasswordEmail');
    xhr.send(formData);
    loading.show();
};

modifyPasswordForm['emailVerify'].onclick = () => {
    modifyPasswordForm.emailLabelObj.setValid(modifyPasswordForm['emailCode'].tests());
    if (!modifyPasswordForm.emailLabelObj.isValid()) return;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', modifyPasswordForm['email'].value);
    formData.append('code', modifyPasswordForm['emailCode'].value);
    formData.append('salt', modifyPasswordForm['emailSalt'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        loading.hide();
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '인증번호가 올바르지 않습니다. 다시 확인해 주세요.', () => modifyPasswordForm['emailCode'].focus()],
            failure_expired: ['경고', '인증 정보가 만료되었습니다. 다시 인증해 주세요.', () => {
                modifyPasswordForm['emailSalt'].value = '';
                modifyPasswordForm['email'].enable();
                modifyPasswordForm['email'].focus();
                modifyPasswordForm['emailSend'].enable();
                modifyPasswordForm['emailCode'].disable();
                modifyPasswordForm['emailCode'].value = '';
                modifyPasswordForm['emailVerify'].disable();
            }],
            success: ['알림', '이메일 인증이 완료되었습니다. 비밀번호 수정을 계속해 주세요.', () => {
                modifyPasswordForm['emailCode'].disable();
                modifyPasswordForm['emailVerify'].disable();
                modifyPasswordForm['nickname'].focus();
            }]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('PATCH', './modifyPasswordEmail');
    xhr.send(formData);
    loading.show();
};

modifyPasswordForm.onsubmit = (e) => {
    e.preventDefault();
    modifyPasswordForm.newPasswordLabelObj.setValid(modifyPasswordForm['newPassword'].tests());
    modifyPasswordForm.nicknameLabelObj.setValid(modifyPasswordForm['nickname'].tests());
    if (modifyPasswordForm['emailSend'].isEnabled() || modifyPasswordForm['emailVerify'].isEnabled()) {
        DialogObj.createSimpleOk('경고', '이메일 인증을 완료해 주세요.').show();
        return;
    }
    if (modifyPasswordForm['newPasswordCheck'].value === '') {
        DialogObj.createSimpleOk('경고', '비밀번호를 한 번 더 입력해 주세요.', () => modifyPasswordForm['newPasswordCheck'].focus()).show();
        return;
    }
    if (modifyPasswordForm['newPassword'].value !== modifyPasswordForm['newPasswordCheck'].value) {
        DialogObj.createSimpleOk('경고', '재입력한 비밀번호가 일치하지 않습니다.', () => modifyPasswordForm['newPasswordCheck'].focus()).show();
        return;
    }
    if (!modifyPasswordForm.newPasswordLabelObj.isValid() || !modifyPasswordForm.nicknameLabelObj.isValid()) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', modifyPasswordForm['email'].value);
    formData.append('code', modifyPasswordForm['emailCode'].value);
    formData.append('password', modifyPasswordForm['newPassword'].value);
    formData.append('nickname', modifyPasswordForm['nickname'].value);
    formData.append('salt', modifyPasswordForm['emailSalt'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;
        loading.hide();
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
    xhr.open('POST', './modifyPassword'); // UserController :: postModifyPassword
    xhr.send(formData);
    loading.show();
};