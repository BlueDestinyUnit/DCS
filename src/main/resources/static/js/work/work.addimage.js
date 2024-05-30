const workListAside = document.getElementById('workListAside');

const photoAddButton = document.getElementById('photoAddButton');
const photoAddButtonForm = document.getElementById('photoAddButtonForm')
const workDate = document.getElementById('workDate').innerText;


loadWorkList();


function loadWorkList() {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {

            return;
        }
        const responseArray = JSON.parse(xhr.responseText);
        console.log(responseArray)
        const listEl = workListAside.querySelector('.menu-list');
        listEl.innerHTML = '';
        if (responseArray.length === 0) {
            const emptyItemEl = new DOMParser().parseFromString(`
                <li class="item empty">사진이 없습니다<br><br>정제 파일을 추가해 주세요.</li>`, 'text/html').querySelector('li');
            listEl.append(emptyItemEl);
            return;
        }
        for (const workObject of responseArray) {
            // const
            const itemEl = new DOMParser().parseFromString(`
            <li class="item">
                <span>${workObject['originalName']}</span>
                <img class="imageList"  src="./subImage?index=${workObject['index']}" alt="" width="100" height="100">
            </li>`,'text/html').querySelector('li');
            itemEl.querySelector('img').onclick = (e) => {
                const mainImage = document.getElementById('image');
                mainImage.src = e.target.src;
            };
            listEl.append(itemEl);
        }
    }
    xhr.open('GET', `../work/?date=${workDate}`) /*?date=${workDate}*/
    xhr.send();
}

document.addEventListener('DOMContentLoaded', loadWorkList);

const input = photoAddButton.querySelector('input');

input.onchange = function (e) {
    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData(photoAddButtonForm);
    formData.append('date',workDate);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {

            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        switch (responseObject['result']) {
            case 'failure':
                alert('알 수 없는 이유로 파일을 가져오지 못했습니다. 잠시 후 다시 시도해 주세요.')
                break;
            case 'success':
                loadWorkList()
                break;
            default:
                alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.')
        }
    }
    xhr.open('POST', `./addWork`)
    xhr.send(formData);

}





