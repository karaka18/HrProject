<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>HR Genie</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
    <script src="<c:url value='/resources/js/script.js' />"></script>
</head>
<body>
    <header class="main-header">
        <div class="logo">
            <a href="<c:url value='/user/main' />">
                <h1>HR Genie</h1>
            </a>
        </div>
        <div class="user-info">
            <div class="notification">
                <a href="<c:url value='/notifications' />">
                    <img src="<c:url value='/resources/img/bell.png' />" alt="알림">
                </a>
            </div>
            <span class="login-time" id="countdown-timer" data-login-time="${loginTime}">
			    로그인시간 60:00
			</span>
            <a href="<c:url value='/member/logout' />" class="logout-btn" onclick="alert('로그아웃 되었습니다.')">로그아웃</a>
        </div>
    </header>
    <div class="container">
    
    <!-- 로그인 시간 전달용 hidden -->
   <%--  <input type="hidden" id="loginTimeValue" value="${loginTime}" /> --%>
    
    <script>
    window.addEventListener('DOMContentLoaded', function() {
        getLoginTimeAndStartTimer();
    });

    function getLoginTimeAndStartTimer(){
        fetch("/common/time", {
            method: "GET"
        })
        .then(function(res) {
            return res.json();
        })
        .then(function(data) {
            const loginTimeStr = data.result.time;
            if (!loginTimeStr) return;

            const timerEl = document.getElementById('countdown-timer');
            const logoutAfterMs = 60 * 60 * 1000;
            let expireTime = new Date(new Date(loginTimeStr).getTime() + logoutAfterMs);
            
         	// 클릭 이벤트 등록 - 클릭하면 expireTime을 현재 시간 + 1시간으로 리셋
            timerEl.addEventListener('click', function () {
                expireTime = new Date(Date.now() + logoutAfterMs);
                alert('세션 시간이 60분으로 연장되었습니다.');
            });

            function updateTimer() {
                const now = new Date();
                const diff = expireTime - now;

                if (diff <= 0) {
                    timerEl.textContent = '자동 로그아웃 되었습니다.';
                    alert("1시간이 지나 자동 로그아웃되었습니다.");
                    window.location.href = '<c:url value="/member/logout" />';
                    return;
                }

                const minutes = String(Math.floor(diff / (60 * 1000))).padStart(2, '0');
                const seconds = String(Math.floor((diff % (60 * 1000)) / 1000)).padStart(2, '0');
                timerEl.textContent = "로그인 남은시간 " + minutes + ":" + seconds;
            }

            // 🔁 1초마다 실행
            setInterval(() => updateTimer(), 1000);
            updateTimer();
         	
        })
        .catch(function(err) {
            console.error(err);
            alert("로그인 시간 불러오기 실패");
        });
    }
	
	
	
	</script>