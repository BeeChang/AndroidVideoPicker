## 디바이스 내 비디오를 보여주며 단일선택이 가능한 피커

##### 코드흐름 비디오 목록을 가져와 리사이클러뷰에 보여지는 경우
> VideoPickerFragment ->  VideoViewModel -> VideoRepository -> LocalMediaVideoSource  
> SelectLocalVideoAdapter <- VideoViewModel <- VideoRepository 

##### 개선해야할 점
> 영상이 많은 경우 불러오는데 오래걸림 -> 페이징처리 추가   
> exoplayer사용으로 wmv,mov등 호환이 되지않아 영상 재생이안됨  
> 이어폰 제거 , 페어링 끊김시에 영상 정지 처리 안되어있음

##### 아웃풋 영상 
![ezgif com-gif-maker (2)](https://user-images.githubusercontent.com/59998259/130817035-2467b19a-30e8-4621-b59f-fa25e36c0712.gif)



