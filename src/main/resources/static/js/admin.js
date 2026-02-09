// Check authentication and admin role
const token = localStorage.getItem('token');
const roles = JSON.parse(localStorage.getItem('roles') || '[]');

if (!token || !roles.includes('ROLE_ADMIN')) {
    window.location.href = '/login';
}

const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
};

// Display user info
document.getElementById('userInfo').textContent = `Hoş geldin, ${localStorage.getItem('fullName') || localStorage.getItem('username')} (Admin)`;

// Load users
async function loadUsers() {
    try {
        const response = await fetch('/api/admin/users', { headers });
        const users = await response.json();

        const tbody = document.getElementById('userTableBody');
        if (users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="no-data">Kullanıcı yok</td></tr>';
            return;
        }

        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${user.fullName || '-'}</td>
                <td>${user.roles.join(', ')}</td>
                <td>${user.enabled ? 'Aktif' : 'Pasif'}</td>
                <td><button class="btn-delete" onclick="deleteUser(${user.id})">Sil</button></td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Kullanıcılar yüklenemedi:', error);
    }
}

// Load categories
async function loadCategories() {
    try {
        const response = await fetch('/api/admin/categories', { headers });
        const categories = await response.json();

        const tbody = document.getElementById('categoryTableBody');
        if (categories.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" class="no-data">Kategori yok</td></tr>';
            return;
        }

        tbody.innerHTML = categories.map(cat => `
            <tr>
                <td>${cat.name}</td>
                <td>${cat.type === 'INCOME' ? 'Gelir' : 'Gider'}</td>
                <td>${cat.description || '-'}</td>
                <td><button class="btn-delete" onclick="deleteCategory(${cat.id})">Sil</button></td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Kategoriler yüklenemedi:', error);
    }
}

// Modal functions
function showUserModal() {
    document.getElementById('userModal').style.display = 'block';
}

function closeUserModal() {
    document.getElementById('userModal').style.display = 'none';
    document.getElementById('userForm').reset();
}

function showCategoryModal() {
    document.getElementById('categoryModal').style.display = 'block';
}

function closeCategoryModal() {
    document.getElementById('categoryModal').style.display = 'none';
    document.getElementById('categoryForm').reset();
}

// Form submissions
document.getElementById('userForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        username: formData.get('username'),
        email: formData.get('email'),
        fullName: formData.get('fullName'),
        password: formData.get('password')
    };

    try {
        const response = await fetch('/api/admin/users', {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        });

        if (response.ok) {
            closeUserModal();
            loadUsers();
        } else {
            const error = await response.json();
            alert(error.message || 'Kullanıcı eklenirken bir hata oluştu');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
});

document.getElementById('categoryForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        name: formData.get('name'),
        type: formData.get('type'),
        description: formData.get('description')
    };

    try {
        const response = await fetch('/api/admin/categories', {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        });

        if (response.ok) {
            closeCategoryModal();
            loadCategories();
        } else {
            alert('Kategori eklenirken bir hata oluştu');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
});

// Delete functions
async function deleteUser(id) {
    if (!confirm('Bu kullanıcıyı silmek istediğinize emin misiniz?')) return;

    try {
        const response = await fetch(`/api/admin/users/${id}`, {
            method: 'DELETE',
            headers
        });

        if (response.ok) {
            loadUsers();
        } else {
            alert('Silme işlemi başarısız');
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Bir hata oluştu');
    }
}

async function deleteCategory(id) {
    if (!confirm('Bu kategoriyi silmek istediğinize emin misiniz?')) return;

    try {
        const response = await fetch(`/api/admin/categories/${id}`, {
            method: 'DELETE',
            headers
        });

        if (response.ok) {
            loadCategories();
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
loadUsers();
loadCategories();
