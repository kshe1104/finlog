# 💰 Finlog - 개인 금융 관리 API

개인 금융을 효율적으로 관리하기 위한 RESTful API 서버입니다.
수입/지출 기록, 구독 관리, 목표 저축 기능을 제공합니다.

기획 → 설계(ERD/아키텍처) → 개발 → 테스트 → CI/CD → 배포까지 전 과정을 혼자 진행한 개인 프로젝트입니다.

---

## 🚀 배포

- **배포 URL**: https://finlog-production-0d9e.up.railway.app
- **Swagger**: https://finlog-production-0d9e.up.railway.app/swagger-ui/index.html
- **로그인 데모**: https://finlog-production-0d9e.up.railway.app/oauth2/authorization/google
  - 로그인 완료 시 Thymeleaf 기반 콜백 페이지에서 발급된 JWT를 바로 확인/복사할 수 있습니다.
  - 복사한 토큰을 Swagger 우측 상단 `Authorize`에 입력하면 모든 API를 바로 테스트할 수 있습니다.
- **인프라**: Railway (Docker 멀티스테이지 빌드, MySQL + Redis)
- **CI/CD**: GitHub Actions → `main` 브랜치 push 시 자동 테스트 및 배포

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.14 |
| ORM | Spring Data JPA + Hibernate |
| 쿼리 최적화 | QueryDSL 5.1.0 |
| DB (운영) | MySQL 8.x |
| DB (테스트) | H2 (in-memory) |
| 캐시 / 세션 | Redis (JWT 블랙리스트, 통계 캐싱) |
| 인증 | Spring Security + OAuth2 (Google/Naver) + JWT |
| 화면 | Thymeleaf (로그인 데모 페이지) |
| API 문서화 | Swagger (SpringDoc OpenAPI) |
| 인프라 | Docker, Docker Compose |
| CI/CD | GitHub Actions, Railway |
| 테스트 | JUnit5, Mockito, @DataJpaTest |
| 빌드 | Gradle |

---

## 📦 주요 기능

### 인증
- Google / Naver OAuth2 소셜 로그인
- JWT 기반 API 인증 (Stateless)
- Redis 기반 JWT 블랙리스트로 로그아웃 처리

### 거래 내역
- 수입/지출 등록, 수정, 삭제
- 월별 거래 내역 조회
- 월별 수입/지출 통계 (Redis 캐싱 적용, TTL 10분)

### 카테고리
- 카테고리 CRUD
- 타입별 조회 (수입/지출)
- 기본 카테고리 보호 (삭제/수정 불가)

### 구독 관리
- 정기 구독 등록, 수정, 삭제
- 구독 활성/비활성 토글
- D-day 결제일 표시
- 스케줄러 기반 자동 거래 생성 (매일 자정)

### 목표 저축
- 목표 등록, 삭제
- 저축 금액 추가
- 달성률 자동 계산
- 기한 만료 시 스케줄러가 자동으로 FAILED 처리

---

## 🏗️ 프로젝트 구조

```
src/main/java/com/finance/finlog/
├── domain/
│   ├── user/
│   ├── category/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   ├── transaction/     (Repository + QueryDSL Custom 구현체 포함)
│   ├── subscription/
│   └── saving/
└── global/
    ├── config/          (Security, Redis, Swagger, QueryDSL 설정)
    ├── common/          (공통 응답, 예외 처리, @CurrentUser)
    ├── security/        (JWT, OAuth2, Redis 블랙리스트)
    ├── scheduler/        (구독 자동 처리, 목표 만료 처리)
    └── view/            (Thymeleaf 데모 페이지 Controller)
```

**도메인 기반 패키지 구조**를 택해 도메인 하나의 관련 코드(Controller~Repository)가 한 폴더에 응집되도록 설계했습니다.

---

## 🔐 인증 방식

### OAuth2 로그인 플로우

```
1. GET /oauth2/authorization/google (또는 naver)
2. 소셜 로그인 완료 → CustomOAuth2UserService에서 최초 로그인 시 자동 회원가입
3. OAuth2SuccessHandler가 JWT 발급 후 리다이렉트
   → /view/oauth/callback?token={jwt}  (Thymeleaf 페이지에서 토큰 확인)
4. 이후 API 요청 시 Authorization 헤더에 토큰 포함
   → Authorization: Bearer {jwt}
5. 로그아웃 시 남은 만료 시간만큼 Redis 블랙리스트에 등록
   → 이후 해당 토큰으로는 재요청 불가
```

---

## 📡 API 명세

### 인증
| Method | URL | 설명 |
|--------|-----|------|
| POST | /api/auth/logout | 로그아웃 (JWT 블랙리스트 등록) |

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
| GET | /api/transactions/stats | 월별 통계 조회 (Redis 캐싱) |
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

전체 API는 [Swagger 문서](https://finlog-production-0d9e.up.railway.app/swagger-ui/index.html)에서 직접 확인 및 테스트할 수 있습니다.

---

## ⚙️ 로컬 실행 방법

### 1. 환경변수 설정

프로젝트 루트에 `.env` 파일 생성

```
GOOGLE_CLIENT_ID=구글_클라이언트_ID
GOOGLE_CLIENT_SECRET=구글_클라이언트_SECRET
NAVER_CLIENT_ID=네이버_클라이언트_ID
NAVER_CLIENT_SECRET=네이버_클라이언트_SECRET
JWT_SECRET=Base64_인코딩된_시크릿_키
MYSQL_ROOT_PASSWORD=MySQL_비밀번호
```

### 2. Docker로 MySQL + Redis 실행

```bash
docker-compose up -d
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

- `@DataJpaTest` + H2로 Repository 계층 테스트
- `Mockito` 기반 Service 단위 테스트
- `@WebMvcTest` + `MockMvc` 기반 Controller 테스트
- GitHub Actions에서 PR/push 시 자동 실행 (Redis 서비스 컨테이너 포함)

---

## 🔧 트러블슈팅

프로젝트를 진행하며 겪은 문제와 해결 과정입니다. (전체 목록 중 학습 가치가 높은 항목 발췌)

### 1. QueryDSL CVE-2024-49203 대응 방식 결정
- **문제**: `com.querydsl` 5.1.0에 SQL/HQL Injection 취약점 발견. 패치는 OpenFeign 포크에서만 제공.
- **검토**: 포크로 교체 시 원본과의 향후 비호환/생태계 분열 리스크 존재.
- **결정**: 원본 라이브러리를 유지하되, 취약점이 트리거되는 패턴(`orderBy`에 사용자 입력 직접 전달)을 코드에서 원천적으로 사용하지 않는 방식으로 대응.
- **배운 점**: 모든 취약점을 "최신 버전 업데이트"로만 해결하는 게 아니라, 실제 사용 패턴 기준으로 위험도를 판단하는 것도 유효한 전략.

### 2. JPA N+1 문제와 QueryDSL 리팩토링
- **문제**: 월별 수입/지출 합계를 문자열 기반 JPQL(`@Query`)로 작성 → 오타가 나도 컴파일 타임에 감지 불가.
- **해결**: `TransactionRepositoryCustom` + `TransactionRepositoryImpl` 패턴으로 QueryDSL 도입, 타입 안전한 쿼리로 전환.
- **배운 점**: Spring Data JPA 커스텀 Repository 확장 패턴(`Impl` 네이밍 규칙)의 동작 원리.

### 3. CI 환경에서 Repository 테스트 실패
- **문제**: 로컬에서는 통과하던 `@DataJpaTest`가 CI에서 `NoSuchBeanDefinitionException` 발생.
- **원인**: `@DataJpaTest`는 JPA 관련 Bean만 로딩하는데, QueryDSL의 `JPAQueryFactory`는 그 범위 밖이라 CI에서 Bean을 찾지 못함.
- **해결**: `@TestConfiguration`으로 `TestQueryDslConfig`를 별도 작성 후 `@Import`로 명시적 주입.
- **배운 점**: 테스트 슬라이스 어노테이션은 "필요한 Bean만 가볍게 로딩"하는 게 목적이므로, 그 범위 밖 Bean은 직접 구성해야 함.

### 4. Railway 배포 시 Dockerfile 미인식
- **문제**: `railway.json`으로 Dockerfile 빌드를 지정했음에도 `couldn't locate a dockerfile` 에러 반복.
- **원인**: Railway의 Root Directory 설정이 기본값과 달라 Dockerfile 경로를 찾지 못함.
- **해결**: Railway 대시보드에서 Root Directory를 `/`로 명시적으로 지정.
- **배운 점**: PaaS 플랫폼은 설정 파일(`railway.json`)보다 대시보드 수동 설정이 우선 적용될 수 있어, 배포 실패 시 인프라 설정값을 함께 점검해야 함.

### 5. 플랫폼별 환경변수 네이밍 불일치
- **문제**: 배포 후 `Failed to parse the host:port pair '${MYSQL_HOST}:${MYSQL_PORT}'` 에러로 크래시.
- **원인**: 로컬(.env) 기준으로 작성한 변수명(`MYSQL_HOST`)과 Railway가 실제로 제공하는 변수명(`MYSQLHOST`, 언더스코어 없음)이 불일치.
- **해결**: Railway 각 서비스의 `Variables` 탭에서 실제 제공 변수명을 확인 후, Variable Reference(`${{MySQL.MYSQLHOST}}`)로 명시적 매핑.
- **배운 점**: 로컬 환경과 배포 환경(Railway, AWS 등)은 변수 네이밍 규칙이 다를 수 있으므로 항상 실제 제공값을 확인해야 함.

### 6. BigDecimal 비교 오류
- **문제**: 저축 목표 테스트에서 금액 비교가 예상과 다르게 동작.
- **원인**: `BigDecimal.equals()`는 scale까지 비교하여 `100`과 `100.00`을 다르게 취급.
- **해결**: 모든 금액 비교를 `compareTo()` 기반으로 통일.
- **배운 점**: 금융 데이터는 `BigDecimal` 사용이 필수이며, 비교 연산자 선택에도 별도 주의가 필요.

---

## 📚 핵심 학습 내용

### JPA
- 연관관계 LAZY 로딩으로 N+1 문제 방지
- 변경 감지(Dirty Checking)로 불필요한 `save()` 호출 제거
- `@Transactional(readOnly = true)`로 조회 성능 최적화

### Spring Security
- OAuth2 로그인 플로우 직접 구현 (Google/Naver 응답 구조 차이 처리)
- JWT Filter Chain 동작 원리, `SecurityContext` 기반 인증 관리
- Redis를 활용한 JWT 무효화(로그아웃) 전략

### 인프라 / 운영
- Docker 멀티스테이지 빌드로 이미지 경량화
- GitHub Actions 기반 CI (테스트 자동화) 및 CD (Railway 자동 배포)
- Redis를 이용한 API 응답 캐싱 및 캐시 무효화 전략

### 테스트
- `@DataJpaTest` + H2로 Repository 테스트
- Mockito 기반 Service 단위 테스트 (given/when/then 패턴)
- `@WebMvcTest` + `MockMvc`로 Controller 계층 테스트

### 설계 원칙
- 도메인 기반 패키지 구조
- DTO 패턴으로 엔티티 직접 노출 방지
- IDOR(권한 상승 공격) 방어를 위한 `findByIdAndUser` 패턴
- 정적 팩토리 메서드 패턴, Setter 대신 의도가 드러나는 메서드 사용
