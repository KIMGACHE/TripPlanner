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
            console.log(res);
            alert('로그인성공');
        },
       error: function(xhr){
            console.log(xhr)
            alert("로그인실패 : " );

       }
    });
})