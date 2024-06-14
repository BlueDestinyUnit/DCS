const menuList = document.getElementById('asideFrame').querySelectorAll('.menu > .item');
const admin = document.querySelector('.admin').textContent;
const today = moment().format('YYYY-MM-DD');
menuList.forEach(item => {
    const menuName = item.dataset.menuName
    switch (menuName) {
        case 'work':
            item.addEventListener('click', (e) => {
                e.preventDefault();
                location.href = `/work?date=${today}`
            })
            break;
        case 'payment':
            console.log('About 메뉴입니다.');
            // About 메뉴에 대한 작업
            break;
        case 'attendance':
            item.addEventListener('click', (e) => {
                e.preventDefault();
                if(admin === 'false'){
                    location.href = `/admin/`
                }else {
                    location.href = `/user/attendance`
                }
            })
            break;
        case 'my':
            console.log('Contact 메뉴입니다.');
            // Contact 메뉴에 대한 작업
            break;
        case 'board':
            item.addEventListener('click', (e) => {
                e.preventDefault();
                location.href = '../board/list'
            })
            break;
        default:
            console.log('알 수 없는 메뉴입니다.');

    }


})

