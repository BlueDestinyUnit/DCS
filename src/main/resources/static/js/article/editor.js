const mainSeciton = document.getElementById('mainSection');

mainSeciton.form = mainSeciton.querySelector(':scope > .form');

ClassicEditor.create(mainSeciton.form['content'], {
    simpleUpload: {
        uploadUrl: './image',
    }
}).then(editor => {
    editor.plugins.get('ImageUploadEditing').on('uploadComplete', (e , data) => {
        const index = data['data']['url'].split('=')[1];
        const inputEl= document.createElement('input');
        inputEl.setAttribute('name', 'imageIndexes');
        inputEl.setAttribute('type', 'hidden');
        inputEl.setAttribute('value', index);
        inputEl.style.display = 'none';
        mainSeciton.form.append(inputEl);
    });
}).catch(error => {
    console.log(error);
});