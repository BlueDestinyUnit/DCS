function findThumbnail() {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            return;
        }
        const jsonObject = JSON.parse(xhr.responseText);

        if(jsonObject['index'] > 0) {
            userThumbnail.src = `/user/thumbnail?index=${jsonObject['index']}`;
        }
    }
    xhr.open('GET', '/user/findThumbnail')
    xhr.send();
}

findThumbnail();