const asideFrame = document.getElementById('asideFrame');
const menuWrapper = asideFrame.querySelector('.menu-wrapper');
const menuList = asideFrame.querySelectorAll('.menu > .item');
const main = document.getElementById('main');

const admin = document.querySelector('.admin').textContent;
const today = moment().format('YYYY-MM-DD');
menuList.forEach(item => {
    const menuName = item.dataset.menuName
    switch (menuName) {
        case 'home' :
            item.addEventListener('click', (e) => {
                e.preventDefault();
                location.href = `/main`
            })

            break;
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
                if(admin === 'true'){
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

menuWrapper.addEventListener('click', (e) => {
    const aside = asideFrame.querySelector('.aside');
    if(!menuWrapper.classList.contains('click')){
        menuWrapper.classList.add('click')
        asideFrame.style.left = '-13rem';
        aside.style.left = '-13rem';
        menuList.forEach(item => {
            item.style.pointerEvents = 'none';
        })
        main.style.marginLeft='7rem';


    }else{
        menuWrapper.classList.remove('click')
        asideFrame.style.left = '';
        aside.style.left = '';
        menuList.forEach(item => {
            item.style.pointerEvents = 'auto';
        })
        main.style.marginLeft='20rem';
    }
})
