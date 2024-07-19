const infoForm = document.getElementById('infoForm');

infoForm.myEmailLabelObj = new LabelObj(infoForm.querySelector('[rel="myEmailLabel"]'));
infoForm.myNameLabelObj = new LabelObj(infoForm.querySelector('[rel="myNameLabel"]'));
infoForm.myNicknameLabelObj = new LabelObj(infoForm.querySelector('[rel="myNicknameLabel"]'));
infoForm.myTelLabelObj = new LabelObj(infoForm.querySelector('[rel="myTelLabel"]'));
infoForm.myAddressLabelObj = new LabelObj(infoForm.querySelector('[rel="myAddressLabel"]'));
infoForm.myWorkTypeLabelObj = new LabelObj(infoForm.querySelector('[rel="myWorkTypeLabel"]'));

function modifyInfo() {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', infoForm['email'].value);
    formData.append('name', infoForm['name'].value);
    formData.append('nickname', infoForm['nickname'].value);
    formData.append('tel', infoForm['tel'].value);
    formData.append('address', infoForm['address'].value);
    formData.append('workType', infoForm['workType'].value);
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
            failure: ['경고', '알 수 없는 이유로 회원 정보 수정에 실패하였습니다. 잠시 후 다시 시도해 주세요.'],
            success: ['알림', '회원 정보 수정에 성공하였습니다.', () => moveMyPage()]
        }[responseObject.result] || ['경고', '서버가 예상치 못한 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.'];
        DialogObj.createSimpleOk(dTitle, dContent, dOnclick).show();
    }
    xhr.open('POST', './myPage');
    xhr.send(formData);
}

