// Check authentication
const token = localStorage.getItem('token');
if (!token) {
    window.location.href = '/login';
}

const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
};

// Display user info
document.getElementById('userInfo').textContent = `Hoş geldin, ${localStorage.getItem('fullName') || localStorage.getItem('username')}`;

// Initialize year selector
const yearSelect = document.getElementById('yearSelect');
const currentYear = new Date().getFullYear();
for (let year = currentYear; year >= currentYear - 5; year--) {
    const option = document.createElement('option');
    option.value = year;
    option.textContent = year;
    yearSelect.appendChild(option);
}

// Set current month and year
const currentMonth = new Date().getMonth() + 1;
document.getElementById('monthSelect').value = currentMonth;
document.getElementById('yearSelect').value = currentYear;

// Load categories
let incomeCategories = [];
let expenseCategories = [];

async function loadCategories() {
    try {
        const incomeResponse = await fetch('/api/categories/income', { headers });
        incomeCategories = await incomeResponse.json();

        const expenseResponse = await fetch('/api/categories/expense', { headers });
        expenseCategories = await expenseResponse.json();

        populateCategorySelects();
    } catch (error) {
        console.error('Kategoriler yüklenemedi:', error);
    }
}

function populateCategorySelects() {
    const incomeSelect = document.getElementById('incomeCategorySelect');
    const expenseSelect = document.getElementById('expenseCategorySelect');

    incomeSelect.innerHTML = '<option value="">Kategori Seçin</option>';
    expenseSelect.innerHTML = '<option value="">Kategori Seçin</option>';

    incomeCategories.forEach(cat => {
        const option = document.createElement('option');
        option.value = cat.id;
        option.textContent = cat.name;
        incomeSelect.appendChild(option);
    });

    expenseCategories.forEach(cat => {
        const option = document.createElement('option');
        option.value = cat.id;
        option.textContent = cat.name;
        expenseSelect.appendChild(option);
    });
}

// Load data
document.getElementById('loadDataBtn').addEventListener('click', loadMonthlyData);

async function loadMonthlyData() {
    const year = document.getElementById('yearSelect').value;
    const month = document.getElementById('monthSelect').value;

    try {
        const response = await fetch(`/api/summary/month/${year}/${month}`, { headers });
        const data = await response.json();

        // Update summary cards
        document.getElementById('totalIncome').textContent = `₺${data.totalIncome.toFixed(2)}`;
        document.getElementById('totalExpense').textContent = `₺${data.totalExpense.toFixed(2)}`;
        document.getElementById('netBalance').textContent = `₺${data.netBalance.toFixed(2)}`;

        // Update tables
        updateIncomeTable(data.incomes);
        updateExpenseTable(data.expenses);
    } catch (error) {
        console.error('Veriler yüklenemedi:', error);
        alert('Veriler yüklenirken bir hata oluştu');
    }
}

function updateIncomeTable(incomes) {
    const tbody = document.getElementById('incomeTableBody');
    if (incomes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="no-data">Veri yok</td></tr>';
        return;
    }

    tbody.innerHTML = incomes.map(income => `
        <tr>
            <td>${income.transactionDate}</td>
            <td>${income.categoryName || '-'}</td>
            <td>${income.description || '-'}</td>
            <td>₺${income.amount.toFixed(2)}</td>
            <td><button class="btn-delete" onclick="deleteIncome(${income.id})">Sil</button></td>
        </tr>
    `).join('');
}

function updateExpenseTable(expenses) {
    const tbody = document.getElementById('expenseTableBody');
    if (expenses.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="no-data">Veri yok</td></tr>';
        return;
    }

    tbody.innerHTML = expenses.map(expense => `
        <tr>
            <td>${expense.transactionDate}</td>
            <td>${expense.categoryName || '-'}</td>
            <td>${expense.description || '-'}</td>
            <td>₺${expense.amount.toFixed(2)}</td>
            <td><button class="btn-delete" onclick="deleteExpense(${expense.id})">Sil</button></td>
        </tr>
    `).join('');
}

// Modal functions
function showIncomeModal() {
    document.getElementById('incomeModal').style.display = 'block';
}

function closeIncomeModal() {
    document.getElementById('incomeModal').style.display = 'none';
    document.getElementById('incomeForm').reset();
}

function showExpenseModal() {
    document.getElementById('expenseModal').style.display = 'block';
}

function closeExpenseModal() {
    document.getElementById('expenseModal').style.display = 'none';
    document.getElementById('expenseForm').reset();
}

// Form submissions
document.getElementById('incomeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        amount: parseFloat(formData.get('amount')),
        categoryId: formData.get('categoryId') ? parseInt(formData.get('categoryId')) : null,
        description: formData.get('description'),
        transactionDate: formData.get('transactionDate')
    };

    try {
        const response = await fetch('/api/income', {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        });

        if (response.ok) {
            closeIncomeModal();
            loadMonthlyData();
        } else {
            alert('Gelir eklenirken bir hata oluştu');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
});

document.getElementById('expenseForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        amount: parseFloat(formData.get('amount')),
        categoryId: formData.get('categoryId') ? parseInt(formData.get('categoryId')) : null,
        description: formData.get('description'),
        transactionDate: formData.get('transactionDate')
    };

    try {
        const response = await fetch('/api/expense', {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        });

        if (response.ok) {
            closeExpenseModal();
            loadMonthlyData();
        } else {
            alert('Gider eklenirken bir hata oluştu');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
});

// Delete functions
async function deleteIncome(id) {
    if (!confirm('Bu geliri silmek istediğinize emin misiniz?')) return;

    try {
        const response = await fetch(`/api/income/${id}`, {
            method: 'DELETE',
            headers
        });

        if (response.ok) {
            loadMonthlyData();
        } else {
            alert('Silme işlemi başarısız');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
}

async function deleteExpense(id) {
    if (!confirm('Bu gideri silmek istediğinize emin misiniz?')) return;

    try {
        const response = await fetch(`/api/expense/${id}`, {
            method: 'DELETE',
            headers
        });

        if (response.ok) {
            loadMonthlyData();
        } else {
            alert('Silme işlemi başarısız');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
}

// Logout function
function logout() {
    fetch('/auth/logout', {
        method: 'POST',
        headers
    }).finally(() => {
        localStorage.clear();
        window.location.href = '/login';
    });
}

// Initialize
loadCategories();
loadMonthlyData();
