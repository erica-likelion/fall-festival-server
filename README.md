# 가을 축제 웹앱 서버

> 한양대학교 ERICA 가을 축제 웹앱 프로젝트의 백엔드 서버입니다.

---

## 기술 스택

| 분야     | 스택                |
|--------|-------------------|
| 언어     | Java 17           |
| 프레임워크  | Spring Boot 3.5.4 |
| 빌드 도구  | Gradle (Groovy)   |
| 데이터베이스 | MySQL 8.0.43      |
| 문서화    | SpringDoc OpenAPI |
| 배포     | (미정)              |

---

## 프로젝트 구조

```text
root/
 ├─ src/
 │   ├─ main/
 │   │   ├─ java/
 │   │   │   └─ likelion/festival/
 │   │   │        ├─ Application.java         
 │   │   │        ├─ config/                 
 │   │   │        ├─ controller/             
 │   │   │        ├─ dto/                    
 │   │   │        ├─ service/                
 │   │   │        ├─ repository/             
 │   │   │        └─ domain/    
 │   │   └─ resources/
 │   │        ├─ application.yml            
 │   │        ├─ static/                    
 │   │        └─ templates/                 
 │   └─ test/java/likelion/festival/ 
 ├─ .gitignore
 ├─ build.gradle
 ├─ README.md
 └─ etc...

```
