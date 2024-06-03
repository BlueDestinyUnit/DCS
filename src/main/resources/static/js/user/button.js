const backButton = document.querySelector('[rel="backButton"]');

backButton.onclick = (e) => {
    e.preventDefault();
    location.href = `./login`;
}