const minPage = document.querySelector('[rel="nextPage"]')
const maxPage = document.querySelector('[rel="previousPage"]')

minPage.addEventListener('click', (e) => {
    e.preventDefault();
    location.href = `/board/list?page=${minPage}&code=${code}`
})

maxPage.addEventListener('click', (e) => {
    e.preventDefault();
    location.href = `/board/list?page=${maxPage}&code=${code}`
})