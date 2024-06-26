const registerForm = document.getElementById('registerForm');

registerForm.emailLabelObj = new LabelObj(registerForm.querySelector('[rel="emailLabel"]'));
registerForm.passwordLabelObj = new LabelObj(registerForm.querySelector('[rel="passwordLabel"]'));
registerForm.nicknameLabelObj = new LabelObj(registerForm.querySelector('[rel="nicknameLabel"]'));
registerForm.nameLabelObj = new LabelObj(registerForm.querySelector('[rel="nameLabel"]'));
registerForm.telLabelObj = new LabelObj(registerForm.querySelector('[rel="telLabelObj"]'));
registerForm.addressLabelObj = new LabelObj(registerForm.querySelector('[rel="addressLabelObj"]'));



registerForm['emailSend'].onclick = () => {
    registerForm.emailLabelObj.setValid(registerForm['email'].tests());
    if (!registerForm.emailLabelObj.isValid()) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', registerForm['email'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.'],
            failure_duplicate_email: ['경고', `입력하신 이메일 <b>${registerForm['email'].value}</b>은 이미 사용 중입니다.`, () => registerForm['email'].focus()],
            success: ['알림', '입력하신 이메일로 인증번호를 전송하였습니다. 인증번호는 5분간만 유효하니 유의해 주세요.', () => {
                registerForm['emailSalt'].value = responseObject.salt;
                registerForm['email'].disable();
                registerForm['emailSend'].disable();
                registerForm['emailCode'].enable();
                registerForm['emailCode'].focus();
                registerForm['emailVerify'].enable();
            }]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './registerEmail');
    xhr.send(formData);
    
};

registerForm['emailVerify'].onclick = () => {
    registerForm.emailLabelObj.setValid(registerForm['emailCode'].tests());
    if (!registerForm.emailLabelObj.isValid()) return;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', registerForm['email'].value);
    formData.append('code', registerForm['emailCode'].value);
    formData.append('salt', registerForm['emailSalt'].value);
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
            failure: ['경고', '인증번호가 올바르지 않습니다. 다시 확인해 주세요.', () => registerForm['emailCode'].focus()],
            failure_expired: ['경고', '인증 정보가 만료되었습니다. 다시 인증해 주세요.', () => {
                registerForm['emailSalt'].value = '';
                registerForm['email'].enable();
                registerForm['email'].focus();
                registerForm['emailSend'].enable();
                registerForm['emailCode'].disable();
                registerForm['emailCode'].value = '';
                registerForm['emailVerify'].disable();
            }],
            success: ['알림', '이메일 인증이 완료되었습니다. 회원가입을 계속해 주세요.', () => {
                registerForm['emailCode'].disable();
                registerForm['emailVerify'].disable();
                registerForm['password'].focus();
            }]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('PATCH', './registerEmail');
    xhr.send(formData);
};

registerForm.onsubmit = (e) => {
    e.preventDefault();
    registerForm.passwordLabelObj.setValid(registerForm['password'].tests());
    registerForm.nicknameLabelObj.setValid(registerForm['nickname'].tests());
    if (registerForm['emailSend'].isEnabled() || registerForm['emailVerify'].isEnabled()) {
        DialogObj.createSimpleOk('경고', '이메일 인증을 완료해 주세요.').show();
        return;
    }
    if (registerForm['passwordCheck'].value === '') {
        DialogObj.createSimpleOk('경고', '비밀번호를 한 번 더 입력해 주세요.', () => registerForm['passwordCheck'].focus()).show();
        return;
    }
    if (registerForm['password'].value !== registerForm['passwordCheck'].value) {
        DialogObj.createSimpleOk('경고', '재입력한 비밀번호가 일치하지 않습니다.', () => registerForm['passwordCheck'].focus()).show();
        return;
    }
    if (!registerForm.passwordLabelObj.isValid() || !registerForm.nicknameLabelObj.isValid()) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', registerForm['email'].value);
    formData.append('code', registerForm['emailCode'].value);
    formData.append('salt', registerForm['emailSalt'].value);
    formData.append('password', registerForm['password'].value);
    formData.append('name', registerForm['name'].value);
    formData.append('nickname', registerForm['nickname'].value);
    formData.append('tel', registerForm['tel'].value);
    formData.append('address', registerForm['address'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;
        
        if (xhr.status < 200 || xhr.status >= 300) {
            DialogObj.createSimpleOk('오류', '요청을 전송하는 도중 오류가 발생하였습니다.').show();
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const [dTitle, dContent, dOnclick] = {
            failure: ['경고', '알 수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요.'],
            failure_duplicate_email: ['경고', `입력하신 이메일 <b>${registerForm['email'].value}</b>은 이미 사용 중입니다.`, () => registerForm['email'].focus()],

            failure_duplicate_name: ['경고', `입력하신 이메일 <b>${registerForm['name'].value}</b>은 이미 사용 중입니다.`, () => registerForm['name'].focus()],

            failure_duplicate_nickname: ['경고', `입력하신 닉네임 <b>${registerForm['nickname'].value}</b>은 이미 사용 중입니다.`, () => registerForm['nickname'].focus()],

            failure_duplicate_tel: ['경고', `입력하신 연락처 <b>${registerForm['tel'].value}</b>은 이미 사용 중입니다.`, () => registerForm['tel'].focus()],

            success: ['알림', '회원가입해 주셔서 감사합니다. 확인 버튼을 클릭하면 로그인 화면으로 이동합니다.', () => moveLogin()]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './'); // UserController :: postRegister
    xhr.send(formData);
    
};