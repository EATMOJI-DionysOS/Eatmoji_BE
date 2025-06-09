# 🍽️ Eatmoji Backend

<div align="center">
  <img src="https://img.shields.io/badge/SpringBoot-61BA55?style=for-the-badge&logo=SpringBoot&logoColor=white", height=40><img src="https://img.shields.io/badge/FastAPI-2BA498?style=for-the-badge&logo=FastAPI&logoColor=white", height=40><img src="https://img.shields.io/badge/MongoDB-4AB349?style=for-the-badge&logo=MongoDB&logoColor=white", height=40><img src="https://img.shields.io/badge/OpenAI-000000?style=for-the-badge&logo=OpenAI&logoColor=white", height=40><img src="https://img.shields.io/badge/langchain-1C3C3C?style=for-the-badge&logo=langchain&logoColor=white", height=40>
</div>


> 감정을 이모지로 표현하고, 그 이모지에 어울리는 음식을 추천해주는 감성 기반 메뉴 추천 서비스 **Eatmoji**의 백엔드 레포지토리입니다.

</br>

## 📌 소개

Eatmoji는 감정 상황을 이모지로 선택하면, 이어 맞는 음식을 추천해주는 **감성 기반 메뉴 추천 서비스**입니다.
추천 결과는 레시피로 직접 만들어보거나, 지역 기반 맛집 정보를 통해 먹으러 가는 두 가지 방식으로 이어집니다.

이 저장소는 Eatmoji 서비스의 **백엔드 서버**로, 다양한 기능들의 API를 제공합니다.

</br>

## ⚙️ 주요 기능

* 🔐 사용자 로그인/회원가입 토큰 기반 인증 및 Spring Security로 보안 관리
* 😊 감정 이모지를 데이터셋을 이용하여 감정과 점수로 변환
* 🍽️ 프롬프트에 따른 감정기반/사용자 맞춤 음식 추천
* 📍 Recipe 데이터셋을 이용한 음식 조리 레시피 검색 및 링크 전달
* ⭐ 추천 결과 좋아요 사용지 프로필 저장

</br>

## 🚀 배포 링크
🐸`http://3.37.53.72:8000`
🐸`http://3.37.53.72:8080`

## 🚀 Swagger 링크
🐸`http://3.37.53.72:8000/docs`
🐸`http://3.37.53.72:8080/swagger-ui/index.html#/`

</br>

## 🧑‍💻 설치 및 실행 방법
- 전체적인 구조는 Spring에서 openAI API 를 이용하려하면 FastAPI를 불러서 사용하는 방식입니다.
  
🔹 개발자나 오픈소스 기여자가 **로컬에서 테스트**하고 싶을 경우, 아래처럼 실행할 수 있습니다:

```bash
# 1️⃣ Spring Boot
./gradlew build
java -jar build/libs/Eatmoji-0.0.1-SNAPSHOT.jar

> 기본 포트는 `http://localhost:8080` 입니다.

# 2️⃣ FastAPI
Fast_API_GPT 폴더에 .env파일 생성
.env 파일에 개인 OPEN_API_KEY 발급 후 추가
change_to_json을 제외한 py파일에 다음 내용 추가

from dotenv import load_dotenv
load_dotenv()

cd Fast_API_GPT
pip install -r requirements.txt
uvicorn main:app --reload --port 8000

> 기본 포트는 `http://localhost:8000` 입니다.

</br>
```
## 📁 폴더 구조

```
EATMOJI_BE/
├── .github/                                                # GitHub 설정 (예: PR 템플릿)
├── .gradle/                                                # Gradle 캐시/설정 (자동 생성)
├── .idea/                                                  # IDE (IntelliJ 등) 설정
├── Fast_API_GPT/                                           # FastAPI 기반 GPT 추천 서버 (Python)
├── gradle/                                                 # Gradle Wrapper 관련 파일
├── src/                                                    # 소스코드 루트
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── DionysOS/
│   │   │           └── Eatmoji/                            # Spring Boot 메인 패키지
│   │   │               ├── config/                         # 환경 설정 클래스
│   │   │               ├── controller/                     # API 컨트롤러 (엔드포인트)
│   │   │               ├── dto/                            # 요청/응답 DTO 정의
│   │   │               ├── model/                          # MongoDB 모델 (도메인)
│   │   │               ├── repository/                     # MongoDB Repository
│   │   │               ├── service/                        # 비즈니스 로직 서비스
│   │   │               ├── util/                           # 유틸리티 클래스
│   │   │               └── EatmojiApplication.java         # Spring Boot 메인 실행 파일
│   │   └── resources/                                      # 정적 리소스 및 설정 파일 (application.yml 등)
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── DionysOS/
│       │           └── Eatmoji/                            # 테스트 코드
│       │               ├── controller/                     # 컨트롤러 테스트
│       │               ├── repository/                     # Repository 테스트
│       │               ├── service/                        # 서비스 테스트
│       │               └── EatmojiApplicationTests.java    # 통합 테스트
├── .gitignore                                              # Git 무시 파일 목록
├── build.gradle                                            # Gradle 빌드 스크립트
├── gradlew                                                 # Gradle Wrapper 실행 파일 (리눅스/맥)
├── gradlew.bat                                             # Gradle Wrapper 실행 파일 (Windows)
├── PULL_REQUEST_TEMPLATE.md # PR 템플릿
├── README.md                                               # 프로젝트 소개 문서
├── settings.gradle                                         # Gradle 프로젝트 설정
└── LICENSE (예정)                                          # 라이선스 파일

```

</br>

## 🛠️ 기술 스택

### ✔️Back-end
<img src="https://img.shields.io/badge/Spring-61BA55?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/SpringBoot-8ED16A?style=for-the-badge&logo=SpringBoot&logoColor=white"><img src="https://img.shields.io/badge/Springsecurity-39A346?style=for-the-badge&logo=Springsecurity&logoColor=white"><img src="https://img.shields.io/badge/FastAPI-2BA498?style=for-the-badge&logo=FastAPI&logoColor=white"><img src="https://img.shields.io/badge/MongoDB-4AB349?style=for-the-badge&logo=MongoDB&logoColor=white">
### ✔️Distribution
<img src="https://img.shields.io/badge/AWS EC2-FFFFFF?style=for-the-badge&logo=AWS&logoColor=white">

### ✔️AI
<img src="https://img.shields.io/badge/OpenAI-000000?style=for-the-badge&logo=OpenAI&logoColor=white"><img src="https://img.shields.io/badge/langchain-1C3C3C?style=for-the-badge&logo=langchain&logoColor=white">

### ✔️API document
<img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">

</br>

## 🤝 가이드

* Pull Request 전에는 반드시 [CONTRIBUTING.md](../Eatmoji_BE/blob/develop/CONTRIBUTING.md)를 확인해주세요.
* 커밋 메시지는 [Conventional Commits](https://www.conventionalcommits.org/) 형식을 권장합니다.
* 브랜치 전략 및 PR 리뷰 프로세스는 Wiki에 설명되어 있습니다.

</br>

## 📄 라이선스

번 프로젝트는 [MIT License](../Eatmoji_BE/blob/develop/LICENSE)에 따라 오픈소스로 공개되어 있습니다.

</br>

## 📚 더 아는 길

* [🔗 GitHub Wiki 바로가기](https://github.com/EATMOJI-DionysOS/Eatmoji_BE/wiki)
* [📦 Frontend Repository](https://github.com/EATMOJI-DionysOS/Eatmoji_FE)

</br>
</br>

> Made with ❤️ by Team DionysOS – 2025  
> This project was created as part of the **Open Source Software Project** course.
> 
