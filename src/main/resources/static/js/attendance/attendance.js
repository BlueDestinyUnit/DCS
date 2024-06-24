function eventsData(current) {
    return new Promise((resolve, reject) => {
        let endDate = moment().endOf('month');
        if (current !== null) {
            endDate = current.endOf('month');
        }
        endDate = endDate.clone().format('YYYY-MM-DD');


        fetch(`./attendance?endDate=${endDate}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(responseObject => {
                const list = responseObject['list'];
                const listLength = list.length;
                let data = [];
                for (let i = 0; i < listLength; i++) {
                    let attendanceDate = list[i].date;
                    let momentDate = moment(attendanceDate);
                    let dateCount = list[i].count;
                    let attendance = {
                        eventName: '출석',
                        calendar: '출석',
                        color: 'orange',
                        date: momentDate,
                        eventDate: attendanceDate
                    };
                    let workCount = {
                        eventName: dateCount,
                        calendar: '작업량',
                        color: 'blue',
                        date: momentDate,
                        eventDate: attendanceDate
                    };
                    data.push(attendance);
                    data.push(workCount);
                }

                resolve(data); // 프로미스 해결
            })
            .catch(error => {
                console.error('Error processing response:', error);
                reject(error);
            });

    });
}



function eventsDataTest(data,current) {
    data = [];
    let endDate = moment().endOf('month');
    if (current !== null) {
        endDate = current.endOf('month');
    }
    endDate = endDate.clone().format('YYYY-MM-DD');

    const xhr = new XMLHttpRequest();


    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            console.log('알 수 없는 오류가 발생했습니다.');
            reject(new Error('알 수 없는 오류가 발생했습니다.'));
            return;
        }

        const responseObject = JSON.parse(xhr.responseText);
        const list = responseObject['list'];
        console.log(list);
        for (let i = 0; i < list.length; i++) {
            const attendance = {
                eventName: '출석',
                calendar: '출석',
                color: 'orange',
                date: moment(list[i].date),
                eventDate: list[i].date
            };
            const workCount = {
                eventName: list[i].count,
                calendar: '작업량',
                color: 'blue',
                date: moment(list[i].date),
                eventDate: list[i].date
            };
            data.push(attendance);
            data.push(workCount);
        }

    };
    xhr.open('GET', `./attendance?endDate=${endDate}`);
    xhr.send();
}


function createCalendar() {
    var today = moment();
    events = [];

    function Calendar(selector, events) {
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
        //Create Header
        this.drawHeader();

        //Draw Month
        this.drawMonth();

        this.drawLegend();
    }

    Calendar.prototype.drawHeader = function() {
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
        var self = this;
        this.events.forEach(function(ev) {
            var eventDate = ev.eventDate.split('-');

            ev.date = self.current.clone().date(eventDate[2]);
        });


        if(this.month) {
            this.current = this.current.startOf('month')
            this.oldMonth = this.month;
            this.oldMonth.className = 'month out ' + (self.next ? 'next' : 'prev');
            this.oldMonth.addEventListener('webkitAnimationEnd', function() {
                const legend = self.el.querySelector('.legend');


                self.oldMonth.parentNode.removeChild(self.oldMonth);
                self.month = createElement('div', 'month');
                self.backFill();
                self.currentMonth();
                self.fowardFill();
                self.el.appendChild(self.month);
                if (legend){
                    self.el.removeChild(legend);
                    self.el.appendChild(legend);
                }


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
        var clone = this.current.clone();
        var dayOfWeek = clone.day();
        if(!dayOfWeek) { return; }
        clone.subtract('days', dayOfWeek+1);
        for(var i = dayOfWeek; i > 0 ; i--) {
            this.drawDay(clone.add('days', 1));
        }
    }

    Calendar.prototype.fowardFill = function() {
        var clone = this.current.clone().add('months', 1).subtract('days', 1);
        var dayOfWeek = clone.day();

        if(dayOfWeek === 6) { return; }

        for(var i = dayOfWeek; i < 6 ; i++) {
            this.drawDay(clone.add('days', 1));
        }
    }

    Calendar.prototype.currentMonth = function() {

        var clone = this.current.clone();

        while(clone.month() === this.current.month()) {
            this.drawDay(clone);
            clone.add('days', 1);
        }
    }

    Calendar.prototype.getWeek = function(day) {
        if(!this.week || day.day() === 0) {
            this.week = createElement('div', 'week');
            this.month.appendChild(this.week);
        }
    }

    Calendar.prototype.drawDay = function(day) {
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
        this.drawEvents(day, events);

        outer.appendChild(name);
        outer.appendChild(number);
        outer.appendChild(events);
        this.week.appendChild(outer);
    }

    Calendar.prototype.drawEvents = function(day, element) {
        if(day.month() === this.current.month()) {
            var todaysEvents = Array.from(this.events).reduce(function(memo, ev) {
                if(ev.date.isSame(day, 'day')) {
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
        classes = ['day'];
        if(day.month() !== this.current.month()) {
            classes.push('other');
        } else if (today.isSame(day, 'day')) {
            classes.push('today');
        }
        return classes.join(' ');
    }

    Calendar.prototype.openDay = function(el) {
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

            // console.log("오늘 날짜의 이벤트:", day.format('YYYY-MM-DD'), todaysEvents);


            details.appendChild(arrow);
            el.parentNode.appendChild(details);
        }

        var todaysEvents = this.events.reduce(function(memo, ev) {
            if(ev.date.isSame(day, 'day')) {
                memo.push(ev);
            }
            return memo;
        }, []);

        this.renderEvents(todaysEvents, details);

        arrow.style.left = el.offsetLeft - el.parentNode.offsetLeft + 27 + 'px';
    }

    Calendar.prototype.renderEvents = function(events, ele) {
        // console.log(13)

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
        // console.log(14)
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
        this.current.add('months', 1);
        const legend = this.el.querySelector('.legend');
        this.el.removeChild(legend)
        eventsData(this.current).then(updatedData => {
            this.events = updatedData;
            this.next = false;
            this.draw();
        }).catch(error => {
            console.error(error);
        });
    }

    Calendar.prototype.prevMonth = function() {
        this.current.subtract('months', 1);
        const legend = this.el.querySelector('.legend');
        this.el.removeChild(legend)
        eventsData(this.current).then(updatedData => {
            this.events = updatedData;
            this.next = false;
            this.draw();
        }).catch(error => {
            console.error(error);
        });
    }

    window.Calendar = Calendar;

    function createElement(tagName, className, innerText) {
        // console.log(17 + " 엘리멘트 요소 생성")

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

function createEvent(data) {
    createCalendar();
    var calendar = new Calendar('#calendar', data);

}


eventsData(null).then(updatedData => {
    createEvent(updatedData)
}).catch(error => {
    console.error(error);
});