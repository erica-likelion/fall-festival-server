# 가을 축제 웹앱 서버

> 한양대학교 ERICA 가을 축제 웹앱 프로젝트의 백엔드 서버입니다.

---

## ✨ 주요 기능

- **공지사항**: 축제 관련 공지사항을 조회합니다.
- **주점 정보**: 운영 중인 부스와 주점의 위치, 설명, 메뉴 등을 안내합니다.
- **공연 정보**: 실시간 공연 및 전체 라인업 정보를 제공합니다.
- **아티스트 정보**: 참여 아티스트의 상세 정보를 조회할 수 있습니다.
- **축제 지도**: 지도 위 부스, 화장실 등 주요 시설물 위치를 마커로 표시합니다.
- **오늘의 운세**: 재미로 즐길 수 있는 오늘의 운세 기능을 제공합니다.
- **진행중인 콘텐츠**: 현재 진행 중인 이벤트나 콘텐츠 정보를 보여줍니다.

---

## 🛠️ 기술 스택

| 분야     | 스택                   |
|--------|----------------------|
| 언어     | Java 17              |
| 프레임워크  | Spring Boot 3.5.4    |
| 데이터베이스 | MySQL 8.0            |
| 빌드 도구  | Gradle 8.9           |
| ORM    | Spring Data JPA      |
| 배포     | Docker / NCP / Nginx |

---

## 시작하기

### 환경 변수 설정

1. 프로젝트 루트의 `.env.template` 파일을 복사하여 `.env` 파일을 생성합니다.
2. `.env` 파일 내의 데이터베이스 연결 정보를 자신의 로컬 환경에 맞게 수정합니다.

   ```dotenv
   # .env
   SPRING_PROFILES_ACTIVE=dev

   DB_URL=jdbc:mysql://localhost:3306/festival
   DB_USERNAME=your-username
   DB_PASSWORD=your-password
   ```

### 애플리케이션 실행 (로컬)

프로젝트 루트 디렉토리에서 아래 명령어를 실행합니다.

```bash
./gradlew bootRun
```

### Docker로 실행하기

`docker-compose`를 사용하여 운영 환경과 동일한 환경으로 애플리케이션을 실행할 수 있습니다.

1. **Docker 이미지 빌드**

   ```bash
   docker build -t fall-festival-server .
   ```

2. **Docker Compose 실행**

   `docker-compose-prod.yml` 파일이 있는 디렉토리에서 아래 명령어를 실행합니다.
   (실행 전, `docker-compose-prod.yml` 파일 내의 환경 변수를 설정해야 합니다.)

   ```bash
   docker-compose -f docker-compose-prod.yml up -d
   ```

---

## 프로젝트 구조

```text
fall-festival-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── likelion/festival/
│   │   │       ├── FestivalApplication.java
│   │   │       ├── config/         
│   │   │       ├── controller/     
│   │   │       ├── domain/         
│   │   │       ├── dto/            
│   │   │       ├── exception/      
│   │   │       ├── repository/     
│   │   │       └── service/        
│   │   └── resources/              
│   └── test/                     
├── .github/                      
├── scripts/                      
├── build.gradle                  
├── Dockerfile                    
├── docker-compose-prod.yml       
└── README.md
```