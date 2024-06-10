const commentForm = document.getElementById('commentForm');
const commentContainer = document.getElementById('commentContainer');

function deleteComment(index) {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', index);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert("알 수없는 오류가 발생했습니다.")
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const result = responseObject['result'];
        if (result === 'success') {
            loadComments();
        }
        if (result === 'failure_denied') {
            alert('작성자가 아닙니다.')
            return;
        }
        if (result === 'failure') {
            alert('로그인을 해주세요.')
            return;
        }

    }
    xhr.open('DELETE', '../comment/');
    xhr.send(formData);
}

function loadComments() {
    // TODO : 댓글을 불러오는 함수
    commentContainer.innerHTML = '';
    const xhr = new XMLHttpRequest();
    // const formData = new FormData(); get에서는 못씀
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            commentContainer.innerHTML = '일시적으로 댓글을 불러오지 못하였습니다. 페이지를 새로 고쳐주세요.'
            return;
        }
        const responseArray = JSON.parse(xhr.responseText);
        console.log(responseArray)
        for (const commentObject of responseArray) {
            const commentEL = new DOMParser().parseFromString(`
                    <div rel="comment" data-index="${commentObject['index']}">
                        <div class="comment-head">
                            <span>${commentObject['index']}</span>
                            <span>${commentObject['userEmail']}</span>
                            <span class="spring"></span>
                            <span>${commentObject['createdAt']}</span>
                        </div>
                        <div class="comment-main">
                            <div class="comment-content">${commentObject['content']}</div>
                            <div class="comment-button">
                        </div>

                            <button class="btn btn-info" rel="modify">수정</button>
                            <button class="btn btn-info" rel="modifyCancle" style="display: none">취소</button>
                            <button class="btn btn-danger" rel="delete">삭제</button>
                        </div>
                        <form rel="modifyForm" style="display: none;">
                            <label>
                                <span>수정할 내용</span>
                                <input class="form-control" name="newContent" type="text">
                            </label>
                            <input class="btn btn-info" type="submit" value="수정하기">
                        </form>
                    </div>
            `,'text/html').querySelector('[rel="comment"]');
            const modifyEl = commentEL.querySelector('[rel="modify"]');
            const modifyCancleEl = commentEL.querySelector('[rel="modifyCancle"]');
            const deleteEl = commentEL.querySelector('[rel="delete"]');
            const modifyFormEl = commentEL.querySelector('[rel="modifyForm"]')
            modifyEl.onclick = function () {
                modifyEl.style.display = 'none';
                modifyCancleEl.style.display = 'inline-block';
                modifyFormEl.style.display = 'block';
                modifyFormEl['newContent'].value = commentObject['content'];
                modifyFormEl['newContent'].focus();
            };
            modifyCancleEl.onclick = function () {
                modifyEl.style.display = 'inline-block';
                modifyCancleEl.style.display = 'none';
                modifyFormEl.style.display = 'none';
            }
            deleteEl.onclick = function () {
                deleteComment(commentObject['index']);
            }
            modifyFormEl.onsubmit = function (e) {
                e.preventDefault();
                modifyComment(commentObject['index'], modifyFormEl['newContent'].value);

            };
            commentContainer.append(commentEL);
            console.log(commentContainer)
        }
    }
    xhr.open('GET', `./comment/?articleIndex=${commentForm['articleIndex'].value}`);
    xhr.send();
}

function modifyComment(index, newContent) {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', index);
    formData.append('newContent', newContent);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert("알 수없는 오류가 발생했습니다.")
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        switch (responseObject['result']) {
            case 'failure':
                alert('알 수 없는 이유로 댓글을 수정하지 못하였습니다. 잠시 후 다시 시도해 주세요.')
                break;
            case 'failure_denied':
                alert('댓글을 수정할 권한이 없습니다.');
                break;
            case 'success':
                loadComments();
                break;
            default:
                alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.');
        }

    }
    xhr.open('PATCH', '../comment/');
    xhr.send(formData);
}

commentForm.onsubmit = function (e) {
    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('articleIndex', commentForm['articleIndex'].value);
    formData.append('content', commentForm['content'].value);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert("알 수없는 오류가 발생했습니다.")
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        switch (responseObject['result']) {
            case 'failure' :
                alert('알 수 없는 이유로 댓글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.')
                break;
            case 'success':
                loadComments();
                break;
            default:
                alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.')
        }
    }
    xhr.open('POST', './comment/');
    xhr.send(formData);
}

// HTML 문자열을 DOM 요소로 변환하는 함수
function htmlToElement(html) {
    const template = document.createElement('template');
    template.innerHTML = html.trim();
    return template.content.firstChild;
}

loadComments();