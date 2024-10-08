# 🚀 AWS EC2에서 JMeter를 활용한 스트레스 테스트

## 📝 개요
AWS EC2 인스턴스와 JMeter를 사용하여 Spring Boot 애플리케이션의 성능을 평가하는 테스트를 진행합니다. 이 테스트 환경은 **두 개의 Public Subnet**으로 구성되며, 하나의 서브넷에는 **Spring Boot 애플리케이션과 MySQL 데이터베이스**가 배포되고, 다른 서브넷에는 **JMeter**가 설치되어 부하 테스트를 실행합니다. 이러한 구성을 통해 실제 환경과 유사한 조건에서 애플리케이션의 성능을 테스트하고, 성능 병목을 발견하여 시스템 최적화를 수행할 수 있습니다.

## 🛠️ 아키텍처 설명

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
- **Java 8**, **MySQL**, **Apache JMeter**

## 🔧 로컬 환경 vs AWS 환경 설정 및 구성

### 1. AWS 환경에서의 메모리 제한
- **Spring Boot 애플리케이션**은 메모리 사용량이 많아 **AWS EC2 인스턴스**에서 가용 리소스에 따라 성능 저하가 발생할 수 있습니다. **AWS EC2**의 메모리 크기는 로컬 환경에 비해 상대적으로 제한적일 수 있으므로, 이를 테스트하여 시스템의 안정성과 성능을 비교합니다.

### 2. 로컬 환경에서의 비교
- 로컬 환경에서는 메모리 리소스가 상대적으로 더 자유로우며, AWS에 비해 애플리케이션 성능이 더 원활하게 작동할 수 있습니다.

## 📊 비교 분석

### AWS EC2 환경 결과:

#### 메모리 사용량:
- **Spring Boot 애플리케이션**은 AWS에서 제한된 메모리 환경에서 실행되며, 이로 인해 메모리 사용량이 높은 상황에서는 성능 저하가 발생할 수 있습니다.
- **AWS EC2 인스턴스**의 메모리 제약으로 인해 **GC(Garbage Collection)** 발생 빈도가 높아져 응답 시간이 길어졌고, 성능에 부정적인 영향을 미쳤습니다.

#### 응답 시간 및 처리량:
- 메모리 사용량이 높아질수록 AWS에서의 **응답 시간**이 점차 느려졌으며, **처리량** 역시 감소했습니다. 이는 인스턴스 메모리 제약으로 인해 발생하는 성능 병목 때문입니다.

### 로컬 환경 결과:

#### 메모리 사용량:
- 로컬 환경에서는 시스템 메모리가 상대적으로 많아 **Spring Boot 애플리케이션**이 부드럽게 동작했습니다.
- AWS에 비해 **GC 발생 빈도**가 적었고, 애플리케이션이 안정적으로 구동되었습니다.

#### 응답 시간 및 처리량:
- 로컬 환경에서는 **응답 시간**이 짧고, **처리량**이 높았습니다. 메모리 사용량이 증가해도 성능 저하가 AWS보다 적게 나타났습니다.

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


## 🚀 EC2 기반 Java 애플리케이션 및 JMeter 부하 테스트 트러블슈팅 🔧

### 1. Java 버전 호환성 문제 해결 🔄
### 문제
- JMeter는 Java 8 또는 11 버전과 호환됩니다. EC2 인스턴스에는 Java 17을 설치 해놨어서 JMeter 실행 시 호환성 문제 발생.

### 해결 방법
- 기존 Java 17 버전을 제거하고, Java 8 버전을 새로 설치하여 문제 해결.

```bash
# 기존 Java 17 제거
sudo yum remove java-17-openjdk

# Java 8 설치
sudo yum install java-1.8.0-openjdk
```
- **결과**: JMeter가 정상적으로 실행되며 부하 테스트가 가능해짐.

### 2. 메모리 부족 문제 해결 🧠
### 문제
- EC2 프리 티어 인스턴스를 사용하여 메모리가 부족한 상황 발생. JMeter는 최소 1GB 이상의 메모리가 필요하여 설치 및 실행에 어려움 발생.

### 해결 방법
- Swap 메모리를 설정하여 메모리 부족 문제 해결.

```bash
# Swap 메모리 생성 (예: 2GB)
sudo dd if=/dev/zero of=/swapfile bs=1M count=2048
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# Swap 메모리 설정 확인
sudo swapon -s
```
- **결과**: 메모리 부족 문제 해결 및 JMeter 정상 설치 및 실행.

### 3. 보안 그룹 설정 🔒
### 문제
- 자바 애플리케이션이 8080 포트에서 실행되지만, EC2 인스턴스의 보안 그룹에 8080 포트가 허용되지 않아 외부에서 접근 불가.

### 해결 방법
1. AWS Management Console에 접속하여 해당 EC2 인스턴스의 보안 그룹을 선택.
2. 인바운드 규칙(Inbound Rules)에 8080 포트를 허용하는 규칙 추가.

    - 유형: Custom TCP Rule
    - 포트 범위: 8080
    - 소스: 0.0.0.0/0 (또는 접근을 허용할 IP 범위)
  
- **결과**: 외부에서 8080 포트로 자바 애플리케이션에 접근 가능해짐.

### 4. VS Code에서는 JMeter 실행 불가, MobaXterm에서 가능 ⚠️
### 문제
- `./JMeter` 명령어로 JMeter GUI를 띄우려고 했으나, VS Code의 터미널에서는 실행되지 않음.
- 이는 EC2 인스턴스의 X11 포워딩 설정이 VS Code 환경에서는 제대로 작동하지 않기 때문으로, GUI 프로그램을 실행할 수 없음.

### 해결 방법
- MobaXterm은 X11 포워딩과 VNC 서버 기능을 기본 제공하여, GUI 프로그램을 손쉽게 실행할 수 있음.
- MobaXterm을 사용하여 EC2 서버에 접속하고, JMeter GUI를 정상적으로 실행하여 문제 해결.

```bash
# MobaXterm 접속 후 JMeter 실행
./JMeter
```
- **결과**: JMeter GUI 실행 및 테스트 환경 구축 완료.

---

### 💡 참고 사항
- 위의 트러블슈팅 과정은 EC2 프리 티어 환경에서 Java 애플리케이션과 JMeter를 함께 사용하는 환경에서 발생할 수 있는 문제들을 해결하는 방법을 포함하고 있습니다.
- 각 문제의 원인과 해결 방법을 자세히 설명하여, 유사한 문제 발생 시 빠르게 대응할 수 있도록 구성하였습니다.

🔗 **[GitHub Repository](https://github.com/your-repository-link)**에서 자세한 내용을 확인하세요!


## 📝 결론
**AWS EC2 인스턴스**에서 **JMeter**를 활용하여 **Spring Boot 애플리케이션**의 성능을 평가하는 스트레스 테스트를 수행했습니다.
두 개의 **Public Subnet**을 사용한 아키텍처는 실제 운영 환경과 유사한 부하를 시뮬레이션하여 시스템 성능 병목을 파악하고 개선할 수 있었습니다.
