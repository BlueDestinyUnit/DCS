# DCS (Data Cleaning System)
## 링크 -> [http:/dcs.jshlab.store](http:/dcs.jshlab.store)

## 이 프로젝트의 목표
AI에게 학습하기위한 이미지들을 정제(모자이크)하기 위한 웹 사이트를 개발하는것을 목표로 하고있습니다. 뿐만 아니라 출결, 급여정산, 실시간 채팅까지 포함하고있습니다.

## 주요 담당 역할

|이름|역할| 주요 Controller|
|------|---|---|
|조성환|팀장, ERD, 게시판, 채팅|BoardController,ChatController|
|김주혁|유저 기능| UserController|
|구본겸|이미지 정제(모자이크)|MosaicController|
|이상규|관리자 기능|AdminController|

## 주요 기능 명세

|기능|세부 기능| 관련 주요 Service|
|------|---|---|
|유저(회원)|회원가입, 회원정보 수정, 로그인, 로그아웃 |UserService|
|모자이크 API|사진 저장, 삭제, 모자이크 처리| WorkService|
|게시판|게시판 글쓰기, 삭제, 댓글 작성|ArticleService|
|출석(달력) API|출석 확인 및 작업량 확인|WorkService|
|채팅|실시간 채팅 구현|ChatService|
|관리자|작업량 통계, 출석률|AdminService|

## 일지
|주차|내용|비고|
|------|---|---|
|1주차|프로젝트 기획|전원 회의|
|2주차|ERD 설계, 프로젝트 시작|전원 회의|
|3주차|게시판 완성|조성환|
|4주차|유저 회원가입, 로그인 완성|김주혁|
|5주차|이미지 정제 기능 완성, 관리자 출결 체크 기능 완성|이상규, 구본겸|
|6주차|유저 페이지 완성, 채팅 기능 완성|조성환, 김주혁|
|7주차|유지보수|전원|
|8주차~|유지보수|전원|





## 🔨 SKILL
### Language
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">

### Back-end
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white"> 

### Template
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">


### DB
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white"> 


## ERD
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/dcs_erd.png)

## 메인 페이지
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/5.jpg)

## 로그인
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/1.jpg)

## 회원가입
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/2.jpg)

## 유저 찾기
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/3.jpg)

## 비밀번호 찾기
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/4.jpg)

## 메인 작업 (모자이크 처리)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/6.jpg)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/7.jpg)

## 개인 작업자 작업리스트
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/8.jpg)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/9.jpg)

## 개인 급여
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/10.jpg)

## 개인 출석현황
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/11.jpg)

## 개인정보
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/12.jpg)

## 게시판
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/13.jpg)

## 채팅방
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/14.jpg)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/15.jpg)

## 총인원 급여(관리자)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/16.jpg)

## 작업물 피드백 (관리자)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/17.jpg)
![이미지](https://github.com/BlueDestinyUnit/BlueDestinyUnit/blob/main/dcs/18.jpg)


## 느낀점
아키택쳐 설계 당시 너무 대형 프로젝트를 준비하는것보다 약간 소규모라도 확실하게 CRUD를 구현할수 있는것에 초점을 두었습니다. 덕분에 전 프로젝트와는 다르게 일단 초기에 만들어야하겠다는 기능들을 빠짐없이 구현이 가능했습니다. 물론 그 과정속에서 팀원들과 분배한 역할에 난이도 차이가 있기 때문에 즉각적으로 인원 분배를 다시 하였습니다. 외부에서 가져온 달력 API는 상당히 특이하게 작성된 코드라 코드분석에 하루 이틀이 걸렸고 실력향상에 큰 도움이 되었습니다. 웹 소켓의 경우는 기존 설계에는 없었지만 본 프로젝트의 취지에 잘 맞을것같아 추가적으로 넣었습니다. 이번 프로젝트동안 팀원들과 꾸준히 의사소통하면서 피드백하였는데 강사님에게 의존하지 않고 제가 이론 설명과 해결방법을 제시했습니다.
아쉬운 점은 전 프로젝트에서 디자인부터 설계하고 시작하느라 애로사항이 있어서 최대한 디자인보다는 실기능적으로 먼저 구현하고 디자인을 입히고자 했는데 조금 늦게 디자인들이 결정되는 바람에 만족스러운 디자인은 나오지 않았던것 같습니다.




















