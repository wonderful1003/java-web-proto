// Check authentication on page load
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    setupCalculator();
});

function checkAuth() {
    const token = localStorage.getItem('authToken') || sessionStorage.getItem('authToken');

    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    // In a real app, validate token with server
    // For now, just assume it's valid
    displayUsername();
}

function displayUsername() {
    const token = localStorage.getItem('authToken') || sessionStorage.getItem('authToken');
    // In production, decode JWT or fetch user info from server
    const usernameElement = document.getElementById('username');
    if (usernameElement) {
        usernameElement.textContent = '사용자';
    }
}

function logout() {
    localStorage.removeItem('authToken');
    sessionStorage.removeItem('authToken');
    window.location.href = '/login.html';
}

function setupCalculator() {
    const calcForm = document.getElementById('calcForm');

    calcForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const oldPrice = parseFloat(document.getElementById('oldPrice').value);
        const oldQuantity = parseFloat(document.getElementById('oldQuantity').value);
        const newPrice = parseFloat(document.getElementById('newPrice').value);
        const newQuantity = parseFloat(document.getElementById('newQuantity').value);

        if (oldPrice <= 0 || oldQuantity <= 0 || newPrice <= 0 || newQuantity <= 0) {
            alert('모든 값은 0보다 커야 합니다.');
            return;
        }

        // Calculate average price
        const totalAmount = (oldPrice * oldQuantity) + (newPrice * newQuantity);
        const totalQuantity = oldQuantity + newQuantity;
        const avgPrice = totalAmount / totalQuantity;

        // Display results
        displayResults(avgPrice, totalQuantity, totalAmount);
    });
}

function displayResults(avgPrice, totalQuantity, totalAmount) {
    const resultSection = document.getElementById('resultSection');
    const avgPriceElement = document.getElementById('avgPrice');
    const totalQuantityElement = document.getElementById('totalQuantity');
    const totalAmountElement = document.getElementById('totalAmount');

    avgPriceElement.textContent = formatNumber(avgPrice) + '원';
    totalQuantityElement.textContent = formatNumber(totalQuantity) + '주';
    totalAmountElement.textContent = formatNumber(totalAmount) + '원';

    // Show result section with animation
    resultSection.style.display = 'block';
    resultSection.style.animation = 'fadeIn 0.5s ease-out';
}

function formatNumber(num) {
    return Math.round(num).toLocaleString('ko-KR');
}

// Add animation
const style = document.createElement('style');
style.textContent = `
    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
`;
document.head.appendChild(style);
