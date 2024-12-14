$("#login-button").on('click',function(e){
    e.preventDefault();
    const data = {
        userid: $('#userid').val(),
        password: $('#password').val()
    };

    $.ajax({
        url: '/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(res){
                alert('로그인성공');
                console.log(res);
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
})