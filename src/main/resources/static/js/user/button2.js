const backButton2 = document.querySelector('[rel="backButton2"]');

backButton2.onclick = (e) => {
    e.preventDefault();
    location.href = `./myPage`;
}