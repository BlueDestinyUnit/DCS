# DCS
##Data Cleaning System 데이터 관리 시스템



AI에게 학습하기위한 이미지들을 정제(모자이크)하기 위한 웹 사이트를 개발하는것을 목표로 하고있습니다. 뿐만 아니라 출결, 급여정산의 기능까지 포함하고있습니다.

Controllers (mapping url 기준)
-----------
AdminController - 관리자가 행하는 모든 기능 (출석 확인, 데이터 검증, 급여 확인) , 이상규
BoardController - 게시판  , 조성환
UserController - 유저 (로그인 , 로그아웃, 자신의 출결 상황, 급여 상황), 김주혁
WorkController - 작업자가 데이터 정제하는 기능, 구본겸
RestController - 기타 API

Services (기능 기준)
----------
UserService  -회원정보 등록 , 수정 등등
BoardService - 게시판 기능
AttendanceService - 출석  기능
DataService - 데이터 입출력 서비스(이미지 파일)
AdminService - 관리자가 데이터를 검증
SalaryService - 급여 정산


Entities (ERD 참조)
------------

