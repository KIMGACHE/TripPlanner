const express = require('express');
const app = express();

// 정적 파일 제공
app.use(express.static('public'));
app.use('/user', express.static('resources/templates/user')); // 추가 설정

app.listen(9000, () => {
    console.log('Server is running on http://localhost:9000');
});




      // 프로필 사진 업로드
    const profileBoxEl = document.querySelector(".profileBox");
    const profileUploadBtn = document.querySelector(".profileUploadBtn");
    const profileImageInput = document.querySelector("#profileImageInput");
    const profileImageButton = document.querySelector("#profileImageButton");







