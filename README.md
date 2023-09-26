# 🔜 ASAP : AS SOON AS POSSIBLE
최적의 회의시간대를 결정해 회의 이전 불필요한 시간적, 감정적 소비를 줄여주는 웹서비스

# 👨🏻‍💻 Contributors
|  <div align = center>강원용 </div> | <div align = center> 도소현 </div> |
|:----------|:----------|
|<div align = center> <img src = "https://github.com/ASAP-as-soon-as-possible/ASAP_Server/assets/79795051/f3b50777-cc04-4245-af19-826b9054c53f.png" width = "17" height = "17"/> [KWY0218](https://github.com/KWY0218) </div> |<div align = center> <img src = "https://github.com/ASAP-as-soon-as-possible/ASAP_Server/assets/79795051/f3b50777-cc04-4245-af19-826b9054c53f.png" width = "17" height = "17"/> [sohyundoh](https://github.com/sohyundoh) </div>|
| <img src = "https://github.com/ASAP-as-soon-as-possible/ASAP_Server/assets/79795051/08d4e1e6-9f8d-4607-9394-4f58259a6e49.png" width = "250" height = "380"/>| <img src = "https://github.com/ASAP-as-soon-as-possible/ASAP_Server/assets/79795051/5d82b08d-9970-418f-8f9a-70d634839e30.png" width = "250" height = "380"/> |
| [참여자 : 회의 가능 시간 뷰] <br/> 참여자 정보 및 회의 가능 시간 입력 API    |[링크 입장 뷰] 유효한 회의 확인 API,<br/> 토큰 유효성 확인 API |
| [방장으로 입장] 방장 정보 입력 API  | [방장 : 회의 가능 시간 뷰] <br/> 회의 가능 시간 입력 API|
| [방장 뷰] 최적의 회의시간 API   | [방장 뷰] 종합 일정 시간표 API  |
| [방장 뷰] 회의 시간 확정 API| [회의 생성 뷰] 회의 생성 API |
| [방장 뷰] 큐카드 정보 불러오는 API  |[가능 시간 입력 뷰] 선택지 제공 |

# 💼 Server Architecture
![image](https://github.com/ASAP-as-soon-as-possible/ASAP_Server/assets/79795051/d1aae75e-4ce9-4b1e-9b70-8a5ea07e37e2)

# 🗂️ Directory
```
├── java
│   └── com
│       └── asap
│           └── server
│               ├── common
│               │   ├── advice
│               │   ├── dto
│               │   └── utils
│               ├── config
│               │   ├── jwt
│               │   ├── resolver
│               │   │   ├── meeting
│               │   │   └── user
│               │   └── swagger
│               ├── controller
│               ├── domain
│               │   └── enums
│               ├── exception
│               │   └── model
│               ├── repository
│               └── service
└── test
```
