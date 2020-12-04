# Vue 프로젝트 진행시 사용해본 기술 정리



## 1. [Video.js](https://videojs.com)

> 스트리밍 플레이어	

### 1. 사용방법

1. npm

   ```bash
   # terminal에
   
   npm install --save-dev video.js
   ```

   - 등록 후 스트리밍을 사용할 component에 가서

   ```bash
   <video
   id="my-player"
   class="video-js"
   controls
   preload="auto"
   poster=""
   data-setup='{}'
   >
     <source src="./test.mp4" type="video/mp4">
   </video>
   ```

   - 형식으로 사용.