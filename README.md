# 🧹 fairer
<p>
    <img src="https://img.shields.io/github/issues-pr-closed/depromeet/fairer-be?color=blueviolet"/>
  <img src="https://img.shields.io/github/issues/depromeet/fairer-be?color=inactive"/>     <img src="https://img.shields.io/github/issues-closed/depromeet/fairer-be"/> 
  <img src="https://img.shields.io/github/stars/depromeet/fairer-be"/>
</p>

<img src="https://user-images.githubusercontent.com/77181984/175760346-a164f36f-3c12-41ab-9a06-2f86885baf9a.png" width="650"/><br/>
집안일을 하는 모든 이들의 평화를 위한 집안일 관리, 분담 서비스 👨‍👧‍👧<br/>
fairer의 백엔드 레포지토리 입니다.
> Play 스토어 : https://play.google.com/store/apps/details?id=com.depromeet.housekeeper

<br/>

## 🎬 Preview
<img src="https://user-images.githubusercontent.com/77181984/175762475-ac741a33-9a1b-414f-97d8-0f92abc45f1e.png" width="230"/> &nbsp;&nbsp;  <img src="https://user-images.githubusercontent.com/77181984/175762490-add086a6-13c6-46ca-a309-f9813c009457.png" width="230"/> 

<br/>

## 👨‍👩‍👧‍👧 BackEnd Developer



|     <img src="https://user-images.githubusercontent.com/77181984/175761511-863c24d7-dae1-4539-9dad-d82b83a3c907.png" width="150"/>     |    <img src="https://user-images.githubusercontent.com/77181984/175761513-22e7b9f1-26b6-43c2-b117-359db6157e06.jpeg" width="150"/>    |     <img src="https://user-images.githubusercontent.com/77181984/175761514-2842fd4b-8a95-4f40-ad01-eb075e32fb85.png" width="150"/>      | <img src="https://user-images.githubusercontent.com/77181984/175761515-5092022d-d4f1-4db4-8fcc-18f9479fda93.jpeg" width="150"/>  |
| :-----------------------------------: | :-----------------: | :----------------: |:----------------: |
|   [김승윤](https://github.com/dskym) |   [신동빈](https://github.com/SDB016)       |   [김다슬](https://github.com/daseulll)  | [곽다은](https://github.com/daeunkwak) |

<br/>

## **💻 Tech Stack**

- Skills

  > Spring Boot, Data JPA(+Querydsl)
>
- Database

  > Mysql, RDS
>
- 개발 환경

  > AWS EC2
  
  > Docker
>
- 운영 환경

  > Elastic Beanstalk
>
- CI/CD

  > [Github Actions](https://github.com/depromeet/fairer-be/actions)
>
- 문서화

  > [Swagger](http://ec2-3-39-60-64.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html)
>
- ETC

  > JWT, Oauth2(google)

  > Spring Batch

  > Firebase Cloud Messaging
>

<br/>

## 📚 Architecture
<img src="https://user-images.githubusercontent.com/77181984/175924599-312a74a4-c506-49b5-a937-3fd82706419f.PNG" width="750"/>

<br/>

## 🗂 Directory
```
📂 fairer-api
    📂 api
    📂 domain
    📂 dto
    📂 global
     ├── 📂 config
     ├── 📂 exception
     |	 ├── 📂 dto
     |   └── 📂 handler
     ├── 📂 resolver
     └── 📂 util
    📂 repository
    📂 service
    📂 vo
	  - Application.java
📂 fairer-batch
    📂 config
    📂 domain
     ├── 📂 command
     └── 📂 config
    📂 scheduler
	  - Application.java
```

<br/>

## 📝ERD
<img width="808" alt="image" src="https://user-images.githubusercontent.com/77181984/218323606-81d2c2f0-4dbe-4d89-89a7-8e124816deda.png">

<br/>

## 💻 Build & Run
1. 빌드
```
$ ./gradlew build
```
2. 빌드된 파일 (*.jar) 실행
```
$ fairer-0.0.1-SNAPSHOT.jar
$ java -jar fairer-0.0.1-SNAPSHOT.jar
```

<br/>


## 📢 Commit message


`feat` : 새로운 기능에 대한 커밋

`fix` : 버그 수정에 대한 커밋

`refactor` : 새로운 기능 추가나 수정 없이 기존 코드 리팩토링

`docs` : 문서화에 대한 커밋


<br/>

## 📞 Contact
[![Facebook](https://img.shields.io/badge/facebook-1877f2?style=flat-square&logo=facebook&logoColor=white&link=https://www.facebook.com/fairer.official/)](https://www.facebook.com/fairer.official)
[![instagram](https://img.shields.io/badge/instagram-E4405F?style=flat-square&logo=Instagram&logoColor=white&link=https://www.instagram.com/fairer.official/)](https://www.instagram.com/fairer.official/)


