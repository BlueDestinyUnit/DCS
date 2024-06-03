let canvas = document.getElementById('canvas');
let ctx = canvas.getContext('2d');
let image = document.getElementById('image');

const selectImageId = document.getElementById('selectImageId');
const selectImageName = document.getElementById('selectImageName');
console.log(image);

image.crossOrigin = "anonymous";
let isDragging = false;
let startX, startY;
const saveForm = document.getElementById('saveForm');
const resetButton = document.getElementById('resetButton');
let mosaicAreas = [];
let dragCount = 0;
image.onload = function() {
    ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
};

canvas.addEventListener('mousedown', startDragging);
canvas.addEventListener('mousemove', drag);
canvas.addEventListener('mouseup', stopDragging);
canvas.addEventListener('mouseleave', stopDragging);

function startDragging(event) {
    isDragging = true;
    startX = event.offsetX;
    startY = event.offsetY;
}

function drag(event) {

    if (isDragging) {
        let endX = event.offsetX;
        let endY = event.offsetY;
        let width = endX - startX;
        let height = endY - startY;

        ctx.drawImage(image, 0, 0, canvas.width, canvas.height);

        // 이전 모자이크 영역을 다시 그립니다. ( 모자이크 증폭 원인 나중에 사용 할 수도 있으니 남겨놈 )
        // mosaicAreas.forEach(area => {
        //     applyMosaic(area.startX, area.startY, area.endX, area.endY, area.size);
        // });

        // 현재 드래그 영역을 시각적으로 표시
        ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
        ctx.fillRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(width), Math.abs(height));
    }
}

function stopDragging(event) {
    if (isDragging) {
        isDragging = false;
        dragCount++;
        console.log(dragCount)
        let endX = event.offsetX;
        let endY = event.offsetY;

        // 현재 드래그 영역을 저장
        mosaicAreas.push({ startX, startY, endX, endY, size: 10 });

        // 최종 이미지에 모자이크 적용
        ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
        mosaicAreas.forEach(area => {
            applyMosaicRect(area.startX, area.startY, area.endX, area.endY, area.size);
        });

        // 다음 드래그를 위해 시각적 피드백 제거
        ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
        mosaicAreas.forEach(area => {
            applyMosaicRect(area.startX, area.startY, area.endX, area.endY, area.size);
        });
    }
}

function applyMosaicRect(startX, startY, endX, endY, size) {
    let x = Math.min(startX, endX);
    let y = Math.min(startY, endY);
    let width = Math.abs(endX - startX);
    let height = Math.abs(endY - startY);
    applyMosaic(x, y, width, height, size);
}

function applyMosaic(x, y, width, height, size) {
    let imageData = ctx.getImageData(x, y, width, height);
    let data = imageData.data;

    for (let yOffset = 0; yOffset < height; yOffset += size) {
        for (let xOffset = 0; xOffset < width; xOffset += size) {
            let red = 0, green = 0, blue = 0;
            let pixelCount = 0;

            for (let dy = 0; dy < size; dy++) {
                for (let dx = 0; dx < size; dx++) {
                    let pixelIndex = ((yOffset + dy) * width + (xOffset + dx)) * 4;
                    if (pixelIndex < data.length) {
                        red += data[pixelIndex];
                        green += data[pixelIndex + 1];
                        blue += data[pixelIndex + 2];
                        pixelCount++;
                    }
                }
            }

            red = Math.floor(red / pixelCount);
            green = Math.floor(green / pixelCount);
            blue = Math.floor(blue / pixelCount);

            for (let dy = 0; dy < size; dy++) {
                for (let dx = 0; dx < size; dx++) {
                    let pixelIndex = ((yOffset + dy) * width + (xOffset + dx)) * 4;
                    if (pixelIndex < data.length) {
                        data[pixelIndex] = red;
                        data[pixelIndex + 1] = green;
                        data[pixelIndex + 2] = blue;
                    }
                }
            }
        }
    }

    ctx.putImageData(imageData, x, y);
}

// saveButton.onclick = function (e) {
//     let imageDataURL = canvas.toDataURL('image/png');
//     let downloadLink = document.createElement('a');
//     downloadLink.href = imageDataURL;
//     downloadLink.download = 'mosaic_image.png';
//     downloadLink.click();
// };

saveForm.onclick = function (e) {
    e.preventDefault()
    canvas.toBlob(function(blob) {
        if (blob) {
            // 이미지 파일을 FormData에 추가
            const formData = new FormData();
            formData.append('index', saveForm['index'].value);
            formData.append('dragCount', dragCount);
            formData.append('images', blob,selectImageName.innerText);
            // 서버로 FormData 전송
            const xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState !== XMLHttpRequest.DONE) {
                    return;
                }
                if (xhr.status < 200 || xhr.status >= 300) {
                    return;
                }
                const responseObject = JSON.parse(xhr.responseText);
                loadWorkList();
            }
            xhr.open('POST', './updateImage');
            xhr.send(formData);
        }
    }, 'image/png', 1);

};

resetButton.onclick = function (e) {
    mosaicAreas = [];
    ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
};

// 사진 파일 - > 버튼
document.querySelector('.photoAddButton').addEventListener('click', function() {
    this.querySelector('input[type="file"]').click();
});


// addimage 부분

const workListAside = document.getElementById('workListAside');

const photoAddButton = document.getElementById('photoAddButton');
const photoAddButtonForm = document.getElementById('photoAddButtonForm')
const workDate = document.getElementById('workDate').innerText;


loadWorkList();


function loadWorkList() {
    const xhr = new XMLHttpRequest();
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
                <span class="imageIndex" style="display: none">${workObject['index']}</span>
                <span>${workObject['originalName']}</span>
                <img class="imageList"  src="./subImage?index=${workObject['index']}" alt="" width="100" height="100">
            </li>`,'text/html').querySelector('li');
            itemEl.querySelector('img').onclick = (e) => {
                const mainImage = document.getElementById('image');
                selectImageId.value = itemEl.querySelector('.imageIndex').innerText;
                selectImageName.innerText = workObject['originalName'];
                mainImage.src = e.target.src;
                mosaicAreas= [];
                dragCount = 0;
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





