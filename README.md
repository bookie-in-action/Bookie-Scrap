# Bookie-Scrap

이 프로젝트는 Docker Compose 를 사용하여 테스트 해볼 수 있습니다.
> 저작권 등의 이유로 .jar 파일은 레포에 포함되어 있지 않습니다. <br>
> 요청시 .jar은 별도로 제공되며, 필요 시 server/lib 디렉토리에 수동으로 위치시키면 됩니다.<br>
```bash 
docker-compose -f docker-compose.yml up --build
```

## 📋 프로젝트 개요

Bookie-Scrap은 Watcha와 같은 플랫폼에서 도서 관련 데이터를 수집하고 MongoDB에 저장하는 스크래핑 애플리케이션입니다. 도서 메타데이터, 댓글, 평점, 사용자 정보 등 다양한 데이터를 체계적으로 수집합니다.

## 🏗️ 아키텍처

### 주요 컴포넌트

- **ScrapApp**: 메인 애플리케이션 클래스 (CommandLineRunner 구현)
- **ScraperJob**: Quartz 스케줄러를 통한 작업 실행 및 관리
- **WatchaCollectorService**: Watcha 플랫폼 데이터 수집 서비스
- **이중 저장소 아키텍처**:
  - **MongoDB**: 원시 데이터 저장을 위한 NoSQL 데이터베이스
  - **JPA/RDB**: 구조화된 데이터 저장을 위한 관계형 데이터베이스
- **Redis 서비스**:
  - **RedisHashService**: 작업 상태(성공/실패) 관리
  - **RedisStringListService**: 대기 작업 관리
  - **RedisProcessResult**: 처리 결과 및 오류 정보 저장
- **예외 처리 메커니즘**: 
  - **CollectionEx**: 기본 수집 예외 처리
  - **RetriableCollectionEx**: 재시도 가능한 예외 자동 관리
  - **WatchaCustomCollectionEx**: Watcha 플랫폼 전용 예외 처리

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
- **MongoDB** (NoSQL 데이터 저장소)
- **Spring Data JPA** (관계형 데이터베이스 연동)
- **Redis** (캐싱 및 데이터 저장)
- **Lombok** (코드 간소화)
- **Gradle** (빌드 도구)

## 📁 프로젝트 구조
```
src/main/java/com/bookie/scrap/
├── ScrapApp.java # 메인 애플리케이션
├── common/ # 공통 컴포넌트
│ ├── domain/ # 공통 도메인
│ ├── exception/ # 예외 처리 클래스
│ │ ├── CollectionEx.java # 기본 수집 예외
│ │ ├── RetriableCollectionEx.java # 재시도 가능한 예외
│ │ └── WatchaCustomCollectionEx.java # Watcha 전용 예외
│ ├── redis/ # Redis 관련 클래스
│ │ ├── RedisHashService.java # Redis Hash 작업 서비스
│ │ ├── RedisProcessResult.java # 처리 결과 저장 객체
│ │ └── RedisStringListService.java # Redis 문자열 리스트 서비스
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

1. **Fetcher**: WebFlux를 활용한 비동기 웹 요청으로 데이터 수집
2. **Persister**: 트랜잭션 관리 하에 MongoDB와 RDB에 데이터 동시 저장
3. **Repository**: MongoDB와 JPA 기반 데이터 접근 계층

### 기술적 특징
- **예외 처리 전략**: 
  - 커스텀 예외 클래스(CollectionEx, RetriableCollectionEx, WatchaCustomCollectionEx)를 통한 체계적 오류 관리
  - 데이터베이스 연결 실패 시 자동 재시도 메커니즘
  - 예외 유형별 차별화된 처리 로직 구현
- **이중 저장소 전략**: 
  - MongoDB: 유연한 스키마로 원시 데이터 저장
  - RDB: 구조화된 데이터 분석 및 조회 최적화
- **Redis 활용**: 
  - 빈번한 요청 데이터 캐싱으로 성능 최적화
  - 작업 상태 관리(성공, 실패, 대기)를 위한 RedisHashService 구현
  - 처리 결과 및 오류 정보 저장을 위한 RedisProcessResult 활용

각 도메인별로 위 프로세스가 구현되어 있어 체계적이고 안정적인 데이터 수집이 가능합니다.

## 📝 라이선스

이 프로젝트는 [MIT License](LICENSE) 하에 배포됩니다.

## ⚖️ 면책 조항

- 이 도구는 교육 및 연구 목적으로만 사용되어야 합니다
- 사용자는 해당 웹사이트의 이용약관 및 robots.txt를 준수해야 합니다
- 이 도구 사용으로 인한 모든 법적 책임은 사용자에게 있습니다
- 과도한 요청으로 인한 서버 부하 방지를 위해 적절한 지연 시간을 설정하세요
- 수집된 데이터의 저작권 및 개인정보 처리는 관련 법규를 준수하세요
