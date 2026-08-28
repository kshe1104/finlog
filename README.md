# 💰 Finlog - 개인 금융 관리 API

개인 금융을 효율적으로 관리하기 위한 RESTful API 서버입니다.
수입/지출 기록, 구독 관리, 목표 저축 기능을 제공합니다.

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.14 |
| ORM | Spring Data JPA + Hibernate |
| DB (운영) | MySQL 8.x |
| DB (테스트) | H2 (in-memory) |
| 인증 | Spring Security + OAuth2 + JWT |
| 쿼리 최적화 | QueryDSL 5.1.0 |
| 빌드 | Gradle |

---

## 📦 주요 기능

### 인증
- Google / Naver OAuth2 소셜 로그인
- JWT 기반 API 인증

### 거래 내역
- 수입/지출 등록, 수정, 삭제
- 월별 거래 내역 조회
- 월별 수입/지출 통계

### 카테고리
- 카테고리 CRUD
- 타입별 조회 (수입/지출)
- 기본 카테고리 보호

### 구독 관리
- 정기 구독 등록, 수정, 삭제
- 구독 활성/비활성 토글
- D-day 결제일 표시
- 스케줄러 기반 자동 거래 생성

### 목표 저축
- 목표 등록, 삭제
- 저축 금액 추가
- 달성률 자동 계산
- 기한 만료 시 자동 FAILED 처리

---

## 🏗️ 프로젝트 구조
src/main/java/com/finance/finlog/
<br>├── domain/
<br>│ ├── user/
<br>│ │ ├── entity/
<br>│ │ ├── repository/
<br>│ │ └── service/
<br>│ ├── category/
<br>│ │ ├── controller/
<br>│ │ ├── dto/
<br>│ │ ├── entity/
<br>│ │ ├── repository/
<br>│ │ └── service/
<br>│ ├── transaction/
<br>│ ├── subscription/
<br>│ └── saving/
<br>└── global/
<br>├── config/
<br>├── common/
<br>├── security/
<br>└── scheduler/

---

## 🔐 인증 방식

### OAuth2 로그인 플로우
GET /oauth2/authorization/google
<br>구글 로그인 완료
<br>JWT 토큰 발급 후 리다이렉트
<br>→ http://localhost:3000/oauth/callback?token={jwt}
<br>이후 요청 시 Authorization 헤더에 토큰 포함
<br>→ Authorization: Bearer {jwt}

---

## 📡 API 명세

### 카테고리

| Method | URL | 설명 |
|--------|-----|------|
| GET | /api/categories | 카테고리 목록 조회 |
| GET | /api/categories?type=EXPENSE | 타입별 조회 |
| POST | /api/categories | 카테고리 생성 |
| PUT | /api/categories/{id} | 카테고리 수정 |
| DELETE | /api/categories/{id} | 카테고리 삭제 |

### 거래 내역

| Method | URL | 설명 |
|--------|-----|------|
| GET | /api/transactions | 월별 거래 내역 조회 |
| GET | /api/transactions/stats | 월별 통계 조회 |
| POST | /api/transactions | 거래 등록 |
| PUT | /api/transactions/{id} | 거래 수정 |
| DELETE | /api/transactions/{id} | 거래 삭제 |

### 구독

| Method | URL | 설명 |
|--------|-----|------|
| GET | /api/subscriptions | 구독 목록 조회 |
| POST | /api/subscriptions | 구독 등록 |
| PUT | /api/subscriptions/{id} | 구독 수정 |
| PATCH | /api/subscriptions/{id}/toggle | 구독 토글 |
| DELETE | /api/subscriptions/{id} | 구독 삭제 |

### 목표 저축

| Method | URL | 설명 |
|--------|-----|------|
| GET | /api/saving-goals | 목표 목록 조회 |
| GET | /api/saving-goals?inProgress=true | 진행중 목표 조회 |
| POST | /api/saving-goals | 목표 등록 |
| PATCH | /api/saving-goals/{id}/amount | 저축 금액 추가 |
| DELETE | /api/saving-goals/{id} | 목표 삭제 |

---

## ⚙️ 로컬 실행 방법

### 1. 환경변수 설정

프로젝트 루트에 `.env` 파일 생성
<br>GOOGLE_CLIENT_ID = 구글_클라이언트_ID
<br>GOOGLE_CLIENT_SECRET = 구글_클라이언트_SECRET
<br>NAVER_CLIENT_ID = 네이버_클라이언트_ID
<br>NAVER_CLIENT_SECRET = 네이버_클라이언트_SECRET
<br>JWT_SECRET = Base64_인코딩된_시크릿_키

### 2. MySQL 데이터베이스 생성

```sql
CREATE DATABASE finlog DEFAULT CHARACTER SET utf8mb4;
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

---

## 🧪 테스트 실행

```bash
./gradlew test
```

---

## 📚 핵심 학습 내용

### JPA
- N+1 문제와 LAZY 로딩으로 해결
- 변경 감지(Dirty Checking)로 불필요한 save() 제거
- @Transactional(readOnly = true) 성능 최적화

### Spring Security
- OAuth2 로그인 플로우 구현
- JWT Filter Chain 동작 원리
- SecurityContext 기반 인증 정보 관리

### 테스트
- @DataJpaTest + H2로 Repository 테스트
- Mockito 기반 Service 단위 테스트
- given/when/then 패턴

### 설계 원칙
- 도메인 기반 패키지 구조
- DTO 패턴으로 엔티티 노출 방지
- IDOR 방어 (findByIdAndUser)
- 정적 팩토리 메서드 패턴



----
## 🚀 배포

- 배포 URL: https://finlog-production-0d9e.up.railway.app
- Swagger: https://finlog-production-0d9e.up.railway.app/swagger-ui/index.html
- 인프라: Railway (Docker 기반 배포)
- CI/CD: GitHub Actions → main 브랜치 push 시 자동 배포
