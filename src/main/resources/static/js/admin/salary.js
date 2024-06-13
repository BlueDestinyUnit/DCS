document.addEventListener('DOMContentLoaded', () => {
    const options = {
        type: 'month', // 월 선택기만 사용
        displayMode: 'dialog',
        dateFormat: 'yyyy-MM',
        lang: 'ko',
        headerPosition: 'top',
        footerPosition: 'none'
    };

    // Attach the calendar to the input element
    bulmaCalendar.attach('#datepicker', options);

    // Example to access calendar instance
    const element = document.querySelector('#datepicker');
    if (element) {
        // Add an event listener to handle changes
        element.bulmaCalendar.on('select', (datepicker) => {
            console.log(datepicker.data.value()); // Print selected value to console
        });
    }
});