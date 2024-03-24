function logout() {
    // Удаляем данные сессии из localStorage
    localStorage.removeItem('sessionToken');

    // Если требуется, делаем запрос к серверу для завершения сессии
    fetch('/logout', {
        method: 'POST',
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/auth_form';
        } else {
            console.error('Logout failed');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
