$("#login-button").on('click',function(e){
    e.preventDefault();
    const data = {
        userid: $('#userid').val(),
        password: $('#password').val(),
        rememberMe : $('#remember-me').is(':checked') //체크 여부 추가
    };

    //AJAX 요청
    $.ajax({
        url: '/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(res){
                alert('로그인성공');
                console.log(res);
                console.log('Access Token :',getCookie('accessToken'));
//                window.location.href = '/'; //메인페이지로 리다이렉트
        },
       error: function(xhr){
            if(xhr.status ===400){
                try{
                   const errorMessage = JSON.parse(xhr.responseText);
                   alert(errorMessage.message);
                }catch(e){
                    alert('로그인 실패 : 알 수 없는 오류');
                    console.log(e);
                }
            }else{alert('로그인 실패');}

       }
    });
});

// 엑세스 토큰 만료시 리프레시 토큰 사용해서 새 토큰 요청
function handleTokenExpiration(){
    $.ajax({
        url: '/refresh', //엑세스 토큰 재발급 엔드포인트
        type: 'POST',
        success: function (res){
            alert('새로운 엑세스 토큰이 발급되었습니다.');
            console.log(res.accessToken);
        },
        error : function(xhr){
            alert('리프레시 토큰이 만료되었습니다. 다시 로그인하세요');
            window.location.href = '/login'; //로그인 페이지로 리다이렉트
        }
    })
}

// 쿠키 정보 가져오기
function getCookie(name){
    const cookies = document.cookie.split('; ');
    for(let cookie of cookies){
        const [key , value] = cookie.split('=');
        if(key === name) return value;
    }
    return null;
}