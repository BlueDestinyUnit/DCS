// const accordion = document.getElementById('#accordionExample');
// const collapseOne = document.getElementById('collapseOne');
//
// collapseOne.onclick = function (e) {
//
// }

const textPercentElement = document.querySelector('.text-percent');
const textPercentValue = textPercentElement.textContent.trim();
const percentValue = textPercentValue.replace('%', '');
const progressBar = document.getElementById('myProgressBar');
progressBar.style.width = percentValue + '%';

const textPercentElementOfYear = document.querySelector('.text-percentOfYear');
const textPercentValueOfYear = textPercentElementOfYear.textContent.trim();
const percentValueOfYear = textPercentValueOfYear.replace('%', '');
const progressBarOfYear = document.getElementById('myProgressBarOfYear');
progressBarOfYear.style.width = percentValueOfYear + '%';



const targetWidth = percentValue;
const targetWidthOfYear = percentValueOfYear;

let currentWidth = 0;
function animateProgressBar() {
    if (currentWidth < targetWidth) {
        currentWidth++;
        progressBar.style.width = currentWidth + '%';
        requestAnimationFrame(animateProgressBar);
    }
    if (currentWidth < targetWidthOfYear) {
        currentWidth++;
        progressBarOfYear.style.width = currentWidth + '%';
        requestAnimationFrame(animateProgressBar);
    }
}

// 페이지 로드 후 애니메이션 시작
animateProgressBar();