# DCS (Data Cleaning System)   - 90% 완성

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

