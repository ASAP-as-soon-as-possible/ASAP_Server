### **💁‍♂️ Introduction**

![image](https://github.com/user-attachments/assets/e9d91189-cecd-4f55-8a2b-9bbf4aa1045c)
![image](https://github.com/user-attachments/assets/5ab65502-bdc4-4ab1-955b-9b23c9c5498f)


ASAP은선호도 분석을 통해 최대 인원이 참석 가능한 회의시간을 자동으로 결정해 줌으로써, **다수가 시간을 조율하며 딜레이되는 시간을 줄여주는 서비스** 입니다.

회의 관련 정보를 하나의 큐카드로 정리해 제공함으로써 회의 시간 외 추가 공지사항을 단톡방에 올리는 수고로움 또한 덜어주고자 합니다.

한마디로 ASAP은 **회의를 진행하는 업무시간 외 일련의 사전작업들을 쉽고 빠르고 간편하게 처리해주는 웹서비스**입니다.

### **🚎 Architecture**

![image](https://github.com/user-attachments/assets/cae98903-bc3d-4e4c-b60f-b499747b227f)


### **📈 DataBase Schema**

![image](https://github.com/user-attachments/assets/abdca3ac-b608-44fe-aa9b-c623864b7bee)


### **🗂️ Directory**

```
├─common # 각 계층에서 공통으로 사용하는 유틸리티와 예외 처리 기능을 제공합니다.
│  ├─exception
│  ├─jwt
│  └─utils
├─infra # 외부 API 통합 및 외부 시스템과의 연동 설정을 담당합니다.
│  ├─redis
│  └─slack
├─persistence # 데이터베이스와의 상호작용을 위한 영속성 관련 기능을 제공합니다.
│  ├─config
│  ├─domain
│  └─repository
├─presentation # 사용자의 요청을 처리하고 응답을 반환하는 UI 관련 기능을 제공합니다.
│  ├─common
│  ├─config
│  └─controller
└─service # 애플리케이션의 핵심 비즈니스 로직을 구현합니다.
```

### **👥 Contributors**

| 강원용| 도소현|
|:-----:|:------:|
| [KWY0218](https://github.com/KWY0218) | [sohyundoh](https://github.com/sohyundoh) |
