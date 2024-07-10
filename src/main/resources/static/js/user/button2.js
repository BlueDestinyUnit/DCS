const modifyButton = document.querySelector('[rel="modifyButton"]');

const modifyPasswordButton = document.querySelector('[rel="modifyPasswordButton"]');

modifyButton.onclick = (e) => {
    e.preventDefault();
    modifyInfo();
}

modifyPasswordButton.onclick = (e) => {
    e.preventDefault();
    location.href = './resetPassword'
}