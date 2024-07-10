let userThumbnail = document.querySelector('.thumbnail');

document.getElementById("uploadLink").addEventListener("click", function(e) {
    e.preventDefault();

    if (confirm("정말 교체하시겠습니까?")) {
        document.getElementById("fileInput").click();
    }

    document.getElementById("fileInput").addEventListener("change", function() {
        const fileInput = document.getElementById("fileInput");
        if (fileInput.files.length === 0) {
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        for (let i = 0; i < fileInput.files.length; i++) {
            formData.append("images", fileInput.files[i]);
        }

        xhr.onreadystatechange = function () {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert("업로드 실패");
                return;
            }
            const jsonObject = JSON.parse(xhr.responseText);
            console.log(jsonObject);
            if(jsonObject['result'] === 'success') {
                userThumbnail.src = `/user/thumbnail?index=${jsonObject['index']}`;    
            }else {
                alert('실패')
            }
        };
        xhr.open('POST', '/user/saveThumbnail');
        xhr.send(formData);
    });
});

