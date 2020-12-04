# git-jira연동

> git과 jira 프로젝트는 생성된 상태에서 진행한다고 가정하고 설명.

1. git의 설정창으로 이동

![image-20200902091838478](C:\Users\multicampus\Desktop\SSAFY\2.Specialization\blockchain-skeleton\WhatTodo\image-20200902091838478.png)

2. 스크롤을 내리다 보면 ![image-20200902091911219](C:\Users\multicampus\Desktop\SSAFY\2.Specialization\blockchain-skeleton\WhatTodo\image-20200902091911219.png)를 확인 할 수 있고 이를 클릭.

![image-20200902091958527](C:\Users\multicampus\Desktop\SSAFY\2.Specialization\blockchain-skeleton\WhatTodo\image-20200902091958527.png)

3. Active 선택
4. Web URL: 지라 base URL을 입력(ex. https://jira.ssafy.com) 
5. Username or Email: ID로 입력(Email X) 
6. Enter new password or api token: 비밀번호 입력 
   - _초기 진행이라면 지라에 등록된 비밀번호를 변경하고 그 변경한 비밀번호를 입력하길 권장._
7. Transition ID(s): admin 권한이 없다면 바꿀 수 없고 [요기](https://community.atlassian.com/t5/Jira-questions/How-to-fine-transition-ID-of-JIRA/qaq-p/1207483)를 참고하여 ID확인이 가능.
8. Test setting and save changes 클릭 후
9. ![image-20200902092425080](C:\Users\multicampus\Desktop\SSAFY\2.Specialization\blockchain-skeleton\WhatTodo\image-20200902092425080.png)이렇게 되면 끝
10. 커밋을 진행해 문제가 없는지 확인한다.