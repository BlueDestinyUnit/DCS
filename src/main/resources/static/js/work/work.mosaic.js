let canvas = document.getElementById('canvas');
let ctx = canvas.getContext('2d');
let image = document.getElementById('image');
image.crossOrigin = "anonymous";
let isDragging = false;
let startX, startY;
const saveButton = document.getElementById('saveButton');
const resetButton = document.getElementById('resetButton');
let mosaicAreas = [];

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

saveButton.onclick = function (e) {
    let imageDataURL = canvas.toDataURL('image/png');
    let downloadLink = document.createElement('a');
    downloadLink.href = imageDataURL;
    downloadLink.download = 'mosaic_image.png';
    downloadLink.click();
};

resetButton.onclick = function (e) {
    mosaicAreas = [];
    ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
};

// 사진 파일 - > 버튼
document.querySelector('.photoAddButton').addEventListener('click', function() {
    this.querySelector('input[type="file"]').click();
});