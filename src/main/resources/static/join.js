const profileBoxEl = document.querySelector(".profileBox");
const profileUploadBtn = document.querySelector(".profileUploadBtn");
const profileImageInput = document.querySelector("#profileImageInput");

// 업로드 버튼 클릭 시, 파일 선택 창 열기
profileUploadBtn.addEventListener("click", function () {
    profileImageInput.click(); // 이미지 업로드 입력 클릭
});

// 파일이 선택되면 이미지 업로드
profileImageInput.addEventListener("change", function (event) {
    const file = event.target.files[0]; // 선택한 파일

    if (file && file.type.startsWith("image/")) {
        const formData = new FormData();
        formData.append("profileImage", file); // FormData에 이미지 파일 추가

        // 서버로 파일 전송 (이미지 저장)
        axios
            .post('/user/join', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            })
            .then(function (response) {
                console.log('Image uploaded successfully');
                // 업로드한 이미지 미리 보기
                const imgTg = document.createElement('img');
                imgTg.src = URL.createObjectURL(file); // 업로드한 이미지 URL을 생성
                imgTg.style = "width:100%;height:100%;border-radius:50%;";
                profileBoxEl.innerHTML = ''; // 기존 이미지를 제거
                profileBoxEl.appendChild(imgTg);
            })
            .catch(function (error) {
                console.error('Error uploading image', error);
            });
    }
});

// 생년월일 입력값 숫자만 허용
function onlyNumber() {
    const reg = /\D/g;
    event.target.value = event.target.value.replace(reg, "");
}

let date = document.querySelector("#date");

// 문자열, 하이픈을 막기 위해 input event 사용
date.addEventListener("input", () => {
    let val = date.value.replace(/\D/g, "");
    let leng = val.length;
    let result = '';

    if (leng < 6) result = val;
    else if (leng < 8) {
        result += val.substring(0, 4);
        result += "-";
        result += val.substring(4);
    } else {
        result += val.substring(0, 4);
        result += "-";
        result += val.substring(4, 6);
        result += "-";
        result += val.substring(6);
    }
    date.value = result;
});
