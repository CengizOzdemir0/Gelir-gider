// Register functionality
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const fullName = document.getElementById('fullName').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorMessage = document.getElementById('errorMessage');
    const successMessage = document.getElementById('successMessage');

    // Hide previous messages
    errorMessage.style.display = 'none';
    successMessage.style.display = 'none';

    // Validate password match
    if (password !== confirmPassword) {
        errorMessage.textContent = 'Şifreler eşleşmiyor';
        errorMessage.style.display = 'block';
        return;
    }

    try {
        const response = await fetch('/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username,
                email,
                fullName,
                password
            })
        });

        if (response.ok) {
            const message = await response.text();
            successMessage.textContent = message;
            successMessage.style.display = 'block';

            // Redirect to login after 2 seconds
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
        } else {
            const error = await response.text();
            errorMessage.textContent = error || 'Kayıt başarısız';
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        errorMessage.textContent = 'Bir hata oluştu. Lütfen tekrar deneyin.';
        errorMessage.style.display = 'block';
    }
});
