document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const remember = document.querySelector('input[name="remember"]').checked;

        // Disable button during login
        const loginButton = loginForm.querySelector('.login-button');
        const originalText = loginButton.textContent;
        loginButton.disabled = true;
        loginButton.textContent = '로그인 중...';

        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password,
                    remember: remember
                })
            });

            if (response.ok) {
                const data = await response.json();

                // Store token if provided
                if (data.token) {
                    if (remember) {
                        localStorage.setItem('authToken', data.token);
                    } else {
                        sessionStorage.setItem('authToken', data.token);
                    }
                }

                // Show success message
                showMessage('로그인 성공!', 'success');

                // Redirect to main page after short delay
                setTimeout(() => {
                    window.location.href = '/index.html';
                }, 1000);

            } else {
                const error = await response.json();
                showMessage(error.message || '로그인에 실패했습니다.', 'error');
                loginButton.disabled = false;
                loginButton.textContent = originalText;
            }
        } catch (error) {
            console.error('Login error:', error);
            showMessage('서버 연결에 실패했습니다.', 'error');
            loginButton.disabled = false;
            loginButton.textContent = originalText;
        }
    });
});

function showMessage(message, type) {
    // Remove existing messages
    const existingMessage = document.querySelector('.message');
    if (existingMessage) {
        existingMessage.remove();
    }

    // Create message element
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.textContent = message;

    // Add styles
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        left: 50%;
        transform: translateX(-50%);
        padding: 15px 30px;
        border-radius: 10px;
        font-weight: 600;
        z-index: 1000;
        animation: slideDown 0.3s ease-out;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    `;

    if (type === 'success') {
        messageDiv.style.background = '#10b981';
        messageDiv.style.color = 'white';
    } else {
        messageDiv.style.background = '#ef4444';
        messageDiv.style.color = 'white';
    }

    document.body.appendChild(messageDiv);

    // Auto remove after 3 seconds
    setTimeout(() => {
        messageDiv.style.animation = 'slideUp 0.3s ease-out';
        setTimeout(() => messageDiv.remove(), 300);
    }, 3000);
}

// Add animation styles
const style = document.createElement('style');
style.textContent = `
    @keyframes slideDown {
        from {
            opacity: 0;
            transform: translate(-50%, -20px);
        }
        to {
            opacity: 1;
            transform: translate(-50%, 0);
        }
    }

    @keyframes slideUp {
        from {
            opacity: 1;
            transform: translate(-50%, 0);
        }
        to {
            opacity: 0;
            transform: translate(-50%, -20px);
        }
    }
`;
document.head.appendChild(style);
