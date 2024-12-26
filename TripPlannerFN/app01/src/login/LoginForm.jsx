import { useState } from "react";
import axios from "axios";


const LoginForm = () =>{
    const [formData , setFormData] = useState({userid:'',password:''});
    const [error,setError] = useState(null);

    //입력 값 변경 처리
    const handleChange = (e)=>{
        const {name,value} = e.target;
        setFormData((prev)=>({...prev,[name]: value,}));
    };

    //로그인 요청
    const handleSubmit = async (e) =>{
        
    }



}