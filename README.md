# 🌊 VODA (Backend Server)

> **한 줄 소개:** 사용자의 감정을 기록하는 AI 다이어리 서비스 VODA의 백엔드 서버입니다.]

<br>

## 🛠 Tech Stack
<div align=left>
  <img src="https://img.shields.io/badge/java-21-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-3.3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  </div>

<br>

## 🚀 Key Features (핵심 기능)

이 프로젝트는 **Spring Security**를 활용한 안전한 인증/인가 시스템 구축을 목표로 합니다.

* **Authentication (인증)**
    * 일반 이메일 로그인/회원가입 (BCrypt 암호화 적용)
    * OAuth 2.0 소셜 로그인 연동 (Kakao, Google, Apple)
* **User Management (회원 관리)**
    * 회원 등급(Role) 기반 접근 제어 (사용자/관리자)
    * 프로필 및 캐릭터 설정 기능
* **Customer Service (고객 지원)**
    * 1:1 문의 내역 관리 및 조회

<br>

## 💾 ERD (Entity Relationship Diagram)

프로젝트의 데이터베이스 설계 구조입니다.

![ERD Image](./images/erd_screenshot.png)

* **User Table:** 소셜 로그인 정보(`SocialID`, `SocialType`)와 일반 회원 정보를 통합 관리하도록 설계하였습니다.

<br>


## 🤝 Contribution Guide (Commit Convention)
| 태그 (Tag) | 설명 (Description) | 예시 (Example) |
| :--- | :--- | :--- |
| **`feat`** | **새로운 기능 추가** | 로그인 API 개발, 일기 저장 기능 추가 |
| **`fix`** | **버그 수정** | NullPointException 해결, 오타 수정 |
| `docs` | 문서 수정 | README.md 수정, 주석 추가 |
| `style` | 코드 스타일 변경 | 세미콜론 누락, 들여쓰기 정렬 (로직 변경 X) |
| `refactor` | 코드 리팩토링 | 변수명 변경, 코드 구조 개선 (기능 변경 X) |
| `test` | 테스트 코드 | 테스트 코드 추가 및 수정 |
| `chore` | 기타 변경사항 | build.gradle 라이브러리 추가, 설정 변경 |

<br>
## 📂 Directory Structure

현업 표준을 지향하는 도메인형 패키지 구조를 채택했습니다.

```text
src/main/java/org/delivery/voda
├── global          # 전역 설정 (Config, Error, Common)
├── security        # 인증/인가 관련 핵심 로직 (Filter, Provider)
└── domain          # 비즈니스 도메인
    ├── user        # 사용자 (Controller, Service, Repository, Entity)
    └── ...