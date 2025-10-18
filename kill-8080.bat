@echo off
echo 포트 8080을 사용하는 프로세스를 찾는 중...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do (
    echo PID %%a 종료 중...
    taskkill /F /PID %%a
)
echo 완료!
pause