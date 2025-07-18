# 🛠️ Eatmoji 프로젝트 기여 가이드

감사합니다! 아래 절차를 따라 기여해주세요.

## 1️⃣ Issue 생성

- 버그나 개선 사항을 발견했다면, 먼저 [Issues](https://github.com/EATMOJI-DionysOS/Eatmoji_BE/issues)에서 검색 후 없으면 새로 작성해주세요.

## 2️⃣ 브랜치 전략

(feat/fix/refactor/chore)/#이슈번호-(UI/API)-기능설명

ex) feat/#2-UI-home

ex) feat/#16-API-create-post

## 3️⃣ Commit Convention

- 커밋 메시지는 아래처럼 작성해주세요.

타입: 부연 설명 및 이유 #이슈번호

```bash
//ex. feat: Login 화면 UI 구현 #1
```

✨ feat: 새로운 기능 추가

🔨 fix: 기능 수정 - 사용자가 동작했을 때 달라지는 것

♻️ refactor: 코드 리펙토링 - 사용자가 동작했을 때 달라지지 않고 코드 변경 (기능 개선, 버전업)

🐛 bug: 버그 수정

🚑️ hotfix: 핫픽스

💄 UI: CSS 수정, UI수정

🎨 style: 코드 포맷팅, 세미 콜론 누락, 코드 변경이 없는 경우

🔧 config: 설정, 환경 변수 변경

✏️ typo: 오타 수정 , 워딩 수정

📝 docs: 문서 수정

💬 comment: Todo, Highlight, Question 등 기타 주석 추가/삭제

📦 package: 새로운 라이브러리 추가

🔥 remove: 코드나 파일 삭제


## 4️⃣ Git Flow

1. Issue 생성
2. Branch 생성
3. add → commit → push → pull request 과정을 거친다.
4. 코드 리뷰 진행 후 모든 팀원들의 승인을 받는다.
5. develop branch로 merge한다.

develop branch으로 이동하여 pull을 받은 다음 위 과정을 반복한다.
