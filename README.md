# Bookie-Scrap

웹 스크래핑을 통해 도서 관련 데이터를 수집하는 Spring Boot 애플리케이션입니다.

## 📋 프로젝트 개요

Bookie-Scrap은 Watcha와 같은 플랫폼에서 도서 관련 데이터를 수집하고 MongoDB에 저장하는 스크래핑 애플리케이션입니다. 도서 메타데이터, 댓글, 평점, 사용자 정보 등 다양한 데이터를 체계적으로 수집합니다.

## 🏗️ 아키텍처

### 주요 컴포넌트

- **ScrapApp**: 메인 애플리케이션 클래스 (CommandLineRunner 구현)
- **WatchaCollectorService**: Watcha 플랫폼 데이터 수집 서비스
- **MongoDB 저장소**: 수집된 데이터 저장을 위한 NoSQL 데이터베이스

### 데이터 수집 도메인

#### 📚 Book (도서)
- **BookMeta**: 도서 메타데이터 (제목, 저자, 출판사 등)
- **BookComment**: 도서 리뷰 및 댓글
- **BookToDecks**: 도서와 컬렉션 간의 관계

#### 📑 Deck (컬렉션)
- **Deck**: 도서 컬렉션 정보

#### 👤 User (사용자)
- **UserBookRating**: 사용자 도서 평점
- **UserInfo**: 사용자 정보
- **UserLikePeople**: 사용자 팔로우 관계
- **UserWishBook**: 사용자 위시리스트
- **WithLoginUserLikeDeck**: 로그인 사용자의 좋아요 컬렉션

## 🛠️ 기술 스택

- **Java 17**
- **Spring Boot** (CommandLineRunner)
- **Spring WebFlux** (비동기 웹 요청)
- **MongoDB** (데이터 저장소)
- **Lombok** (코드 간소화)
- **Gradle** (빌드 도구)

## 📁 프로젝트 구조
```
src/main/java/com/bookie/scrap/
├── ScrapApp.java # 메인 애플리케이션
├── common/ # 공통 컴포넌트
│ ├── domain/ # 공통 도메인
│ └── util/ # 유틸리티 클래스
└── watcha/ # Watcha 플랫폼 스크래핑
    ├── domain/ # 도메인 서비스
    │ ├── WatchaCollectorService.java # 수집 인터페이스
    │ ├── WatchaFetcherFactory.java # API 요청 팩토리
    │ └── WatchaHeaders.java # HTTP 헤더
    └── request/ # 요청 처리
    ├── book/ # 도서 관련
    │ ├── bookcomment/ # 도서 댓글
    │ ├── bookmeta/ # 도서 메타정보
    │ └── booktodecks/ # 도서-컬렉션 매핑
    ├── deck/ # 컬렉션 관련
    └── user/ # 사용자 관련
        ├── userinfo/ # 사용자 정보
        ├── userbookrating/ # 도서 평점
        ├── userlikepeople/ # 팔로우 관계
        └── userwishbook/ # 위시리스트
```
### 설정 (application.yml)

#### Quartz 스케줄러
- **스레드 풀**: 최대 10개 동시 작업 실행
- **작업 저장소**: 메모리 기반 (빠른 처리, 재시작 시 초기화)
- **안전 종료**: 실행 중인 작업 완료 후 종료

#### 스크래핑 설정
- **HTTP 재시도**: 실패 시 최대 3회 재시도
- **요청 지연**: 5초 간격으로 http 요청 실행
- **스케줄링**: 매달 1일마다 스크래핑 작업 실행

#### 작업 관리
- **동적 작업 등록**: YAML 설정으로 새 작업 추가 가능
- **작업 제어**: enabled 플래그로 개별 작업 활성화/비활성화
- **서버 식별**: 분산 환경 지원을 위한 서버 ID

## 📊 데이터 수집 프로세스

1. **Fetcher**: 웹 요청을 통해 데이터 수집
2. **Document**: 수집된 데이터를 MongoDB 문서 형태로 변환
3. **Persister**: MongoDB에 데이터 저장
4. **Repository**: 데이터 접근 계층

각 도메인별로 위 프로세스가 구현되어 있어 체계적인 데이터 수집이 가능합니다.

## 📝 라이선스

이 프로젝트는 [MIT License](LICENSE) 하에 배포됩니다.

## ⚖️ 면책 조항

- 이 도구는 교육 및 연구 목적으로만 사용되어야 합니다
- 사용자는 해당 웹사이트의 이용약관 및 robots.txt를 준수해야 합니다
- 이 도구 사용으로 인한 모든 법적 책임은 사용자에게 있습니다
- 과도한 요청으로 인한 서버 부하 방지를 위해 적절한 지연 시간을 설정하세요
- 수집된 데이터의 저작권 및 개인정보 처리는 관련 법규를 준수하세요