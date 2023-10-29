<!-- [![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/fnZ3vxy8) -->
# Capstone-2023-44
>부산대학교 정보컴퓨터공학과 2023 전기 졸업과제 (뜨거운 아아) </br>
 프로젝트명: mmwave센서를 사용한 위험 행동 및 일상행동 인식 장치

## 프로젝트 개요
* 프로젝트 소개
* 팀 소개
* 구성도
* 소개 및 시연 영상
* 사용법

## 프로젝트 소개
&nbsp;&nbsp;낙상사고 후 바로 일어나지 못하거나 도움을 부르지 못한다면 낙상은 더 많은 문제를 초래할 수 있다. 이런 문제를 해결하기 위한 cctv설치는 사생활을 침범할 수 있으므로 mmwave센서를 통해 낙상과 같은 위험행동을 감지하고 알림이 가는 서비스를 제공하여 사생활을 침범하지 않으면서 고령자들에게 도움이 될 수 있는 시스템을 제공하고자 한다.</br> 
&nbsp;&nbsp;mmwave 기술은 고주파 전자기파를 이용하여 거리, 속도 및 위치
등을 정밀하게 측정할 수 있는 기술로, 거리에 따른 물체의 이동을 감지하는 데에 효과
적이다. 이를 이용하여 낙상이 감지되면 즉시 경고가 울리고 상태를 모니터링할 수 있다. 

## 팀 소개
<table>
  <tr>
    <th></th>
    <th style="text-align: center; vertical-align: middle;">강수빈</th>
    <th style="text-align: center; vertical-align: middle;">박준형</th>
    <th style="text-align: center; vertical-align: middle;">이영인</th>
  </tr>
  <tr>
    <td>
      <div align="center">이메일</div>
    </td>
    <td style="text-align: center; vertical-align: middle;">in9161binok@pusan.ac.kr</td>
    <td style="text-align: center; vertical-align: middle;">pjh3383663@pusan.ac.kr</td>
    <td style="text-align: center; vertical-align: middle;">youngin5757@pusan.ac.kr</td>
  </tr>
  <tr>
    <td>
      <div align="center">역할</div>
    </td>
    <td>
      <div align="center">데이터 전처리</br>어플리케이션 UI 개발</div>
    </td>
    <td>
      <div align="center">서버 및 DB구축</br>어플리케이션 서버 연동</div>
    </td>
    <td>
      <div align="center">서버 및 DB구축</br>어플리케이션 서버 연동</div>
    </td>
  </tr>
  <tr>
    <td>
      <div align="center">공통 역할</div>
    </td>
    <td colspan="3">
      <div align="center">mmwave데이터 수집 및 분석 </br></center>행동인식 머신러닝 학습</br>학습 모델 개선 및 수정</div>
    </td>
  </tr>
</table>

## 구성도
### [ 시스템 구성도 ]
<div align="center"><img width="500" alt="시스템구성도" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/4054ebf8-fea5-4a41-8c1f-245d92245af9"></div>

1. Mmwave 센서를 통한 행동 데이터 수집</br>
2. 서버에서 데이터 처리 및 동작 추론</br>
3. 추론 결과 데이터를 DB에 저장</br>
4. 앱을 통한 동작 정보 확인 및 알림</br>
### [ 학습 모델 ]
동작을 추론하는 모델의 학습 데이터와 구조는 아래와 같다.
* 데이터셋</br>
일상 행동 6가지 위험 행동 2가지로 분류하여 모델 학습 데이터로 사용하였다. 
<div align="center"><img width="500" alt="데이터셋" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/a5ceb76a-ce64-4092-baad-1b576cee6d46"></div>

</br>

* 모델 구조</br>
3DCNN 모델 구조를 사용하였다.
<div align="center"><img width="500" alt="모델구조" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/bb048299-33ca-4fe7-95c6-cb34d7a49df9"></div>

### [ 어플리케이션 ]
어플리케이션으로 현재 동작, 위험행동 발생기록을 볼 수 있다.</br>
<div align="center">
<img width="282" height="550" alt="일상행동" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/235d1515-aa5f-4a82-b8f0-d20f2c63a9d1"><img width="282 "height="550" alt="위험행동기록" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/fceefbad-7972-4a13-990f-63db280bc143"></br>
</div></br>
낙상이 발생하면 핸드폰 알림이 발생한다. 또한, 알림을 받고싶은 전화번호를 입력하면 입력된 번호로 위험행동이 발생했을 때 문자가 전송된다.</br>
</br><div align="center"><img width="235" height="500" alt="핸드폰알림" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/354bc5f4-7915-4a5f-b805-e6789d5be299">
<img width="235" height="500" alt="sms전송" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/3e38fbbf-74d0-4c4b-aebc-6abc5e7735f7"><img width="235" height="500" alt="문자화면" src="https://github.com/pnucse-capstone/Capstone-Template-2023/assets/118142745/8ec65a44-92c3-4418-ad8e-d7abad022b4f">
</div>



## 소개 및 시연 영상

[![2023년 전기 졸업과제 44 뜨거운 아아]((https://www.youtube.com/vi/FYeGC1-U2Ug/0.jpg)](https://www.youtube.com/watch?v=FYeGC1-U2Ug)

## 사용법
mmwave센서를 linux환경에서 실행하였기 때문에 서버 실행 및 DB 생성 또한 linux환경에서 이루어지게하였다.
</br>

### 1. 데이터베이스
MySQL을 사용하여 DB생성 및 관리
* MySQL 설치
```bash
$ sudo apt update
$ sudo apt-get install mysql-server

# 계정 암호 및 보안 설정
# host='127.0.0.1'
# user='root', password='root'
$ sudo mysql_secure_installation 

# DB접속 확인
$ sudo mysql -u root -p 
```
* MySQL Workbench 설치
```bash
$ sudo apt update
$ sudo apt -y install wget
$ wget https://dev.mysql.com/get/mysql-apt-config_0.8.15-1_all.deb

# apt 설치스크립트 실행
$ sudo apt install ./mysql-apt-config_0.8.15-1_all.deb 

# workbench 설치 실행
$ sudo apt update
$ sudo apt install mysql-workbench-community
```
* DB 생성
```sql
CREATE DATABASE action_db default CHARACTER SET UTF8; 
```
* action table생성 (현재 동작 정보 저장소)
```sql
CREATE TABLE `action` (
  `action_num` int NOT NULL,
  `action_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
```
* abnormal_action table생성 (위험 동작 기록정보 저장소)
```sql
CREATE TABLE `abnormal_action` (
  `abnormal_ID` int NOT NULL AUTO_INCREMENT,
  `abnormal_name` varchar(45) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`abnormal_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
```
</br>

### 2. mmave 센서
IWR6843ISK + DCA1000EVM 보드를 사용</br> 
보드를 사용법은 mmMesh-master파일 안의 IWR6843ISK 메뉴얼을 참고하여 설치 및 실행한다. 

* python은 3.8.* version을 사용
* realtime_pc_gen.py 파일을 실행하면서 필요한 package 설치가 필요하다.
```bash
$ pip install [package name]
```
* mmMesh-master 파일의 realtime_pc_gen.py 파일을 실행하여 mmwave센서를 통해 동작에 대한 point cloud 수집 시작</br>

<div align="center">
<img width="500" alt="Screenshot_10" src="https://github.com/pnucse-capstone/capstone-2023-1-44/assets/118142745/9eb4d3a6-d3ea-4d8c-b251-708ac00c7309"></br>
<div align="left" style="padding-left:75px">
1. keyboard R : 동작 기록 시작</br>
2. keyboard E : 동작 기록 종료</br>
3. keyboard G : 동작에 대한 Point cloud 생성</br>
4. keyboard V : Point cloud 시각화</br></div>
</div>
</br>

* G를 입력하여 Poin cloud를 생성하면 자동으로 서버로 데이터가 서버로 POST되기 때문에 서버를 동시에 실행하고 있어야한다.
</br>

### 3. 서버
Flask 웹 프레임워크를 사용하여 서버를 구축</br> 
Sever 파일의 app.py 파일을 실행하여 서버를 실행시켜준다.
* app.py를 실행하기 전 필요한 package를 설치해준다.
```bash
$ pip install flask
$ pip install tensorflow
$ pip install pymysql
```
</br>

### 4. 어플리케이션
안드로이드 스튜디오를 사용하여 어플리케이션을 개발</br>
repository의 Fallalert 파일의 코드를 실행한다.
* 안드로이드 스튜디오 설치
```bash
#  64비트 시스템에 필요한 라이브러리를 설치
$ sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386

# i386이 출력되면 정상적으로 설치됨을 의미
$ sudo dpkg --print-foreign-architectures 
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
설치 후 안드로이드 스튜디오 사이트에 방문하여 다운로드한다.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
다운로드가 완료되면 다운로드한 파일의 압축을 풀어준다.
```bash
#  압축 풀기
$ tar -xvzf android-studio-ide-${버전}-linux.tar.gz

# 압축이 풀린 android-studio 폴더로 이동
$ cd android-studio

# 안드로이드 스튜디오 실행
$ bin/studio.sh
```
서버와 통신하기 위해 URL_STR을 사용하는 ip주소로 변경해줘야 한다.
* ip확인
```bash
$ hostname -I
```
* 사용하는 ip주소로 변경
```java
URL_STR = "http://{사용하는 ip주소}:5000/get_data";
/* 
    사용하는 ip가 10.0.2.2인경우
    URL_STR = "http://10.0.2.2:5000/get_data";
*/
```
