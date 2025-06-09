# 🍽️ Eatmoji 프로젝트


<img src="https://github.com/user-attachments/assets/1aef44d0-4151-454d-86b3-0251b0e0d2a2" alt="moji" width="500"/>


**이모지를 이용한 사용자의 감정과 건강 정보를 기반으로 음식 메뉴를 추천하는 AI 기반 추천 시스템입니다.**

---

## 🚀 주요 기능

- 😊 이모지 기반으로 사용자 감정 파악 후 메뉴 추천
- 📊 사용자 프로필(선호 카테고리, 맛, 질병, 알레르기)과 과거 히스토리(좋아요한 음식)를 반영한 개인화 추천
- ⚡️ Spring Boot와 FastAPI를 연동하여, GPT-4o 기반 AI 추천 결과를 제공합니다
- 🗃️ MongoDB로 사용자 기록 관리 및 분석

---

## 서비스 이용 방법

1. 로그인을 안 한 경우
   
   - 일반 이모지 기반 음식 추천만 가능합니다.
     
2. 로그인을 한 경우
   - 개인 프로필에 접근하여 본인의 알레르기, 선호 카테고리 등의 개인정보를 추가할 수 있습니다.
   - 추천 결과에 나온 좋아요 버튼을 누르면 해당 음식이 추후 추천 결과에 적용됩니다.
   - 로그인을 한 경우 이모지 기반 음식 추천 시 본인의 프로필과 히스토리를 반영한 개인화 추천이 가능합니다.
   - 감정에 기반하지 않고 프로필과 히스토리만을 반영한 오메추 기능을 사용할 수 있습니다.


## ⚙️ 배포 상태 및 API 엔드포인트

✅ 현재 Eatmoji 서비스는 **AWS EC2 서버**에서 배포·운영되고 있습니다.

| 기능                  | 엔드포인트                                                  |
|-----------------------|-------------------------------------------------------------|
| 일반 이모지 기반 추천  | `http://3.37.53.72:8080/api/recommend/emoji`                |
| 일반 개인화 추천       | `http://3.37.53.72:8080/api/recommend/personalized`         |
| 이모지 개인화 추천     | `http://3.37.53.72:8080/api/recommend/emoji/login`          |
| Swagger 문서          | `http://3.37.53.72:8080/swagger-ui/index.html`              |

👉 **사용자는 위 API 엔드포인트를 통해 서비스를 바로 사용할 수 있습니다.**

---

## 🔗 기술 스택

| 분류             | 기술/툴                |
|------------------|------------------------|
| **백엔드**       | Spring Boot (Java 21), FastAPI (Python 3.11)|                    
| **AI**           | GPT-4o (OpenAI API)    |
| **DB**           | MongoDB                |
| **프론트**       | React, Next            |
| **기타**         | LangChain, Swagger    |
| **배포/서버**    | AWS EC2                |

---

## 📦 로컬 실행 (선택)

🔹 개발자나 오픈소스 기여자가 **로컬에서 테스트**하고 싶을 경우, 아래처럼 실행할 수 있습니다:

```bash
# 1️⃣ Spring Boot
./gradlew build
java -jar build/libs/Eatmoji-0.0.1-SNAPSHOT.jar

# 2️⃣ FastAPI
Fast_API_GPT 폴더에 .env파일 생성
.env 파일에 개인 OPEN_API_KEY 발급 후 추가

cd Fast_API_GPT
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
