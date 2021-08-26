# Flying : pomodoro timer🍅 

<img width="1375" alt="스크린샷 2021-08-26 오후 3 33 39" src="https://user-images.githubusercontent.com/31922389/130913889-bbc62a2a-cee7-4061-a48e-9c8930dbecd0.png">

>  뽀모도로 타이머로 떠나는 세계여행~ ✈️

#### (work-in-progress 👷🔧️👷‍♀️⛏)



+ AAC(Android Architecture Components)를 적극 활용한 MVVM 디자인패턴의 뽀모도로 타이머
+  [InTimeAndroid](https://github.com/p-hlp/InTimeAndroid)을 참고해서 타이머 기능을 구현했습니다. 





## Overview







## Features

+ 스플레쉬 화면

  + user id 확인하고 결과에 따라 이동
  + 24시간마다 뽀모도로 데이터 초기화 

+ 로그인 화면

+ 뽀모도로 생성 화면

+ 뽀모도로 리스트 화면 
  
  + 해당 리스트 스와이프하면 삭제 가능 
  
+ 뽀모도로 보기 및 수정 화면

+ 타이머 화면
  + 타이머 1회 완료하면 짧은 휴식 1회
  + 타이머 n(user setting)회 완료하면 긴 휴식 1회
  
+ 사용자 설정 화면

  + Running time, Short break time, Long break time, Long break term 설정 

  + 타이머 Flag 설정 

    +  "자동으로 뽀모도로 넘김", "자동으로 휴식 시작", "휴식없이 무한 질주"


+ 티켓 리스트 화면

  + 사용자가 하루동안 여행한 도시와 각 도시에서 실행했던 뽀모도로 task들을 보여줌 





## Architecture



<img width="617" alt="스크린샷 2021-08-26 오후 3 24 19" src="https://user-images.githubusercontent.com/31922389/130913949-a38c0bed-8ea9-4f32-b52e-d97fa544d72b.png">



+ Single Activity Application + Navigation Architecture Component
+ MVVM design pattern
+ Room database
+ Service 







## Used Libraries

- [Navigation](https://developer.android.com/topic/libraries/architecture/navigation) (Fragment transitions)

- [View Binding](https://developer.android.com/topic/libraries/view-binding) (Bind views)

- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) (Store and manage UI-related data)

- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) (Observable data)

- [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) (Light-weight threads)

- [Room](https://developer.android.com/topic/libraries/architecture/room) (Abstraction layer over SQLite)

  

## 👷Under Development🚧

+ Report feature

+ Notification

+ Duedate Alarm

  