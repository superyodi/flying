# Flying ✈️: pomodoro timer 

<img width="1375" alt="스크린샷 2021-08-26 오후 3 33 39" src="https://user-images.githubusercontent.com/31922389/130913889-bbc62a2a-cee7-4061-a48e-9c8930dbecd0.png">

>  뽀모도로 타이머로 떠나는 세계여행~ ✈️



재미있게 달성하는 하루 목표, 뽀모도로 어플리케이션 플라잉과 함께 비행해요 



*Flying*은 

+ 카카오 아이디만 있으면 누구나 이용할 수 있어요

+ 뽀모도로 타이머에요 
  + 25분 집중하고 5분 쉬는걸 4번 반복하면 15분 쉴 수 있어요 

+ 집중 시간만큼 비행해요
  + <u>제주도에서 달까지</u>, 2시간 단위로 도시가 바껴요 
  + 도시에 도착할때마다 티켓이 발급돼서 집중 시간에 처리한 작업들을 직관적으로 느낄 수 있어요 
+ To do list에요 
  + 간편하게 그날 해야 할 작업을 만들고 수정할 수 있어요 
    + 매일 새벽 5시에 초기화돼요
  + `매일매일`
    + 목표일까지 매일매일 해야하는 작업이에요
      + 목표일이 지나면 리스트에서 삭제돼요
      + 24시간이 지나면 작업 횟수가 초기화돼요
  + `오늘 하루만 `
    + 오늘 하루동안 해야 할 작업이에요
    + 24시간이 지나면 리스트에서 삭제돼요 
+ 사용자 취향에 따라 설정할 수 있어요
  + `집중시간`,` 짧은 휴식`, `긴 휴식`, `긴 휴식 간격`을 커스텀할 수 있어요
  + `자동으로 타이머 시작`, `자동으로 휴식 시작`, `휴식없이 무한 질주`를 선택할 수 있어요 





## Preview



> 상황별 미리보기 

| <img src="https://user-images.githubusercontent.com/31922389/132655362-86c4f065-dff1-43b6-9238-f61d5b9cc4f2.gif" width="400px"> | <img src="https://user-images.githubusercontent.com/31922389/132668814-f501c162-492b-49a8-976f-503d89dd3609.gif" width="400px"> | <img src="https://user-images.githubusercontent.com/31922389/132669301-7dcbcca6-a536-4e9b-84ca-76d2b2b1fd68.gif" width="400px"> | <img src="https://user-images.githubusercontent.com/31922389/132669854-6a4e1306-ad53-497b-98fa-50b06fad0817.gif" width="400px"> |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|                           *Log in*                           |                           *Setup*                            |                       *Pomodoro list*                        |                      *Delete Pomodoro*                       |
| <img src="https://user-images.githubusercontent.com/31922389/132667436-ce1dee00-1dcd-4d65-9edf-9f205216564d.gif" width="400px"> | <img src = "https://user-images.githubusercontent.com/31922389/132668296-b7ed0d88-51a9-41c6-8b8c-d5c12fd28c45.gif" width="400px"> | <img src = "https://user-images.githubusercontent.com/31922389/132789677-5d051d0c-e1ff-4ae4-9d24-98269bd75c2d.gif" width="400px"> | <img src = "https://user-images.githubusercontent.com/31922389/132793092-bb7cac16-8daf-4132-80a2-2c7121236096.gif" width="400px"> |
|                      *Insert Pomodoro*                       |                      *Update Pomodoro*                       |                         *Timer View*                         |                     *Timer Notification*                     |
| <img src="https://user-images.githubusercontent.com/31922389/132791067-74df6d6e-c86d-444c-a371-dbf62305ea7d.gif" width="400px"> |                                                             | <img src="https://user-images.githubusercontent.com/31922389/132794867-5c08fddd-b725-4ae0-bbb2-f56ce1f59c44.gif" width="400px">                                                              |                                                              |
|                         *Preference*                         |                        *Ticket list*                         |                       *Various cities*                       |                                                              |

<br>




## Architecture



<img width="597" alt="스크린샷 2021-09-10 오후 12 37 53" src="https://user-images.githubusercontent.com/31922389/132795733-8fd66b90-8aa5-461d-baba-53dd71c84aa0.png">



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

+ 기능

  + 리포트 화면
  + 알람 기능 
  + 백업 기능

+ 코드 개선 

  + DI
  + Test code 

  

  