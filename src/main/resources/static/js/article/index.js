const main = document.getElementById('mainSection');

main.form = main.querySelector(':scope > .form');

ClassicEditor.create(main.form['content'], {
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
        main.form.append(inputEl);
    });
}).catch(error => {
    console.log(error);
});