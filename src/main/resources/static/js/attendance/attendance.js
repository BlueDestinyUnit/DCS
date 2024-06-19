/*
 1. 모든 날짜 불러오기 events() 첫날짜랑 끝날짜만 보내면됨
 2. 날짜마다 해당 출석여부 AdminMapper의 selectUserProperty 에서 attendance 참고
 3. 작업량 가져오기 (XHR)
 4. 서비스에서 반복문으로 하나씩 처리하기
 5. Calendar.prototype.drawMonth 쪽 ev를 적절히 넣기
 */

createCalendar();

function events2() {
    var startDate = moment().startOf('month'); // 이번 달의 첫째 날
    var endDate = moment().endOf('month'); // 이번 달의 마지막 날
    endDate = endDate.clone().format(('YYYY-MM-DD'));

    var data = [];

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('endDate', endDate);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert("알 수없는 오류가 발생했습니다.");
            return;
        }
        const responseObject = JSON.parse(xhr.responseText);
        const list = responseObject['list'];
        for (let i = 0; i < list.length; i++){
            list[i].eventAttendance.date = moment(list[i].eventAttendance.date);
            list[i].eventCount.date = moment(list[i].eventCount.date);
            data.push(list[i].eventAttendance);
            data.push(list[i].eventCount);
        }

    }

    xhr.open('GET', `./attendance?endDate=${endDate}`); // 본인 ?email=${유저이메일}&date=${dates[i]} 일단 현재 date는 String 문자열로 넘길것
    xhr.send();
    return data;
}

events2();

function createCalendar() {
    var today = moment();
    function Calendar(selector, events) {
        console.log(1+': 생성')
        this.el = document.querySelector(selector);
        this.events = events;
        this.current = moment().date(1);
        this.draw();
        var current = document.querySelector('.today');
        if(current) {
            var self = this;
            window.setTimeout(function() {
                self.openDay(current);
            }, 500);
        }
    }

    Calendar.prototype.draw = function() {
        console.log(2+" 핸들러")
        //Create Header
        this.drawHeader();

        //Draw Month
        this.drawMonth();

        this.drawLegend();
    }

    Calendar.prototype.drawHeader = function() {
        console.log(3+" 헤더")
        var self = this;
        if(!this.header) {
            //Create the header elements
            this.header = createElement('div', 'header');
            this.header.className = 'header';

            this.title = createElement('h1');

            var right = createElement('div', 'right');
            right.addEventListener('click', function() { self.nextMonth(); });

            var left = createElement('div', 'left');
            left.addEventListener('click', function() { self.prevMonth(); });

            //Append the Elements
            this.header.appendChild(this.title);
            this.header.appendChild(right);
            this.header.appendChild(left);
            this.el.appendChild(this.header);
        }

        this.title.innerHTML = this.current.format('MMMM YYYY');
    }

    Calendar.prototype.drawMonth = function() {
        console.log(4+" 달 그리기")
        var self = this;
        console.log(this.events);
        this.events.forEach(function(ev) {
            var eventDate = ev.eventDate.split('-');

            ev.date = self.current.clone().date(eventDate[2]);
            console.log(ev.date)
        });


        if(this.month) {
            this.oldMonth = this.month;
            this.oldMonth.className = 'month out ' + (self.next ? 'next' : 'prev');
            this.oldMonth.addEventListener('webkitAnimationEnd', function() {
                self.oldMonth.parentNode.removeChild(self.oldMonth);
                self.month = createElement('div', 'month');
                self.backFill();
                self.currentMonth();
                self.fowardFill();
                self.el.appendChild(self.month);
                window.setTimeout(function() {
                    self.month.className = 'month in ' + (self.next ? 'next' : 'prev');
                }, 16);
            });
        } else {
            this.month = createElement('div', 'month');
            this.el.appendChild(this.month);
            this.backFill();
            this.currentMonth();
            this.fowardFill();
            this.month.className = 'month new';
        }
    }

    Calendar.prototype.backFill = function() {
        console.log(5)
        var clone = this.current.clone();
        var dayOfWeek = clone.day();

        if(!dayOfWeek) { return; }

        clone.subtract('days', dayOfWeek+1);

        for(var i = dayOfWeek; i > 0 ; i--) {
            this.drawDay(clone.add('days', 1));
        }
    }

    Calendar.prototype.fowardFill = function() {
        console.log(6)
        var clone = this.current.clone().add('months', 1).subtract('days', 1);
        var dayOfWeek = clone.day();

        if(dayOfWeek === 6) { return; }

        for(var i = dayOfWeek; i < 6 ; i++) {
            this.drawDay(clone.add('days', 1));
        }
    }

    Calendar.prototype.currentMonth = function() {
        console.log(7)
        var clone = this.current.clone();

        while(clone.month() === this.current.month()) {
            this.drawDay(clone);
            clone.add('days', 1);
        }
    }

    Calendar.prototype.getWeek = function(day) {
        console.log(8)
        if(!this.week || day.day() === 0) {
            this.week = createElement('div', 'week');
            this.month.appendChild(this.week);
        }
    }

    Calendar.prototype.drawDay = function(day) {
        console.log(9)
        var self = this;
        this.getWeek(day);

        //Outer Day
        var outer = createElement('div', this.getDayClass(day));
        outer.addEventListener('click', function() {
            self.openDay(this);
        });

        //Day Name
        var name = createElement('div', 'day-name', day.format('ddd'));

        //Day Number
        var number = createElement('div', 'day-number', day.format('DD'));


        //Events
        var events = createElement('div', 'day-events');
        console.log(events)
        this.drawEvents(day, events);
        console.log("day"+day);
        console.log("event",events)

        outer.appendChild(name);
        outer.appendChild(number);
        outer.appendChild(events);
        this.week.appendChild(outer);
    }

    Calendar.prototype.drawEvents = function(day, element) {
        console.log(10)
        console.log(day.month() === this.current.month());
        if(day.month() === this.current.month()) {
            console.log("테스트")
            console.log(this.events);


            var todaysEvents = this.events.reduce(function(memo, ev) {
                console.log(ev.date.isSame(day, 'day'));
                console.log(typeof memo);
                console.log("ev"+ev);
                console.log("ev.date"+ev.date);
                if(ev.date.isSame(day, 'day')) {
                    console.log("푸시")
                    memo.push(ev);
                }
                return memo;
            }, []);

            todaysEvents.forEach(function(ev) {
                var evSpan = createElement('span', ev.color);
                element.appendChild(evSpan);
            });
        }
    }

    Calendar.prototype.getDayClass = function(day) {
        console.log(11)
        classes = ['day'];
        if(day.month() !== this.current.month()) {
            classes.push('other');
        } else if (today.isSame(day, 'day')) {
            classes.push('today');
        }
        return classes.join(' ');
    }

    Calendar.prototype.openDay = function(el) {
        console.log(12)
        var details, arrow;
        var dayNumber = +el.querySelectorAll('.day-number')[0].innerText || +el.querySelectorAll('.day-number')[0].textContent;
        var day = this.current.clone().date(dayNumber);

        var currentOpened = document.querySelector('.details');

        //Check to see if there is an open detais box on the current row
        if(currentOpened && currentOpened.parentNode === el.parentNode) {
            details = currentOpened;
            arrow = document.querySelector('.arrow');
        } else {
            //Close the open events on differnt week row
            //currentOpened && currentOpened.parentNode.removeChild(currentOpened);
            if(currentOpened) {
                currentOpened.addEventListener('webkitAnimationEnd', function() {
                    currentOpened.parentNode.removeChild(currentOpened);
                });
                currentOpened.addEventListener('oanimationend', function() {
                    currentOpened.parentNode.removeChild(currentOpened);
                });
                currentOpened.addEventListener('msAnimationEnd', function() {
                    currentOpened.parentNode.removeChild(currentOpened);
                });
                currentOpened.addEventListener('animationend', function() {
                    currentOpened.parentNode.removeChild(currentOpened);
                });
                currentOpened.className = 'details out';
            }

            //Create the Details Container
            details = createElement('div', 'details in');

            //Create the arrow
            var arrow = createElement('div', 'arrow');

            //Create the event wrapper

            console.log("오늘 날짜의 이벤트:", day.format('YYYY-MM-DD'), todaysEvents);


            details.appendChild(arrow);
            el.parentNode.appendChild(details);
        }

        var todaysEvents = this.events.reduce(function(memo, ev) {
            console.log(memo);
            console.log(ev);
            console.log("현재 :" + day)
            if(ev.date.isSame(day, 'day')) {
                memo.push(ev);
            }
            return memo;
        }, []);

        this.renderEvents(todaysEvents, details);

        arrow.style.left = el.offsetLeft - el.parentNode.offsetLeft + 27 + 'px';
    }

    Calendar.prototype.renderEvents = function(events, ele) {
        console.log(13)

        //Remove any events in the current details element
        var currentWrapper = ele.querySelector('.events');
        var wrapper = createElement('div', 'events in' + (currentWrapper ? ' new' : ''));

        events.forEach(function(ev) {
            var div = createElement('div', 'event');
            var square = createElement('div', 'event-category ' + ev.color);
            var span = createElement('span', '', ev.eventName);

            div.appendChild(square);
            div.appendChild(span);
            wrapper.appendChild(div);
        });

        if(!events.length) {
            var div = createElement('div', 'event empty');
            var span = createElement('span', '', 'No Events');

            div.appendChild(span);
            wrapper.appendChild(div);
        }

        if(currentWrapper) {
            currentWrapper.className = 'events out';
            currentWrapper.addEventListener('webkitAnimationEnd', function() {
                currentWrapper.parentNode.removeChild(currentWrapper);
                ele.appendChild(wrapper);
            });
            currentWrapper.addEventListener('oanimationend', function() {
                currentWrapper.parentNode.removeChild(currentWrapper);
                ele.appendChild(wrapper);
            });
            currentWrapper.addEventListener('msAnimationEnd', function() {
                currentWrapper.parentNode.removeChild(currentWrapper);
                ele.appendChild(wrapper);
            });
            currentWrapper.addEventListener('animationend', function() {
                currentWrapper.parentNode.removeChild(currentWrapper);
                ele.appendChild(wrapper);
            });
        } else {
            ele.appendChild(wrapper);
        }
    }

    Calendar.prototype.drawLegend = function() {
        console.log(14)
        var legend = createElement('div', 'legend');
        var calendars = this.events.map(function(e) {
            return e.calendar + '|' + e.color;
        }).reduce(function(memo, e) {
            if(memo.indexOf(e) === -1) {
                memo.push(e);
            }
            return memo;
        }, []).forEach(function(e) {
            var parts = e.split('|');
            var entry = createElement('span', 'entry ' +  parts[1], parts[0]);
            legend.appendChild(entry);
        });
        this.el.appendChild(legend);
    }

    Calendar.prototype.nextMonth = function() {
        console.log(15)
        this.current.add('months', 1);
        this.next = true;
        this.draw();
    }

    Calendar.prototype.prevMonth = function() {
        console.log(16)
        this.current.subtract('months', 1);
        this.next = false;
        this.draw();
    }

    window.Calendar = Calendar;

    function createElement(tagName, className, innerText) {
        console.log(17 + " 엘리멘트 요소 생성")

        var ele = document.createElement(tagName);
        if(className) {
            ele.className = className;
        }
        if(innerText) {

            ele.innderText = ele.textContent = innerText;
        }
        return ele;
    }
};

// function createEvent() {
//     var data = events2();
//     var calendar = new Calendar('#calendar', data);
// }
//
// createEvent();

!function() {
    console.log(18)
    var data = events2();
    // console.log("dfaf"+data);
    // var data = [
    //     // {date: 2024-06-01 , attendance: true,work : 123123}
    //     // {date: 2024-06-02 , attendance: true,work : 123123}
    //
    //     {eventName: '출석',calendar: 'Work', color: 'orange', date: '2024-06-19',eventDate:'2024-06-19'},
    //     {eventName: 1,calendar: 'Sports', color: 'blue', date: '2024-06-19',eventDate:'2024-06-19'}
    //
    //     // 출석 여부{ eventName: 'Lunch Meeting w/ Mark', calendar: 'Work', color: 'orange', date: '2024-06-07' },
    //     // 작업량{ eventName: 'Interview - Jr. Web Developer', calendar: 'Work', color: 'orange' }
    //
    // ];
    console.log(data)


    function addDate(ev) {

    }

    var calendar = new Calendar('#calendar', data);

}();
