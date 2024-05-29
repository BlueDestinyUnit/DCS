const findEmailForm = document.getElementById('findEmailForm');

findEmailForm.nicknameLabelObj = new LabelObj(findEmailForm.querySelector('[rel="nicknameLabel"]'));

findEmailForm.onsubmit = (e) => {
    e.preventDefault();
    findEmailForm.nicknameLabelObj.setValid(findEmailForm['nickname'].tests());
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('nickname', findEmailForm['nickname'].value);
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
            failure: ['경고', '알 수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요.'],
            success: ['알림', `해당 닉네임으로 찾은 회원의 이메일은 <b>${responseObject['email']}</b>입니다. 확인을 클릭하면 로그인 페이지로 돌아갑니다.`]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './findEmail');
    xhr.send(formData);
}