
# 🚀 AWS EC2에서 JMeter를 활용한 스트레스 테스트

## 📝 개요
AWS EC2 인스턴스와 JMeter를 사용하여 Spring Boot 애플리케이션의 성능을 평가하는 테스트를 진행합니다. 이 테스트 환경은 **두 개의 Public Subnet**으로 구성되며, 하나의 서브넷에는 **Spring Boot 애플리케이션과 MySQL 데이터베이스**가 배포되고, 다른 서브넷에는 **JMeter**가 설치되어 부하 테스트를 실행합니다. 이러한 구성을 통해 실제 환경과 유사한 조건에서 애플리케이션의 성능을 테스트하고, 성능 병목을 발견하여 시스템 최적화를 수행할 수 있습니다.

## 🛠️ 아키텍처 설명
![image (7)]()
- **VPC 및 두 개의 Public Subnet**: AWS VPC 안에 **두 개의 Public Subnet**이 존재하며, 각각 서로 다른 EC2 인스턴스가 배치되어 있습니다.
  - **Public Subnet 1**: Spring Boot 애플리케이션과 MySQL이 배포된 EC2 인스턴스가 위치합니다.
    - **Spring Boot 애플리케이션**: Java 기반 웹 애플리케이션이 배포되어 외부 사용자 요청을 처리합니다.
    - **MySQL**: Spring Boot 애플리케이션과 연결된 데이터베이스로, 요청 데이터를 저장하고 관리합니다.
  - **Public Subnet 2**: JMeter가 설치된 EC2 인스턴스가 위치합니다.
    - **JMeter**: Subnet 1에 배포된 Spring Boot 애플리케이션에 부하를 가해 성능을 테스트합니다.

## 🎯 목표
- **Spring Boot 애플리케이션**과 **MySQL 데이터베이스**가 EC2 인스턴스에서 적절히 구동되는지 확인합니다.
- **JMeter**를 사용하여 애플리케이션이 고부하 상황에서 어떻게 반응하는지 평가합니다.
- 애플리케이션의 처리량, 응답 시간, 최대 동시 사용자 수 등을 테스트하여 성능을 분석합니다.

## 🔧 필수 요구 사항
- **AWS 계정**
- **EC2 인스턴스** 2개 (하나는 Spring Boot & MySQL, 하나는 JMeter 설치용)
- **VPC** 및 **Public Subnet** 2개
- **Java 8 이상**, **MySQL**, **Apache JMeter**

## ⚙️ 설정 및 구성

### 1. AWS 환경 설정
1. **VPC와 Public Subnet 생성**: 
   - AWS 콘솔을 통해 **VPC**를 생성하고 두 개의 **Public Subnet**을 만듭니다. Subnet 1에는 Spring Boot와 MySQL을, Subnet 2에는 JMeter를 배포합니다.

2. **EC2 인스턴스 생성**:
   - **Public Subnet 1**: Spring Boot 애플리케이션과 MySQL이 구동될 EC2 인스턴스를 생성합니다.
   - **Public Subnet 2**: JMeter를 설치하여 부하 테스트를 수행할 EC2 인스턴스를 생성합니다.

### 2. EC2 인스턴스 설정

#### A. **Spring Boot 애플리케이션 & MySQL 설치 (Public Subnet 1)**
1. **Java 및 MySQL 설치**:
   - EC2 인스턴스에 SSH로 접속한 후, **Java**와 **MySQL**을 설치합니다.
   - 예시 명령어:
     ```bash
     sudo yum install java-1.8.0-openjdk
     sudo yum install mysql-server
     ```

2. **Spring Boot 애플리케이션 배포**:
   - 로컬 환경에서 Spring Boot 애플리케이션을 빌드한 후 EC2 인스턴스에 배포합니다.
   - **MySQL** 데이터베이스와 연결되도록 애플리케이션 설정을 조정합니다.

3. **MySQL 설정**:
   - MySQL에서 필요한 데이터베이스와 테이블을 생성합니다.
   - 애플리케이션이 정상적으로 데이터베이스와 연동되는지 확인합니다.

#### B. **JMeter 설치 및 설정 (Public Subnet 2)**
1. **JMeter 설치**:
   - EC2 인스턴스에 SSH로 접속한 후, **Apache JMeter**를 설치합니다.
   - 설치 명령어:
     ```bash
     sudo yum install java-1.8.0
     wget https://downloads.apache.org//jmeter/binaries/apache-jmeter-5.4.1.tgz
     tar -xvzf apache-jmeter-5.4.1.tgz
     cd apache-jmeter-5.4.1
     ```

2. **JMeter 테스트 플랜 설정**:
   - **Thread Group**: 동시 사용자 수와 반복 횟수를 설정하여 부하 조건을 정의합니다.
   - **HTTP Request**: Subnet 1의 Spring Boot 애플리케이션으로 HTTP 요청을 보냅니다.
   - **Listeners**: 테스트 결과를 수집하고 시각화하는 리스너를 추가합니다 (예: Summary Report).

### 3. 스트레스 테스트 실행
1. **JMeter 테스트 실행**:
   - JMeter CLI 또는 GUI 모드에서 Subnet 1에 있는 Spring Boot 애플리케이션에 부하를 가하는 테스트를 실행합니다.
     - CLI 명령어 예시:
       ```bash
       ./jmeter -n -t test_plan.jmx -l results.jtl
       ```
   - 테스트 중에는 애플리케이션의 응답 시간, 처리량, 에러율 등을 모니터링합니다.

2. **결과 수집 및 분석**:
   - 테스트 결과를 분석하여 애플리케이션의 성능 병목 지점을 파악하고 개선할 방안을 도출합니다.

## 📊 테스트 결과 보고
- 테스트 결과 파일(`.jtl`)을 통해 성능 지표를 분석합니다. JMeter 내장 리포트 생성 기능을 사용하여 HTML 보고서를 생성하고, 결과를 시각화할 수 있습니다.
- **Throughput**, **응답 시간**, **에러율** 등 주요 성능 지표를 확인하여 최적화할 영역을 식별합니다.

### 4. 결론

