document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');

    console.log('document load')
    const sessionToken = localStorage.getItem('sessionToken');
    if (sessionToken) {
        // Если сессия существует, делаем редирект на main_menu
        window.location.href = '/main_menu';
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const username = form.querySelector('input[name="username"]').value;
        const password = form.querySelector('input[name="password"]').value;

        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Ошибка авторизации');
            }
        })
        .then(data => {
            localStorage.setItem('sessionToken', data.token);
            // Перенаправляем пользователя на главную страницу
            window.location.href = '/main_menu';
        })
        .catch(error => {
            console.error('Ошибка авторизации: ', error.message);
            alert('Ошибка входа. Проверьте логин и пароль.');
        });
    });
});
