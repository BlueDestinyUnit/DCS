var ws;

// var roomName = /*[[${roomName}]]*/ '';
function wsOpen(roomIndex) {
    $("#yourName").hide();
    $("#yourMsg").show();
    ws = new WebSocket("ws://" + location.host + "/chating/" + roomIndex);
    wsEvt(roomIndex);
}

wsOpen(roomIndex);

function wsEvt(roomIndex) {
    ws.onopen = function (e) {
    }

    ws.onmessage = function (e) {
        console.log(onmessage)
        console.log(e.data);
        var msg = e.data;
        if (msg != null && msg.trim() !== '') {
            var d = JSON.parse(msg);
            console.log(d)
            if (d.type === "getId") {
                var si = d.sessionId !== null ? d.sessionId : "";
                if (si !== '') {
                    $("#sessionId").val(si);

                    var obj = {
                        type: "open",
                        sessionId: $("#sessionId").val(),
                        userName: userName,
                        roomIndex: roomIndex
                    }
                    console.log(obj)
                    ws.send(JSON.stringify(obj));
                }
            } else if (d.type === "message") {
                console.log(2)
                if (d.sessionId === $("#sessionId").val()) {
                    $("#chating").append("<p class='me'>" + d.msg + "</p>");
                } else {
                    $("#chating").append("<p class='others'>" + d.userName + " : " + d.msg + "</p>");
                }
            } else if (d.type == "open") {
                console.log(3)
                if (d.sessionId == $("#sessionId").val()) {
                    console.log(4)
                    $("#chating").append("<p class='start'>[채팅에 참가하였습니다.]</p>");
                } else {
                    console.log(5)
                    $("#chating").append("<p class='start'>[" + d.userName + "]님이 입장하였습니다.</p>");
                }
            } else if (d.type == "close") {
                $("#chating").append("<p class='exit'>[" + d.userName + "]님이 퇴장하였습니다.</p>");
            } else {
                console.warn("unknown type!")
            }
        }
    }

    document.addEventListener("keypress", function (e) {
        if (e.keyCode == 13) {
            console.log(121231)
            send();
        }
    });
}

// function chatName(userName,roomIndex) {
//     if (userName == null || userName.trim() == "") {
//         alert("사용자 이름을 입력해주세요.");
//         $("#userName").focus();
//     } else {
//         $.ajax({
//             type: "POST",
//             url: "./createRoom",
//             data: {
//                 userName: userName,
//                 roomName: roomName,
//                 index : roomIndex
//             },
//             success: function(response) {
//                 const jsonObject  = JSON.parse(response);
//                 console.log("json")
//                 console.log(jsonObject)
//                 roomName = jsonObject.roomName; // 서버에서 생성된 roomName를 받아 변수에 할당합니다.
//                 roomIndex = jsonObject.roomIndex;
//                 console.log(roomIndex)
//                 wsOpen(roomIndex); // WebSocket 연결
//                 $("#yourName").hide();
//                 $("#yourMsg").show();
//             },
//             error: function(xhr, status, error) {
//                 console.error("Error creating room:", error);
//             }
//         });
//     }
// }


function send() {
    var obj = {
        type: "message",
        sessionId: $("#sessionId").val(),
        userName: userName,
        msg: $("#chatting").val(),
        roomIndex: roomIndex
    }
    ws.send(JSON.stringify(obj));
    $('#chatting').val("");
}