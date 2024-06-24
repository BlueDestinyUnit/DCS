const infoForm = document.getElementById('infoForm');

infoForm.myEmailLabelObj = new LabelObj(infoForm.querySelector('[rel="myEmailLabel"]'));
infoForm.myNameLabelObj = new LabelObj(infoForm.querySelector('[rel="myNameLabel"]'));
infoForm.myNicknameLabelObj = new LabelObj(infoForm.querySelector('[rel="myNicknameLabel"]'));
infoForm.myTelLabelObj = new LabelObj(infoForm.querySelector('[rel="myTelLabel"]'));
infoForm.myAddressLabelObj = new LabelObj(infoForm.querySelector('[rel="myAddressLabel"]'));

function modifyInfo() {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {

            return;
        }
    }
    xhr.open('POST', './user/myPage');
    xhr.send(formData);
}