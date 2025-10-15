// Initialize calculator on page load
document.addEventListener('DOMContentLoaded', function() {
    setupModeToggle();
    setupForwardCalculator();
    setupReverseCalculator();
});

function setupModeToggle() {
    const modeButtons = document.querySelectorAll('.mode-btn');
    const forwardMode = document.getElementById('forwardMode');
    const reverseMode = document.getElementById('reverseMode');
    const resultSection = document.getElementById('resultSection');

    modeButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            // Update active button
            modeButtons.forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            // Hide result section when switching modes
            resultSection.style.display = 'none';

            // Switch modes
            const mode = this.dataset.mode;
            if (mode === 'forward') {
                forwardMode.style.display = 'block';
                reverseMode.style.display = 'none';
            } else {
                forwardMode.style.display = 'none';
                reverseMode.style.display = 'block';
            }
        });
    });
}

function setupForwardCalculator() {
    const forwardForm = document.getElementById('forwardForm');

    forwardForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const oldPrice = parseFloat(document.getElementById('fOldPrice').value);
        const oldQuantity = parseFloat(document.getElementById('fOldQuantity').value);
        const newPrice = parseFloat(document.getElementById('fNewPrice').value);
        const newQuantity = parseFloat(document.getElementById('fNewQuantity').value);

        if (oldPrice <= 0 || oldQuantity <= 0 || newPrice <= 0 || newQuantity <= 0) {
            alert('모든 값은 0보다 커야 합니다.');
            return;
        }

        // Calculate average price
        const totalAmount = (oldPrice * oldQuantity) + (newPrice * newQuantity);
        const totalQuantity = oldQuantity + newQuantity;
        const avgPrice = totalAmount / totalQuantity;

        // Display results
        displayResults({
            avgPrice: avgPrice,
            totalQuantity: totalQuantity,
            totalAmount: totalAmount
        });
    });
}

function setupReverseCalculator() {
    const reverseForm = document.getElementById('reverseForm');

    reverseForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const currentAvg = parseFloat(document.getElementById('rCurrentAvg').value);
        const currentQuantity = parseFloat(document.getElementById('rCurrentQuantity').value);
        const targetAvg = parseFloat(document.getElementById('rTargetAvg').value);
        const buyPrice = parseFloat(document.getElementById('rBuyPrice').value);

        if (currentAvg <= 0 || currentQuantity <= 0 || targetAvg <= 0 || buyPrice <= 0) {
            alert('모든 값은 0보다 커야 합니다.');
            return;
        }

        if (targetAvg >= currentAvg) {
            alert('목표 평단가는 현재 평단가보다 낮아야 합니다.');
            return;
        }

        if (buyPrice >= targetAvg) {
            alert('추가 매수 단가는 목표 평단가보다 낮아야 합니다.');
            return;
        }

        // Reverse calculation formula:
        // targetAvg = (currentAvg * currentQuantity + buyPrice * X) / (currentQuantity + X)
        // targetAvg * (currentQuantity + X) = currentAvg * currentQuantity + buyPrice * X
        // targetAvg * currentQuantity + targetAvg * X = currentAvg * currentQuantity + buyPrice * X
        // targetAvg * X - buyPrice * X = currentAvg * currentQuantity - targetAvg * currentQuantity
        // X * (targetAvg - buyPrice) = currentQuantity * (currentAvg - targetAvg)
        // X = currentQuantity * (currentAvg - targetAvg) / (targetAvg - buyPrice)

        const requiredQuantity = currentQuantity * (currentAvg - targetAvg) / (targetAvg - buyPrice);
        const totalQuantity = currentQuantity + requiredQuantity;
        const totalAmount = (currentAvg * currentQuantity) + (buyPrice * requiredQuantity);

        // Display results
        displayResults({
            avgPrice: targetAvg,
            totalQuantity: totalQuantity,
            totalAmount: totalAmount,
            additionalQuantity: requiredQuantity,
            additionalAmount: buyPrice * requiredQuantity
        });
    });
}

function displayResults(data) {
    const resultSection = document.getElementById('resultSection');
    const resultGrid = resultSection.querySelector('.result-grid');

    // Clear previous results
    resultGrid.innerHTML = '';

    // Always show these
    addResultItem(resultGrid, '평균 매수 단가', formatNumber(data.avgPrice) + '원');
    addResultItem(resultGrid, '총 보유 수량', formatNumber(data.totalQuantity) + '주');
    addResultItem(resultGrid, '총 투자 금액', formatNumber(data.totalAmount) + '원');

    // Additional info for reverse calculation
    if (data.additionalQuantity !== undefined) {
        addResultItem(resultGrid, '추가 매수 필요 수량', formatNumber(data.additionalQuantity) + '주', 'highlight');
        addResultItem(resultGrid, '추가 투자 필요 금액', formatNumber(data.additionalAmount) + '원', 'highlight');
    }

    // Show result section with animation
    resultSection.style.display = 'block';
    resultSection.style.animation = 'fadeIn 0.5s ease-out';
}

function addResultItem(container, label, value, extraClass = '') {
    const item = document.createElement('div');
    item.className = 'result-item' + (extraClass ? ' ' + extraClass : '');
    item.innerHTML = `
        <span class="result-label">${label}</span>
        <span class="result-value">${value}</span>
    `;
    container.appendChild(item);
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
