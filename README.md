# upload2

MainActivity
- 각 액티비티로 이동

MainActivity2
- UPLOAD FILE(2번째) 버튼 누르면

MainActivity3
- +버튼 누르면 AlertDialog (사진찍기, 갤러리 선택, 취소)
- 갤러리 선택은 ACTION_PICK, 1장씩만
- 업로드 버튼 누르면 서버로 전송

MainActivity4
- 카메라 버튼 기능 없음.
- SELECT 버튼은 Activity5 코드와 중복.

MainActivity5
- Android 11까지 권한 체크
- ACTION_OPEN_DOCUMENT에서 사진 pick, 여러장도 가능. (레이아웃에는 선택한 사진 표시 안함)
- 업로드 버튼 누르면 서버로 전송

MainActivity6
- 카메라로 사진 찍으면 imageView에 표시
- 저장 버튼 누르면 Environment.getExternalStorageDirectory() + "/capture" 폴더에 저장
